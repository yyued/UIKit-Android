package com.yy.codex.uikit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

import com.yy.codex.foundation.NSLog;

/**
 * Created by it on 17/1/6.
 */


public class UIScrollView extends UIView {

    private CGPoint mPreContentOffset = new CGPoint(0, 0);
    private CGPoint mContentOffset = new CGPoint(0, 0);

    private OverScroller mOverScroller;
    private Interpolator mInterpolator;

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

        mOverScroller = new OverScroller(getContext());
    }

    @Override
    public void animate(@NonNull String aKey, float aValue) {
        super.animate(aKey, aValue);
        if (aKey.equalsIgnoreCase("contentOffset.x")) {
            scrollTo((int)aValue, getScrollY());
            mContentOffset = new CGPoint((int)aValue / UIScreen.mainScreen.scale(), getScrollY() / UIScreen.mainScreen.scale());
        }
        else if (aKey.equalsIgnoreCase("contentOffset.y")) {
            scrollTo(getScrollX(), (int)aValue);
            mContentOffset = new CGPoint(getScrollX() / UIScreen.mainScreen.scale(), (int)aValue / UIScreen.mainScreen.scale());
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        double originX = (double) this.getScrollX();
        double originY = (double) this.getScrollY();
        super.scrollTo(x, y);
        UIView.sAnimator.addAnimationState(this, "contentOffset.x", originX, (double) x);
        UIView.sAnimator.addAnimationState(this, "contentOffset.y", originY, (double) y);
    }

    private UIViewAnimation mViewAnimation;
    private boolean tracking = false;
    public boolean getTracking() {
        return tracking;
    }

    public void pan(UIPanGestureRecognizer panGestureRecognizer) {
        if (!tracking) {
            tracking = true;
            panGestureRecognizer.setTranslation(mContentOffset);
            if (mViewAnimation != null) {
                mViewAnimation.cancel();
            }
        }

        double scrollY = -(panGestureRecognizer.translation().getY());

        mContentOffset = new CGPoint(0, -(panGestureRecognizer.translation().getY()));
        scrollTo(0, (int)(mContentOffset.getY() * UIScreen.mainScreen.scale()));

        NSLog.log(mContentOffset.getY());


        if (panGestureRecognizer.getState() == UIGestureRecognizerState.Ended) {
            tracking = false;
//            if (mContentOffset.getY() < 0) {
//                viewAnimation = UIView.sAnimator.linear(0.25, new Runnable() {
//                    @Override
//                    public void run() {
//                        scrollTo(0, 0);
//                    }
//                }, null);
//            }
        }

    }


}
