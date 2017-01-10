package com.yy.codex.uikit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.OverScroller;
import android.widget.ScrollView;

/**
 * Created by it on 17/1/6.
 */


public class UIScrollView extends UIView {

    OverScroller overScroller;

    public UIScrollView(Context context, View view) {
        super(context, view);
    }

    public UIScrollView(Context context) {
        super(context);
    }

    public UIScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UIScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UIScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initScrollView() {
        overScroller = new OverScroller(getContext(), new AccelerateInterpolator());
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (overScroller.computeScrollOffset()) {
            scrollBy(overScroller.getCurrX(), overScroller.getCurrY());
            invalidate();
        }
    }

    public void scroll() {
        overScroller.startScroll(0, 0, 0, 20);
        invalidate();
    }
}
