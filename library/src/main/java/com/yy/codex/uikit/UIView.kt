package com.yy.codex.uikit

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import com.yy.codex.coreanimation.CALayer
import java.util.ArrayList

/**
 * Created by cuiminghui on 2016/12/30.
 */

open class UIView : FrameLayout, UIResponder {

    /* FrameLayout initialize methods */

    constructor(context: Context, view: View) : super(context) {
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

    open fun init() {
        UIScreen.mainScreen.context = context
        setWillNotDraw(false)
    }

    /* UIResponder */

    var viewController: UIViewController? = null

    override val nextResponder: UIResponder?
        get() {
            if (viewController != null) {
                return viewController
            }
            if (superview != null) {
                return superview
            }
            return null
        }

    /* category Material Design */

    var materialDesign = false
        get() {
            var materialDesign = field
            var superview = superview
            while (!materialDesign && superview != null) {
                materialDesign = superview.materialDesign
                superview = superview.superview
            }
            return materialDesign
        }
        set(value) {
            field = value
            materialDesignDidChanged()
        }

    open fun materialDesignDidChanged() {
        val subviews = subviews
        for (i in subviews.indices) {
            subviews[i].materialDesignDidChanged()
        }
    }

    /* category UIView Layout */

    var frame: CGRect = CGRect(0.0, 0.0, 0.0, 0.0)
        set(frame) {
            if (field == frame) {
                return
            }
            val oldValue = field
            field = frame
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
            layerShadowView?.let { resetShadowView() }
            UIViewAnimator.addAnimationState(this, "frame.origin.x", oldValue.origin.x, frame.origin.x)
            UIViewAnimator.addAnimationState(this, "frame.origin.y", oldValue.origin.y, frame.origin.y)
            UIViewAnimator.addAnimationState(this, "frame.size.mWidth", oldValue.size.width, frame.size.width)
            UIViewAnimator.addAnimationState(this, "frame.size.height", oldValue.size.height, frame.size.height)
        }

    val center: CGPoint
        get() = CGPoint((frame.origin.x + frame.size.width) / 2.0, (frame.origin.y + frame.size.height) / 2.0)

    var constraint: UIConstraint? = null
        set(constraint) {
            field = constraint
            superview?.let(UIView::layoutSubviews)
        }

    open var maxWidth = 0.0

    open fun layoutSubviews() {
        val nextResponder = nextResponder
        if (nextResponder != null && nextResponder is UIViewController) {
            nextResponder.viewWillLayoutSubviews()
        }
        var previous: UIView? = null
        for (subview in subviews) {
            subview.constraint?.let {
                if (!it.disabled) {
                    subview.frame = it.requestFrame(subview, this, previous)
                }
            }
            subview.automaticallyAdjustsTopSpace()
            previous = subview
        }
        (nextResponder as? UIViewController)?.let { postDelayed({it.viewDidLayoutSubviews()}, 1) }
    }

    var automaticallyAdjustsSpace = false

    private fun automaticallyAdjustsTopSpace() {
        if (automaticallyAdjustsSpace) {
            var nextResponder = nextResponder
            while (nextResponder != null) {
                if (nextResponder is UIViewController) {
                    val topSpace = nextResponder.topLayoutLength()
                    val bottomSpace = nextResponder.bottomLayoutLength()
                    var frame = frame
                    frame = frame.setY(topSpace)
                    superview?.let {
                        frame = frame.setHeight(it.frame.height - topSpace - bottomSpace)
                    }
                    frame = frame
                    break
                }
                nextResponder = nextResponder.nextResponder
            }
        }
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
        var thisWidthMeasureSpec = widthMeasureSpec
        var thisHeightMeasureSpec = heightMeasureSpec
        if (View.MeasureSpec.getMode(widthMeasureSpec) == View.MeasureSpec.AT_MOST) {
            thisWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec((frame.width * UIScreen.mainScreen.scale()).toInt(), View.MeasureSpec.AT_MOST)
        }
        if (View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.AT_MOST) {
            thisHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec((frame.height * UIScreen.mainScreen.scale()).toInt(), View.MeasureSpec.AT_MOST)
        }
        super.onMeasure(thisWidthMeasureSpec, thisHeightMeasureSpec)
    }

    /* category UIView Rendering */

    fun setBackgroundColor(color: UIColor?) {
        color?.let {
            if (wantsLayer) {
                layer.backgroundColor = color
            }
            else {
                setBackgroundColor(it.toInt())
            }
        }
    }

    override fun setAlpha(alpha: Float) {
        val oldValue = this.alpha
        super.setAlpha(alpha)
        UIViewAnimator.addAnimationState(this, "alpha", oldValue.toDouble(), alpha.toDouble())
    }

    var tintColor: UIColor? = null
        get() {
            var tintColor = field
            var superview = superview
            while (tintColor == null && superview != null) {
                tintColor = superview.tintColor
                superview = superview.superview
            }
            if (tintColor == null) {
                tintColor = UIColor(0x00 / 255.0, 0x7a / 255.0, 0xff / 255.0, 1.0)
            }
            return tintColor
        }
        set(tintColor) {
            field = tintColor
            tintColorDidChanged()
        }

    open fun tintColorDidChanged() {
        for (subview in subviews) {
            subview.tintColorDidChanged()
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
            return (0..childCount - 1).map { getChildAt(it) }.filterIsInstance<UIView>()
        }

    fun removeFromSuperview() {
        layerShadowView?.let(UIView::removeFromSuperview)
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
        if (child is UIView) {
            child.willMoveToSuperview(this)
        }
        super.onViewAdded(child)
        if (child is UIView) {
            didAddSubview(child)
            child.didMoveToSuperview()
        }
    }

    override fun onViewRemoved(child: View) {
        super.onViewRemoved(child)
        if (child is UIView) {
            willRemoveSubview(child)
        }
    }

    override fun onAttachedToWindow() {
        willMoveToWindow()
        super.onAttachedToWindow()
        didMoveToWindow()
    }

    /* category UIView Layer-Backed Service */

    var wantsLayer = false
        set(value) {
            if (field != value) {
                field = value
                if (field) {
                    layer.bindView(this)
                    setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                }
                invalidate()
            }
        }

    val layer: CALayer = CALayer()
    var layerShadowView: UIView? = null
    var wantShadow: Boolean = false
        get() = layer.shadowColor != null

    fun createShadowView() {
        val shadowView = UIView(context)
        shadowView.userInteractionEnabled = false
        shadowView.tag = -1
        shadowView.wantsLayer = true
        shadowView.wantsLayer = true
        val contentLayer = CALayer()
        shadowView.layer.addSubLayer(contentLayer)
        superview?.let {
            it.insertBelowSubview(shadowView, this)
        }
        layerShadowView = shadowView
    }

    fun resetShadowView() {
        layerShadowView?.let {
            val enlargeRadiusX = layer.shadowX + layer.shadowRadius
            val enlargeRadiusY = layer.shadowY + layer.shadowRadius
            it.frame = CGRect(frame.x - enlargeRadiusX, frame.y - enlargeRadiusY, frame.width + enlargeRadiusX * 2, frame.height + enlargeRadiusY * 2)
            val contentLayer = it.layer.sublayers.first()
            this.layer.copyProps(contentLayer)
            contentLayer.frame = CGRect(enlargeRadiusX, enlargeRadiusY, frame.width, frame.height)
        }
    }

    fun drawRect(canvas: Canvas, rect: CGRect) {
        if (wantsLayer) {
            if (wantShadow && tag != -1) {
                if (layerShadowView == null) {
                    createShadowView()
                }
                resetShadowView()
                return
            }
            layerShadowView?.let(UIView::removeFromSuperview)
            layer.drawRect(canvas, rect)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawRect(canvas, CGRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble()))
    }

    /* category: UIView touch events */

    var userInteractionEnabled = true

    var gestureRecognizers = ArrayList<UIGestureRecognizer>()
        protected set

    fun addGestureRecognizer(gestureRecognizer: UIGestureRecognizer) {
        gestureRecognizers.add(gestureRecognizer)
        gestureRecognizer.didAddToView(this)
    }

    protected var touchHandler: UIViewTouchHandler? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (touchHandler == null) {
            touchHandler = UIViewTouchHandler(this)
        }
        touchHandler?.let { it.onTouchEvent(event) }
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

    /* UIResponder */

    override fun canBecomeFirstResponder(): Boolean {
        return false
    }

    override fun becomeFirstResponder() {
        UIResponder.firstResponder = this
    }

    override fun canResignFirstResponder(): Boolean {
        return true
    }

    override fun resignFirstResponder() {
        if (isFirstResponder()) {
            UIResponder.firstResponder = null
        }
    }

    override fun isFirstResponder(): Boolean {
        return UIResponder.firstResponder === this
    }

    override fun touchesBegan(touches: List<UITouch>, event: UIEvent) {
        nextResponder?.touchesBegan(touches, event)
        if (UIGestureRecognizerLooper.isHitTestedView(touches, this)) {
            if (gestureRecognizerLooper == null || gestureRecognizerLooper?.isFinished ?: false || gestureRecognizerLooper?.gestureRecognizers?.size == 0) {
                gestureRecognizerLooper = UIGestureRecognizerLooper(this)
            }
            gestureRecognizerLooper?.let { it.onTouchesBegan(touches, event) }
        }
    }

    override fun touchesMoved(touches: List<UITouch>, event: UIEvent) {
        nextResponder?.touchesMoved(touches, event)
        if (UIGestureRecognizerLooper.isHitTestedView(touches, this)) {
            if (gestureRecognizerLooper == null || gestureRecognizerLooper?.isFinished ?: false) {
                gestureRecognizerLooper = UIGestureRecognizerLooper(this)
            }
            gestureRecognizerLooper?.let { it.onTouchesMoved(touches, event) }
        }
    }

    override fun touchesEnded(touches: List<UITouch>, event: UIEvent) {
        nextResponder?.touchesEnded(touches, event)
        if (UIGestureRecognizerLooper.isHitTestedView(touches, this)) {
            if (gestureRecognizerLooper == null || gestureRecognizerLooper?.isFinished ?: false) {
                gestureRecognizerLooper = UIGestureRecognizerLooper(this)
            }
            gestureRecognizerLooper?.let { it.onTouchesEnded(touches, event) }
        }
    }

    override fun keyboardPressDown(event: UIKeyEvent) {
        nextResponder?.keyboardPressDown(event)
    }

    override fun keyboardPressUp(event: UIKeyEvent) {
        nextResponder?.keyboardPressUp(event)
    }

    open fun animate(aKey: String, aValue: Float) {
        if (aKey.equals("frame.origin.x", ignoreCase = true)) {
            frame = this.frame.setX(aValue.toDouble())
        } else if (aKey.equals("frame.origin.y", ignoreCase = true)) {
            frame = this.frame.setY(aValue.toDouble())
        } else if (aKey.equals("frame.size.mWidth", ignoreCase = true)) {
            frame = this.frame.setWidth(aValue.toDouble())
        } else if (aKey.equals("frame.size.height", ignoreCase = true)) {
            frame = this.frame.setHeight(aValue.toDouble())
        } else if (aKey.equals("alpha", ignoreCase = true)) {
            alpha = aValue
        } else if (aKey.startsWith("layer.")) {
            layer.animate(aKey, aValue)
        }
    }

    companion object {

        internal var gestureRecognizerLooper: UIGestureRecognizerLooper? = null

    }

}