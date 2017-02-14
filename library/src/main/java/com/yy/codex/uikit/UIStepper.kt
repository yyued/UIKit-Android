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

        defaultTint = UIColor(0x12 / 255.0, 0x6a / 255.0, 1.0, 1.0)

        contentView = UIView(context)
        contentView.frame = CGRect(0.0, 0.0, 95.0, 30.0)
        addSubview(contentView)

        decreaseBtn = UIStepperButton(context)
        decreaseBtn.frame = CGRect(0.0, 0.0, 46.0, 30.0)
        decreaseBtn.setImage(UIImage("iVBORw0KGgoAAAANSUhEUgAAAC4AAAAEAQMAAADLQJ8kAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAABlBMVEUAev8AAADIAEdQAAAAAWJLR0QB/wIt3gAAAAlwSFlzAAALEgAACxIB0t1+/AAAAAtJREFUCNdjYMANAAAcAAHMmrA6AAAAJXRFWHRkYXRlOmNyZWF0ZQAyMDE3LTAyLTEzVDExOjE2OjA4KzA4OjAwvyjpMwAAACV0RVh0ZGF0ZTptb2RpZnkAMjAxNy0wMi0xM1QxMToxNjowOCswODowMM51UY8AAAAASUVORK5CYII=", 3.0), UIControl.State.Normal)
        contentView.addSubview(decreaseBtn)

        increaseBtn = UIStepperButton(context)
        increaseBtn.frame = CGRect(47.0, 0.0, 46.0, 30.0)
        increaseBtn.setImage(UIImage("iVBORw0KGgoAAAANSUhEUgAAAC4AAAAuAgMAAAAq18OkAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAACVBMVEUAAAAAev8AAADuBBdfAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxIAAAsSAdLdfvwAAAAdSURBVCjPY2AAAVEHBgQY4ZxQBAggljPwrh5cHADQ1R3PZlHuywAAACV0RVh0ZGF0ZTpjcmVhdGUAMjAxNy0wMi0xM1QxMToxNjowNSswODowMN7/iPMAAAAldEVYdGRhdGU6bW9kaWZ5ADIwMTctMDItMTNUMTE6MTY6MDUrMDg6MDCvojBPAAAAAElFTkSuQmCC", 3.0), UIControl.State.Normal)
        contentView.addSubview(increaseBtn)

        borderView = UIView(context)
        borderView.frame = CGRect(0.0, 0.0, 95.0, 30.0)
        borderView.userInteractionEnabled = false
        borderView.wantsLayer = true
        borderView.layer.borderWidth = 1.0
        borderView.layer.borderColor = tintColor ?: defaultTint
        borderView.layer.cornerRadius = 3.0
        addSubview(borderView)

        dividerView = UIView(context)
        dividerView.frame = CGRect(46.0, 0.0, 1.0, 30.0)
        dividerView.setBackgroundColor(tintColor ?: defaultTint)
        addSubview(dividerView)
    }

    // appearance

    private lateinit var decreaseBtn : UIStepperButton

    private lateinit var increaseBtn : UIStepperButton

    private lateinit var contentView : UIView

    private lateinit var borderView: UIView

    private lateinit var dividerView : UIView

    var bgColor: UIColor = UIColor.whiteColor // @Td should be clearColor

    var defaultTint: UIColor = UIColor(0x12 / 255.0, 0x6a / 255.0, 1.0, 1.0)
        private set

    override fun tintColorDidChanged() {
        super.tintColorDidChanged()
        borderView.layer.borderColor = tintColor ?: defaultTint
        dividerView.setBackgroundColor(tintColor ?: defaultTint)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        increaseBtn.enable = if (wraps) true else (value < maxValue)
        decreaseBtn.enable = if (wraps) true else (value > minValue)
    }

    // data

    var value : Double = 0.0
        set(value) {
            if (field == value) {
                return
            }
            if (value <= minValue){
                field = if (wraps) maxValue else minValue
                decreaseBtn.enable = wraps
            }
            else if (value >= maxValue){
                field = if (wraps) minValue else maxValue
                increaseBtn.enable = wraps
            }
            else {
                field = value
                increaseBtn.enable = true
                decreaseBtn.enable = true
            }
            onEvent(Event.ValueChanged)
        }

    var minValue : Double = 0.0
        set(value) {
            if (field == value){
                return
            }
            field = value
            layoutSubviews()
        }

    var maxValue : Double = 100.0
        set(value) {
            if (field == value){
                return
            }
            field = value
            layoutSubviews()
        }

    var stepValue : Double = 1.0
        set(value) {
            if (field == value){
                return
            }
            field = value
            layoutSubviews()
        }

    var wraps : Boolean = false
        set(value) {
            if (field == value){
                return
            }
            field = value
            layoutSubviews()
        }

    // supp

    fun onSelectWithButton(btn: UIStepperButton){
        value += if (btn == increaseBtn) stepValue else -stepValue
    }
}