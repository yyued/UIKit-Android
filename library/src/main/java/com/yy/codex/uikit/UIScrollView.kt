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
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    /* Scrolls */

    private var panGestureRecognizer: UIPanGestureRecognizer? = null
    var contentOffset = CGPoint(0.0, 0.0)
        private set
    var contentSize = CGSize(0.0, 0.0)
    private var contentInset = UIEdgeInsets.zero

    protected open var delegate: UIScrollViewDelegate? = null

    public var tracking: Boolean = false
    private var scrollEnabled: Boolean = false
    public var alwaysBounceVertical: Boolean = false
    public var alwaysBounceHorizontal: Boolean = false
    var bounces: Boolean = false
    private var pagingEnabled: Boolean = false
    public var decelerating: Boolean = false
    public var dragging: Boolean = false

    /*Animation*/
    private var currentAnimationY: UIViewAnimation? = null
    private var currentAnimationX: UIViewAnimation? = null
    private var verticalMoveDiscance = 0.0
    private var horizontalMoveDiscance = 0.0
    private var trackingPoint: CGPoint? = null

    private var windowSizePoint: CGPoint? = null

    private var fingerVerticalMoveDistance = 0.00
    private var fingerHorizontalMoveDistance = 0.00

    override fun init() {
        super.init()

        tracking = false
        scrollEnabled = true
        alwaysBounceVertical = false
        alwaysBounceHorizontal = false
        pagingEnabled = false
        bounces = true
        windowSizePoint = CGPoint(0.0, 0.0)
        this.contentInset = UIEdgeInsets(0.0, 0.0, 0.0, 0.0)
        fingerVerticalMoveDistance = frame.size.height / 5
        fingerHorizontalMoveDistance = frame.size.width / 5

        panGestureRecognizer = UIPanGestureRecognizer(this, "handlePan:")
        if (scrollEnabled) {
            panGestureRecognizer?.let {
                addGestureRecognizer(it)
            }
        }
    }

    override fun touchesBegan(touches: List<UITouch>, event: UIEvent) {
        super.touchesBegan(touches, event)
        if (currentAnimationY != null) {
            currentAnimationY!!.cancel()
            currentAnimationY = null
        }

        if (currentAnimationX != null) {
            currentAnimationX!!.cancel()
            currentAnimationX = null
        }

        tracking = true
        decelerating = false
    }

    fun handlePan(panGestureRecognizer: UIPanGestureRecognizer) {
        val originY = -panGestureRecognizer.translation().y
        val originX = -panGestureRecognizer.translation().x
        if (!dragging) {
            /* Began */
            dragging = true
            trackingPoint = CGPoint(originX, originY)
            panGestureRecognizer.setTranslation(contentOffset)
            if (delegate != null) {
                delegate!!.scrollViewWillBeginDragging(this)
            }
            return
        }
        if (dragging && panGestureRecognizer.state === UIGestureRecognizerState.Changed) {
            /* Move */
            val offset = calculateMovePoint(CGPoint(originX, originY), pagingEnabled)

            verticalMoveDiscance = originY + Math.abs(trackingPoint!!.y) - windowSizePoint!!.y
            horizontalMoveDiscance = originX + Math.abs(trackingPoint!!.x) - windowSizePoint!!.x

            setContentOffset(offset)
        } else if (panGestureRecognizer.state === UIGestureRecognizerState.Ended) {
            /* Ended */
            dragging = false
            tracking = false
            decelerating = true
            delegate?.let {
                it.scrollViewDidEndDragging(this, false)
            }

            val velocity = panGestureRecognizer.velocity()

            if (pagingEnabled) {
                calculateScrollPagingPoint(velocity)
                setContentOffsetWithSpring(windowSizePoint!!, velocity.x)
            } else {
                val xOptions = UIViewAnimator.UIViewAnimationDecayBoundsOptions()
                xOptions.allowBounds = bounces
                xOptions.alwaysBounds = alwaysBounceHorizontal
                xOptions.fromValue = contentOffset.x
                xOptions.velocity = -velocity.x / 1000.0
                xOptions.topBounds = 0.0
                xOptions.bottomBounds = contentSize.width - frame.size.width
                xOptions.viewBounds = frame.size.width
                currentAnimationX = UIViewAnimator.decayBounds(this, "contentOffset.x", xOptions, null)
                val yOptions = UIViewAnimator.UIViewAnimationDecayBoundsOptions()
                yOptions.allowBounds = bounces
                yOptions.alwaysBounds = alwaysBounceVertical
                yOptions.fromValue = contentOffset.y
                yOptions.velocity = -velocity.y / 1000.0
                yOptions.topBounds = 0.0
                yOptions.bottomBounds = contentSize.height + this.contentInset!!.bottom - frame.size.height
                yOptions.viewBounds = frame.size.height
                currentAnimationY = UIViewAnimator.decayBounds(this, "contentOffset.y", yOptions, null)
            }
            horizontalMoveDiscance = 0.0
            verticalMoveDiscance = 0.0
        }
    }

    private fun calculateScrollPagingPoint(velocity: CGPoint) {
        var verticalPageCurrentIndex = Math.round(windowSizePoint!!.y / frame.size.height).toInt()
        var horizontalPageCurrentIndex = Math.round(windowSizePoint!!.x / frame.size.width).toInt()

        var moveOffsetX = horizontalPageCurrentIndex * frame.size.width
        var moveOffsetY = verticalPageCurrentIndex * frame.size.height
        if (Math.abs(horizontalMoveDiscance) > fingerHorizontalMoveDistance || Math.abs(verticalMoveDiscance) > fingerVerticalMoveDistance || Math.abs(velocity.x) > FINGER_VELOCITY || Math.abs(velocity.y) > FINGER_VELOCITY) {
            verticalPageCurrentIndex = if (verticalMoveDiscance > 0) ++verticalPageCurrentIndex else --verticalPageCurrentIndex
            horizontalPageCurrentIndex = if (horizontalMoveDiscance > 0) ++horizontalPageCurrentIndex else --horizontalPageCurrentIndex
            if (verticalPageCurrentIndex < 0) {
                verticalPageCurrentIndex = 0
            }
            if (horizontalPageCurrentIndex < 0) {
                horizontalPageCurrentIndex = 0
            }

            moveOffsetX = horizontalPageCurrentIndex * frame.size.width
            moveOffsetY = verticalPageCurrentIndex * frame.size.height
        }

        val offset = calculateMovePoint(CGPoint(moveOffsetX, moveOffsetY), pagingEnabled)
        windowSizePoint = offset
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

        val mAlwaysBounceOrientation = if (isX) alwaysBounceHorizontal else alwaysBounceVertical

        var retValue = xOry
        val deltaBottom = calculateContentSizeValue + this.contentInset!!.bottom + this.contentInset!!.top - calculateThisValue
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
            if (xOry > Math.abs(calculateContentSizeValue + this.contentInset!!.bottom + this.contentInset!!.top - calculateThisValue)) {
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
            setContentOffset(contentOffset.setX(aValue.toDouble()), false)
        } else if (aKey.equals("contentOffset.y", ignoreCase = true)) {
            setContentOffset(contentOffset.setY(aValue.toDouble()), false)
        }
    }

    open fun setContentOffset(contentOffset: CGPoint, animated: Boolean = false) {
        val oldValue = this.contentOffset
        val self = this
        this.contentOffset = contentOffset
        if (animated) {
            if (currentAnimationY != null) {
                currentAnimationY!!.cancel()
            }
            currentAnimationY = UIViewAnimator.linear(0.25, Runnable {
                UIViewAnimator.addAnimationState(self, "contentOffset.x", oldValue.x, this.contentOffset.x)
                UIViewAnimator.addAnimationState(self, "contentOffset.y", oldValue.y, this.contentOffset.y)
            }, null)
        } else {
            scrollTo((this.contentOffset.x * UIScreen.mainScreen.scale()).toInt(), (this.contentOffset.y * UIScreen.mainScreen.scale() - this.contentInset!!.top).toInt())
            if (delegate != null) {
                delegate!!.scrollViewDidScroll(this)
            }
            UIViewAnimator.addAnimationState(self, "contentOffset.x", oldValue.x, this.contentOffset.x)
            UIViewAnimator.addAnimationState(self, "contentOffset.y", oldValue.y, this.contentOffset.y)
        }
    }

    private fun setContentOffsetWithSpring(contentOffset: CGPoint, velocity: Double) {
        currentAnimationY = UIViewAnimator.springWithOptions(120.0, 20.0, Runnable { setContentOffset(contentOffset, false) }, null)
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
