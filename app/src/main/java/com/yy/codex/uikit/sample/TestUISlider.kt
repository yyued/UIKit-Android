package com.yy.codex.uikit.sample

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.yy.codex.foundation.NSLog
import com.yy.codex.uikit.*

/**
 * Created by adi on 17/2/13.
 */
class TestUISlider : UIView {
    constructor(context: Context, view: View) : super(context, view)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun init() {
        super.init()

        // configure width
        val s1 = UISlider(context)
        s1.frame = CGRect(10.0, 60.0, 300.0, 42.0)
        addSubview(s1)

        // configure appearance
        val s2 = UISlider(context)
        s2.frame = CGRect(10.0, 160.0, 200.0, 42.0)
        s2.tintColor = UIColor.orangeColor
        addSubview(s2)

        // configure status
        val s3 = UISlider(context)
        s3.frame = CGRect(10.0, 260.0, 200.0, 42.0)
        s3.progressValue = 1.0
        addSubview(s3)

        // onValueChanged
        s1.addBlock(Runnable {
            NSLog.log(s1.progressValue)
        }, UIControl.Event.ValueChanged)
    }
}