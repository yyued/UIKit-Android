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

        mTracking = false;
        mScrollEnabled = true;
        mAlwaysBounceVertical = false;
        mAlwaysBounceHorizontal= false;
        mPagingEnabled = true;
        mBounces = true;

        mPanGestureRecognizer = new UIPanGestureRecognizer(this, "handlePan:");
        if (mScrollEnabled) {
            addGestureRecognizer(mPanGestureRecognizer);
        }
    }

    /* Scrolls */

    @NonNull private UIPanGestureRecognizer mPanGestureRecognizer;
    @NonNull private CGPoint mContentOffset = new CGPoint(0, 0);
    @NonNull private CGSize mContentSize = new CGSize(0, 0);
    private boolean mTracking;
    private boolean mScrollEnabled;
    private boolean mAlwaysBounceVertical;
    private boolean mAlwaysBounceHorizontal;
    private boolean mBounces;
    private boolean mPagingEnabled;
    @Nullable private UIViewAnimation mCurrentAnimationY = null;
    @Nullable private UIViewAnimation mCurrentAnimationX = null;

    @Override
    public void touchesBegan(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesBegan(touches, event);
        if (mCurrentAnimationY != null) {
            mCurrentAnimationY.cancel();
            mCurrentAnimationY = null;
        }

        if (mCurrentAnimationX != null) {
            mCurrentAnimationX.cancel();
            mCurrentAnimationX = null;
        }
    }

    @Override
    public void touchesEnded(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesEnded(touches, event);
        if (mPanGestureRecognizer.getState() != UIGestureRecognizerState.Ended) {
//            if (yOutOfBounds(mContentOffset)) {
//                moveYToBounds();
//            }
//
//            if (xOutOfBounds(mContentOffset)) {
//                moveXToBounds();
//            }
        }
    }

    public void handlePan(UIPanGestureRecognizer panGestureRecognizer) {
        if (!mTracking) {
            /* Began */
            mTracking = true;
            panGestureRecognizer.setTranslation(mContentOffset);
            if (mCurrentAnimationY != null) {
                mCurrentAnimationY.cancel();
                mCurrentAnimationY = null;
            }

            if (mCurrentAnimationX != null) {
                mCurrentAnimationX.cancel();
                mCurrentAnimationX = null;
            }
            return;
        }
        if (mTracking && panGestureRecognizer.getState() == UIGestureRecognizerState.Changed) {
            /* Move */
            double originY = -(panGestureRecognizer.translation().y);
            double originX = -(panGestureRecognizer.translation().x);

            CGPoint offset = calculateMovePoint(new CGPoint(originX, originY));

            if (mAlwaysBounceHorizontal) {
                offset = offset.setX(overBoundsCheckX(offset.x));
            }
            if (mAlwaysBounceVertical) {
                offset = offset.setY(overBoundsCheckY(offset.y));
            }
            if (!mBounces) {
                offset = overBoundsCheck(offset);
            }

            setContentOffset(offset);
        }
        else if (panGestureRecognizer.getState() == UIGestureRecognizerState.Ended) {
            /* Ended */
            mTracking = false;
            CGPoint velocity = panGestureRecognizer.velocity();
            mCurrentAnimationY = UIView.animator.decayBounds(this, "contentOffset.y", mContentOffset.y, -velocity.y / 1000.0, 0.0, mContentSize.height < getFrame().size.height ? 0.0 : (mContentSize.height - getFrame().size.height), null);
            mCurrentAnimationX = UIView.animator.decayBounds(this, "contentOffset.x", mContentOffset.x, -velocity.x / 1000.0, 0.0, mContentSize.width < getFrame().size.width ? 0.0 : (mContentSize.width - getFrame().size.width), null);
        }
    }

    private CGPoint calculateMovePoint(CGPoint point) {
        double y = calculateY(point.y);
        double x = calculateX(point.x);
        return new CGPoint(x, y);
    }

    private double calculateY(double y) {
        if (mContentSize.height < getFrame().size.height) {
            double delta = y;
            return delta / 3.0;
        }
        else if (y < 0.0) {
            // out of top
            double delta = Math.abs(y);
            return -(delta / 3.0);
        }
        else if (y > mContentSize.height - getFrame().size.height) {
            // out of bottom
            double delta = y - (mContentSize.height - getFrame().size.height);
            return delta / 3.0;
        }
        return y;
    }

    private double calculateX(double x) {
        if (mContentSize.width < getFrame().size.width) {
            double delta = x;
            return delta / 3.0;
        }
        else if (x < 0.0) {
            double delta = Math.abs(x);
            return -(delta / 3.0);
        }
        else if (x > mContentSize.width - getFrame().size.width) {
            double delta = x - (mContentSize.width - getFrame().size.width);
            return delta / 3.0;
        }
        return x;
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
            if (mCurrentAnimationY != null) {
                mCurrentAnimationY.cancel();
            }
            mCurrentAnimationY = UIView.animator.linear(0.25, new Runnable() {
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

    private void moveYToBounds() {
        if (mCurrentAnimationY != null) {
            mCurrentAnimationY.cancel();
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
        mCurrentAnimationY = UIView.animator.springWithOptions(120.0, 20.0, 0.0, new Runnable() {
            @Override
            public void run() {
            setContentOffset(mContentOffset.setY(finalNearestBounds), false);
            }
        }, null);
    }

    private CGPoint overBoundsCheck(CGPoint point) {
        double nearestBoundsY = overBoundsCheckY(point.y);
        double nearestBoundsX = overBoundsCheckX(point.x);
        return new CGPoint(nearestBoundsX, nearestBoundsY);
    }

    private double overBoundsCheckX(double x) {
        double nearestBoundsX = x;
        //check x
        if (x < 0.0) {
            nearestBoundsX = 0.0;
        }
        else if (x > mContentSize.width - getFrame().size.width) {
            nearestBoundsX = mContentSize.width - getFrame().size.width;
        }
        return nearestBoundsX;
    }

    private double overBoundsCheckY(double y) {
        double nearestBoundsY = y;
        //check x
        if (y < 0.0) {
            nearestBoundsY = 0.0;
        }
        else if (y > mContentSize.height - getFrame().size.height) {
            nearestBoundsY = mContentSize.height - getFrame().size.height;
        }
        return nearestBoundsY;
    }
}
