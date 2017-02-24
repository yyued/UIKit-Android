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

    override fun init() {
        super.init()
        initTrackView()
        addSubview(trackView)
        initProgressView()
        addSubview(progressView)
        initThumbView()
        addSubview(thumbView)
        resetViewFrames()
    }

    override fun tintColorDidChanged() {
        super.tintColorDidChanged()
        lets(progressView, tintColor, { progressView, tintColor ->
            progressView.layer.backgroundColor = tintColor
        })
    }

    override fun intrinsicContentSize(): CGSize {
        return CGSize(0.0, 44.0)
    }

    /* appearance */

    private lateinit var trackView: UIView

    private fun initTrackView() {
        trackView = UIView(context)
        trackView.wantsLayer = true
        trackView.layer.backgroundColor = UIColor(0xb7 / 255.0, 0xb7 / 255.0, 0xb7 / 255.0, 1.0)
        trackView.layer.cornerRadius = 1.0
    }

    private lateinit var thumbView: UIView

    private fun initThumbView() {
        thumbView = UIView(context)
        thumbView.wantsLayer = true
        thumbView.layer.shadowX = 2.0
        thumbView.layer.shadowY = 2.0
        thumbView.layer.shadowRadius = 2.0
        thumbView.layer.shadowColor = UIColor(.3, .3, .3, .2)
        thumbView.layer.cornerRadius = (kThumbRadius / 2.0)
        thumbView.layer.borderWidth = 0.5
        thumbView.layer.borderColor = UIColor(0x00 / 255.0, 0x00 / 255.0, 0x00 / 255.0, 0.15)
        thumbView.layer.backgroundColor = UIColor.whiteColor
        thumbView.layer.wantsEnlargerLayer()
    }

    private lateinit var progressView: UIView

    private fun initProgressView() {
        progressView = UIView(context)
        progressView.wantsLayer = true
        progressView.layer.backgroundColor = if (tintColor != null) tintColor!! else UIColor(0x10 / 255.0, 0x6a / 255.0, 1.0, 1.0)
        progressView.layer.cornerRadius = 1.0
    }

    var progressValue = 0.5
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
            resetViewFrames()
        }

    override fun layoutSubviews() {
        super.layoutSubviews()
        resetViewFrames()
    }

    private fun resetViewFrames() {
        val frameW = frame.size.width
        trackView.frame = CGRect(0.0, 15.0, frameW - 6, 2.0)
        progressView.frame = CGRect(0.0, 15.0, (frameW - kThumbRadius - 4) * progressValue, 2.0)
        thumbView.frame = CGRect((frameW - kThumbRadius - 4) * progressValue, 1.0, kThumbRadius, kThumbRadius)
    }

    private var sliderTracking = false

    override fun onLongPressed(sender: UILongPressGestureRecognizer) {
        super.onLongPressed(sender)
        if (sender.state == UIGestureRecognizerState.Began) {
            if (UIViewHelpers.pointInside(thumbView, sender.location(thumbView))) {
                sliderTracking = true
            }
        }
        else if (sender.state == UIGestureRecognizerState.Changed && sliderTracking) {
            this.progressValue = Math.max(0.0, Math.min(1.0, sender.location(this).x / frame.width))
            onEvent(Event.ValueChanged)
        }
        else if (sender.state == UIGestureRecognizerState.Ended) {
            sliderTracking = false
        }
    }

    companion object {

        private val kThumbRadius = 30.0

    }

}
