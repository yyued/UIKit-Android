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

        trackColor = UIColor(0xb7 / 255.0, 0xb7 / 255.0, 0xb7 / 255.0, 1.0)

        trackView = UIView(context)
        trackView?.let {
            it.wantsLayer = true
            it.layer.backgroundColor = trackColor
            it.layer.cornerRadius = 1.0
            addSubview(it)
        }

        progressView = UIView(context)
        progressView?.let {
            it.wantsLayer = true
            it.layer.backgroundColor = if (tintColor != null) tintColor!! else UIColor(0x10 / 255.0, 0x6a / 255.0, 1.0, 1.0)
            it.layer.cornerRadius = 1.0
            addSubview(it)
        }
    }

    /* UI Details & Rendering */

    private var progressView: UIView? = null

    private var trackView: UIView? = null

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
            trackView?.let {
                it.layer.backgroundColor = value
            }
        }

    private var currentAnimation: UIViewAnimation? = null

    override fun layoutSubviews() {
        super.layoutSubviews()
        val frameW = frame.size.width
        trackView?.let {
            it.frame = CGRect(0.0, 15.0, frameW, 2.0)
            it.layer.backgroundColor = trackColor
        }
        progressView?.let {
            it.frame = CGRect(0.0, 15.0, frameW * value, 2.0)
            it.layer.backgroundColor = if (tintColor != null) tintColor!! else UIColor(0x10 / 255.0, 0x6a / 255.0, 1.0, 1.0)
        }
    }

    override fun tintColorDidChanged() {
        super.tintColorDidChanged()
        lets(progressView, tintColor, { progressView, tintColor ->
            progressView.layer.backgroundColor = tintColor
        })
    }

    /* Support */

    private fun setValueAnimated(value: Double){
        cancelAnimation()
        currentAnimation = UIViewAnimator.spring(Runnable {
            progressView?.let {
                it.frame = CGRect(0.0, 15.0, frame.size.width * value, 2.0)
            }
        })
    }

    private fun cancelAnimation() {
        currentAnimation?.let {
            it.cancel()
        }
    }

}