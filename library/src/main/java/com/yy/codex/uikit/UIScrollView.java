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
    private boolean mTracking;
    private boolean mScrollEnabled = true;
    private boolean mAlwaysBounceVertical = false;
    private boolean mAlwaysBounceHorizontal = false;
    private boolean mBounces = true;
    private boolean mPagingEnabled = false;

    public void setAlwaysBounceHorizontal(boolean alwaysBounceHorizontal) {
        mAlwaysBounceHorizontal = alwaysBounceHorizontal;
    }

    public void setAlwaysBounceVertical(boolean alwaysBounceVertical) {
        mAlwaysBounceVertical = alwaysBounceVertical;
    }

    @Nullable private UIViewAnimation mCurrentAnimationY = null;
    @Nullable private UIViewAnimation mCurrentAnimationX = null;
    private double mVerticalMoveDiscance = 0;
    private double mHorizontalMoveDiscance = 0;
    private CGPoint mTrackingPoint;

    private CGPoint mWindowSizePoint;

    private static final double FINGER_VERTICAL_MOVE_DISTANCE = 250;
    private static final double FINGER_VELOCITY = 300;
    private static final double FINGER_HORIZONTAL_MOVE_DISTANCE = 120;

    public void setPagingEnabled(boolean pagingEnabled) {
        this.mPagingEnabled = pagingEnabled;
    }

    public void setBounces(boolean mBounces) {
        this.mBounces = mBounces;
    }
    public boolean getBounces() {
        return this.mBounces;
    }

//    @Override
//    public void addSubview(UIView subview) {
//        subview.
//
//        mContentInset.
//
//
//        super.addSubview(subview);
//
//
//    }

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

            if (mPagingEnabled) {
                int verticalPageCurrentIndex = (int)(mWindowSizePoint.y / getFrame().size.height);
                int horizontalPageCurrentIndex = (int)(mWindowSizePoint.x / getFrame().size.width);

                double moveOffsetX = horizontalPageCurrentIndex * getFrame().size.width;
                double moveOffsetY = verticalPageCurrentIndex * getFrame().size.height;
                if ((Math.abs(mHorizontalMoveDiscance) > FINGER_VERTICAL_MOVE_DISTANCE || Math.abs(mVerticalMoveDiscance) > FINGER_HORIZONTAL_MOVE_DISTANCE) || (Math.abs(velocity.x) > FINGER_VELOCITY || Math.abs(velocity.y) > FINGER_VELOCITY )) {
                    verticalPageCurrentIndex = mVerticalMoveDiscance > 0 ? ++verticalPageCurrentIndex : --verticalPageCurrentIndex;
                    horizontalPageCurrentIndex = mHorizontalMoveDiscance > 0 ? ++horizontalPageCurrentIndex : --horizontalPageCurrentIndex;
                    if (verticalPageCurrentIndex < 0) {
                        verticalPageCurrentIndex = 0;
                    }
                    if (horizontalPageCurrentIndex < 0) {
                        horizontalPageCurrentIndex = 0;
                    }

                    NSLog.log(horizontalPageCurrentIndex);
                    moveOffsetX = horizontalPageCurrentIndex * getFrame().size.width;
                    moveOffsetY = verticalPageCurrentIndex * getFrame().size.height;
                }

                CGPoint offset = calculateMovePoint(new CGPoint(moveOffsetX, moveOffsetY), mPagingEnabled);
                NSLog.log(offset);
                NSLog.log(velocity.x);
                mWindowSizePoint = offset;
                setContentOffset(offset, true);
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

        if (xOry < 0.0) {
            // out of top
            if (calculateContentSizeValue > calculateThisValue) {
                double delta = Math.abs(xOry);
                return -(delta / 3.0);
            }
            return 0;
        }

        if (xOry > 0.0 && calculateContentSizeValue == 0) {
            return 0;
        }

        if (xOry > Math.abs(calculateContentSizeValue - calculateThisValue)) {
            double delta = xOry - (calculateContentSizeValue - calculateThisValue);
            return delta / 3.0;
        }

        return xOry;
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
        else if (y > mContentSize.height - getFrame().size.height) {
            nearestBoundsY = mContentSize.height - getFrame().size.height;
        }
        return nearestBoundsY;
    }
}
