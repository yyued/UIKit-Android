package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

import com.yy.codex.foundation.NSLog
import com.yy.codex.foundation.lets

import java.util.Timer
import java.util.TimerTask

/**
 * Created by it on 17/1/6.
 */

open class UIScrollView : UIView {

    open interface Delegate {
        fun didScroll(scrollView: UIScrollView)
        fun willBeginDragging(scrollView: UIScrollView)
        fun didEndDragging(scrollView: UIScrollView, willDecelerate: Boolean)
        fun willBeginDecelerating(scrollView: UIScrollView)
        fun didEndDecelerating(scrollView: UIScrollView)
    }

    constructor(context: Context, view: View) : super(context, view) {}
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    /* Public */

    var contentOffset = CGPoint(0.0, 0.0)
        internal set

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

    open var delegate: Delegate? = null

    var tracking: Boolean = false
        internal set

    var scrollEnabled: Boolean = true

    var alwaysBounceVertical: Boolean = false

    var alwaysBounceHorizontal: Boolean = false

    var bounces: Boolean = true

    var pagingEnabled: Boolean = false

    var decelerating: Boolean = false
        internal set

    var dragging: Boolean = false
        internal set

    var showsHorizontalScrollIndicator: Boolean = true

    var showsVerticalScrollIndicator: Boolean = true

    open fun setContentOffset(contentOffset: CGPoint, animated: Boolean = false) {
        _setContentOffset(contentOffset, animated)
    }

    open fun scrollToVisible(visibleRect: CGRect, animated: Boolean) {
        _scrollToVisible(visibleRect, animated)
    }

    /* Private */

    internal var _panGestureRecognizer: UIPanGestureRecognizer? = null
    internal var _currentAnimationY: UIViewAnimation? = null
    internal var _currentAnimationX: UIViewAnimation? = null
    internal var _verticalMoveDistance = 0.0
    internal var _horizontalMoveDistance = 0.0
    internal var _trackingPoint: CGPoint? = null
    internal var _windowSizePoint: CGPoint? = null
    internal var _fingerVerticalMoveDistance = 0.00
    internal var _fingerHorizontalMoveDistance = 0.00
    internal lateinit var _horizontalScrollIndicator: UIView
    internal lateinit var _verticalScrollIndicator: UIView
    internal var _scrollIndicatorHideAnimation: UIViewAnimation? = null
    internal var _deceleratingCancelled = false

    override fun init() {
        super.init()
        contentInset = UIEdgeInsets(0.0, 0.0, 0.0, 0.0)
        _initScroller()
        _initScrollIndicator()
    }

    override fun hitTest(point: CGPoint, event: MotionEvent): UIView? {
        if (decelerating) {
            val view = super.hitTest(point, event)
            if (view != null && view !== this) {
                return this
            }
            else {
                return view
            }
        }
        return super.hitTest(point, event)
    }

    override fun touchesBegan(touches: List<UITouch>, event: UIEvent) {
        super.touchesBegan(touches, event)
        _scrollerTouchBegan()
    }

    override fun touchesEnded(touches: List<UITouch>, event: UIEvent) {
        super.touchesEnded(touches, event)
        _scrollerTouchEnded()
    }

    protected open fun handlePan(panGestureRecognizer: UIPanGestureRecognizer) {
        _scrollerHandlePan(panGestureRecognizer)
    }

    override fun animate(aKey: String, aValue: Float) {
        super.animate(aKey, aValue)
        if (aKey.equals("contentOffset.x", ignoreCase = true)) {
            setContentOffset(contentOffset.setX(aValue.toDouble()), false)
        } else if (aKey.equals("contentOffset.y", ignoreCase = true)) {
            setContentOffset(contentOffset.setY(aValue.toDouble()), false)
        }
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        _fingerVerticalMoveDistance = frame.size.height / 5.0
        _fingerHorizontalMoveDistance = frame.size.width / 5.0
    }

}
