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
        UIPanGestureRecognizer pan = new UIPanGestureRecognizer(this, "pan:");
        this.addGestureRecognizer(pan);
    }

    public void pan(UIPanGestureRecognizer panGestureRecognizer) {
        CGPoint point = panGestureRecognizer.location(panGestureRecognizer.getView());
        NSLog.log(point);
    }


}
