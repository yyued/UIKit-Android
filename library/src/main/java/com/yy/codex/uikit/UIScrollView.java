package com.yy.codex.uikit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

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
    @NonNull private CGSize mContentSize = new CGSize(0, 0);
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

    @Override
    public void touchesEnded(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesEnded(touches, event);
        if (mPanGestureRecognizer.getState() != UIGestureRecognizerState.Ended) {
            if (yOutOfBounds(mContentOffset)) {
                moveYToBounds();
            }
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
            if (yOutOfBounds(mContentOffset.setY(originY))) {
                moveYResisted(mContentOffset.setY(originY));
            }
            else {
                setContentOffset(mContentOffset.setY(originY));
            }
        }
        else if (panGestureRecognizer.getState() == UIGestureRecognizerState.Ended) {
            /* Ended */
            mTracking = false;
            CGPoint velocity = panGestureRecognizer.velocity();
            if (yOutOfBounds(mContentOffset)) {
                moveYToBounds();
            }
            else {
                mCurrentAnimation = UIView.animator.decayBounds(this, "contentOffset.y", mContentOffset.y, -velocity.y / 1000.0, 0.0, (mContentSize.height - getFrame().size.height), null);
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

    public void setContentSize(@NonNull CGSize contentSize) {
        mContentSize = contentSize;
    }

    private boolean yOutOfBounds(@NonNull CGPoint ofPoint) {
        if (mContentSize.height < getFrame().size.height) {
            return true;
        }
        if (ofPoint.y < 0.0) {
            return true;
        }
        else if (ofPoint.y > mContentSize.height - getFrame().size.height) {
            return true;
        }
        return false;
    }

    private void moveYResisted(@NonNull CGPoint contentOffset) {
        if (mContentSize.height < getFrame().size.height) {
            double delta = contentOffset.y;
            setContentOffset(contentOffset.setY(delta / 3.0), false);
        }
        else if (contentOffset.y < 0.0) {
            double delta = Math.abs(contentOffset.y);
            setContentOffset(contentOffset.setY(-delta / 3.0), false);
        }
        else if (contentOffset.y > mContentSize.height - getFrame().size.height) {
            double delta = contentOffset.y - (mContentSize.height - getFrame().size.height);
            setContentOffset(contentOffset.setY((mContentSize.height - getFrame().size.height) + delta / 3.0), false);
        }
    }

    private void moveYToBounds() {
        if (mCurrentAnimation != null) {
            mCurrentAnimation.cancel();
        }
        double nearestBounds;
        if (mContentSize.height < getFrame().size.height) {
            nearestBounds = 0.0;
        }
        else if (mContentOffset.y < 0.0) {
            nearestBounds = 0.0;
        }
        else {
            nearestBounds = mContentSize.height - getFrame().size.height;
        }
        final double finalNearestBounds = nearestBounds;
        mCurrentAnimation = UIView.animator.springWithOptions(120.0, 20.0, 0.0, new Runnable() {
            @Override
            public void run() {
            setContentOffset(mContentOffset.setY(finalNearestBounds), false);
            }
        }, null);
    }

}
