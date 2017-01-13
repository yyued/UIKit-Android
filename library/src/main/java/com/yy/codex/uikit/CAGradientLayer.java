package com.yy.codex.uikit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;

/**
 * Created by adi on 17/1/13.
 */

public class CAGradientLayer extends CALayer {

    @Override
    protected void drawInCanvas(@NonNull Canvas canvas) {
//        super.drawInCanvas(canvas);

        canvas.drawARGB(255, 139, 197, 186);

        int canvasW = canvas.getWidth();
        int canvasH = canvas.getHeight();
        int layerId = canvas.saveLayer(0, 0, canvasW, canvasH, null, Canvas.ALL_SAVE_FLAG);

        {
            int r = canvasW / 3;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.RED);
            canvas.rotate(45);
            canvas.drawCircle(r, r, r, paint);
canvas.rotate(-45);
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            paint.setColor(Color.BLUE);
            canvas.drawRect(r, r, r * 2.7f, r * 2.7f, paint);
            paint.setXfermode(null);
        }

        canvas.restoreToCount(layerId);

    }
}
