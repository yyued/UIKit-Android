package com.yy.codex.uikit.sample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import com.yy.codex.foundation.NSLog
import com.yy.codex.uikit.*

/**
 * Created by adi on 17/1/18.
 */

class TestUIComponent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val TView = TestComponent(this)
        setContentView(TView)
    }
}

internal class TestComponent : UIView {
    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun init() {
        super.init()

        val uiSwitch = UISwitch(context)
        uiSwitch.frame = CGRect(10.0, 10.0, 52.0, 44.0)
        uiSwitch.setOn(true)
        uiSwitch.addBlock( Runnable{
            NSLog.log(uiSwitch.isOn())
        }, UIControl.Event.ValueChanged)
        addSubview(uiSwitch)

        val uiSlider = UISlider(context)
        uiSlider.frame = CGRect(10.0, 60.0, 200.0, 42.0)
        uiSlider.tintColor = UIColor.orangeColor
        uiSlider.value = 1.0
        addSubview(uiSlider)
        uiSlider.addBlock(Runnable {
            NSLog.log(uiSlider.value)
        }, UIControl.Event.ValueChanged)

        val uiProgressView = UIProgressView(context)
        uiProgressView.frame = CGRect(10.0, 100.0, 200.0, 32.0)
        uiProgressView.tintColor = UIColor.greenColor
        addSubview(uiProgressView)
        uiProgressView.addBlock(Runnable {
            NSLog.log(uiProgressView.value)
        }, UIControl.Event.ValueChanged)
        postDelayed({
            uiProgressView.value = 0.8
        }, 1000)

        val uiPageControl = UIPageControl(context)
        uiPageControl.frame = CGRect(10.0, 150.0, 100.0, 32.0)
        uiPageControl.numberOfPages = 5
        uiPageControl.pageIndicatorColor = UIColor.orangeColor.colorWithAlpha(0.6)
        uiPageControl.currentPageIndicatorColor = UIColor.orangeColor
        addSubview(uiPageControl)
        uiPageControl.addBlock(Runnable{
            NSLog.log(uiPageControl.currentPage)
        }, UIControl.Event.ValueChanged)
        postDelayed({
            uiPageControl.currentPage = 3
        }, 1000)

        val uiLoading = UIActivityIndicator(context)
        uiLoading.frame = CGRect(140.0, 140.0, 30.0, 30.0)
        uiLoading.color = UIColor.orangeColor
        // addSubview(uiLoading)

        // @Td
        val uiStepper = UIStepper(context)
        uiStepper.frame = CGRect(10.0, 200.0, 100.0, 30.0)
        addSubview(uiStepper)

        val uiSegmentedControl = UISegmentedControl(context)
        uiSegmentedControl.frame = CGRect(10.0, 250.0, 200.0, 30.0)
        uiSegmentedControl.tintColor = UIColor.orangeColor
        uiSegmentedControl.setBackgroundColor(UIColor.yellowColor)
        uiSegmentedControl.setItems(listOf(UISegmentedItem("aaa"), UISegmentedItem("abc"), UISegmentedItem("ccc", false), UISegmentedItem("ddd", true), UISegmentedItem("eee", true)))
        addSubview(uiSegmentedControl)
        uiSegmentedControl.addBlock(Runnable {
            NSLog.log(uiSegmentedControl.activeIndex)
        }, UIControl.Event.ValueChanged)

//        val view1 = TestXView(context)
//        view1.frame = CGRect(10.0, 200.0, 100.0, 100.0)
//        view1.setBackgroundColor(UIColor.orangeColor);
//        addSubview(view1)
    }

}

internal class TestXView : UIView {

    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = UIColor.yellowColor.toInt()
        paint.setShadowLayer(1f, 50f, 50f, UIColor.greenColor.toInt())
        canvas.drawRoundRect(RectF(30f, 30f, 130f, 130f), 50f, 50f, paint)
    }

    override fun init() {
        super.init()
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

}
