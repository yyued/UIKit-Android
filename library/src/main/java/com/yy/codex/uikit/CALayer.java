package com.yy.codex.uikit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;


/**
 * Created by cuiminghui on 2017/1/3.
 */

public class CALayer {

    /* layoutProps */

    @NonNull
    private CGRect frame = new CGRect(0, 0, 0, 0);

    /* styleProps */

    private UIColor backgroundColor = UIColor.clearColor;
    private double cornerRadius = 0.0;
    private double borderWidth = 0.0;
    private UIColor borderColor = UIColor.blackColor;
    private double shadowX = 2.0;
    private double shadowY = 2.0;
    private double shadowRadius = 0.0;
    private UIColor shadowColor = UIColor.blackColor;
    @Nullable
    private Bitmap bitmap = null;
    private int bitmapGravity = CALayerBitmapPainter.GRAVITY_SCALE_ASCEPT_FIT;
    private UIColor bitmapColor = null;
    private boolean clipToBounds = false;
    private boolean hidden = false;

    /* renderProps */

    private boolean needDisplay = false;
    private boolean newCanvasContext = false;
    @Nullable
    private CALayer mask = null; // not support

    /* hierarchyProps */

    private UIView view;
    private CALayer superLayer;
    @NonNull
    private ArrayList<CALayer> subLayers = new ArrayList<CALayer>();

    /* transformProp */

    private CGTransform[] transforms = null;

    /* scaledDensityProp */

    public static float scaledDensity = (float) UIScreen.mainScreen.scale();

    /* category CALayer Constructor */

    public CALayer() {}

    public CALayer(@NonNull CGRect frame) {
        float x = (float) (frame.origin.getX());
        float y = (float) (frame.origin.getY());
        float w = (float) (frame.size.getWidth());
        float h = (float) (frame.size.getHeight());
        this.frame = new CGRect(x, y, w, h);
    }

    /* category CALayer Hierarchy */

    public CALayer getSuperLayer() {
        return superLayer;
    }

    @NonNull
    public CALayer[] getSubLayers() { return subLayers.toArray(new CALayer[subLayers.size()]); }

    public void removeFromSuperLayer(){
        if (this.superLayer != null){
            this.superLayer.subLayers.remove(this);
        }
    }

    public void addSubLayer(@NonNull CALayer layer){
        layer.removeFromSuperLayer();
        layer.superLayer = this;
        subLayers.add(layer);
    }

    public void insertSubLayerAtIndex(@NonNull CALayer subLayer, int index){
        subLayer.removeFromSuperLayer();
        if (index < 1){
            this.subLayers.add(0, subLayer);
        }
        else if (index > this.subLayers.size() - 1){
            this.subLayers.add(subLayer);
        }
        else {
            this.subLayers.add(index, subLayer);
        }
    }

    public void insertBelowSubLayer(@NonNull CALayer subLayer, CALayer siblingSubview){
        int idx = subLayers.indexOf(siblingSubview);
        if (idx > -1){
            subLayer.removeFromSuperLayer();
            subLayers.add(idx, subLayer);
        }
    }

    public void insertAboveSubLayer(@NonNull CALayer subLayer, CALayer siblingSubview){
        int idx = subLayers.indexOf(siblingSubview);
        if (idx > -1){
            subLayer.removeFromSuperLayer();
            subLayers.add(idx + 1, subLayer);
        }
    }

    public void replaceSubLayer(@NonNull CALayer subLayer, @NonNull CALayer newLayer){
        int idx = subLayers.indexOf(subLayer);
        if (idx > -1){
            subLayer.removeFromSuperLayer();
            insertSubLayerAtIndex(newLayer, idx);
        }
    }

    /* category CALayer Appearance */

    public void drawRect(@NonNull Canvas canvas, CGRect rect){
        if(this.askIfNeedDispaly()){
            this.resetNeedDisplayToFalse();
            drawAllLayers(canvas, rect);
        }
    }

