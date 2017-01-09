package com.yy.codex.uikit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import java.util.ArrayList;


/**
 * Created by cuiminghui on 2017/1/3.
 */

public class CALayer {

    private CGRect frame = new CGRect(0, 0, 0, 0);

    private float scaledDensity = 2;

    private int backgroundColor;

    private float cornerRadius; // default 0
    private float borderWidth;  // default 0
    private int borderColor = Color.BLACK;  // default #000

    private float shadowX = 2.0f;
    private float shadowY = 2.0f;
    private float shadowRadius;
    private int shadowColor = Color.BLACK;

    private Bitmap bitmap;
    private int bitmapGravity = GRAVITY_SCALE_TO_FILL;

    private boolean clipToBounds;

    private boolean hidden;

    private CALayer superLayer;
    private ArrayList<CALayer> subLayers = new ArrayList<CALayer>();

    /* const imageGravity */

    public static final int GRAVITY_SCALE_TO_FILL = 0x01;
    public static final int GRAVITY_SCALE_ASCEPT_FIT = 0x02;
    public static final int GRAVITY_SCALE_ASCEPT_FILL = 0x03;
    public static final int GRAVITY_TOP = 0x04;
    public static final int GRAVITY_LEFT = 0x05;
    public static final int GRAVITY_BOTTOM = 0x06;
    public static final int GRAVITY_RIGHT = 0x07;
    public static final int GRAVITY_CENRER = 0x08;

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

    private void drawLayer(Canvas canvas, CGRect rect){
        // normalize layer's prop
        CGRect frame = this.frame;
        Float halfBorderW = borderWidth / 2.0f;

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
            canvas.drawBitmap(bitmap, calcBitmapRect(bitmap, bitmapGravity), frame.toRectF(), paint);
            return;
        }

        // border clipToBounds
        if (cornerRadius > 0){
            if (clipToBounds){
                Path clipPath = new Path();
                clipPath.addRoundRect(frame.toRectF(), cornerRadius, cornerRadius, Path.Direction.CW);
                canvas.clipPath(clipPath);
            }
            if (shadowRadius > 0){
                paint.setShadowLayer(shadowRadius, shadowX, shadowY, shadowColor);
            }
            canvas.drawRoundRect(frame.toRectF(halfBorderW), cornerRadius, cornerRadius, paint);
            if (borderWidth > 0){
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(borderWidth);
                paint.setColor(borderColor);
                canvas.drawRoundRect(frame.toRectF(halfBorderW), cornerRadius, cornerRadius, paint);
            }
        }
        else {
            if (clipToBounds){
                canvas.clipRect(frame.toRectF());
            }
            if (shadowRadius > 0){
                paint.setShadowLayer(shadowRadius, shadowX, shadowY, shadowColor);
            }
            canvas.drawRect(frame.toRectF(), paint);
            if (borderWidth > 0){
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(borderWidth * 2);
                paint.setColor(borderColor);
                canvas.drawRect(frame.toRectF(), paint);
            }
        }
    }

    public void drawRect(Canvas canvas, CGRect rect){
        if (hidden){
            return;
        }
        drawLayer(canvas, rect);
        for (CALayer item : subLayers){ // @Td unnecessary draw
            item.drawRect(canvas, rect);
        }
    }

    private Rect calcBitmapRect(Bitmap bitmap, int bitmapGravity){
        // @Td
        double imageW = bitmap.getWidth();
        double imageH = bitmap.getHeight();
        double frameW = frame.getSize().getWidth();
        double frameH = frame.getSize().getHeight();
        double frameRatio = frameW / frameH;
        switch (bitmapGravity){
            case GRAVITY_SCALE_ASCEPT_FIT:
            case GRAVITY_SCALE_ASCEPT_FILL:
            case GRAVITY_SCALE_TO_FILL:
        }
        // return new Rect(bitmap.getWidth() / 3, bitmap.getHeight() / 3, bitmap.getWidth() / 3 * 2, bitmap.getHeight() / 3 * 2);
        return null;
    }

    /* category CALayer Getter&Setter */

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public CALayer setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public CALayer setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius * scaledDensity;
        return this;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public CALayer setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth * scaledDensity;
        return this;
    }

    public CALayer setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public float getShadowX() {
        return shadowX;
    }

    public float getShadowY() {
        return shadowY;
    }

    public float getShadowRadius() {
        return shadowRadius;
    }

    public int getShadowColor() {
        return shadowColor;
    }

    public CALayer setShadowX(float shadowX) {
        this.shadowX = shadowX * scaledDensity;
        return this;
    }

    public CALayer setShadowY(float shadowY) {
        this.shadowY = shadowY * scaledDensity;
        return this;
    }

    public CALayer setShadowRadius(float shadowRadius) {
        this.shadowRadius = shadowRadius * scaledDensity;
        return this;
    }

    public CALayer setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
        return this;
    }

    public CGRect getFrame() {
        return frame;
    }

    public CALayer setFrame(CGRect frame) {
        float x = (float) frame.origin.getX() * scaledDensity;
        float y = (float) frame.origin.getY() * scaledDensity;
        float w = (float) frame.size.getWidth() * scaledDensity;
        float h = (float) frame.size.getHeight() * scaledDensity;
        this.frame = new CGRect(x, y, w, h);
        return this;
    }

    public CALayer setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }

    public CALayer setBitmapGravity(int bitmapGravity) {
        this.bitmapGravity = bitmapGravity;
        return this;
    }

    public Boolean getClipToBounds() {
        return clipToBounds;
    }

    public CALayer setClipToBounds(Boolean clipToBounds) {
        this.clipToBounds = clipToBounds;
        return this;
    }

    public float getScaledDensity() {
        return scaledDensity;
    }

    public CALayer setScaledDensity(float scaledDensity) {
        this.scaledDensity = scaledDensity;
        return this;
    }

    public CALayer setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }
}


