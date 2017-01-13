package com.yy.codex.uikit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.OverScroller;
import android.widget.ScrollView;

/**
 * Created by it on 17/1/6.
 */


public class UIScrollView extends UIView {

    OverScroller overScroller;
    float y;

    public UIScrollView(@NonNull Context context, @NonNull View view) {
        super(context, view);
    }

    public UIScrollView(@NonNull Context context) {
        super(context);
        initScrollView();
    }

    public UIScrollView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        initScrollView();
    }

    public UIScrollView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initScrollView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UIScrollView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initScrollView();
    }

    private void initScrollView() {
        overScroller = new OverScroller(getContext());
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (overScroller.computeScrollOffset()) {
            scrollBy(overScroller.getCurrX(), overScroller.getCurrY());
            postInvalidate();
        }
    }

    public void scroll() {
        overScroller.startScroll(0, 0, 0, -20);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        Log.i(null, ""+event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = (float) ((event.getY() - y) * 0.05);
                System.out.print((event.getY() - y));
                scrollBy(0, (int)deltaY);
                break;
            default:
                break;
        }
        return true;
    }
}
