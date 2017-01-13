package com.yy.codex.uikit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.graphics.Rect;

/**
 * Created by adi on 17/1/10.
 */

public class CATextLayer extends CALayer {

    private String string;
    private float fontSize = 14;
    private int fontColor = Color.BLACK;
    private int alignMode = ALIGN_LEFT;
    private Paint.Align _alignMode = Paint.Align.LEFT;
    private int truncateMode; // not support
    private Boolean wrapped; // not support

    /* Const alignMode */

    public static final int ALIGN_LEFT = 0x01;
    public static final int ALIGN_RIGHT = 0x02;
    public static final int ALIGN_CENTER = 0x03;
    public static final int ALIGN_JUSTIFY = 0x04; // not support

    /* category CATextLayer Constructor */

    public CATextLayer(@NonNull CGRect frame) {
        super(frame);
    }

    @Override
    protected void drawLayer(@NonNull Canvas canvas, CGRect rect, boolean inNewCanvas) {
        super.drawLayer(canvas, rect, inNewCanvas);
    }

    @Override
    protected void drawInCanvas(Canvas canvas) {
        super.drawInCanvas(canvas);
        Rect rect = this.getFrame().toRect();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(fontColor);
        paint.setTextSize(fontSize);
        paint.setTextAlign(_alignMode);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        int drawX = rect.left;
        switch (alignMode){
            case ALIGN_CENTER: drawX = rect.centerX(); break;
            case ALIGN_RIGHT: drawX = rect.right; break;
            case ALIGN_LEFT: drawX = rect.left; break;
        }
        canvas.drawText(string, drawX, baseline, paint);
    }

    /* category CATextLayer Getter&Setter */

    public String getString() {
        return string;
    }

    public float getFontSize() {
        return fontSize;
    }

    public int getFontColor() {
        return fontColor;
    }

    @NonNull
    public CATextLayer setString(String string) {
        this.string = string;
        return this;
    }

    @NonNull
    public CATextLayer setFontSize(float fontSize) {
        this.fontSize = fontSize * scaledDensity;
        return this;
    }

    @NonNull
    public CATextLayer setFontColor(int fontColor) {
        this.fontColor = fontColor;
        return this;
    }

    public int getAlignMode() {
        return alignMode;
    }

    public CATextLayer setAlignMode(int alignMode) {
        this.alignMode = alignMode;
        switch (alignMode){
            case ALIGN_LEFT: _alignMode = Paint.Align.LEFT; break;
            case ALIGN_RIGHT: _alignMode = Paint.Align.RIGHT; break;
            case ALIGN_CENTER: _alignMode = Paint.Align.CENTER; break;
            default: _alignMode = Paint.Align.LEFT; break;
        }
        return this;
    }



}
