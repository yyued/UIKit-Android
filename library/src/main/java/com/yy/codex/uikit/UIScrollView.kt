package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

import com.yy.codex.foundation.NSLog

import java.util.Timer
import java.util.TimerTask

/**
 * Created by it on 17/1/6.
 */

open class UIScrollView : UIView {

    open interface UIScrollViewDelegate {
        fun scrollViewDidScroll(scrollView: UIScrollView)
        fun scrollViewWillBeginDragging(scrollView: UIScrollView)
        fun scrollViewDidEndDragging(scrollView: UIScrollView, willDecelerate: Boolean)
        fun scrollViewWillBeginDecelerating(scrollView: UIScrollView)
        fun scrollViewDidEndDecelerating(scrollView: UIScrollView)
    }

    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun init() {
        super.init()

        mTracking = false
        mScrollEnabled = true
        mAlwaysBounceVertical = false
        mAlwaysBounceHorizontal = false
        mPagingEnabled = false
        bounces = true
        mWindowSizePoint = CGPoint(0.0, 0.0)
        mContentInset = UIEdgeInsets(0.0, 0.0, 0.0, 0.0)
        mFingerVerticalMoveDistance = frame.size.height / 5
        mFingerHorizontalMoveDistance = frame.size.width / 5

        mPanGestureRecognizer = UIPanGestureRecognizer(this, "handlePan:")
        if (mScrollEnabled) {
            mPanGestureRecognizer?.let {
                addGestureRecognizer(it)
            }
        }
    }

    /* Scrolls */

    private var mPanGestureRecognizer: UIPanGestureRecognizer? = null
    private var mContentOffset = CGPoint(0.0, 0.0)
    var contentSize = CGSize(0.0, 0.0)
    private var mContentInset: UIEdgeInsets = UIEdgeInsets.zero

    protected open var delegate: UIScrollViewDelegate? = null

    var contentInset: UIEdgeInsets
        get() = this.mContentInset
        set(contentInset) {
            this.mContentInset = contentInset
            setContentOffset(mContentOffset, false)
        }

    fun setAlwaysBounceHorizontal(alwaysBounceHorizontal: Boolean) {
        mAlwaysBounceHorizontal = alwaysBounceHorizontal
    }

    fun setAlwaysBounceVertical(alwaysBounceVertical: Boolean) {
        mAlwaysBounceVertical = alwaysBounceVertical
    }

    public var mTracking: Boolean = false
    private var mScrollEnabled: Boolean = false
    private var mAlwaysBounceVertical: Boolean = false
    private var mAlwaysBounceHorizontal: Boolean = false
    var bounces: Boolean = false
    private var mPagingEnabled: Boolean = false
    public var decelerating: Boolean = false
    public var dragging: Boolean = false

    private var mCurrentAnimationY: UIViewAnimation? = null
    private var mCurrentAnimationX: UIViewAnimation? = null
    private var mVerticalMoveDiscance = 0.0
    private var mHorizontalMoveDiscance = 0.0
    private var mTrackingPoint: CGPoint? = null

    private var mWindowSizePoint: CGPoint? = null

    private var mFingerVerticalMoveDistance: Double = 0.toDouble()
    private var mFingerHorizontalMoveDistance: Double = 0.toDouble()

    fun setPagingEnabled(pagingEnabled: Boolean) {
        this.mPagingEnabled = pagingEnabled
    }

    override fun touchesBegan(touches: List<UITouch>, event: UIEvent) {
        super.touchesBegan(touches, event)
        if (mCurrentAnimationY != null) {
            mCurrentAnimationY!!.cancel()
            mCurrentAnimationY = null
        }

        if (mCurrentAnimationX != null) {
            mCurrentAnimationX!!.cancel()
            mCurrentAnimationX = null
        }

        mTracking = true
        decelerating = false
    }

