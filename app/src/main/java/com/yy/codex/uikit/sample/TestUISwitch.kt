package com.yy.codex.uikit.sample

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.yy.codex.foundation.NSLog
import com.yy.codex.uikit.*

/**
 * Created by adi on 17/2/13.
 */
class TestUISwitch : UIView {
    constructor(context: Context, view: View) : super(context, view)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun init() {
        super.init()

        // simple
        val s1 = UISwitch(context)
        s1.frame = CGRect(10.0, 10.0, 52.0, 44.0)
        addSubview(s1)

        // configure appearance
        val s2 = UISwitch(context)
        s2.frame = CGRect(210.0, 10.0, 52.0, 44.0)
        s2.setOnTrackColor(UIColor.orangeColor)
        s2.setOnThumbColor(UIColor.orangeColor.colorWithDarken(0.3))
        s2.setOffTrackColor(UIColor.grayColor)
        s2.setOffThumbColor(UIColor.blackColor)
        addSubview(s2)

        // configure status
        val s3 = UISwitch(context)
        s3.frame = CGRect(110.0, 10.0, 52.0, 44.0)
        s3.setOn(true)
        addSubview(s3)

        // onValueChanged
        s1.addBlock( Runnable{
            NSLog.log(s1.isOn())
        }, UIControl.Event.ValueChanged)

    }


}