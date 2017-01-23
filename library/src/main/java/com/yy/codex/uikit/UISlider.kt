package com.yy.codex.uikit

import android.content.Context
import android.graphics.Outline
import android.graphics.RectF
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
    private var slideListener : (Double) -> Unit = {};
    private var thumbRadius = 30.0

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
        thumbView!!.layer.shadowX = 2.0
        thumbView!!.layer.shadowY = 2.0
        thumbView!!.layer.shadowRadius = 2.0
        thumbView!!.layer.shadowColor = UIColor(.3, .3, .3, .2)
        thumbView!!.layer.cornerRadius = (thumbRadius / 2.0)
        thumbView!!.layer.borderWidth = 0.5
        thumbView!!.layer.borderColor = UIColor(0x00 / 255.0, 0x00 / 255.0, 0x00 / 255.0, 0.15)
        thumbView!!.layer.backgroundColor = UIColor.whiteColor

        addSubview(trackView!!)
        addSubview(trackPassedView!!)
        addSubview(thumbView!!)
    }


    override fun layoutSubviews() {
        super.layoutSubviews()
        val frameW = frame.size.width
        trackView!!.frame = CGRect(0.0, 14.0, frameW - 4, 4.0)
        trackPassedView!!.frame = CGRect(0.0, 14.0, frameW * value, 4.0)
        thumbView!!.frame = CGRect((frameW - thumbRadius) * value, 2.0, thumbRadius, thumbRadius)
    }

    override fun onEvent(event: UIControl.Event) {
        super.onEvent(event)
        when (event) {
            UIControl.Event.TouchUpOutside, UIControl.Event.TouchUpInside -> {
            }
            UIControl.Event.TouchDown -> {
            }
        }
    }

    override fun onLongPressed(sender: UILongPressGestureRecognizer) {
        super.onLongPressed(sender)
        if (sender.state == UIGestureRecognizerState.Changed) {
            if (pointInThumbView(sender.location())){
                val percentValue = (sender.location().x - frame.x) / frame.width
                setValueAnimated(percentValue)
            }
        }
    }

    public fun onSlide(listener: (Double) -> Unit){
        this.slideListener = listener
    }

    private fun setValueAnimated(value: Double) {
        if (value < 0.0) {
            this.value = 0.0
        } else if (value > 1.0) {
            this.value = 1.0
        } else {
            this.value = value
        }

        setValue(this.value)
        this.slideListener(this.value)
    }

    public fun setValue(value: Double) {
        if (value < 0.0) {
            this.value = 0.0
        } else if (value > 1.0) {
            this.value = 1.0
        } else {
            this.value = value
        }

        val frameW = frame.size.width
        trackPassedView!!.frame = CGRect(0.0, 14.0, (frameW - thumbRadius) * this.value, 4.0)
        thumbView!!.frame = CGRect((frameW - thumbRadius) * this.value, 2.0, thumbRadius, thumbRadius)
    }

    public fun getValue(): Double {
        return this.value
    }

    private fun pointInThumbView(point: CGPoint): Boolean {
//        val touchRadius = thumbRadius/2.0 + 10;
//        val pointCenter = CGPoint(thumbView!!.frame.x + touchRadius, thumbView!!.frame.y + touchRadius)
//        return point.inRange(touchRadius, touchRadius, pointCenter)
        return true
    }

}
