package com.yy.codex.uikit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by adi on 17/1/10.
 */

public class CATextLayer extends CALayer {

    private String string;
    private float fontSize = 14;
    private int fontColor = Color.BLACK;

    private int alignMode = CATextLayerAlignmentLeft;
    private int truncateMode; //@Td
    private Boolean wrapped; //@Td

    /* const alignMode */
    public static final int CATextLayerAlignmentLeft = 0x01;
    public static final int CATextLayerAlignmentRight = 0x02;
    public static final int CATextLayerAlignmentCenter = 0x03;
    public static final int CATextLayerAlignmentJustify = 0x04; // not support

    public CATextLayer(CGRect frame) {
        super(frame);
    }

    @Override
    protected void drawLayer(Canvas canvas, CGRect rect) {
        super.drawLayer(canvas, rect);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(fontColor);
        paint.setTextSize(fontSize);
        paint.setTextAlign(Paint.Align.CENTER);
//        switch (alignMode){
//            case CATextLayerAlignmentLeft: paint.setTextAlign(Paint.Align.RIGHT);break;
//            case CATextLayerAlignmentRight: paint.setTextAlign(Paint.Align.LEFT);break;
//            case CATextLayerAlignmentCenter: paint.setTextAlign(Paint.Align.CENTER);break;
//        }
        canvas.drawText(string, (float) getFrame().origin.getX(), (float) getFrame().origin.getY() + fontSize, paint);
//        canvas.drawText("xxxJjox", 20, 20 + 14 * 2, paint);
    }

    public String getString() {
        return string;
    }

    public float getFontSize() {
        return fontSize;
    }

    public int getFontColor() {
        return fontColor;
    }

    public CATextLayer setString(String string) {
        this.string = string;
        return this;
    }

    public CATextLayer setFontSize(float fontSize) {
        this.fontSize = fontSize * scaledDensity;
        return this;
    }

    public CATextLayer setFontColor(int fontColor) {
        this.fontColor = fontColor;
        return this;
    }
}