    fun handlePan(panGestureRecognizer: UIPanGestureRecognizer) {
        val originY = -panGestureRecognizer.translation().y
        val originX = -panGestureRecognizer.translation().x
        if (!dragging) {
            /* Began */
            dragging = true
            mTrackingPoint = CGPoint(originX, originY)
            panGestureRecognizer.setTranslation(mContentOffset)
            if (delegate != null) {
                delegate!!.scrollViewWillBeginDragging(this)
            }
            return
        }
        if (dragging && panGestureRecognizer.state === UIGestureRecognizerState.Changed) {
            /* Move */
            val offset = calculateMovePoint(CGPoint(originX, originY), mPagingEnabled)

            mVerticalMoveDiscance = originY + Math.abs(mTrackingPoint!!.y) - mWindowSizePoint!!.y
            mHorizontalMoveDiscance = originX + Math.abs(mTrackingPoint!!.x) - mWindowSizePoint!!.x

            setContentOffset(offset)
        } else if (panGestureRecognizer.state === UIGestureRecognizerState.Ended) {
            /* Ended */
            dragging = false
            mTracking = false
            decelerating = true
            delegate?.let {
                it.scrollViewDidEndDragging(this, false)
            }

            val velocity = panGestureRecognizer.velocity()

            if (mPagingEnabled) {
                calculateScrollPagingPoint(velocity)
                setContentOffsetWithSpring(mWindowSizePoint!!, velocity.x)
            } else {
                val xOptions = UIViewAnimator.UIViewAnimationDecayBoundsOptions()
                xOptions.allowBounds = bounces
                xOptions.alwaysBounds = mAlwaysBounceHorizontal
                xOptions.fromValue = mContentOffset.x
                xOptions.velocity = -velocity.x / 1000.0
                xOptions.topBounds = 0.0
                xOptions.bottomBounds = contentSize.width - frame.size.width
                xOptions.viewBounds = frame.size.width
                mCurrentAnimationX = UIViewAnimator.decayBounds(this, "contentOffset.x", xOptions, null)
                val yOptions = UIViewAnimator.UIViewAnimationDecayBoundsOptions()
                yOptions.allowBounds = bounces
                yOptions.alwaysBounds = mAlwaysBounceVertical
                yOptions.fromValue = mContentOffset.y
                yOptions.velocity = -velocity.y / 1000.0
                yOptions.topBounds = 0.0
                yOptions.bottomBounds = contentSize.height + mContentInset!!.bottom - frame.size.height
                yOptions.viewBounds = frame.size.height
                mCurrentAnimationY = UIViewAnimator.decayBounds(this, "contentOffset.y", yOptions, null)
            }
            mHorizontalMoveDiscance = 0.0
            mVerticalMoveDiscance = 0.0
        }
    }

    private fun calculateScrollPagingPoint(velocity: CGPoint) {
        var verticalPageCurrentIndex = Math.round(mWindowSizePoint!!.y / frame.size.height).toInt()
        var horizontalPageCurrentIndex = Math.round(mWindowSizePoint!!.x / frame.size.width).toInt()

        var moveOffsetX = horizontalPageCurrentIndex * frame.size.width
        var moveOffsetY = verticalPageCurrentIndex * frame.size.height
        if (Math.abs(mHorizontalMoveDiscance) > mFingerHorizontalMoveDistance || Math.abs(mVerticalMoveDiscance) > mFingerVerticalMoveDistance || Math.abs(velocity.x) > FINGER_VELOCITY || Math.abs(velocity.y) > FINGER_VELOCITY) {
            verticalPageCurrentIndex = if (mVerticalMoveDiscance > 0) ++verticalPageCurrentIndex else --verticalPageCurrentIndex
            horizontalPageCurrentIndex = if (mHorizontalMoveDiscance > 0) ++horizontalPageCurrentIndex else --horizontalPageCurrentIndex
            if (verticalPageCurrentIndex < 0) {
                verticalPageCurrentIndex = 0
            }
            if (horizontalPageCurrentIndex < 0) {
                horizontalPageCurrentIndex = 0
            }

            moveOffsetX = horizontalPageCurrentIndex * frame.size.width
            moveOffsetY = verticalPageCurrentIndex * frame.size.height
        }

        val offset = calculateMovePoint(CGPoint(moveOffsetX, moveOffsetY), mPagingEnabled)
        mWindowSizePoint = offset
    }

    private fun calculateMovePoint(point: CGPoint, PagingEnabled: Boolean): CGPoint {
        val y = calculateY(point.y)
        val x = calculateX(point.x)
        return CGPoint(x, y)
    }

    private fun calculateY(y: Double): Double {
        return calculateXY(y, false)
    }

    private fun calculateX(x: Double): Double {
        return calculateXY(x, true)
    }

