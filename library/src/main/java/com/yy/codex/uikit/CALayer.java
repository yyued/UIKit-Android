package com.yy.codex.uikit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;


/**
 * Created by cuiminghui on 2017/1/3.
 */

public class CALayer {

    /* layoutProps */

    private CGRect frame = new CGRect(0, 0, 0, 0);

    /* styleProps */

    private int backgroundColor;
    private double cornerRadius;
    private double borderWidth;
    private int borderColor = Color.BLACK;
    private double shadowX = 2.0;
    private double shadowY = 2.0;
    private double shadowRadius;
    private int shadowColor = Color.BLACK;
    private Bitmap bitmap;
    private int bitmapGravity = GRAVITY_SCALE_ASCEPT_FIT;
    private boolean clipToBounds;
    private boolean hidden;

    /* renderProps */

    private boolean needDisplay;

    /* hierarchyProps */

    private UIView view;
    private CALayer superLayer;
    private ArrayList<CALayer> subLayers = new ArrayList<CALayer>();

    /* scaledDensityProp */

    public static float scaledDensity = 2;

    /* imageGravity const */

    public static final int GRAVITY_SCALE_TO_FILL = 0x01;
    public static final int GRAVITY_SCALE_ASCEPT_FIT = 0x02;
    public static final int GRAVITY_SCALE_ASCEPT_FILL = 0x03;
    public static final int GRAVITY_CENRER = 0x04;
    public static final int GRAVITY_TOP = 0x05;
    public static final int GRAVITY_TOP_LEFT = 0x06;
    public static final int GRAVITY_TOP_RIGHT = 0x07;
    public static final int GRAVITY_BOTTOM = 0x08;
    public static final int GRAVITY_BOTTOM_LEFT = 0x09;
    public static final int GRAVITY_BOTTOM_RIGHT = 0x0a;
    public static final int GRAVITY_LEFT = 0x0b;
    public static final int GRAVITY_RIGHT = 0x0c;

    /* category CALayer Constructor */

    public CALayer() {}

    public CALayer(CGRect frame) {
        float x = (float) frame.origin.getX() * scaledDensity;
        float y = (float) frame.origin.getY() * scaledDensity;
        float w = (float) frame.size.getWidth() * scaledDensity;
        float h = (float) frame.size.getHeight() * scaledDensity;
        this.frame = new CGRect(x, y, w, h);
    }

    /* category CALayer Hierarchy */

    public CALayer getSuperLayer() {
        return superLayer;
    }

    public CALayer[] getSubLayers() { return subLayers.toArray(new CALayer[subLayers.size()]); }

    public void removeFromSuperLayer(){
        if (this.superLayer != null){
            this.superLayer.subLayers.remove(this);
        }
    }

    public void addSubLayer(CALayer layer){
        layer.removeFromSuperLayer();
        layer.superLayer = this;
        subLayers.add(layer);
    }