    private void drawAllLayers(@NonNull Canvas canvas, CGRect rect){
        if (hidden){
            return;
        }
        if (isNewCanvasContext()){
            drawLayer(canvas, rect, true);
        }
        else {
            drawLayer(canvas, rect, false);
            for (CALayer item : subLayers){
                item.drawAllLayers(canvas, rect);
            }
        }
    }

    protected void drawLayer(@NonNull Canvas canvas, CGRect rect, boolean isDrawInNewCanvas){
        if (isDrawInNewCanvas){
            double frameW = frame.size.getWidth() * scaledDensity;
            double frameH = frame.size.getHeight() * scaledDensity;
            CGPoint origin = calcOriginInSuperCoordinate(this);

            // create srcBitmap
            Bitmap srcBitmap = Bitmap.createBitmap((int)( frameW + origin.getX()), (int)(frameH + origin.getY()), Bitmap.Config.ARGB_8888);
            Canvas srcCanvas = new Canvas(srcBitmap);
            drawLayersInCanvas(srcCanvas);

            // create maskBitmap
            Bitmap maskBitmap = Bitmap.createBitmap((int)( frameW + origin.getX()), (int)(frameH + origin.getY()), Bitmap.Config.ARGB_8888);
            Canvas maskCanvas = new Canvas(maskBitmap);
            Paint maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            RectF maskRectF = new CGRect(origin.getX(), origin.getY(), frameW, frameH).toRectF();
            maskCanvas.drawRoundRect(maskRectF, (float) cornerRadius * scaledDensity, (float) cornerRadius * scaledDensity, maskPaint);

            // draw srcBitmap, and apply maskBitmap if need
            Paint mixPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            if (this.transforms != null & this.transforms.length > 0){
                Matrix matrix = createMatrix(this.transforms);
                if (this.clipToBounds){
                    canvas.drawBitmap(maskBitmap, matrix, mixPaint);
                    mixPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                }
                canvas.drawBitmap(srcBitmap, matrix, mixPaint);
            }
            else {
                canvas.drawBitmap(maskBitmap, 0, 0, mixPaint);
                mixPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(srcBitmap, 0, 0, mixPaint);
            }
        }
        else {
            drawInCanvas(canvas);
        }
    }

    private void drawLayersInCanvas(@NonNull Canvas canvas){
        drawInCanvas(canvas);
        for (CALayer item : subLayers){
            item.drawLayersInCanvas(canvas);
        }
    }

    protected void drawInCanvas(@NonNull Canvas canvas){
        CALayerPainter.draw(this, canvas);
    }

    private Matrix createMatrix(CGTransform[] transforms){
        Matrix matrix = new Matrix();
        if (transforms == null || transforms.length == 0){
            return matrix;
        }
        RectF rectF = frame.toRectF();
        for (CGTransform transform : transforms){
            if (!transform.enable){
                continue;
            }
            if (transform instanceof CGTransformRotation){
                matrix.preRotate((float) ((CGTransformRotation) transform).angle, rectF.centerX() * (float) scaledDensity, rectF.centerY() * (float) scaledDensity);
            }
            else if (transform instanceof CGTransformTranslation){
                CGTransformTranslation translation = (CGTransformTranslation) transform;
                matrix.postTranslate((float) translation.tx, (float) translation.ty);
            }
            else if (transform instanceof CGTransformScale){
                CGTransformScale scale = (CGTransformScale)transform;
                matrix.postScale((float) scale.sx, (float) scale.sy, rectF.centerX() * (float) scaledDensity, rectF.centerY() * (float) scaledDensity);
            }
            else if (transform instanceof CGTransformMatrix){
                // @TODO
            }
        }
        return matrix;
    }

    private Bitmap createRadiusMask(@NonNull CGRect rect, double radius){
        Bitmap maskBitmap = Bitmap.createBitmap((int)(rect.size.getWidth()+rect.origin.getX()), (int)(rect.size.getHeight()+rect.origin.getY()), Bitmap.Config.ARGB_8888);
        Canvas canvasB = new Canvas(maskBitmap);
        Paint p3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvasB.drawRoundRect(rect.toRectF(), (float) radius, (float) radius, p3);
        return maskBitmap;
    }

