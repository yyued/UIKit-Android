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
        progressView.layer.backgroundColor = tintColor
        progressView.layer.cornerRadius = 1.0
        addSubview(progressView)
    }

    override fun intrinsicContentSize(): CGSize {
        return CGSize(0.0, 2.0)
    }

    override fun prepareProps(attrs: AttributeSet) {
        super.prepareProps(attrs)
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.UIProgressView, 0, 0)
        typedArray.getFloat(R.styleable.UIProgressView_progressview_value, 0.0f)?.let {
            initializeAttributes.put("UIProgressView.value", it)
        }
        typedArray.getColor(R.styleable.UIProgressView_progressview_trackColor, -1)?.let {
            if (it != -1) {
                initializeAttributes.put("UIProgressView.trackColor", UIColor(it))
            }
        }
    }

    override fun resetProps() {
        super.resetProps()
        initializeAttributes?.let {
            (it["UIProgressView.value"] as? Float)?.let {
                value = it.toDouble()
            }
            (it["UIProgressView.trackColor"] as? UIColor)?.let {
                trackColor = it
            }
        }
    }

    /* appearance */

    private lateinit var progressView: UIView

    private lateinit var trackView: UIView

    var value: Double = 0.5
        private set(value) {
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
        }

    var trackColor: UIColor = UIColor(0xb7 / 255.0, 0xb7 / 255.0, 0xb7 / 255.0, 1.0)
        set(value) {
            field = value
            trackView.layer.backgroundColor = value
        }

    private var currentAnimation: UIViewAnimation? = null

    override fun layoutSubviews() {
        super.layoutSubviews()
        trackView.frame = CGRect(0.0, 0.0, frame.size.width, 2.0)
        progressView.frame = CGRect(0.0, 0.0, frame.size.width * value, 2.0)
    }

    override fun tintColorDidChanged() {
        super.tintColorDidChanged()
        lets(progressView, tintColor, { progressView, tintColor ->
            progressView.layer.backgroundColor = tintColor
        })
    }

    /* support */

    fun setValue(value: Double, animated: Boolean){
        this.value = value
        currentAnimation?.let(UIViewAnimation::cancel)
        if (animated) {
            currentAnimation = UIViewAnimator.springWithBounciness(1.0, 20.0, Runnable {
                progressView.frame = CGRect(0.0, 0.0, frame.size.width * value, 2.0)
            }, Runnable {  })
        }
        else {
            progressView.frame = CGRect(0.0, 0.0, frame.size.width * value, 2.0)
        }
    }

}