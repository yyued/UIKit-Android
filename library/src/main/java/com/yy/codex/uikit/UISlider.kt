package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import com.yy.codex.foundation.lets

/**
 * Created by adi on 17/1/19.
 */

class UISlider : UIControl {

    /* initialize */

    constructor(context: Context, view: View) : super(context, view) {}
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    private fun defaultValue() {
        progressValue = 0.5
        thumbRadius = 30.0
    }

    override fun init() {
        super.init()
        defaultValue()

        trackView = UIView(context)
        trackView.wantsLayer = true
        trackView.layer.backgroundColor = UIColor(0xb7 / 255.0, 0xb7 / 255.0, 0xb7 / 255.0, 1.0)
        trackView.layer.cornerRadius = 1.0
        addSubview(trackView)

        progressView = UIView(context)
        progressView.wantsLayer = true
        progressView.layer.backgroundColor = if (tintColor != null) tintColor!! else UIColor(0x10 / 255.0, 0x6a / 255.0, 1.0, 1.0)
        progressView.layer.cornerRadius = 1.0
        addSubview(progressView)

        thumbView = UIView(context)
        thumbView.wantsLayer = true
        thumbView.layer.shadowX = 2.0
        thumbView.layer.shadowY = 2.0
        thumbView.layer.shadowRadius = 2.0
        thumbView.layer.shadowColor = UIColor(.3, .3, .3, .2)
        thumbView.layer.cornerRadius = (thumbRadius / 2.0)
        thumbView.layer.borderWidth = 0.5
        thumbView.layer.borderColor = UIColor(0x00 / 255.0, 0x00 / 255.0, 0x00 / 255.0, 0.15)
        thumbView.layer.backgroundColor = UIColor.whiteColor
        thumbView.layer.wantsEnlargerLayer()
        addSubview(thumbView)
    }

    override fun tintColorDidChanged() {
        super.tintColorDidChanged()
        lets(progressView, tintColor, { progressView, tintColor ->
            progressView.layer.backgroundColor = tintColor
        })
    }

    /* appearance */

    private lateinit var thumbView: UIView

    private lateinit var trackView: UIView

    private lateinit var progressView: UIView

    private var thumbRadius = 30.0

    private var progressValue: Double = 0.toDouble()
        set(value) {
            if (value < 0.0){
                field = 0.0
            }
            else if (value > 1.0){
                field = 1.0
            }
            else {
                field = value
            }
        }

    var value: Double
        set(value) {
            this.progressValue = value
            progressView.frame = CGRect(0.0, 15.0, (frame.width - thumbRadius - 4) * this.progressValue, 2.0)
            thumbView.frame = CGRect((frame.width - thumbRadius - 4) * this.progressValue, 1.0, thumbRadius, thumbRadius)
        }
        get():Double {
            return this.progressValue
        }

    override fun layoutSubviews() {
        super.layoutSubviews()
        val frameW = frame.size.width
        trackView.frame = CGRect(0.0, 15.0, frameW - 6, 2.0)
        progressView.frame = CGRect(0.0, 15.0, (frameW - thumbRadius - 4) * progressValue, 2.0)
        thumbView.frame = CGRect((frameW - thumbRadius - 4) * progressValue, 1.0, thumbRadius, thumbRadius)
    }

    override fun onLongPressed(sender: UILongPressGestureRecognizer) {
        super.onLongPressed(sender)
        if (sender.state == UIGestureRecognizerState.Changed) {
            if (pointInThumbView(sender.location())){
                val percentValue = (sender.location().x - frame.x) / frame.width
                this.value = percentValue
                onEvent(UIControl.Event.ValueChanged)
            }
        }
    }

    /* supports */

    private fun pointInThumbView(point: CGPoint): Boolean {
        return true
//        thumbView?.let {
//            val touchRadius = thumbRadius/2.0 + 10;
//            val pointCenter = CGPoint(it.frame.x + touchRadius, it.frame.y + touchRadius)
//            return point.inRange(touchRadius, touchRadius, pointCenter)
//        }
//        return false
    }
}
