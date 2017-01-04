package com.yy.codex.uikit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


/**
 * Created by cuiminghui on 2017/1/3.
 */

public class CALayer {

    private CGRect frame = new CGRect(0, 0, 0, 0);

    private int backgroundColor;

    private float cornerRadius; // default 0
    private float borderWidth;  // default 0
    private int borderColor = Color.BLACK;  // default #000

    private float shadowX = 2.0f;
    private float shadowY = 2.0f;
    private float shadowRadius;
    private int shadowColor = Color.BLACK;

    // main
    public void drawRect(Canvas canvas, CGRect rect){

        // normalize layer's prop
        Float halfBorderW = borderWidth / 2.0f;

        // background paint
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(backgroundColor);

        // border paint
        Paint borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setColor(borderColor);

        if (cornerRadius > 0){
            if (shadowRadius > 0){
                paint.setShadowLayer(shadowRadius, shadowX, shadowY, shadowColor);
            }
            canvas.drawRoundRect(rect.toRectF(), cornerRadius, cornerRadius, paint);
            canvas.drawRoundRect(halfBorderW, halfBorderW, canvas.getWidth()-halfBorderW, canvas.getHeight()-halfBorderW, cornerRadius, cornerRadius, borderPaint);
        }
        else {
            if (shadowRadius > 0){
                paint.setShadowLayer(shadowRadius, shadowX, shadowY, shadowColor);
            }
            canvas.drawRect(rect.toRectF(), paint);
            canvas.drawRect(rect.toRectF(), borderPaint);
        }

//        if (layer.getShadowRadius() > 0){
//            if (getLayerType() != LAYER_TYPE_SOFTWARE) {
//                setLayerType(LAYER_TYPE_SOFTWARE, null);
//            }
//            Paint shadowPaint = new Paint();
//            shadowPaint.setAntiAlias(true);
//            shadowPaint.setShadowLayer(layer.getShadowRadius(), layer.getShadowX(), layer.getShadowY(), layer.getShadowColor());
//            canvas.drawRect(rect.toRectF(), shadowPaint);
//            canvas.drawCircle(50, 130, 30, shadowPaint);
//        }
    }

    // getter setter

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
        this.cornerRadius = cornerRadius;
        return this;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public CALayer setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
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
        this.shadowX = shadowX;
        return this;
    }

    public CALayer setShadowY(float shadowY) {
        this.shadowY = shadowY;
        return this;
    }

    public CALayer setShadowRadius(float shadowRadius) {
        this.shadowRadius = shadowRadius;
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
        this.frame = frame;
        return this;
    }
}


