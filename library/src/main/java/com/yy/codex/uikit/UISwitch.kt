package com.yy.codex.uikit

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

/**
 * Created by cuiminghui on 2017/1/17.
 */

class UISwitch : UIControl {

    /* initialize */

    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun init() {
        super.init()
        initContentView()
        addSubview(contentView)
    }

    override fun intrinsicContentSize(): CGSize {
        return CGSize(52.0, 44.0)
    }

    override fun prepareProps(attrs: AttributeSet) {
        super.prepareProps(attrs)
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.UISwitch, 0, 0)
        typedArray.getBoolean(R.styleable.UISwitch_switch_on, false)?.let {
            initializeAttributes.put("UISwitch.on", it)
        }
        typedArray.getColor(R.styleable.UISwitch_switch_onThumbColor, -1)?.let {
            if (it != -1) {
                initializeAttributes.put("UISwitch.onThumbColor", UIColor(it))
            }
        }
        typedArray.getColor(R.styleable.UISwitch_switch_onTrackColor, -1)?.let {
            if (it != -1) {
                initializeAttributes.put("UISwitch.onTrackColor", UIColor(it))
            }
        }
        typedArray.getColor(R.styleable.UISwitch_switch_offThumbColor, -1)?.let {
            if (it != -1) {
                initializeAttributes.put("UISwitch.offThumbColor", UIColor(it))
            }
        }
        typedArray.getColor(R.styleable.UISwitch_switch_offTrackColor, -1)?.let {
            if (it != -1) {
                initializeAttributes.put("UISwitch.offTrackColor", UIColor(it))
            }
        }
    }

    override fun resetProps() {
        super.resetProps()
        initializeAttributes?.let {
            (it["UISwitch.on"] as? Boolean)?.let {
                on = it
            }
            (it["UISwitch.onThumbColor"] as? UIColor)?.let {
                onThumbColor = it
            }
            (it["UISwitch.onTrackColor"] as? UIColor)?.let {
                onTrackColor = it
            }
            (it["UISwitch.offThumbColor"] as? UIColor)?.let {
                offThumbColor = it
            }
            (it["UISwitch.offTrackColor"] as? UIColor)?.let {
                offTrackColor = it
            }
        }
    }

    /* appearance */

    private lateinit var contentView: UIView

    private fun initContentView() {
        contentView = UIView(context)
        contentView.constraint = UIConstraint.center()
        contentView.constraint?.width = "52"
        contentView.constraint?.height = "32"
        initOffBackgroundView()
        contentView.addSubview(offBackgroundView)
        initOnBackgroundView()
        contentView.addSubview(onBackgroundView)
        initHandleView()
        contentView.addSubview(handleView)
    }

    private lateinit var offBackgroundView: UIView

    private fun initOffBackgroundView() {
        offBackgroundView = UIView(context)
        offBackgroundView.frame = CGRect(0.0, 0.0, 52.0, 32.0)
        offBackgroundView.alpha = 1.0f
        offBackgroundView.wantsLayer = true
        offBackgroundView.layer.cornerRadius = 16.0
        offBackgroundView.layer.borderWidth = 1.0
        offBackgroundView.layer.borderColor = UIColor(0x00 / 255.0, 0x00 / 255.0, 0x00 / 255.0, 0.1)
        offBackgroundView.layer.backgroundColor = offTrackColor
    }

    private lateinit var onBackgroundView: UIView

    private fun initOnBackgroundView() {
        onBackgroundView = UIView(context)
        onBackgroundView.frame = CGRect(0.0, 0.0, 52.0, 32.0)
        onBackgroundView.alpha = 0.0f
        onBackgroundView.wantsLayer = true
        onBackgroundView.layer.cornerRadius = 16.0
        onBackgroundView.layer.backgroundColor = onTrackColor
    }

    private lateinit var handleView: UIView

    private fun initHandleView() {
        handleView = UIView(context)
        handleView.frame = CGRect(1.0, 1.0, kHandleRadius.toDouble(), kHandleRadius.toDouble())
        handleView.alpha = 1.0f
        handleView.wantsLayer = true
        handleView.layer.shadowX = 1.0
        handleView.layer.shadowY = 1.0
        handleView.layer.shadowRadius = 0.5
        handleView.layer.shadowColor = UIColor(.3, .3, .3, .2)
        handleView.layer.cornerRadius = kHandleRadius / 2.0
        handleView.layer.borderWidth = 0.5
        handleView.layer.borderColor = UIColor(0x00 / 255.0, 0x00 / 255.0, 0x00 / 255.0, 0.15)
        handleView.layer.backgroundColor = UIColor.whiteColor
    }
    
    var on: Boolean = false
        private set

    private var active: Boolean = false

    private var currentAnimation: UIViewAnimation? = null

    var onThumbColor = UIColor.whiteColor
        set(value) {
            field = value
            if (on){
                handleView.layer.backgroundColor = value
            }
        }

    var onTrackColor = UIColor(0x00 / 255.0, 0xe3 / 255.0, 0x64 / 255.0, 1.0)
        set(value) {
            field = value
            onBackgroundView.layer.backgroundColor = value
        }

    var offThumbColor = UIColor.whiteColor
        set(value) {
            field = value
            if (!on){
                handleView.layer.backgroundColor = value
            }
        }

    var offTrackColor = UIColor.whiteColor
        set(value) {
            field = value
            offBackgroundView.layer.backgroundColor = value
        }

    override fun onEvent(event: UIControl.Event) {
        super.onEvent(event)
        when (event) {
            UIControl.Event.TouchDown -> setActive(true)
            UIControl.Event.TouchDragEnter -> setActive(true)
            UIControl.Event.TouchDragExit -> setActive(false)
            UIControl.Event.TouchUpInside -> setOn(!this.on, true)
            UIControl.Event.TouchUpOutside -> setOn(this.on, true)
        }
    }

    fun setOn(value: Boolean, animated: Boolean) {
        on = value
        resetContentView(animated)
        onEvent(Event.ValueChanged)
    }

    private fun resetContentView(animated: Boolean) {
        currentAnimation?.let { it.cancel() }
        if (animated) {
            currentAnimation = UIViewAnimator.springWithBounciness(1.0, 24.0, Runnable {
                if (on) {
                    offBackgroundView.alpha = 0.0f
                    onBackgroundView.alpha = 1.0f
                    onBackgroundView.layer.backgroundColor = onTrackColor
                    handleView.frame = CGRect(frame.width - kHandleRadius.toDouble() - 1.0, 1.0, kHandleRadius.toDouble(), kHandleRadius.toDouble())
                    handleView.layer.backgroundColor = onThumbColor
                } else {
                    offBackgroundView.alpha = 1.0f
                    offBackgroundView.layer.backgroundColor = offTrackColor
                    onBackgroundView.alpha = 0.0f
                    handleView.frame = CGRect(1.0, 1.0, kHandleRadius.toDouble(), kHandleRadius.toDouble())
                    handleView.layer.backgroundColor = offThumbColor
                }
            }, Runnable { active = false })
        }
        else {
            if (on) {
                offBackgroundView.alpha = 0.0f
                onBackgroundView.alpha = 1.0f
                onBackgroundView.layer.backgroundColor = onTrackColor
                handleView.frame = CGRect(frame.width - kHandleRadius.toDouble() - 1.0, 1.0, kHandleRadius.toDouble(), kHandleRadius.toDouble())
                handleView.layer.backgroundColor = onThumbColor
            } else {
                offBackgroundView.alpha = 1.0f
                offBackgroundView.layer.backgroundColor = offTrackColor
                onBackgroundView.alpha = 0.0f
                handleView.frame = CGRect(1.0, 1.0, kHandleRadius.toDouble(), kHandleRadius.toDouble())
                handleView.layer.backgroundColor = offThumbColor
            }
        }
    }

    private fun setActive(isActive: Boolean) {
        this.active = isActive
        cancelAnimation()
        currentAnimation = UIViewAnimator.spring(Runnable {
            val widthExpanded = 8f
            if (isActive) {
                if (on) {
                    handleView.frame = CGRect((21 - widthExpanded).toDouble(), 1.0, (kHandleRadius + widthExpanded).toDouble(), kHandleRadius.toDouble())
                } else {
                    handleView.frame = CGRect(1.0, 1.0, (kHandleRadius + widthExpanded).toDouble(), kHandleRadius.toDouble())
                    offBackgroundView.layer.backgroundColor = offTrackColor.colorWithDarken(0.3)
                }
            } else {
                if (on) {
                    handleView.frame = CGRect(21.0, 1.0, kHandleRadius.toDouble(), kHandleRadius.toDouble())
                } else {
                    handleView.frame = CGRect(1.0, 1.0, kHandleRadius.toDouble(), kHandleRadius.toDouble())
                    offBackgroundView.layer.backgroundColor = offTrackColor
                }
            }
        }, Runnable { active = false })
    }

    private fun cancelAnimation() {
        currentAnimation?.let(UIViewAnimation::cancel)
    }

    companion object {

        private val kHandleRadius = 30.0f

    }

}
