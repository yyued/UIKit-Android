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

    private @NonNull CGRect mFrame = new CGRect(0, 0, 0, 0);

    public @NonNull CGRect getFrame() {
        return mFrame;
    }

    public @NonNull CALayer setFrame(@NonNull CGRect mFrame) {
        float x = (float) mFrame.origin.x;
        float y = (float) mFrame.origin.y;
        float w = (float) mFrame.size.width;
        float h = (float) mFrame.size.height;
        CGRect newValue = new CGRect(x, y, w, h);
        if (!this.mFrame.equals(newValue)){
            this.mFrame = newValue;
            this.setNeedDisplay(true);
        }
        return this;
    }

    /* styleProps */

    private UIColor mBackgroundColor = UIColor.clearColor;

    public UIColor getBackgroundColor() {
        return mBackgroundColor;
    }

    public @NonNull CALayer setBackgroundColor(UIColor backgroundColor) {
        if (this.mBackgroundColor != backgroundColor){
            this.mBackgroundColor = backgroundColor;
            this.setNeedDisplay(true);
        }
        return this;
    }

    private double mCornerRadius = 0.0;

    public double getCornerRadius() {
        return mCornerRadius;
    }

    public @NonNull CALayer setCornerRadius(double mCornerRadius) {
        if (!doubleEqual(this.mCornerRadius, mCornerRadius)){
            double oldValue = this.mCornerRadius;
            this.mCornerRadius = mCornerRadius;
            this.setNeedDisplay(true);
            if (this.requestRootLayer().mView != null) {
                UIView.animator.addAnimationState(this.requestRootLayer().mView, "layer.mCornerRadius", oldValue, mCornerRadius);
            }
        }
        return this;
    }

    private double mBorderWidth = 0.0;

    public double getBorderWidth() {
        return mBorderWidth;
    }

    public @NonNull CALayer setBorderWidth(double mBorderWidth) {
        double oldValue = this.mBorderWidth;
        if (!doubleEqual(this.mBorderWidth, mBorderWidth)){
            this.mBorderWidth = mBorderWidth;
            this.setNeedDisplay(true);
            if (this.requestRootLayer().mView != null) {
                UIView.animator.addAnimationState(this.requestRootLayer().mView, "layer.mBorderWidth", oldValue, mBorderWidth);
            }
        }
        return this;
    }

    private UIColor mBorderColor = UIColor.blackColor;

    public UIColor getBorderColor() {
        return mBorderColor;
    }

    public @NonNull CALayer setBorderColor(UIColor mBorderColor) {
        if (this.mBorderColor != mBorderColor){
            this.mBorderColor = mBorderColor;
            this.setNeedDisplay(true);
        }
        return this;
    }

    private double mShadowX = 2.0;

    public double getShadowX() {
        return mShadowX;
    }

    public @NonNull CALayer setShadowX(double mShadowX) {
        if (!doubleEqual(this.mShadowX, mShadowX)){
            this.mShadowX = mShadowX;
            this.setNeedDisplay(true);
        }
        return this;
    }

    private double mShadowY = 2.0;

    public double getShadowY() {
        return mShadowY;
    }

    public @NonNull CALayer setShadowY(double mShadowY) {
        if (!doubleEqual(this.mShadowY, mShadowY)){
            this.mShadowY = mShadowY;
            this.setNeedDisplay(true);
        }
        return this;
    }

    private double mShadowRadius = 0.0;

    public double getShadowRadius() {
        return mShadowRadius;
    }

    public @NonNull CALayer setShadowRadius(double mShadowRadius) {
        if (!doubleEqual(this.mShadowRadius, mShadowRadius)){
            this.mShadowRadius = mShadowRadius;
            this.setNeedDisplay(true);
        }
        return this;
    }

    private UIColor mShadowColor = UIColor.blackColor;

    public UIColor getShadowColor() {
        return mShadowColor;
    }

    public @NonNull CALayer setShadowColor(UIColor mShadowColor) {
        if (this.mShadowColor != mShadowColor){
            this.mShadowColor = mShadowColor;
            this.setNeedDisplay(true);
        }
        return this;
    }

    private @Nullable Bitmap mBitmap = null;

    public @Nullable Bitmap getBitmap() {
        return mBitmap;
    }

    public @NonNull CALayer setBitmap(Bitmap mBitmap) {
        if (this.mBitmap != mBitmap){
            this.mBitmap = mBitmap;
            this.setNeedDisplay(true);
        }
        return this;
    }

    private int mBitmapGravity = CALayerBitmapPainter.GRAVITY_SCALE_ASPECT_FIT;

    public int getBitmapGravity() {
        return mBitmapGravity;
    }

    public @NonNull CALayer setBitmapGravity(int mBitmapGravity) {
        if (this.mBitmapGravity != mBitmapGravity){
            this.mBitmapGravity = mBitmapGravity;
            this.setNeedDisplay(true);
        }
        return this;
    }

    private UIColor mBitmapColor = null;

    public UIColor getBitmapColor() {
        return mBitmapColor;
    }

    public void setBitmapColor(UIColor mBitmapColor) {
        this.mBitmapColor = mBitmapColor;
    }

    private boolean mClipToBounds = false;

    public Boolean getClipToBounds() {
        return mClipToBounds;
    }

    public @NonNull CALayer setClipToBounds(Boolean mClipToBounds) {
        if (this.mClipToBounds != mClipToBounds){
            this.mClipToBounds = mClipToBounds;
            this.setNeedDisplay(true);
        }
        return this;
    }

    private boolean mHidden = false;

    public boolean isHidden() {
        return mHidden;
    }

    public @NonNull CALayer setHidden(boolean mHidden) {
        if (this.mHidden != mHidden){
            this.mHidden = mHidden;
            this.setNeedDisplay(true);
        }
        return this;
    }

    /* renderProps */

    private boolean mNeedDisplay = false;

    public void setNeedDisplay(boolean mNeedDisplay) {
        this.mNeedDisplay = mNeedDisplay;
        if (mNeedDisplay){
            UIView view = this.requestRootLayer().mView;
            if (view != null){
                view.invalidate();
            }
        }
    }

    private boolean mNewCanvasContext = false;

    public boolean isNewCanvasContext() {
        /*
            以下情况，在新画布绘制。
            1. 有 transform 属性时
            2. 有子节点 且 mClipToBounds 时
        */
        boolean result = (this.mTransforms != null && this.mTransforms.length > 0)
                || (this.getSubLayers().length > 0 && this.mClipToBounds);
        return result;
    }

    public void setNewCanvasContext(boolean mNewCanvasContext) {
        this.mNewCanvasContext = mNewCanvasContext;
    }

    private @Nullable CALayer mMask = null; // not support

    public @Nullable CALayer getMask() {
        return mMask;
    }

    public @NonNull CALayer setMask(CALayer mMask) {
        if (this.mMask != mMask){
            this.mMask = mMask;
            this.setNeedDisplay(true);
        }
        return this;
    }

    /* hierarchyProps */

    private UIView mView;

    public void bindView(UIView view){
        this.mView = view;
    }

    private CALayer mSuperLayer;

    public CALayer getSuperLayer() {
        return mSuperLayer;
    }

    private @NonNull ArrayList<CALayer> mSubLayers = new ArrayList<CALayer>();

    public @NonNull CALayer[] getSubLayers() { return mSubLayers.toArray(new CALayer[mSubLayers.size()]); }

    /* transformProp */

    private CGTransform[] mTransforms = null;

    public CGTransform[] getTransforms() {
        return mTransforms;
    }

    public void setTransforms(CGTransform[] mTransforms) {
        this.mTransforms = mTransforms;
    }

    public void setTransform(CGTransform a) {
        CGTransform[] tf = {a};
        this.mTransforms = tf;
    }

    /* category CALayer Constructor */

    public CALayer() {}

    public CALayer(@NonNull CGRect mFrame) {
        float x = (float) (mFrame.origin.x);
        float y = (float) (mFrame.origin.y);
        float w = (float) (mFrame.size.width);
        float h = (float) (mFrame.size.height);
        this.mFrame = new CGRect(x, y, w, h);
    }

    /* category CALayer Hierarchy */

    public void removeFromSuperLayer(){
        if (this.mSuperLayer != null){
            this.mSuperLayer.mSubLayers.remove(this);
        }
    }

    public void addSubLayer(@NonNull CALayer layer){
        layer.removeFromSuperLayer();
        layer.mSuperLayer = this;
        mSubLayers.add(layer);
    }

    public void insertSubLayerAtIndex(@NonNull CALayer subLayer, int index){
        subLayer.removeFromSuperLayer();
        if (index < 1){
            this.mSubLayers.add(0, subLayer);
        }
        else if (index > this.mSubLayers.size() - 1){
            this.mSubLayers.add(subLayer);
        }
        else {
            this.mSubLayers.add(index, subLayer);
        }
    }

    public void insertBelowSubLayer(@NonNull CALayer subLayer, CALayer siblingSubview){
        int idx = mSubLayers.indexOf(siblingSubview);
        if (idx > -1){
            subLayer.removeFromSuperLayer();
            mSubLayers.add(idx, subLayer);
        }
    }

    public void insertAboveSubLayer(@NonNull CALayer subLayer, CALayer siblingSubview){
        int idx = mSubLayers.indexOf(siblingSubview);
        if (idx > -1){
            subLayer.removeFromSuperLayer();
            mSubLayers.add(idx + 1, subLayer);
        }
    }

    public void replaceSubLayer(@NonNull CALayer subLayer, @NonNull CALayer newLayer){
        int idx = mSubLayers.indexOf(subLayer);
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
        if (mHidden){
            return;
        }
        if (this.isNewCanvasContext()){
            CALayerPainter.drawLayerTree(this, canvas);
        }
        else {
            this.drawInCanvas(canvas);
            for (CALayer item : mSubLayers){
                item.drawAllLayers(canvas, rect);
            }
        }
    }

    protected void drawInCanvas(@NonNull Canvas canvas){
        CALayerPainter.drawCurrentLayer(this, canvas);
    }

    protected void drawLayerTreeInCanvas(@NonNull Canvas canvas){
        this.drawInCanvas(canvas);
        for (CALayer item : mSubLayers){
            item.drawLayerTreeInCanvas(canvas);
        }
    }

    private boolean askIfNeedDispaly(){
        return true;
    }

    private void resetNeedDisplayToFalse(){
        this.mNeedDisplay = false;
        for (CALayer item : mSubLayers){
            item.resetNeedDisplayToFalse();
        }
    }

    /* category CALayer support method */

    public static @NonNull CGPoint calcOriginInSuperCoordinate(@NonNull CALayer layer){
        float scaledDensity = (float) UIScreen.mainScreen.scale();
        double oriX = layer.mFrame.origin.x;
        double oriY = layer.mFrame.origin.y;
        CALayer p = layer.getSuperLayer();
        while (p != null){
//            if (p.isNewCanvasContext()) {
//                break;
//            }
            oriX += p.mFrame.origin.x;
            oriY += p.mFrame.origin.y;
            p = p.getSuperLayer();
        }
        return new CGPoint(oriX * scaledDensity, oriY * scaledDensity);
    }

    private CALayer requestRootLayer(){
        CALayer root = this;
        while (root.mSuperLayer !=null){
            root = root.mSuperLayer;
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
        if (aKey.equalsIgnoreCase("layer.mCornerRadius")) {
            setCornerRadius(aValue);
        }
        else if (aKey.equalsIgnoreCase("layer.mBorderWidth")){
            setBorderWidth(aValue);
        }
    }

}