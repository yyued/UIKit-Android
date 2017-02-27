package com.yy.codex.uikit

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar

/**
 * Created by adi on 17/2/7.
 */
class UIActivityIndicatorView : UIView {

    constructor(context: Context, view: View) : super(context, view)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private lateinit var indicator: ProgressBar

    var color: UIColor = tintColor ?: UIColor.blackColor
        set(value) {
            field = value
            indicator.indeterminateDrawable.setColorFilter(value.toInt(), PorterDuff.Mode.MULTIPLY)
        }

    override fun init() {
        super.init()
        indicator = ProgressBar(context)
        addView(indicator)
        color = tintColor ?: UIColor.blackColor
        stopAnimating()
    }

    override fun prepareProps(attrs: AttributeSet) {
        super.prepareProps(attrs)
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.UIActivityIndicatorView, 0, 0)
        typedArray.getColor(R.styleable.UIActivityIndicatorView_activityIndicator_color, -1)?.let {
            if (it != -1) {
                initializeAttributes.put("UIActivityIndicatorView.color", UIColor(it))
            }
        }
        typedArray.getBoolean(R.styleable.UIActivityIndicatorView_activityIndicator_animating, false)?.let {
            initializeAttributes.put("UIActivityIndicatorView.animating", it)
        }
    }

    override fun resetProps() {
        super.resetProps()
        initializeAttributes?.let {
            (it["UIActivityIndicatorView.color"] as? UIColor)?.let {
                color = it
            }
            (it["UIActivityIndicatorView.animating"] as? Boolean)?.let {
                if (it) startAnimating() else stopAnimating()
            }
        }
    }

    override fun intrinsicContentSize(): CGSize {
        return CGSize(44.0, 44.0)
    }

    fun startAnimating() {
        alpha = 1.0f
    }

    fun stopAnimating() {
        alpha = 0.0f
    }

}