    public void insertSubLayerAtIndex(CALayer subLayer, int index){
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

    public void insertBelowSubLayer(CALayer subLayer, CALayer siblingSubview){
        int idx = subLayers.indexOf(siblingSubview);
        if (idx > -1){
            subLayer.removeFromSuperLayer();
            subLayers.add(idx, subLayer);
        }
    }

    public void insertAboveSubLayer(CALayer subLayer, CALayer siblingSubview){
        int idx = subLayers.indexOf(siblingSubview);
        if (idx > -1){
            subLayer.removeFromSuperLayer();
            subLayers.add(idx + 1, subLayer);
        }
    }

    public void replaceSubLayer(CALayer subLayer, CALayer newLayer){
        int idx = subLayers.indexOf(subLayer);
        if (idx > -1){
            subLayer.removeFromSuperLayer();
            insertSubLayerAtIndex(newLayer, idx);
        }
    }

    /* category CALayer Appearance */

    protected void drawLayer(Canvas canvas, CGRect rect){
        // normalize layer's prop
        CGRect frame = this.frame;
        float halfBorderW = (float) borderWidth / 2.0f;

        // background & shadow
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(backgroundColor);

        // visible
        if (hidden){
            return;
        }

        // image
        if (bitmap != null){
            drawBitmap(canvas, frame, bitmap, bitmapGravity);
//            return;
        }

        // border clipToBounds
        if (cornerRadius > 0){
            if (clipToBounds){
                Path clipPath = new Path();
                clipPath.addRoundRect(frame.toRectF(), (float) cornerRadius, (float) cornerRadius, Path.Direction.CW);
                canvas.clipPath(clipPath);
            }
            if (shadowRadius > 0){
                paint.setShadowLayer((float) shadowRadius, (float) shadowX, (float) shadowY, shadowColor);
            }
            canvas.drawRoundRect(frame.toRectF(halfBorderW), (float) cornerRadius, (float) cornerRadius, paint);
            if (borderWidth > 0){
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth((float) borderWidth);
                paint.setColor(borderColor);
                canvas.drawRoundRect(frame.toRectF(halfBorderW), (float) cornerRadius, (float) cornerRadius, paint);
            }
        }
        else {
            if (clipToBounds){
                canvas.clipRect(frame.toRectF());
            }
            if (shadowRadius > 0){
                paint.setShadowLayer((float) shadowRadius, (float) shadowX, (float) shadowY, shadowColor);
            }
            canvas.drawRect(frame.toRectF(), paint);
            if (borderWidth > 0){
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth((float) borderWidth * 2);
                paint.setColor(borderColor);
                canvas.drawRect(frame.toRectF(), paint);
            }
        }
    }

    public void drawRect(Canvas canvas, CGRect rect){
        if(this.askIfNeedDispaly()){
            this.resetNeedDisplayToFalse();
            drawAllLayers(canvas, rect);
        }
    }

    private void drawBitmap(Canvas canvas, CGRect rect, Bitmap bitmap, int bitmapGravity){
        double imageW = bitmap.getWidth();
        double imageH = bitmap.getHeight();
        double imageRatio = imageW / imageH;
        double frameW = frame.getSize().getWidth();
        double frameH = frame.getSize().getHeight();
        double frameRatio = frameW / frameH;
        double frameX = rect.origin.getX();
        double frameY = rect.origin.getY();
        CGRect imageRect = new CGRect(0, 0, imageW, imageH);
        CGRect frameRect = rect;

        switch (bitmapGravity){
            case GRAVITY_SCALE_TO_FILL:
                break;
            case GRAVITY_SCALE_ASCEPT_FIT:
                if (frameRatio > imageRatio){
                    double scaledFrameW = frameH * imageRatio;
                    frameRect = new CGRect(frameX + (frameW - scaledFrameW) / 2, frameY, scaledFrameW, frameH);
                }
                else {
                    double scaledH = frameW / imageRatio;
                    frameRect = new CGRect(frameX, frameY + (frameH - scaledH)/2, frameW, scaledH);
                }
                break;
            case GRAVITY_SCALE_ASCEPT_FILL:
                if (frameRatio > imageRatio){
                    double clipedImageH = imageW / frameRatio;
                    imageRect = new CGRect(0, (imageH - clipedImageH)/2, imageW, clipedImageH);
                }
                else {
                    double clipedImageW = imageH * frameRatio;
                    imageRect = new CGRect((imageW - clipedImageW) / 2, 0, clipedImageW, imageH);
                }
                break;
            case GRAVITY_CENRER:
                if (frameW >= imageW && frameH >= imageH){
                    frameRect = new CGRect(frameX + (frameW - imageW) / 2, frameY + (frameH - imageH) / 2, imageW, imageH);
                }
                else if (frameW < imageW && frameH >= imageH ){
                    imageRect = new CGRect((imageW - frameW)/2, 0, frameW, imageH);
                    frameRect = new CGRect(frameX, frameY+(frameH - imageH)/2, frameW, imageH);
                }
                else if (frameH < imageH && frameW >= imageW) {
                    imageRect = new CGRect(0, (imageH - frameH)/2, imageW, frameH);
                    frameRect = new CGRect(frameX+(frameW - imageW)/2, frameY, imageW, frameH);
                }
                else {
                    imageRect = new CGRect((imageW - frameW)/2, (imageH - frameH)/2, frameW, frameH);
                }
                break;
            case GRAVITY_TOP:
                if (frameW >= imageW && frameH >= imageH){
                    frameRect = new CGRect(frameX+(frameW-imageW)/2, frameY, imageW, imageH);
                }
                else if (frameW < imageW && frameH >= imageH ){
                    imageRect = new CGRect((imageW-frameW)/2, 0, frameW, imageH);
                    frameRect = new CGRect(frameX, frameY, frameW, imageH);
                }
                else if (frameH < imageH && frameW >= imageW) {
                    imageRect = new CGRect(0, 0, imageW, frameH);
                    frameRect = new CGRect(frameX+(imageW-frameW)/2, frameY, imageW, frameH);
                }
                else {
                    imageRect = new CGRect((imageW-frameW)/2, 0, frameW, frameH);
                }
                break;
            case GRAVITY_TOP_LEFT:
                if (frameW >= imageW && frameH >= imageH){
                    frameRect = new CGRect(frameX, frameY, imageW, imageH);
                }
                else if (frameW < imageW && frameH >= imageH ){
                    imageRect = new CGRect(0, 0, frameW, imageH);
                    frameRect = new CGRect(frameX, frameY, frameW, imageH);
                }
                else if (frameH < imageH && frameW >= imageW) {
                    imageRect = new CGRect(0, 0, imageW, frameH);
                    frameRect = new CGRect(frameX, frameY, imageW, frameH);
                }
                else {
                    imageRect = new CGRect(0, 0, frameW, frameH);
                }
                break;
            case GRAVITY_TOP_RIGHT:
                if (frameW >= imageW && frameH >= imageH){
                    frameRect = new CGRect(frameX+(frameW-imageW), frameY, imageW, imageH);
                }
                else if (frameW < imageW && frameH >= imageH ){
                    imageRect = new CGRect((imageW-frameW), 0, frameW, imageH);
                    frameRect = new CGRect(frameX, frameY, frameW, imageH);
                }
                else if (frameH < imageH && frameW >= imageW) {
                    imageRect = new CGRect(0, 0, imageW, frameH);
                    frameRect = new CGRect(frameX+(frameW-imageW), frameY, imageW, frameH);
                }
                else {
                    imageRect = new CGRect((imageW-frameW), 0, frameW, frameH);
                }
                break;
            case GRAVITY_BOTTOM:
                if (frameW >= imageW && frameH >= imageH){
                    frameRect = new CGRect(frameX+(frameW-imageW)/2, frameY+(frameH-imageH), imageW, imageH);
                }
                else if (frameW < imageW && frameH >= imageH ){
                    imageRect = new CGRect((imageW-frameW)/2, 0, frameW, imageH);
                    frameRect = new CGRect(frameX, frameY+(frameH-imageH), frameW, imageH);
                }
                else if (frameH < imageH && frameW >= imageW) {
                    imageRect = new CGRect(0, (imageH-frameH), imageW, frameH);
                    frameRect = new CGRect(frameX+(frameW-imageW)/2, frameY, imageW, frameH);
                }
                else {
                    imageRect = new CGRect((imageW-frameW)/2, (imageH-frameH), frameW, frameH);
                }
                break;
            case GRAVITY_BOTTOM_LEFT:
                if (frameW >= imageW && frameH >= imageH){
                    frameRect = new CGRect(frameX, frameY+(frameH-imageH), imageW, imageH);
                }
                else if (frameW < imageW && frameH >= imageH ){
                    imageRect = new CGRect(0, 0, frameW, imageH);
                    frameRect = new CGRect(frameX, frameY+(frameH-imageH), frameW, imageH);
                }
                else if (frameH < imageH && frameW >= imageW) {
                    imageRect = new CGRect(0, (imageH-frameH), imageW, frameH);
                    frameRect = new CGRect(frameX, frameY, imageW, frameH);
                }
                else {
                    imageRect = new CGRect(0, (imageH-frameH), frameW, frameH);
                }
                break;
            case GRAVITY_BOTTOM_RIGHT:
                if (frameW >= imageW && frameH >= imageH){
                    frameRect = new CGRect(frameX+(frameW-imageW), frameY+(frameH-imageH), imageW, imageH);
                }
                else if (frameW < imageW && frameH >= imageH ){
                    imageRect = new CGRect((imageW-frameW), 0, frameW, imageH);
                    frameRect = new CGRect(frameX, frameY+(frameH-imageH), frameW, imageH);
                }
                else if (frameH < imageH && frameW >= imageW) {
                    imageRect = new CGRect(0, (imageH-frameH), imageW, frameH);
                    frameRect = new CGRect(frameX+(frameW-imageW), frameY, imageW, frameH);
                }
                else {
                    imageRect = new CGRect((imageW-frameW), (imageH-frameH), frameW, frameH);
                }
                break;
            case GRAVITY_LEFT:
                if (frameW >= imageW && frameH >= imageH){
                    frameRect = new CGRect(frameX, frameY+(frameH-imageH)/2, imageW, imageH);
                }
                else if (frameW < imageW && frameH >= imageH ){
                    imageRect = new CGRect(0, 0, frameW, imageH);
                    frameRect = new CGRect(frameX, frameY+(frameH-imageH)/2, frameW, imageH);
                }
                else if (frameH < imageH && frameW >= imageW) {
                    imageRect = new CGRect(0, (imageH-frameH)/2, imageW, frameH);
                    frameRect = new CGRect(frameX, frameY, imageW, frameH);
                }
                else {
                    imageRect = new CGRect(0, 0, frameW, frameH);
                }
                break;
            case GRAVITY_RIGHT:
                if (frameW >= imageW && frameH >= imageH){
                    frameRect = new CGRect(frameX+(frameW-imageW), frameY+(frameH-imageH)/2, imageW, imageH);
                }
                else if (frameW < imageW && frameH >= imageH ){
                    imageRect = new CGRect((imageW-frameW), 0, frameW, imageH);
                    frameRect = new CGRect(frameX, frameY+(frameH-imageH)/2, frameW, imageH);
                }
                else if (frameH < imageH && frameW >= imageW) {
                    imageRect = new CGRect(0, (imageH-frameH)/2, imageW, frameH);
                    frameRect = new CGRect(frameX+(frameW-imageW), frameY, imageW, frameH);
                }
                else {
                    imageRect = new CGRect((imageW-frameW), (imageH-frameH)/2, frameW, frameH);
                }
                break;
        }

        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), new Paint());
    }

    /* category CALayer Getter&Setter */

    public void bindView(UIView view){
        this.view = view;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public double getCornerRadius() {
        return cornerRadius;
    }

    public double getBorderWidth() {
        return borderWidth;
    }

    public int getBorderColor() {
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

    public int getShadowColor() {
        return shadowColor;
    }

    public CGRect getFrame() {
        return frame;
    }

    public Boolean getClipToBounds() {
        return clipToBounds;
    }

    public CALayer setFrame(CGRect frame) {
        float x = (float) frame.origin.getX() * scaledDensity;
        float y = (float) frame.origin.getY() * scaledDensity;
        float w = (float) frame.size.getWidth() * scaledDensity;
        float h = (float) frame.size.getHeight() * scaledDensity;
        CGRect newValue = new CGRect(x, y, w, h);
        if (!this.frame.equals(newValue)){
            this.frame = newValue;
            this.setNeedDisplay(true);
        }
        return this;
    }

    public CALayer setBitmap(Bitmap bitmap) {
//        if (this.bitmap != bitmap){
            this.bitmap = bitmap;
            this.setNeedDisplay(true);
//        }
        return this;
    }

    public CALayer setBitmapGravity(int bitmapGravity) {
        if (this.bitmapGravity != bitmapGravity){
            this.bitmapGravity = bitmapGravity;
            this.setNeedDisplay(true);
        }
        return this;
    }

    public CALayer setClipToBounds(Boolean clipToBounds) {
        if (this.clipToBounds != clipToBounds){
            this.clipToBounds = clipToBounds;
            this.setNeedDisplay(true);
        }
        return this;
    }

    public CALayer setHidden(boolean hidden) {
        if (this.hidden != hidden){
            this.hidden = hidden;
            this.setNeedDisplay(true);
        }
        return this;
    }

    public CALayer setBorderWidth(double borderWidth) {
        if (!doubleEqual(this.borderWidth, borderWidth * scaledDensity)){
            this.borderWidth = borderWidth * scaledDensity;
            this.setNeedDisplay(true);
        }
        return this;
    }

    public CALayer setBorderColor(int borderColor) {
        if (this.borderColor != borderColor){
            this.borderColor = borderColor;
            this.setNeedDisplay(true);
        }
        return this;
    }

    public CALayer setCornerRadius(double cornerRadius) {
        if (!doubleEqual(this.cornerRadius, cornerRadius * scaledDensity)){
            this.cornerRadius = cornerRadius * scaledDensity;
            this.setNeedDisplay(true);
        }
        return this;
    }

    public CALayer setBackgroundColor(int backgroundColor) {
        if (this.backgroundColor != backgroundColor){
            this.backgroundColor = backgroundColor;
            this.setNeedDisplay(true);
        }
        return this;
    }

    public CALayer setShadowX(double shadowX) {
        if (!doubleEqual(this.shadowX, shadowX * scaledDensity)){
            this.shadowX = shadowX * scaledDensity;
            this.setNeedDisplay(true);
        }
        return this;
    }

    public CALayer setShadowY(double shadowY) {
        if (!doubleEqual(this.shadowY, shadowY * scaledDensity)){
            this.shadowY = shadowY * scaledDensity;
            this.setNeedDisplay(true);
        }
        return this;
    }

    public CALayer setShadowRadius(double shadowRadius) {
        if (!doubleEqual(this.shadowRadius, shadowRadius * scaledDensity)){
            this.shadowRadius = shadowRadius * scaledDensity;
            this.setNeedDisplay(true);
        }
        return this;
    }

    public CALayer setShadowColor(int shadowColor) {
        if (this.shadowColor != shadowColor){
            this.shadowColor = shadowColor;
            this.setNeedDisplay(true);
        }
        return this;
    }

    public void setNeedDisplay(boolean needDisplay) {
        this.needDisplay = needDisplay;
        UIView view = this.requestRootLayer().view;
        if (view != null){
            view.invalidate();
        }
    }

    /* category CALayer support method */

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

    private boolean askIfNeedDispaly(){
        if (this.needDisplay){
            return true;
        }
        else {
            for (CALayer item : subLayers){
                if (item.askIfNeedDispaly()){
                    return true;
                }
                else {
                    continue;
                }
            }
        }
        return false;
    }

    private void resetNeedDisplayToFalse(){
        this.needDisplay = false;
        for (CALayer item : subLayers){
            item.resetNeedDisplayToFalse();
        }
    }

    private void drawAllLayers(Canvas canvas, CGRect rect){
        if (hidden){
            return;
        }
        drawLayer(canvas, rect);
        for (CALayer item : subLayers){
            item.drawAllLayers(canvas, rect);
        }
    }

    private boolean doubleEqual(Double a, Double b){
        if (Math.abs(a - b) < 0.01) {
            return true;
        }
        return false;
    }
}


