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

    private String mString;
    private float mFontSize = 14;
    private int mFontColor = Color.BLACK;
    private int mAlignMode = ALIGN_LEFT;
    private Paint.Align _alignMode = Paint.Align.LEFT;
    private int mTruncateMode; // not support
    private Boolean mWrapped; // not support

    /* Const mAlignMode */

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
        paint.setColor(mFontColor);
        paint.setTextSize(mFontSize * (float) UIScreen.mainScreen.scale());
        paint.setTextAlign(_alignMode);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        int drawX = rect.left;
        switch (mAlignMode){
            case ALIGN_CENTER: drawX = rect.centerX(); break;
            case ALIGN_RIGHT: drawX = rect.right; break;
            case ALIGN_LEFT: drawX = rect.left; break;
        }
        canvas.drawText(mString, drawX, baseline, paint);
    }

    /* category CATextLayer Getter&Setter */

    public String getString() {
        return mString;
    }

    public float getFontSize() {
        return mFontSize;
    }

    public int getFontColor() {
        return mFontColor;
    }

    @NonNull
    public CATextLayer setString(String mString) {
        this.mString = mString;
        return this;
    }

    @NonNull
    public CATextLayer setFontSize(float mFontSize) {
        this.mFontSize = mFontSize;
        return this;
    }

    @NonNull
    public CATextLayer setFontColor(int mFontColor) {
        this.mFontColor = mFontColor;
        return this;
    }

    public int getAlignMode() {
        return mAlignMode;
    }

    public CATextLayer setAlignMode(int mAlignMode) {
        this.mAlignMode = mAlignMode;
        switch (mAlignMode){
            case ALIGN_LEFT: _alignMode = Paint.Align.LEFT; break;
            case ALIGN_RIGHT: _alignMode = Paint.Align.RIGHT; break;
            case ALIGN_CENTER: _alignMode = Paint.Align.CENTER; break;
            default: _alignMode = Paint.Align.LEFT; break;
        }
        return this;
    }

}
