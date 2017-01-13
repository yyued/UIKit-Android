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

    private Path path = null;
    private int strokeColor = Color.BLACK;
    private double lineWidth = 1;

    /* category CAShapeLayer Constructor */

    @Override
    protected void drawInCanvas(@NonNull Canvas canvas) {
        super.drawInCanvas(canvas);

        if (path != null){
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth((float) lineWidth);
            paint.setColor(strokeColor);
            canvas.drawPath(path, paint);
        }
    }

    /* category CAShapeLayer Getter&Setter */

    public Path getPath() {
        return path;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public double getLineWidth() {
        return lineWidth;
    }

    @NonNull
    public CAShapeLayer setPath(Path path) {
        this.path = path;
        return this;
    }

    @NonNull
    public CAShapeLayer setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    public CAShapeLayer setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }
}
