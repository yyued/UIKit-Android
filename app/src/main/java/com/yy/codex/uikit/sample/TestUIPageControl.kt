package com.yy.codex.uikit.sample

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.yy.codex.foundation.NSLog
import com.yy.codex.uikit.*

/**
 * Created by adi on 17/2/13.
 */
class TestUIPageControl : UIView {
    constructor(context: Context, view: View) : super(context, view)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun init() {
        super.init()

//        setBackgroundColor(UIColor.yellowColor.colorWithAlpha(0.3))
//
//        // simple
//        val s1 = UIPageControl(context)
//        s1.frame = CGRect(10.0, 50.0, 100.0, 32.0)
//        addSubview(s1)
//
//        // configure appearance
//        val s2 = UIPageControl(context)
//        s2.frame = CGRect(10.0, 100.0, 100.0, 32.0)
//        s2.pageIndicatorColor = UIColor.orangeColor.colorWithAlpha(0.6)
//        s2.currentPageIndicatorColor = UIColor.orangeColor
//        addSubview(s2)
//
//        // configure status, numberOfPages
//        val s3 = UIPageControl(context)
//        s3.frame = CGRect(10.0, 150.0, 100.0, 32.0)
//        s3.numberOfPages = 5
//        addSubview(s3)
//
//        // configure status, hidesForSinglePage
//        val s4 = UIPageControl(context)
//        s4.frame = CGRect(10.0, 200.0, 100.0, 32.0)
//        addSubview(s4)
//        postDelayed({
//            s4.hidesForSinglePage = true
//            s4.numberOfPages = 1
//        }, 1000)
//
//        // onValueChanged
//        s1.addBlock(Runnable{
//            NSLog.log(s1.currentPage)
//        }, UIControl.Event.ValueChanged)
//        postDelayed({
//            s1.currentPage = 1
//        }, 2000)
    }
}