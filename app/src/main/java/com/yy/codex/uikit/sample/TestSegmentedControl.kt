package com.yy.codex.uikit.sample

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.yy.codex.foundation.NSLog
import com.yy.codex.uikit.*

/**
 * Created by adi on 17/2/14.
 */
class TestSegmentedControl : UIView {
    constructor(context: Context, view: View) : super(context, view)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun init() {
        super.init()

        // simple
        val s1 = UISegmentedControl(context)
        s1.frame = CGRect(10.0, 50.0, 200.0, 30.0)
        s1.setItems(listOf(UISegmentedItem("aaa"), UISegmentedItem("abc"), UISegmentedItem("ccc")))
        addSubview(s1)

        // configure appearance
        val s2 = UISegmentedControl(context)
        s2.frame = CGRect(10.0, 150.0, 200.0, 30.0)
        s2.tintColor = UIColor.orangeColor
        s2.setBackgroundColor(UIColor.yellowColor)
        s2.setItems(listOf(UISegmentedItem("aaa"), UISegmentedItem("abc"), UISegmentedItem("ccc")))
        addSubview(s2)

        // configure status
        val s3 = UISegmentedControl(context)
        s3.frame = CGRect(10.0, 250.0, 200.0, 30.0)
        s3.setItems(listOf(UISegmentedItem("aaa"), UISegmentedItem("abc"), UISegmentedItem("ccc", false), UISegmentedItem("ddd", true), UISegmentedItem("eee", true)))
        addSubview(s3)

        // onValueChanged
        s3.addBlock(Runnable {
            NSLog.log(s3.activeIndex)
        }, UIControl.Event.ValueChanged)
    }
}