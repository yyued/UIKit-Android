package com.yy.codex.uikit

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import java.util.ArrayList

/**
 * Created by cuiminghui on 2016/12/30.
 */

open class UIView : FrameLayout, UIResponder {

    /* FrameLayout initialize methods */

    constructor(context: Context, view: View) : super(context) {
        init()
        addView(view)
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    protected open fun init() {
        UIScreen.mainScreen.setContext(context)
        this.layer.bindView(this)
        setWillNotDraw(false)
    }

    /* UIResponder */

    var viewController: UIViewController? = null

    override val nextResponder: UIResponder?
        get() {
            val viewController = viewController
            if (viewController != null) {
                return viewController
            }
            val superview = superview
            if (superview != null) {
                return superview
            }
            return null
        }

    /* category Material Design */

    protected var mMaterialDesign = false

    var isMaterialDesign: Boolean
        get() {
            var materialDesign = mMaterialDesign
            var superview = superview
            while (materialDesign == false && superview != null) {
                materialDesign = superview.mMaterialDesign
                superview = superview.superview
            }
            return materialDesign
        }
        set(materialDesign) {
            mMaterialDesign = materialDesign
            materialDesignDidChanged()
        }

    open fun materialDesignDidChanged() {
        val subviews = subviews
        for (i in subviews.indices) {
            subviews[i].materialDesignDidChanged()
        }
    }

    /* category UIView Layout */

    protected var mFrame = CGRect(0.0, 0.0, 0.0, 0.0)

    var frame: CGRect
        get() = mFrame
        set(frame) {
            if (this.frame == frame) {
                return
            }
            val oldValue = this.mFrame
            this.mFrame = frame
            layoutSubviews()
            this.x = (frame.origin.x * UIScreen.mainScreen.scale()).toFloat()
            this.y = (frame.origin.y * UIScreen.mainScreen.scale()).toFloat()

            var mWidth = frame.size.width * UIScreen.mainScreen.scale()
            var mHeight = frame.size.height * UIScreen.mainScreen.scale()
            if (Math.ceil(mWidth) - mWidth < 0.1) {
                mWidth = Math.ceil(mWidth)
            }
            if (Math.ceil(mWidth) - mHeight < 0.1) {
                mHeight = Math.ceil(mHeight)
            }
            this.minimumWidth = mWidth.toInt()
            this.minimumHeight = mHeight.toInt()
            this.layer.frame = CGRect(0.0, 0.0, frame.size.width, frame.size.height)
            UIViewAnimator.addAnimationState(this, "frame.origin.x", oldValue.origin.x, frame.origin.x)
            UIViewAnimator.addAnimationState(this, "frame.origin.y", oldValue.origin.y, frame.origin.y)
            UIViewAnimator.addAnimationState(this, "frame.size.mWidth", oldValue.size.width, frame.size.width)
            UIViewAnimator.addAnimationState(this, "frame.size.height", oldValue.size.height, frame.size.height)
        }

    val center: CGPoint
        get() = CGPoint((mFrame.origin.x + mFrame.size.width) / 2.0, (mFrame.origin.y + mFrame.size.height) / 2.0)

    protected var mConstraint: UIConstraint? = null

    var constraint: UIConstraint?
        get() = mConstraint
        set(constraint) {
            this.mConstraint = constraint
            val superview = superview
            superview?.layoutSubviews()
        }

    open var maxWidth = 0.0

    open fun layoutSubviews() {
        val nextResponder = nextResponder
        if (nextResponder != null && nextResponder is UIViewController) {
            nextResponder.viewWillLayoutSubviews()
        }
        var previous: UIView? = null
        val subviews = subviews
        for (i in subviews.indices) {
            val subview = subviews[i]
            if (subview.mConstraint != null && !subview.mConstraint!!.disabled) {
                subview.frame = subview.mConstraint!!.requestFrame(subview, this, previous)
            }
            subview.automaticallyAdjustsTopSpace()
            previous = subview
        }
        if (nextResponder != null && nextResponder is UIViewController) {
            postDelayed({ nextResponder.viewDidLayoutSubviews() }, 1)
        }
    }

    private var mAutomaticallyAdjustsSpace = false

    private fun automaticallyAdjustsTopSpace() {
        if (mAutomaticallyAdjustsSpace) {
            var nextResponder = nextResponder
            while (nextResponder != null) {
                if (nextResponder is UIViewController) {
                    val topSpace = nextResponder.topLayoutLength()
                    val bottomSpace = nextResponder.bottomLayoutLength()
                    var frame = frame
                    frame = frame.setY(topSpace)
                    if (superview != null) {
                        frame = frame.setHeight(superview!!.frame.height - topSpace - bottomSpace)
                    }
                    frame = frame
                    break
                }
                nextResponder = nextResponder.nextResponder
            }
        }
    }

    fun setAutomaticallyAdjustsSpace(automaticallyAdjustsSpace: Boolean) {
        mAutomaticallyAdjustsSpace = automaticallyAdjustsSpace
    }

    var marginInsets: UIEdgeInsets = UIEdgeInsets.zero
        set(marginInsets) {
            field = marginInsets
        }

    open fun intrinsicContentSize(): CGSize {
        return CGSize(0.0, 0.0)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (superview == null) {
            frame = CGRect(left.toFloat() / UIScreen.mainScreen.scale(), top.toFloat() / UIScreen.mainScreen.scale(), (right.toFloat() - left.toFloat()) / UIScreen.mainScreen.scale(), (bottom.toFloat() - top.toFloat()) / UIScreen.mainScreen.scale())
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        if (View.MeasureSpec.getMode(widthMeasureSpec) == View.MeasureSpec.AT_MOST) {
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec((frame.width * UIScreen.mainScreen.scale()).toInt(), View.MeasureSpec.AT_MOST)
        }
        if (View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.AT_MOST) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((frame.height * UIScreen.mainScreen.scale()).toInt(), View.MeasureSpec.AT_MOST)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    /* category UIView Rendering */

    fun setBackgroundColor(color: UIColor?) {
        if (color != null) {
            setBackgroundColor(color.toInt())
        }
    }

    override fun setAlpha(alpha: Float) {
        val oldValue = this.alpha
        super.setAlpha(alpha)
        UIViewAnimator.addAnimationState(this, "alpha", oldValue.toDouble(), alpha.toDouble())
    }

    protected var mTintColor: UIColor? = null

    var tintColor: UIColor
        get() {
            var tintColor = mTintColor
            var superview = superview
            while (tintColor == null && superview != null) {
                tintColor = superview.mTintColor
                superview = superview.superview
            }
            if (tintColor == null) {
                tintColor = UIColor(0x00 / 255.0, 0x7a / 255.0, 0xff / 255.0, 1.0)
            }
            return tintColor
        }
        set(tintColor) {
            this.mTintColor = tintColor
            tintColorDidChanged()
        }

    open fun tintColorDidChanged() {
        val subviews = subviews
        for (i in subviews.indices) {
            subviews[i].tintColorDidChanged()
        }
    }

    /* category UIView Hierarchy */

    val superview: UIView?
        get() {
            val parent = parent
            if (parent != null && UIView::class.java.isAssignableFrom(parent.javaClass)) {
                return parent as UIView
            }
            return null
        }

    val subviews: List<UIView>
        get() {
            return (childCount - 1 downTo 0).map { getChildAt(it) }.filterIsInstance<UIView>()
        }

    fun removeFromSuperview() {
        superview?.let { it.removeView(this) }
    }

    fun insertSubview(subview: UIView, atIndex: Int) {
        subview.removeFromSuperview()
        if (atIndex < 0 || atIndex > childCount) {
            return
        }
        addView(subview, atIndex, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    fun addSubview(subview: UIView) {
        subview.removeFromSuperview()
        addView(subview, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    fun insertBelowSubview(subview: UIView, siblingSubview: UIView) {
        if (siblingSubview.superview === this) {
            val atIndex = indexOfChild(siblingSubview)
            insertSubview(subview, atIndex)
        }
    }

    fun insertAboveSubview(subview: UIView, siblingSubview: UIView) {
        if (siblingSubview.superview === this) {
            val atIndex = indexOfChild(siblingSubview)
            insertSubview(subview, atIndex + 1)
        }
    }

    fun bringSubviewToFront(subview: UIView) {
        bringChildToFront(subview)
    }

    fun sendSubviewToBack(subview: UIView) {
        if (subview.superview === this) {
            subview.removeFromSuperview()
            addSubview(subview)
        }
    }

    fun didAddSubview(subview: UIView) {}

    fun willRemoveSubview(subview: UIView) {}

    open fun willMoveToSuperview(newSuperview: UIView?) {
        materialDesignDidChanged()
        tintColorDidChanged()
    }

    open fun didMoveToSuperview() {}

    fun willMoveToWindow() {}

    fun didMoveToWindow() {}

    override fun onViewAdded(child: View) {
        if (UIView::class.java.isAssignableFrom(child.javaClass)) {
            (child as UIView).willMoveToSuperview(this)
        }
        super.onViewAdded(child)
        if (UIView::class.java.isAssignableFrom(child.javaClass)) {
            didAddSubview(child as UIView)
            child.didMoveToSuperview()
        }
    }

    override fun onViewRemoved(child: View) {
        super.onViewRemoved(child)
        if (UIView::class.java.isAssignableFrom(child.javaClass)) {
            willRemoveSubview(child as UIView)
        }
    }

    override fun onAttachedToWindow() {
        willMoveToWindow()
        super.onAttachedToWindow()
        didMoveToWindow()
    }

    /* category UIView Layer-Backed Service */

    protected var mWantsLayer = false
    var layer = CALayer()
        protected set

    var isWantsLayer: Boolean
        get() = mWantsLayer
        set(wantsLayer) {
            if (this.mWantsLayer != wantsLayer) {
                this.mWantsLayer = wantsLayer
                if (wantsLayer) {
                    setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                }
                invalidate()
            }
        }

    fun drawRect(canvas: Canvas, rect: CGRect) {
        // TODO: 2017/1/3 adi
        if (mWantsLayer) {
            layer.drawRect(canvas, rect)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawRect(canvas, CGRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble()))
    }

    /* category: UIView touch events */

    var isUserInteractionEnabled = true

    var gestureRecognizers = ArrayList<UIGestureRecognizer>()
        protected set

    fun addGestureRecognizer(gestureRecognizer: UIGestureRecognizer) {
        gestureRecognizers.add(gestureRecognizer)
        gestureRecognizer.didAddToView(this)
    }

    protected var mTouchHandler: UIViewTouchHandler? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mTouchHandler == null) {
            mTouchHandler = UIViewTouchHandler(this)
        }
        mTouchHandler!!.onTouchEvent(event)
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return true
    }

    fun hitTest(point: CGPoint, event: MotionEvent): UIView? {
        return UIViewHelpers.hitTest(this, point, event)
    }

    fun convertPoint(point: CGPoint, toView: UIView): CGPoint {
        return UIViewHelpers.convertPoint(this, point, toView)
    }

    override fun touchesBegan(touches: Array<UITouch>, event: UIEvent) {
        if (nextResponder != null) {
            nextResponder!!.touchesBegan(touches, event)
        }
        if (UIGestureRecognizerLooper.isHitTestedView(touches, this)) {
            if (sGestureRecognizerLooper == null || sGestureRecognizerLooper!!.isFinished || sGestureRecognizerLooper!!.mGestureRecognizers.size == 0) {
                sGestureRecognizerLooper = UIGestureRecognizerLooper(this)
            }
            sGestureRecognizerLooper!!.onTouchesBegan(touches, event)
        }
    }

    override fun touchesMoved(touches: Array<UITouch>, event: UIEvent) {
        if (nextResponder != null) {
            nextResponder!!.touchesMoved(touches, event)
        }
        if (UIGestureRecognizerLooper.isHitTestedView(touches, this)) {
            if (sGestureRecognizerLooper == null || sGestureRecognizerLooper!!.isFinished) {
                sGestureRecognizerLooper = UIGestureRecognizerLooper(this)
            }
            sGestureRecognizerLooper!!.onTouchesMoved(touches, event)
        }
    }

    override fun touchesEnded(touches: Array<UITouch>, event: UIEvent) {
        if (nextResponder != null) {
            nextResponder!!.touchesEnded(touches, event)
        }
        if (UIGestureRecognizerLooper.isHitTestedView(touches, this)) {
            if (sGestureRecognizerLooper == null || sGestureRecognizerLooper!!.isFinished) {
                sGestureRecognizerLooper = UIGestureRecognizerLooper(this)
            }
            sGestureRecognizerLooper!!.onTouchesEnded(touches, event)
        }
    }

    open fun animate(aKey: String, aValue: Float) {
        if (aKey.equals("frame.origin.x", ignoreCase = true)) {
            frame = this.mFrame.setX(aValue.toDouble())
        } else if (aKey.equals("frame.origin.y", ignoreCase = true)) {
            frame = this.mFrame.setY(aValue.toDouble())
        } else if (aKey.equals("frame.size.mWidth", ignoreCase = true)) {
            frame = this.mFrame.setWidth(aValue.toDouble())
        } else if (aKey.equals("frame.size.height", ignoreCase = true)) {
            frame = this.mFrame.setHeight(aValue.toDouble())
        } else if (aKey.equals("alpha", ignoreCase = true)) {
            alpha = aValue
        } else if (aKey.startsWith("layer.")) {
            layer.animate(aKey, aValue)
        }
    }

    companion object {

        var sGestureRecognizerLooper: UIGestureRecognizerLooper? = null

    }

}