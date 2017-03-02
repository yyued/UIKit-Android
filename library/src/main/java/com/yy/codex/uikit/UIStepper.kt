package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

/**
 * Created by adi on 17/1/23.
 */
class UIStepper : UIControl {

    // initialize

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
        initBorderView()
        addSubview(borderView)
        initDividerView()
        addSubview(dividerView)
    }

    override fun prepareProps(attrs: AttributeSet) {
        super.prepareProps(attrs)
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.UIStepper, 0, 0)
        typedArray.getFloat(R.styleable.UIStepper_stepper_value, 0.0f)?.let {
            initializeAttributes.put("UIStepper.value", it)
        }
        typedArray.getFloat(R.styleable.UIStepper_stepper_minValue, 0.0f)?.let {
            initializeAttributes.put("UIStepper.minValue", it)
        }
        typedArray.getFloat(R.styleable.UIStepper_stepper_maxValue, 100.0f)?.let {
            initializeAttributes.put("UIStepper.maxValue", it)
        }
        typedArray.getFloat(R.styleable.UIStepper_stepper_stepValue, 1.0f)?.let {
            initializeAttributes.put("UIStepper.stepValue", it)
        }
        typedArray.getBoolean(R.styleable.UIStepper_stepper_wraps, true)?.let {
            initializeAttributes.put("UIStepper.wraps", it)
        }
    }

    override fun resetProps() {
        super.resetProps()
        initializeAttributes?.let {
            (it["UIStepper.value"] as? Float)?.let {
                value = it.toDouble()
            }
            (it["UIStepper.minValue"] as? Float)?.let {
                minValue = it.toDouble()
            }
            (it["UIStepper.maxValue"] as? Float)?.let {
                maxValue = it.toDouble()
            }
            (it["UIStepper.stepValue"] as? Float)?.let {
                stepValue = it.toDouble()
            }
            (it["UIStepper.wraps"] as? Boolean)?.let {
                wraps = it
            }
        }
    }

    override fun intrinsicContentSize(): CGSize {
        return CGSize(95.0, 30.0)
    }

    override fun willMoveToWindow() {
        super.willMoveToWindow()
        resetEnabled()
    }

    private lateinit var contentView : UIView

    private fun initContentView() {
        contentView = UIView(context)
        contentView.frame = CGRect(0.0, 0.0, 95.0, 30.0)
        initDecreaseButton()
        contentView.addSubview(decreaseButton)
        initIncreaseButton()
        contentView.addSubview(increaseButton)
    }

    private lateinit var decreaseButton: UIButton

    private fun initDecreaseButton() {
        decreaseButton = UIButton(context)
        decreaseButton.frame = CGRect(0.0, 0.0, 46.0, 30.0)
        decreaseButton.setImage(UIImage("iVBORw0KGgoAAAANSUhEUgAAAC4AAAAEAQMAAADLQJ8kAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAABlBMVEUAev8AAADIAEdQAAAAAWJLR0QB/wIt3gAAAAlwSFlzAAALEgAACxIB0t1+/AAAAAtJREFUCNdjYMANAAAcAAHMmrA6AAAAJXRFWHRkYXRlOmNyZWF0ZQAyMDE3LTAyLTEzVDExOjE2OjA4KzA4OjAwvyjpMwAAACV0RVh0ZGF0ZTptb2RpZnkAMjAxNy0wMi0xM1QxMToxNjowOCswODowMM51UY8AAAAASUVORK5CYII=", 3.0), UIControl.State.Normal)
        decreaseButton.addTarget(this, "onDecreaseButtonTouchUpInside", Event.TouchUpInside)
    }

    private fun onDecreaseButtonTouchUpInside() {
        value = if (wraps) Math.max(minValue, value - stepValue) else if (value - stepValue <= minValue) value else value - stepValue
    }

    private lateinit var increaseButton: UIButton

    private fun initIncreaseButton() {
        increaseButton = UIButton(context)
        increaseButton.frame = CGRect(47.0, 0.0, 46.0, 30.0)
        increaseButton.setImage(UIImage("iVBORw0KGgoAAAANSUhEUgAAAC4AAAAuAgMAAAAq18OkAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAACVBMVEUAAAAAev8AAADuBBdfAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxIAAAsSAdLdfvwAAAAdSURBVCjPY2AAAVEHBgQY4ZxQBAggljPwrh5cHADQ1R3PZlHuywAAACV0RVh0ZGF0ZTpjcmVhdGUAMjAxNy0wMi0xM1QxMToxNjowNSswODowMN7/iPMAAAAldEVYdGRhdGU6bW9kaWZ5ADIwMTctMDItMTNUMTE6MTY6MDUrMDg6MDCvojBPAAAAAElFTkSuQmCC", 3.0), UIControl.State.Normal)
        increaseButton.addTarget(this, "onIncreaseButtonTouchUpInside", Event.TouchUpInside)
    }

    private fun onIncreaseButtonTouchUpInside() {
        value = if (wraps) Math.min(maxValue, value + stepValue) else if (value + stepValue >= minValue) value else value + stepValue
    }

    private lateinit var borderView: UIView

    private fun initBorderView() {
        borderView = UIView(context)
        borderView.frame = CGRect(0.0, 0.0, 95.0, 30.0)
        borderView.userInteractionEnabled = false
        borderView.wantsLayer = true
        borderView.layer.borderWidth = 1.0
        borderView.layer.borderColor = tintColor
        borderView.layer.cornerRadius = 3.0
    }

    private lateinit var dividerView : UIView

    private fun initDividerView() {
        dividerView = UIView(context)
        dividerView.frame = CGRect(46.0, 0.0, 1.0, 30.0)
        dividerView.setBackgroundColor(tintColor)
    }

    override fun tintColorDidChanged() {
        super.tintColorDidChanged()
        borderView.layer.borderColor = tintColor
        dividerView.setBackgroundColor(tintColor)
    }

    var value : Double = 0.0
        set(value) {
            if (field == value) {
                return
            }
            field = value
            resetEnabled()
            onEvent(Event.ValueChanged)
        }

    var minValue : Double = 0.0
        set(value) {
            if (field == value){
                return
            }
            field = value
            resetEnabled()
        }

    var maxValue : Double = 100.0
        set(value) {
            if (field == value){
                return
            }
            field = value
            resetEnabled()
        }

    var stepValue : Double = 1.0
        set(value) {
            if (field == value){
                return
            }
            field = value
            resetEnabled()
        }

    var wraps : Boolean = true
        set(value) {
            if (field == value){
                return
            }
            field = value
            resetEnabled()
        }

    private fun resetEnabled() {
        increaseButton.enable = if (wraps) (value + stepValue <= maxValue) else (value + stepValue < maxValue)
        decreaseButton.enable = if (wraps) (value - stepValue >= minValue) else (value - stepValue > minValue)
    }

}