    private Bitmap createMask(@NonNull CALayer layer){
        CGRect rect = layer.getFrame();
        Bitmap maskBitmap = Bitmap.createBitmap((int)(rect.size.getWidth()+rect.origin.getX()), (int)(rect.size.getHeight()+rect.origin.getY()), Bitmap.Config.ARGB_8888);
        Canvas canvasB = new Canvas(maskBitmap);
        layer.drawInCanvas(canvasB);
        return maskBitmap;
    }

    private boolean askIfNeedDispaly(){
        return true;
    }

    private void resetNeedDisplayToFalse(){
        this.needDisplay = false;
        for (CALayer item : subLayers){
            item.resetNeedDisplayToFalse();
        }
    }

    /* category CALayer Getter&Setter */

    public void bindView(UIView view){
        this.view = view;
    }

    public boolean isHidden() {
        return hidden;
    }

    @Nullable
    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getBitmapGravity() {
        return bitmapGravity;
    }

    public UIColor getBackgroundColor() {
        return backgroundColor;
    }

    public double getCornerRadius() {
        return cornerRadius;
    }

    public double getBorderWidth() {
        return borderWidth;
    }

    public UIColor getBorderColor() {
        return borderColor;
    }

    public double getShadowX() {
        return shadowX;
    }

    public double getShadowY() {
        return shadowY;
    }

    public double getShadowRadius() {
        return shadowRadius;
    }

    public UIColor getShadowColor() {
        return shadowColor;
    }

    public UIColor getBitmapColor() {
        return bitmapColor;
    }

    @Nullable
    public CALayer getMask() {
        return mask;
    }

    @NonNull
    public CGRect getFrame() {
        return frame;
    }

    public Boolean getClipToBounds() {
        return clipToBounds;
    }

    /*
        以下情况，在新画布绘制。
        1. 有 transform 属性时
        2. 有子节点 且 clipToBounds 时
     */
    public boolean isNewCanvasContext() {
        boolean result = (this.transforms != null && this.transforms.length > 0)
                || (this.getSubLayers().length > 0 && this.clipToBounds);
        return result;
    }

    public CGTransform[] getTransforms() {
        return transforms;
    }

    public void setNewCanvasContext(boolean newCanvasContext) {
        this.newCanvasContext = newCanvasContext;
    }

    @NonNull
    public CALayer setFrame(@NonNull CGRect frame) {
        float x = (float) frame.origin.getX();
        float y = (float) frame.origin.getY();
        float w = (float) frame.size.getWidth();
        float h = (float) frame.size.getHeight();
        CGRect newValue = new CGRect(x, y, w, h);
        if (!this.frame.equals(newValue)){
            this.frame = newValue;
            this.setNeedDisplay(true);
        }
        return this;
    }

    @NonNull
    public CALayer setBitmap(Bitmap bitmap) {
        if (this.bitmap != bitmap){
            this.bitmap = bitmap;
            this.setNeedDisplay(true);
        }
        return this;
    }

    public void setBitmapColor(UIColor bitmapColor) {
        this.bitmapColor = bitmapColor;
    }

    @NonNull
    public CALayer setBitmapGravity(int bitmapGravity) {
        if (this.bitmapGravity != bitmapGravity){
            this.bitmapGravity = bitmapGravity;
            this.setNeedDisplay(true);
        }
        return this;
    }

    @NonNull
    public CALayer setClipToBounds(Boolean clipToBounds) {
        if (this.clipToBounds != clipToBounds){
            this.clipToBounds = clipToBounds;
            this.setNeedDisplay(true);
        }
        return this;
    }

    @NonNull
    public CALayer setHidden(boolean hidden) {
        if (this.hidden != hidden){
            this.hidden = hidden;
            this.setNeedDisplay(true);
        }
        return this;
    }

    @NonNull
    public CALayer setBorderWidth(double borderWidth) {
        double oldValue = this.borderWidth;
        if (!doubleEqual(this.borderWidth, borderWidth)){
            this.borderWidth = borderWidth;
            this.setNeedDisplay(true);
            if (this.requestRootLayer().view != null) {
                UIView.sAnimator.addAnimationState(this.requestRootLayer().view, "layer.borderWidth", oldValue, borderWidth);
            }
        }
        return this;
    }

