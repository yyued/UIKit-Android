package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import com.yy.codex.foundation.lets

/**
 * Created by adi on 17/2/6.
 */

class UIProgressView : UIControl {

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
    }

    /* appearance */

    private lateinit var progressView: UIView

    private lateinit var trackView: UIView

    var value: Double = 0.5
        set(value) {
            if (field == value){
                return
            }
            else if (value < 0.0){
                field = 0.0
            }
            else if (value > 1.0){
                field = 1.0
            }
            else {
                field = value
            }
            onEvent(Event.ValueChanged)
            setValueAnimated(value)
        }

    var trackColor: UIColor = UIColor(0xb7 / 255.0, 0xb7 / 255.0, 0xb7 / 255.0, 1.0)
        set(value) {
            field = value
            trackView.layer.backgroundColor = value
        }

    private var currentAnimation: UIViewAnimation? = null

    override fun layoutSubviews() {
        super.layoutSubviews()
        val frameW = frame.size.width
        trackView.frame = CGRect(0.0, 15.0, frameW, 2.0)
        trackView.layer.backgroundColor = trackColor
        progressView.frame = CGRect(0.0, 15.0, frameW * value, 2.0)
        progressView.layer.backgroundColor = if (tintColor != null) tintColor!! else UIColor(0x10 / 255.0, 0x6a / 255.0, 1.0, 1.0)
    }

    override fun tintColorDidChanged() {
        super.tintColorDidChanged()
        lets(progressView, tintColor, { progressView, tintColor ->
            progressView.layer.backgroundColor = tintColor
        })
    }

    /* support */

    private fun setValueAnimated(value: Double){
        cancelAnimation()
        currentAnimation = UIViewAnimator.spring(Runnable {
            progressView.frame = CGRect(0.0, 15.0, frame.size.width * value, 2.0)
        })
    }

    private fun cancelAnimation() {
        currentAnimation?.let(UIViewAnimation::cancel)
    }

}