    private fun calculateXY(xOry: Double, isX: Boolean): Double {
        val contentSizeWidth = contentSize.width
        val contentSizeHeight = contentSize.height

        val thisWidth = frame.size.width
        val thisHeight = frame.size.height

        val calculateContentSizeValue = if (isX) contentSizeWidth else contentSizeHeight
        val calculateThisValue = if (isX) thisWidth else thisHeight

        val mAlwaysBounceOrientation = if (isX) mAlwaysBounceHorizontal else mAlwaysBounceVertical

        var retValue = xOry
        val deltaBottom = calculateContentSizeValue + mContentInset!!.bottom + mContentInset!!.top - calculateThisValue
        val over = xOry - deltaBottom

        if (calculateContentSizeValue < calculateThisValue) {
            retValue = 0.0
            if (bounces && mAlwaysBounceOrientation) {
                retValue = xOry / 3.0// add
            }
        } else {
            // out of top
            if (xOry < 0.0) {
                retValue = 0.0
                if (bounces) {
                    // can Bounces
                    retValue = xOry / 3.0
                }
            }

            //out of bottom
            if (xOry > Math.abs(calculateContentSizeValue + mContentInset!!.bottom + mContentInset!!.top - calculateThisValue)) {
                retValue = deltaBottom
                if (bounces) {
                    // can Bounces
                    retValue = deltaBottom + over / 3.0
                }
            }
        }
        return retValue
    }

    override fun animate(aKey: String, aValue: Float) {
        super.animate(aKey, aValue)
        if (aKey.equals("contentOffset.x", ignoreCase = true)) {
            setContentOffset(mContentOffset.setX(aValue.toDouble()), false)
        } else if (aKey.equals("contentOffset.y", ignoreCase = true)) {
            setContentOffset(mContentOffset.setY(aValue.toDouble()), false)
        }
    }

    @JvmOverloads open fun setContentOffset(contentOffset: CGPoint, animated: Boolean = false) {
        val oldValue = mContentOffset
        val self = this
        mContentOffset = contentOffset
        if (animated) {
            if (mCurrentAnimationY != null) {
                mCurrentAnimationY!!.cancel()
            }
            mCurrentAnimationY = UIViewAnimator.linear(0.25, Runnable {
                UIViewAnimator.addAnimationState(self, "contentOffset.x", oldValue.x, mContentOffset.x)
                UIViewAnimator.addAnimationState(self, "contentOffset.y", oldValue.y, mContentOffset.y)
            }, null)
        } else {
            scrollTo((mContentOffset.x * UIScreen.mainScreen.scale()).toInt(), (mContentOffset.y * UIScreen.mainScreen.scale() - mContentInset!!.top).toInt())
            if (delegate != null) {
                delegate!!.scrollViewDidScroll(this)
            }
            UIViewAnimator.addAnimationState(self, "contentOffset.x", oldValue.x, mContentOffset.x)
            UIViewAnimator.addAnimationState(self, "contentOffset.y", oldValue.y, mContentOffset.y)
        }
    }

    private fun setContentOffsetWithSpring(contentOffset: CGPoint, velocity: Double) {
        mCurrentAnimationY = UIViewAnimator.springWithOptions(120.0, 20.0, Runnable { setContentOffset(contentOffset, false) }, null)
    }

    private fun overBoundsCheck(point: CGPoint): CGPoint {
        val nearestBoundsY = overBoundsCheckY(point.y)
        val nearestBoundsX = overBoundsCheckX(point.x)
        return CGPoint(nearestBoundsX, nearestBoundsY)
    }

    private fun overBoundsCheckX(x: Double): Double {
        var nearestBoundsX = x
        //check x
        if (x < 0.0) {
            nearestBoundsX = 0.0
        } else if (x > contentSize.width - frame.size.width && contentSize.width > 0) {
            nearestBoundsX = contentSize.width - frame.size.width
        }
        return nearestBoundsX
    }

    private fun overBoundsCheckY(y: Double): Double {
        var nearestBoundsY = y
        if (y < 0.0) {
            nearestBoundsY = 0.0
        } else if (y > contentSize.height - frame.size.height && contentSize.height > 0) {
            nearestBoundsY = contentSize.height - frame.size.height
        }
        return nearestBoundsY
    }

    companion object {
        private val FINGER_VELOCITY = 300.0
    }
}