    @NonNull
    public CALayer setBorderColor(UIColor borderColor) {
        if (this.borderColor != borderColor){
            this.borderColor = borderColor;
            this.setNeedDisplay(true);
        }
        return this;
    }

    @NonNull
    public CALayer setCornerRadius(double cornerRadius) {
        if (!doubleEqual(this.cornerRadius, cornerRadius)){
            double oldValue = this.cornerRadius;
            this.cornerRadius = cornerRadius;
            this.setNeedDisplay(true);
            if (this.requestRootLayer().view != null) {
                UIView.sAnimator.addAnimationState(this.requestRootLayer().view, "layer.cornerRadius", oldValue, cornerRadius);
            }
        }
        return this;
    }

    @NonNull
    public CALayer setBackgroundColor(UIColor backgroundColor) {
        if (this.backgroundColor != backgroundColor){
            this.backgroundColor = backgroundColor;
            this.setNeedDisplay(true);
        }
        return this;
    }

    @NonNull
    public CALayer setShadowX(double shadowX) {
        if (!doubleEqual(this.shadowX, shadowX)){
            this.shadowX = shadowX;
            this.setNeedDisplay(true);
        }
        return this;
    }

    @NonNull
    public CALayer setShadowY(double shadowY) {
        if (!doubleEqual(this.shadowY, shadowY)){
            this.shadowY = shadowY;
            this.setNeedDisplay(true);
        }
        return this;
    }

    @NonNull
    public CALayer setShadowRadius(double shadowRadius) {
        if (!doubleEqual(this.shadowRadius, shadowRadius)){
            this.shadowRadius = shadowRadius;
            this.setNeedDisplay(true);
        }
        return this;
    }

    @NonNull
    public CALayer setShadowColor(UIColor shadowColor) {
        if (this.shadowColor != shadowColor){
            this.shadowColor = shadowColor;
            this.setNeedDisplay(true);
        }
        return this;
    }

    public void setTransforms(CGTransform[] transforms) {
        this.transforms = transforms;
    }

    public void setTransform(CGTransform a) {
        CGTransform[] tf = {a};
        this.transforms = tf;
    }

    public void setNeedDisplay(boolean needDisplay) {
        this.needDisplay = needDisplay;
        if (needDisplay){
            UIView view = this.requestRootLayer().view;
            if (view != null){
                view.invalidate();
            }
        }
    }

    @NonNull
    public CALayer setMask(CALayer mask) {
        if (this.mask != mask){
            this.mask = mask;
            this.setNeedDisplay(true);
        }
        return this;
    }

    /* category CALayer support method */

    @NonNull
    public static CGPoint calcOriginInSuperCoordinate(@NonNull CALayer layer){
        double oriX = layer.frame.origin.getX();
        double oriY = layer.frame.origin.getY();
        CALayer p = layer.getSuperLayer();
        while (p != null){
//            if (p.isNewCanvasContext()) {
//                break;
//            }
            oriX += p.frame.origin.getX();
            oriY += p.frame.origin.getY();
            p = p.getSuperLayer();
        }
        return new CGPoint(oriX * scaledDensity, oriY * scaledDensity);
    }

    private CALayer requestRootLayer(){
        CALayer root = this;
        while (root.superLayer!=null){
            root = root.superLayer;
            if (root == null){
                return root;
            }
        }
        return root;
    }

    private boolean doubleEqual(Double a, Double b){
        if (Math.abs(a - b) < 0.01) {
            return true;
        }
        return false;
    }

    /* Animation */

    public void animate(@NonNull String aKey, float aValue) {
        if (aKey.equalsIgnoreCase("layer.cornerRadius")) {
            setCornerRadius(aValue);
        }
        else if (aKey.equalsIgnoreCase("layer.borderWidth")){
            setBorderWidth(aValue);
        }
    }

}


