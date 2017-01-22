package com.yy.codex.uikit

import android.content.Context
import android.graphics.Outline
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider

import com.yy.codex.foundation.NSLog

/**
 * Created by adi on 17/1/19.
 */

class UISlider : UIControl {

    private var thumbView: UIView? = null
    private var trackView: UIView? = null
    private var trackPassedView: UIView? = null
    private var value: Double = 0.toDouble() // range: 0.0 ~ 1.0
    private var callback: UISliderCallback? = null

    private var thumbRadius = 30.0

    private val active: Boolean = false


    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    private fun defaultValue() {
        value = 0.5
        thumbRadius = 30.0
    }

    override fun init() {
        super.init()
        defaultValue()

        trackView = UIView(context)
        trackView!!.isWantsLayer = true
        trackView!!.layer.backgroundColor = UIColor(0xb7 / 255.0, 0xb7 / 255.0, 0xb7 / 255.0, 1.0)
        trackView!!.layer.cornerRadius = 2.0

        trackPassedView = UIView(context)
        trackPassedView!!.isWantsLayer = true
        trackPassedView!!.layer.backgroundColor = UIColor(0x10 / 255.0, 0x6a / 255.0, 1.0, 1.0)
        trackPassedView!!.layer.cornerRadius = 2.0

        thumbView = UIView(context)
        thumbView!!.isWantsLayer = true
        thumbView!!.layer.setShadowX(2.0).setShadowY(2.0).setShadowRadius(0.5).shadowColor = UIColor(.3, .3, .3, .2)
        thumbView!!.layer.setCornerRadius(thumbRadius / 2.0).setBorderWidth(0.5).borderColor = UIColor(0x00 / 255.0, 0x00 / 255.0, 0x00 / 255.0, 0.15)
        thumbView!!.layer.backgroundColor = UIColor.whiteColor

        addSubview(trackView!!)
        addSubview(trackPassedView!!)
        addSubview(thumbView!!)
    }


    override fun layoutSubviews() {
        super.layoutSubviews()
        NSLog.warn(frame)

        val frameW = frame.size.width
        trackView!!.frame = CGRect(0.0, 14.0, frameW, 4.0)
        trackPassedView!!.frame = CGRect(0.0, 14.0, frameW * value, 4.0)
        thumbView!!.frame = CGRect(frameW * value - thumbRadius / 2.0, 2.0, thumbRadius, thumbRadius)
    }

    override fun onEvent(event: UIControl.Event) {
        super.onEvent(event)
        when (event) {
            UIControl.Event.TouchUpOutside, UIControl.Event.TouchUpInside -> {
            }
        }// reset active = false
        // is pointInSide?
        // if YES
        // ... active = true
        // ...
        //            case Touch
    }

    override fun onLongPressed(sender: UILongPressGestureRecognizer) {
        super.onLongPressed(sender)
        if (sender.state == UIGestureRecognizerState.Changed) {
            NSLog.warn(sender.location())

            // location.x -> n%
            // setValue(n%), slideCallback
        }
    }

    fun onSlide(callback: UISliderCallback) {
        this.callback = callback // callback.handle(this.value);
    }

    private fun setValueAnimated(value: Double) {

    }

    fun setValue(value: Double) {
        if (value < 0.0) {
            this.value = 0.0
        } else if (value > 1.0) {
            this.value = 1.0
        } else {
            this.value = value
        }
    }

    fun getValue(): Double {
        return this.value
    }

}
