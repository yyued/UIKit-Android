package com.yy.codex.uikit.sample

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.yy.codex.uikit.CGRect
import com.yy.codex.uikit.UIActivityIndicator
import com.yy.codex.uikit.UIColor
import com.yy.codex.uikit.UIView

/**
 * Created by adi on 17/2/14.
 */
class TestActivityIndicator : UIView {
    constructor(context: Context, view: View) : super(context, view)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun init() {
        super.init()

        // simple
        val s1 = UIActivityIndicator(context)
        s1.frame = CGRect(140.0, 140.0, 30.0, 30.0)
        addSubview(s1)

        // configure appearance
        val s2 = UIActivityIndicator(context)
        s2.frame = CGRect(140.0, 240.0, 30.0, 30.0)
        s2.color = UIColor.orangeColor
        addSubview(s2)

        // configure size
        val s3 = UIActivityIndicator(context)
        s3.frame = CGRect(140.0, 340.0, 60.0, 60.0)
        addSubview(s3)

    }
}