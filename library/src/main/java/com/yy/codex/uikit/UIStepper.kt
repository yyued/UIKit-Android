package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

/**
 * Created by adi on 17/1/23.
 */
class UIStepper : UIControl {

    /* UIStepper initialize methods */

    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    private fun defaultValues(){
        skinColor = UIColor(0x12 / 255.0, 0x6a / 255.0, 1.0, 1.0)
    }

    override fun init() {
        super.init()
        defaultValues()

        contentView = UIView(context)
        contentView?.let {
            it.frame = CGRect(0.0, 0.0, 95.0, 30.0)
            it.wantsLayer = true
            it.layer.cornerRadius = 3.0
            it.layer.borderWidth = 1.0
            it.layer.borderColor = skinColor
            it.layer.backgroundColor = UIColor.whiteColor
            addSubview(it)
        }

        dividerView = UIView(context)
        dividerView?.let {
            it.frame = CGRect(46.0, 0.0, 1.0, 30.0)
            it.setBackgroundColor(skinColor)
            addSubview(it)
        }

        decreaseView = UIView(context)
        decreaseView?.let {
            it.frame = CGRect(0.0, 0.0, 46.0, 30.0)
            it.wantsLayer = true
            val layer1 = CALayer()
            layer1.frame = CGRect(15.0, 15.0, 16.0, 1.5)
            layer1.backgroundColor = skinColor
            it.layer.addSubLayer(layer1)
            addSubview(it)
        }

        increaseView = UIView(context)
        increaseView?.let {
            it.frame = CGRect(47.0, 0.0, 46.0, 30.0)
            it.wantsLayer = true
            val layer1 = CALayer()
            layer1.frame = CGRect(15.0, 14.0, 16.0, 1.5)
            layer1.backgroundColor = skinColor
            val layer2 = CALayer()
            layer2.frame = CGRect(22.0, 7.5, 1.5, 15.0)
            layer2.backgroundColor = skinColor
            it.layer.addSubLayer(layer1)
            it.layer.addSubLayer(layer2)
            addSubview(it)
        }

    }

    /* UISlider UI Details */

    // subViews

    private var contentView : UIView? = null

    private var dividerView : UIView? = null

    private var decreaseView : UIView? = null

    private var increaseView : UIView? = null

    // data

    var value : Double = 0.0

    var minValue : Double = 0.0

    var maxValue : Double = 1.0

    var stepValue : Double = 0.1

    var wraps : Boolean = false

    var autoRepeat : Boolean = true

    var continuous : Boolean = true

    // appearance

    var skinColor: UIColor = UIColor(0x12 / 255.0, 0x6a / 255.0, 1.0, 1.0)


    /* UIStepper exports methods */


    /* UIStepper supports methods */

    private fun deactiveView() {

    }
}