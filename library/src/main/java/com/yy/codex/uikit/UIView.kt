package com.yy.codex.uikit

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.yy.codex.coreanimation.CALayer
import java.util.*

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
        prepareProps(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
        prepareProps(attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
        prepareProps(attrs)
    }

    open fun init() {
        UIScreen.mainScreen.context = context
        setWillNotDraw(false)
    }

    /* XML */

    protected var initializeAttributes: HashMap<String, Any> = hashMapOf()

    open fun awakeFromXML() {
        if (initializeAttributes.size > 0) {
            resetProps()
            initializeAttributes.clear()
        }
        subviews.forEach(UIView::awakeFromXML)
    }

    protected open fun prepareProps(attrs: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.UIView, 0, 0)
        typedArray.getBoolean(R.styleable.UIView_view_materialDesign, false)?.let {
            initializeAttributes.put("UIView.materialDesign", it)
        }
        CGRect.create(typedArray, this)?.let {
            initializeAttributes.put("UIView.frame", it)
        }
        UIConstraint.create(typedArray)?.let {
            initializeAttributes.put("UIView.constraint", it)
        }
        typedArray.getFloat(R.styleable.UIView_view_maxWidth, -1.0f)?.let {
            if (it != -1.0f) {
                initializeAttributes.put("UIView.maxWidth", it)
            }
        }
        typedArray.getBoolean(R.styleable.UIView_view_automaticallyAdjustsSpace, false)?.let {
            initializeAttributes.put("UIView.automaticallyAdjustsSpace", it)
        }
        initializeAttributes.put("UIView.marginInsets", UIEdgeInsets(
                typedArray.getFloat(R.styleable.UIView_marginInset_top, 0.0f).toDouble(),
                typedArray.getFloat(R.styleable.UIView_marginInset_left, 0.0f).toDouble(),
                typedArray.getFloat(R.styleable.UIView_marginInset_bottom, 0.0f).toDouble(),
                typedArray.getFloat(R.styleable.UIView_marginInset_right, 0.0f).toDouble()
        ))
        typedArray.getColor(R.styleable.UIView_view_tintColor, -1)?.let {
            if (it != -1) {
                initializeAttributes.put("UIView.tintColor", UIColor(it))
            }
        }
        typedArray.getBoolean(R.styleable.UIView_view_wantsLayer, false)?.let {
            initializeAttributes.put("UIView.wantsLayer", it)
        }
        typedArray.getColor(R.styleable.UIView_layer_backgroundColor, -1)?.let {
            if (it != -1) {
                initializeAttributes.put("UIView.layer.backgroundColor", UIColor(it))
            }
        }
        typedArray.getFloat(R.styleable.UIView_layer_cornerRadius, -1.0f)?.let {
            if (it != -1.0f) {
                initializeAttributes.put("UIView.layer.cornerRadius", it)
            }
        }
        typedArray.getFloat(R.styleable.UIView_layer_borderWidth, -1.0f)?.let {
            if (it != -1.0f) {
                initializeAttributes.put("UIView.layer.borderWidth", it)
            }
        }
        typedArray.getColor(R.styleable.UIView_layer_borderColor, -1)?.let {
            if (it != -1) {
                initializeAttributes.put("UIView.layer.borderColor", UIColor(it))
            }
        }
        typedArray.getFloat(R.styleable.UIView_layer_shadowX, -1.0f)?.let {
            if (it != -1.0f) {
                initializeAttributes.put("UIView.layer.shadowX", it)
            }
        }
        typedArray.getFloat(R.styleable.UIView_layer_shadowY, -1.0f)?.let {
            if (it != -1.0f) {
                initializeAttributes.put("UIView.layer.shadowY", it)
            }
        }
        typedArray.getFloat(R.styleable.UIView_layer_shadowRadius, -1.0f)?.let {
            if (it != -1.0f) {
                initializeAttributes.put("UIView.layer.shadowRadius", it)
            }
        }
        typedArray.getColor(R.styleable.UIView_layer_shadowColor, -1)?.let {
            if (it != -1) {
                initializeAttributes.put("UIView.layer.shadowColor", UIColor(it))
            }
        }
        initializeAttributes.put("UIView.layer.clipToBounds", typedArray.getBoolean(R.styleable.UIView_layer_clipToBounds, false))
        initializeAttributes.put("UIView.userInteractionEnabled", typedArray.getBoolean(R.styleable.UIView_view_userInteractionEnabled, true))
        typedArray.recycle()
    }

    protected open fun resetProps() {
        initializeAttributes?.let {
            (it["UIView.materialDesign"] as? Boolean)?.let {
                materialDesign = it
            }
            (it["UIView.frame"] as? CGRect)?.let {
                frame = it
            }
            (it["UIView.constraint"] as? UIConstraint)?.let {
                constraint = it
            }
            (it["UIView.maxWidth"] as? Float)?.let {
                maxWidth = it.toDouble()
            }
            (it["UIView.automaticallyAdjustsSpace"] as? Boolean)?.let {
                automaticallyAdjustsSpace = it
            }
            (it["UIView.marginInsets"] as? UIEdgeInsets)?.let {
                marginInsets = it
            }
            (it["UIView.tintColor"] as? UIColor)?.let {
                tintColor = it
            }
            (it["UIView.wantsLayer"] as? Boolean)?.let {
                wantsLayer = it
            }
            (it["UIView.layer.backgroundColor"] as? UIColor)?.let {
                layer.backgroundColor = it
            }
            (it["UIView.layer.cornerRadius"] as? Float)?.let {
                layer.cornerRadius = it.toDouble()
            }
            (it["UIView.layer.borderWidth"] as? Float)?.let {
                layer.borderWidth = it.toDouble()
            }
            (it["UIView.layer.borderColor"] as? UIColor)?.let {
                layer.borderColor = it
            }
            (it["UIView.layer.shadowX"] as? Float)?.let {
                layer.shadowX = it.toDouble()
            }
            (it["UIView.layer.shadowY"] as? Float)?.let {
                layer.shadowY = it.toDouble()
            }
            (it["UIView.layer.shadowRadius"] as? Float)?.let {
                layer.shadowRadius = it.toDouble()
            }
            (it["UIView.layer.shadowColor"] as? UIColor)?.let {
                layer.shadowColor = it
            }
            (it["UIView.layer.clipToBounds"] as? Boolean)?.let {
                layer.clipToBounds = it
            }
            (it["UIView.userInteractionEnabled"] as? Boolean)?.let {
                userInteractionEnabled = it
            }
        }
    }

    /* UIResponder */

    internal var viewController: UIViewController? = null

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
            this.x = Math.ceil((frame.origin.x * UIScreen.mainScreen.scale())).toFloat()
            this.y = Math.ceil((frame.origin.y * UIScreen.mainScreen.scale())).toFloat()
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
            enlargerView?.let { resetEnlargerLayerView() }
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

    open fun layoutIfNeeded() {
        superview?.let(UIView::layoutSubviews)
    }

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

    open fun setBackgroundColor(color: UIColor?) {
        color?.let {
            if (wantsLayer) {
                layer.backgroundColor = color
            }
            else {
                setBackgroundColor(it.toInt())
            }
        }
    }

    var hidden: Boolean = false
        set(value) {
            field = value
            alpha = _alpha
        }

    private var _alpha: Float = 1.0f

    override fun setAlpha(alpha: Float) {
        _alpha = alpha
        var value = alpha
        if (hidden) {
            value = 0.0f
        }
        enlargerView?.let {
            it.alpha = value
        }
        val oldValue = this.alpha
        super.setAlpha(value)
        UIViewAnimator.addAnimationState(this, "alpha", oldValue.toDouble(), value.toDouble())
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
                tintColor = UIColor(0x14 / 255.0, 0x6d / 255.0, 0xde / 255.0, 1.0)
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
        willMoveToSuperview(null)
        enlargerView?.let(UIView::removeFromSuperview)
        superview?.let { it.removeView(this) }
        didMoveToSuperview()
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

    open fun didAddSubview(subview: UIView) {}

    open fun willRemoveSubview(subview: UIView) {}

    open fun willMoveToSuperview(newSuperview: UIView?) {
        materialDesignDidChanged()
        tintColorDidChanged()
    }

    open fun didMoveToSuperview() {}

    open fun willMoveToWindow() {}

    open fun didMoveToWindow() {}

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

    private var enlargerView: UIEnlargerView? = null

    private fun createEnlargerView() {
        val view = UIEnlargerView(context)
        view.userInteractionEnabled = false
        view.tag = -1
        view.wantsLayer = true
        val contentLayer = CALayer()
        view.layer.addSubLayer(contentLayer)
        superview?.let {
            it.insertBelowSubview(view, this)
        }
        enlargerView = view
    }

    private fun resetEnlargerLayerView() {
        enlargerView?.let {
            val insets = layer.enlargerInsets()
            it.frame = CGRect(frame.x - insets.left, frame.y - insets.top, frame.width + insets.left + insets.right, frame.height + insets.top + insets.bottom)
            val contentLayer = it.layer.sublayers.first()
            this.layer.copyProps(contentLayer)
            contentLayer.frame = CGRect(insets.left, insets.top, frame.width, frame.height)
        }
    }

    open fun drawRect(canvas: Canvas, rect: CGRect) {
        if (wantsLayer) {
            layer.resetBelongings(true)
            if (this !is UIEnlargerView && layer.wantsEnlargerLayer()) {
                if (enlargerView == null) {
                    createEnlargerView()
                }
                resetEnlargerLayerView()
                return
            }
            enlargerView?.let(UIView::removeFromSuperview)
            layer.drawRect(canvas, rect)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawRect(canvas, CGRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble()))
    }

    /* category: UIView touch events */

    var userInteractionEnabled = true

    var gestureRecognizers: List<UIGestureRecognizer> = listOf()
        protected set

    fun addGestureRecognizer(gestureRecognizer: UIGestureRecognizer) {
        val mutableList = gestureRecognizers.toMutableList()
        mutableList.add(gestureRecognizer)
        gestureRecognizers = mutableList.toList()
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

    open fun hitTest(point: CGPoint, event: MotionEvent): UIView? {
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

    override open fun resignFirstResponder() {
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