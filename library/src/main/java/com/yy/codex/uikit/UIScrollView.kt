package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

import com.yy.codex.foundation.NSLog
import com.yy.codex.foundation.lets

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
        set(value) {
            field = value
            if (contentSize.width < contentOffset.x + frame.width) {
                setContentOffset(CGPoint(Math.max(0.0, contentSize.width - frame.width), contentOffset.y), true)
            }
            if (contentSize.height < contentOffset.y + frame.height) {
                setContentOffset(CGPoint(contentOffset.x, Math.max(0.0, contentSize.height - frame.height)), true)
            }
        }

    var contentInset = UIEdgeInsets.zero

    open var delegate: UIScrollViewDelegate? = null

    var tracking: Boolean = false
        private set

    var scrollEnabled: Boolean = false

    var alwaysBounceVertical: Boolean = false

    var alwaysBounceHorizontal: Boolean = false

    var bounces: Boolean = false

    var pagingEnabled: Boolean = false

    var decelerating: Boolean = false
        private set

    var dragging: Boolean = false
        private set

    /* Private */

    private var currentAnimationY: UIViewAnimation? = null
    private var currentAnimationX: UIViewAnimation? = null
    private var verticalMoveDistance = 0.0
    private var horizontalMoveDistance = 0.0
    private var trackingPoint: CGPoint? = null
    private var windowSizePoint: CGPoint? = null
    private var fingerVerticalMoveDistance = 0.00
    private var fingerHorizontalMoveDistance = 0.00
    private lateinit var horizontalScrollIndicator: UIView
    private lateinit var verticalScrollIndicator: UIView

    override fun init() {
        super.init()
        tracking = false
        scrollEnabled = true
        alwaysBounceVertical = false
        alwaysBounceHorizontal = false
        pagingEnabled = false
        bounces = true
        windowSizePoint = CGPoint(0.0, 0.0)
        contentInset = UIEdgeInsets(0.0, 0.0, 0.0, 0.0)
        fingerVerticalMoveDistance = frame.size.height / 5.0
        fingerHorizontalMoveDistance = frame.size.width / 5.0
        panGestureRecognizer = UIPanGestureRecognizer(this, "handlePan:")
        if (scrollEnabled) {
            panGestureRecognizer?.let {
                addGestureRecognizer(it)
            }
        }
        horizontalScrollIndicator = UIView(context)
        verticalScrollIndicator = UIView(context)
        addSubview(horizontalScrollIndicator)
        addSubview(verticalScrollIndicator)
    }

    private var _bounceAnimationCancelled = false

    override fun touchesBegan(touches: List<UITouch>, event: UIEvent) {
        super.touchesBegan(touches, event)
        _bounceAnimationCancelled = checkOutOfBounds()
        currentAnimationY?.let(UIViewAnimation::cancel)
        currentAnimationY = null
        currentAnimationX?.let(UIViewAnimation::cancel)
        currentAnimationX = null
        tracking = true
        decelerating = false
    }

    override fun touchesEnded(touches: List<UITouch>, event: UIEvent) {
        super.touchesEnded(touches, event)
        if (_bounceAnimationCancelled && checkOutOfBounds()) {
            setContentOffsetWithSpring(requestBoundsPoint(contentOffset))
        }
    }

    fun handlePan(panGestureRecognizer: UIPanGestureRecognizer) {
        val originY = -panGestureRecognizer.translation().y
        val originX = -panGestureRecognizer.translation().x
        if (!dragging) {
            /* Began */
            dragging = true
            trackingPoint = CGPoint(originX, originY)
            panGestureRecognizer.setTranslation(contentOffset)
            delegate?.let { it.scrollViewWillBeginDragging(this) }
            showScrollIndicator()
            return
        }
        if (dragging && panGestureRecognizer.state === UIGestureRecognizerState.Changed) {
            /* Move */
            val offset = computeMovePoint(CGPoint(originX, originY), pagingEnabled)
            lets(trackingPoint, windowSizePoint) { trackingPoint, windowSizePoint ->
                verticalMoveDistance = originY + Math.abs(trackingPoint.y) - windowSizePoint.y
                horizontalMoveDistance = originX + Math.abs(trackingPoint.x) - windowSizePoint.x
            }
            setContentOffset(offset)
            updateScrollIndicator()
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
                computeScrollPagingPoint(velocity)
                windowSizePoint?.let {
                    setContentOffsetWithSpring(it)
                }
            } else {
                currentAnimationX?.let { it.cancel() }
                currentAnimationY?.let { it.cancel() }
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
                yOptions.bottomBounds = contentSize.height + contentInset.bottom - frame.size.height
                yOptions.viewBounds = frame.size.height
                currentAnimationY = UIViewAnimator.decayBounds(this, "contentOffset.y", yOptions, null)
            }
            horizontalMoveDistance = 0.0
            verticalMoveDistance = 0.0
            hideScrollIndicator()
        }
    }

    private fun computeScrollPagingPoint(velocity: CGPoint) {
        var verticalPageCurrentIndex = Math.round((windowSizePoint?.y ?: 0.0) / frame.size.height).toInt()
        var horizontalPageCurrentIndex = Math.round((windowSizePoint?.x ?: 0.0) / frame.size.width).toInt()
        var moveOffsetX = horizontalPageCurrentIndex * frame.size.width
        var moveOffsetY = verticalPageCurrentIndex * frame.size.height
        if (Math.abs(horizontalMoveDistance) > fingerHorizontalMoveDistance || Math.abs(verticalMoveDistance) > fingerVerticalMoveDistance || Math.abs(velocity.x) > FINGER_VELOCITY || Math.abs(velocity.y) > FINGER_VELOCITY) {
            verticalPageCurrentIndex = if (verticalMoveDistance > 0) ++verticalPageCurrentIndex else --verticalPageCurrentIndex
            horizontalPageCurrentIndex = if (horizontalMoveDistance > 0) ++horizontalPageCurrentIndex else --horizontalPageCurrentIndex
            if (verticalPageCurrentIndex < 0) {
                verticalPageCurrentIndex = 0
            }
            if (horizontalPageCurrentIndex < 0) {
                horizontalPageCurrentIndex = 0
            }

            moveOffsetX = horizontalPageCurrentIndex * frame.size.width
            moveOffsetY = verticalPageCurrentIndex * frame.size.height
        }
        val offset = computeMovePoint(CGPoint(moveOffsetX, moveOffsetY), pagingEnabled)
        windowSizePoint = offset
    }

    private fun computeMovePoint(point: CGPoint, pagingEnabled: Boolean): CGPoint {
        val y = computeY(point.y)
        val x = computeX(point.x)
        return CGPoint(x, y)
    }

    private fun computeY(y: Double): Double {
        return computeXY(y, false)
    }

    private fun computeX(x: Double): Double {
        return computeXY(x, true)
    }

    private fun computeXY(xOry: Double, isX: Boolean): Double {
        val contentSizeWidth = contentSize.width
        val contentSizeHeight = contentSize.height

        val thisWidth = frame.size.width
        val thisHeight = frame.size.height

        val calculateContentSizeValue = if (isX) contentSizeWidth else contentSizeHeight
        val calculateThisValue = if (isX) thisWidth else thisHeight

        val mAlwaysBounceOrientation = if (isX) alwaysBounceHorizontal else alwaysBounceVertical

        var retValue = xOry
        val deltaBottom = calculateContentSizeValue + contentInset.bottom + contentInset.top - calculateThisValue
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
            if (xOry > Math.abs(calculateContentSizeValue + contentInset.bottom + contentInset.top - calculateThisValue)) {
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
            currentAnimationX?.let { it.cancel() }
            currentAnimationY?.let { it.cancel() }
            currentAnimationX = UIViewAnimator.linear(0.25, Runnable {
                UIViewAnimator.addAnimationState(self, "contentOffset.x", oldValue.x, this.contentOffset.x)
                UIViewAnimator.addAnimationState(self, "contentOffset.y", oldValue.y, this.contentOffset.y)
            }, null)
            currentAnimationY = currentAnimationX
        } else {
            scrollTo((this.contentOffset.x * UIScreen.mainScreen.scale()).toInt(), (this.contentOffset.y * UIScreen.mainScreen.scale() - contentInset.top).toInt())
            delegate?.let {
                it.scrollViewDidScroll(this)
            }
            UIViewAnimator.addAnimationState(self, "contentOffset.x", oldValue.x, this.contentOffset.x)
            UIViewAnimator.addAnimationState(self, "contentOffset.y", oldValue.y, this.contentOffset.y)
        }
    }

    open fun scrollToVisible(visibleRect: CGRect, animated: Boolean) {
        val leftVisible = visibleRect.x < contentOffset.x + frame.width
        val topVisible = visibleRect.y < contentOffset.y + frame.height
        var computedOffsetX = contentOffset.x
        var computedOffsetY = contentOffset.y
        if (!leftVisible) {
            computedOffsetX = if (visibleRect.width < frame.width) visibleRect.x + visibleRect.width - frame.width else visibleRect.x
        }
        if (!topVisible) {
            computedOffsetY = if (visibleRect.height < frame.height) visibleRect.y + visibleRect.height - frame.height else visibleRect.y
        }
        if (!leftVisible || !topVisible) {
            setContentOffset(CGPoint(computedOffsetX, computedOffsetY), animated)
        }
    }

    private fun setContentOffsetWithSpring(contentOffset: CGPoint) {
        currentAnimationX?.let { it.cancel() }
        currentAnimationY?.let { it.cancel() }
        currentAnimationX = UIViewAnimator.springWithOptions(120.0, 20.0, Runnable { setContentOffset(contentOffset, false) }, null)
        currentAnimationY = currentAnimationX
    }

    private fun checkOutOfBounds(): Boolean {
        if (contentOffset.x < 0.0 || contentOffset.y < 0.0) {
            return true
        }
        else if (contentOffset.x > contentSize.width - frame.size.width && contentSize.width > 0 || contentOffset.y > contentSize.height - frame.size.height && contentSize.height > 0) {
            return true
        }
        return false
    }

    private fun requestBoundsPoint(point: CGPoint): CGPoint {
        val nearestBoundsY = requestBoundsY(point.y)
        val nearestBoundsX = requestBoundsX(point.x)
        return CGPoint(nearestBoundsX, nearestBoundsY)
    }

    private fun requestBoundsX(x: Double): Double {
        var nearestBoundsX = x
        if (x < 0.0) {
            nearestBoundsX = 0.0
        }
        else if (x > contentSize.width - frame.size.width && contentSize.width > 0) {
            nearestBoundsX = contentSize.width - frame.size.width
        }
        return nearestBoundsX
    }

    private fun requestBoundsY(y: Double): Double {
        var nearestBoundsY = y
        if (y < 0.0) {
            nearestBoundsY = 0.0
        }
        else if (y > contentSize.height - frame.size.height && contentSize.height > 0) {
            nearestBoundsY = contentSize.height - frame.size.height
        }
        return nearestBoundsY
    }

    private fun showScrollIndicator() {

    }

    private fun updateScrollIndicator() {

    }

    private fun hideScrollIndicator() {

    }

    companion object {
        private val FINGER_VELOCITY = 300.0
    }

}
