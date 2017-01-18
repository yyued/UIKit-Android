package com.yy.codex.uikit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cuiminghui on 2017/1/18.
 */

public class UIPixelLine extends UIView {

    public UIPixelLine(@NonNull Context context, @NonNull View view) {
        super(context, view);
    }

    public UIPixelLine(@NonNull Context context) {
        super(context);
    }

    public UIPixelLine(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public UIPixelLine(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UIPixelLine(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private UIColor mColor = UIColor.blackColor;

    public void setColor(UIColor color) {
        mColor = color;
        invalidate();
    }

    private boolean mVertical = false;

    public void setVertical(boolean vertical) {
        mVertical = vertical;
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(mColor.toInt());
        paint.setStrokeWidth(1.0f);
        if (mVertical) {
            canvas.drawLine(0, 0, 0, canvas.getHeight(), paint);
        }
        else {
            canvas.drawLine(0, 0, canvas.getWidth(), 0, paint);
        }
    }

}
