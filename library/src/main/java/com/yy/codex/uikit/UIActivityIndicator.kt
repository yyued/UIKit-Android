package com.yy.codex.uikit

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar

/**
 * Created by adi on 17/2/7.
 */
class UIActivityIndicator : UIView {

    constructor(context: Context, view: View) : super(context, view)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun init() {
        super.init()
        indicator = ProgressBar(context)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        if (!indicatorDidSet) {
            addView(indicator, (frame.size.width * UIScreen.mainScreen.scale()).toInt(), (frame.size.height.toInt() * UIScreen.mainScreen.scale()).toInt())
            indicatorDidSet = true
        }
    }

    private var indicatorDidSet: Boolean = false

    private lateinit var indicator: ProgressBar

    var color: UIColor = UIColor.whiteColor
        set(value) {
            indicator.indeterminateDrawable.setColorFilter(value.toInt(), PorterDuff.Mode.MULTIPLY)
        }

}