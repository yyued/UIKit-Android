package com.yy.codex.uikit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.yy.codex.foundation.NSLog;

import java.util.Timer;
import java.util.TimerTask;

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
        mPagingEnabled = false;
        mBounces = true;
        mWindowSizePoint = new CGPoint(0, 0);
        mContentInset = new UIEdgeInsets(0, 0, 0, 0);
        mFingerVerticalMoveDistance = getFrame().size.height / 5;
        mFingerHorizontalMoveDistance = getFrame().size.width / 5;

        mPanGestureRecognizer = new UIPanGestureRecognizer(this, "handlePan:");
        if (mScrollEnabled) {
            addGestureRecognizer(mPanGestureRecognizer);
        }
    }

    /* Scrolls */

    @NonNull private UIPanGestureRecognizer mPanGestureRecognizer;
    @NonNull private CGPoint mContentOffset = new CGPoint(0, 0);
    @NonNull private CGSize mContentSize = new CGSize(0, 0);
    private UIEdgeInsets mContentInset;

    public void setAlwaysBounceHorizontal(boolean alwaysBounceHorizontal) {
        mAlwaysBounceHorizontal = alwaysBounceHorizontal;
    }

    public void setAlwaysBounceVertical(boolean alwaysBounceVertical) {
        mAlwaysBounceVertical = alwaysBounceVertical;
    }
    private boolean mTracking;
    private boolean mScrollEnabled;
    private boolean mAlwaysBounceVertical;
    private boolean mAlwaysBounceHorizontal;
    private boolean mBounces;
    private boolean mPagingEnabled;

    @Nullable private UIViewAnimation mCurrentAnimationY = null;
    @Nullable private UIViewAnimation mCurrentAnimationX = null;
    private double mVerticalMoveDiscance = 0;
    private double mHorizontalMoveDiscance = 0;
    private CGPoint mTrackingPoint;

    private CGPoint mWindowSizePoint;

    private double mFingerVerticalMoveDistance;
    private double mFingerHorizontalMoveDistance;
    private static final double FINGER_VELOCITY = 300;

    public void setPagingEnabled(boolean pagingEnabled) {
        this.mPagingEnabled = pagingEnabled;
    }

    public void setBounces(boolean mBounces) {
        this.mBounces = mBounces;
    }
    public boolean getBounces() {
        return this.mBounces;
    }

    @Override
    public void touchesBegan(UITouch[] touches, UIEvent event) {
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

    public void handlePan(UIPanGestureRecognizer panGestureRecognizer) {
        double originY = -panGestureRecognizer.translation().y;
        double originX = -panGestureRecognizer.translation().x;
        if (!mTracking) {
            /* Began */
            mTracking = true;
            mTrackingPoint = new CGPoint(originX, originY);
            panGestureRecognizer.setTranslation(mContentOffset);
            return;
        }
        if (mTracking && panGestureRecognizer.getState() == UIGestureRecognizerState.Changed) {
            /* Move */
            CGPoint offset = calculateMovePoint(new CGPoint(originX, originY), mPagingEnabled);

            mVerticalMoveDiscance = (originY + Math.abs(mTrackingPoint.y)) - mWindowSizePoint.y;
            mHorizontalMoveDiscance = (originX + Math.abs(mTrackingPoint.x)) - mWindowSizePoint.x;

            NSLog.log(offset);
            setContentOffset(offset);
        }
        else if (panGestureRecognizer.getState() == UIGestureRecognizerState.Ended) {
            /* Ended */
            mTracking = false;
            CGPoint velocity = panGestureRecognizer.velocity();

            if (mPagingEnabled) {
                calculateScrollPagingPoint(velocity);
                setContentOffsetWithSpring(mWindowSizePoint, velocity.x);
            }
            else {
                UIViewAnimator.UIViewAnimationDecayBoundsOptions xOptions = new UIViewAnimator.UIViewAnimationDecayBoundsOptions();
                xOptions.allowBounds = mBounces;
                xOptions.alwaysBounds = mAlwaysBounceHorizontal;
                xOptions.fromValue = mContentOffset.x;
                xOptions.velocity = -velocity.x / 1000.0;
                xOptions.topBounds = 0.0;
                xOptions.bottomBounds = mContentSize.width - getFrame().size.width;
                xOptions.viewBounds = getFrame().size.width;
                mCurrentAnimationX = UIView.animator.decayBounds(this, "contentOffset.x", xOptions, null);
                UIViewAnimator.UIViewAnimationDecayBoundsOptions yOptions = new UIViewAnimator.UIViewAnimationDecayBoundsOptions();
                yOptions.allowBounds = mBounces;
                yOptions.alwaysBounds = mAlwaysBounceVertical;
                yOptions.fromValue = mContentOffset.y;
                yOptions.velocity = -velocity.y / 1000.0;
                yOptions.topBounds = 0.0;
                yOptions.bottomBounds = mContentSize.height - getFrame().size.height;
                yOptions.viewBounds = getFrame().size.height;
                mCurrentAnimationY = UIView.animator.decayBounds(this, "contentOffset.y", yOptions, null);
            }
            mHorizontalMoveDiscance = 0;
            mVerticalMoveDiscance = 0;
        }
    }

    private void calculateScrollPagingPoint(CGPoint velocity) {
        int verticalPageCurrentIndex = (int)(mWindowSizePoint.y / getFrame().size.height);
        int horizontalPageCurrentIndex = (int)(mWindowSizePoint.x / getFrame().size.width);

        double moveOffsetX = horizontalPageCurrentIndex * getFrame().size.width;
        double moveOffsetY = verticalPageCurrentIndex * getFrame().size.height;
        if ((Math.abs(mHorizontalMoveDiscance) > mFingerHorizontalMoveDistance || Math.abs(mVerticalMoveDiscance) > mFingerVerticalMoveDistance ) || (Math.abs(velocity.x) > FINGER_VELOCITY || Math.abs(velocity.y) > FINGER_VELOCITY )) {
            verticalPageCurrentIndex = mVerticalMoveDiscance > 0 ? ++verticalPageCurrentIndex : --verticalPageCurrentIndex;
            horizontalPageCurrentIndex = mHorizontalMoveDiscance > 0 ? ++horizontalPageCurrentIndex : --horizontalPageCurrentIndex;
            if (verticalPageCurrentIndex < 0) {
                verticalPageCurrentIndex = 0;
            }
            if (horizontalPageCurrentIndex < 0) {
                horizontalPageCurrentIndex = 0;
            }

            moveOffsetX = horizontalPageCurrentIndex * getFrame().size.width;
            moveOffsetY = verticalPageCurrentIndex * getFrame().size.height;
        }

        CGPoint offset = calculateMovePoint(new CGPoint(moveOffsetX, moveOffsetY), mPagingEnabled);
        mWindowSizePoint = offset;
    }

    private CGPoint calculateMovePoint(CGPoint point, boolean PagingEnabled) {
        double y = calculateY(point.y);
        double x = calculateX(point.x);
        return new CGPoint(x, y);
    }

    private double calculateY(double y) {
        return calculateXY(y, false);
    }

    private double calculateX(double x) {
        return calculateXY(x, true);
    }

    private double calculateXY(double xOry, boolean isX) {
        double contentSizeWidth = mContentSize.width;
        double contentSizeHeight = mContentSize.height;

        double thisWidth = getFrame().size.width;
        double thisHeight = getFrame().size.height;

        double calculateContentSizeValue = isX ? contentSizeWidth : contentSizeHeight;
        double calculateThisValue = isX ? thisWidth : thisHeight;

        boolean mAlwaysBounceOrientation = isX ? mAlwaysBounceHorizontal : mAlwaysBounceVertical;

        double retValue = xOry;
        double deltaBottom = calculateContentSizeValue - calculateThisValue;
        double over = xOry - deltaBottom;

        if (calculateContentSizeValue < calculateThisValue) {
            retValue = 0;
            if (mBounces && mAlwaysBounceOrientation) {
                retValue = xOry / 3.0;
            }
        }
        else {
            // out of top
            if (xOry < 0.0) {
                retValue = 0;
                if (mBounces) {
                    // can Bounces
                    retValue = xOry / 3.0;
                }
            }

            //out of bottom
            if (xOry > Math.abs(calculateContentSizeValue - calculateThisValue)) {
                retValue = deltaBottom;
                if (mBounces) {
                    // can Bounces
                    retValue = deltaBottom + (over / 3.0);
                }
            }
        }
        return retValue;
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
        final CGPoint oldValue = mContentOffset;
        final UIScrollView self = this;
        mContentOffset = contentOffset;
        if (animated) {
            if (mCurrentAnimationY != null) {
                mCurrentAnimationY.cancel();
            }
            mCurrentAnimationY = UIView.animator.linear(0.25, new Runnable() {
                @Override
                public void run() {
                    UIView.animator.addAnimationState(self, "contentOffset.x", oldValue.x, mContentOffset.x);
                    UIView.animator.addAnimationState(self, "contentOffset.y", oldValue.y, mContentOffset.y);
                }
            }, null);
        }
        else {
            scrollTo((int)(mContentOffset.x * UIScreen.mainScreen.scale()), (int)(mContentOffset.y * UIScreen.mainScreen.scale()));
            UIView.animator.addAnimationState(self, "contentOffset.x", oldValue.x, mContentOffset.x);
            UIView.animator.addAnimationState(self, "contentOffset.y", oldValue.y, mContentOffset.y);
        }
    }

    private void setContentOffsetWithSpring(@NonNull final CGPoint contentOffset, double velocity) {
        mCurrentAnimationY = UIView.animator.springWithOptions(120, 20, velocity, new Runnable() {
            @Override
            public void run() {
                setContentOffset(contentOffset, false);
            }
        }, null);
    }

    public void setContentSize(@NonNull CGSize contentSize) {
        mContentSize = contentSize;
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
        else if (x > mContentSize.width - getFrame().size.width && mContentSize.width > 0) {
            nearestBoundsX = mContentSize.width - getFrame().size.width;
        }
        return nearestBoundsX;
    }

    private double overBoundsCheckY(double y) {
        double nearestBoundsY = y;
        if (y < 0.0) {
            nearestBoundsY = 0.0;
        }
        else if (y > mContentSize.height - getFrame().size.height && mContentSize.height > 0) {
            nearestBoundsY = mContentSize.height - getFrame().size.height;
        }
        return nearestBoundsY;
    }
}
