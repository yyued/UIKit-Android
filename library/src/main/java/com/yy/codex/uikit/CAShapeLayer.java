package com.yy.codex.uikit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;

/**
 * Created by adi on 17/1/10.
 */

public class CAShapeLayer extends CALayer {

    private Path mPath = null;
    private int mStrokeColor = Color.BLACK;
    private double mLineWidth = 1;

    /* category CAShapeLayer Constructor */

    @Override
    protected void drawInCanvas(@NonNull Canvas canvas) {
        super.drawInCanvas(canvas);

        if (mPath != null){
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth((float) mLineWidth);
            paint.setColor(mStrokeColor);
            canvas.drawPath(mPath, paint);
        }
    }

    /* category CAShapeLayer Getter&Setter */

    public Path getPath() {
        return mPath;
    }

    public int getStrokeColor() {
        return mStrokeColor;
    }

    public double getLineWidth() {
        return mLineWidth;
    }

    public @NonNull CAShapeLayer setPath(Path mPath) {
        this.mPath = mPath;
        return this;
    }

    public @NonNull CAShapeLayer setStrokeColor(int mStrokeColor) {
        this.mStrokeColor = mStrokeColor;
        return this;
    }

    public CAShapeLayer setLineWidth(double mLineWidth) {
        this.mLineWidth = mLineWidth;
        return this;
    }
}
