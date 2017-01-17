package com.yy.codex.uikit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    public UIScrollView(@NonNull Context context, @NonNull View view) {
        super(context, view);
    }

    public UIScrollView(@NonNull Context context) {
        super(context);
    }

    public UIScrollView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public UIScrollView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UIScrollView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init() {
        super.init();
        mPanGestureRecognizer = new UIPanGestureRecognizer(this, "handlePan:");
        addGestureRecognizer(mPanGestureRecognizer);
    }

    /* Scrolls */

    @NonNull private UIPanGestureRecognizer mPanGestureRecognizer;
    @NonNull private CGPoint mContentOffset = new CGPoint(0, 0);
    private boolean mTracking = false;
    @Nullable private UIViewAnimation mCurrentAnimation = null;

    @Override
    public void touchesBegan(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesBegan(touches, event);
        if (mCurrentAnimation != null) {
            mCurrentAnimation.cancel();
            mCurrentAnimation = null;
        }
    }

    public void handlePan(UIPanGestureRecognizer panGestureRecognizer) {
        if (!mTracking) {
            /* Began */
            mTracking = true;
            if (mContentOffset.y < 0.0) {
                panGestureRecognizer.setTranslation(new CGPoint(mContentOffset.x, mContentOffset.y * 3));
            }
            else {
                panGestureRecognizer.setTranslation(mContentOffset);
            }
            if (mCurrentAnimation != null) {
                mCurrentAnimation.cancel();
                mCurrentAnimation = null;
            }
            return;
        }
        if (mTracking && panGestureRecognizer.getState() == UIGestureRecognizerState.Changed) {
            /* Move */
            double originY = -(panGestureRecognizer.translation().y);
            if (originY < 0.0) {
                originY = originY / 3.0;
            }
            setContentOffset(mContentOffset.setY(originY));
        }
        else if (panGestureRecognizer.getState() == UIGestureRecognizerState.Ended) {
            /* Ended */
            mTracking = false;
            CGPoint velocity = panGestureRecognizer.velocity();
            if (mContentOffset.y < 0.0) {
                mCurrentAnimation = UIView.animator.springWithOptions(120.0, 20.0, Math.abs(velocity.y), new Runnable() {
                    @Override
                    public void run() {
                        setContentOffset(mContentOffset.setY(0), false);
                    }
                }, null);
            }
            else {
                mCurrentAnimation = UIView.animator.decay(this, "contentOffset.y", mContentOffset.y, -velocity.y / 1000.0, null);
            }
        }
    }

    @Override
    public void animate(@NonNull String aKey, float aValue) {
        super.animate(aKey, aValue);
        if (aKey.equalsIgnoreCase("contentOffset.x")) {
            setContentOffset(mContentOffset.setX(aValue), false);
        }
        else if (aKey.equalsIgnoreCase("contentOffset.y")) {
            setContentOffset(mContentOffset.setY(aValue), false);
        }
    }

    public void setContentOffset(@NonNull CGPoint contentOffset) {
        setContentOffset(contentOffset, false);
    }

    public void setContentOffset(@NonNull final CGPoint contentOffset, boolean animated) {
        CGPoint oldValue = mContentOffset;
        mContentOffset = contentOffset;
        if (animated) {
            if (mCurrentAnimation != null) {
                mCurrentAnimation.cancel();
            }
            mCurrentAnimation = UIView.animator.linear(0.25, new Runnable() {
                @Override
                public void run() {
                    setContentOffset(contentOffset, false);
                }
            }, null);
        }
        else {
            scrollTo((int)(mContentOffset.x * UIScreen.mainScreen.scale()), (int)(mContentOffset.y * UIScreen.mainScreen.scale()));
        }
        UIView.animator.addAnimationState(this, "contentOffset.x", oldValue.x, mContentOffset.x);
        UIView.animator.addAnimationState(this, "contentOffset.y", oldValue.y, mContentOffset.y);
    }

}
