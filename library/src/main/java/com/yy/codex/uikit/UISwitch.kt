package com.yy.codex.uikit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Outline
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider

/**
 * Created by cuiminghui on 2017/1/17.
 */

class UISwitch : UIControl {

    /* UISwitch initialize methods */

    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    private fun defaultValue() {
        mHandleRadius = 30f
        mOnThumbColor = UIColor.whiteColor
        mOnTrackColor = UIColor(0x00 / 255.0, 0xe3 / 255.0, 0x64 / 255.0, 1.0)
        mOnBitmap = null
        mOffThumbColor = UIColor.whiteColor
        mOffTrackColor = UIColor.whiteColor
        mOffBitmap = null
    }

    override fun init() {
        super.init()
        defaultValue()
        mOffBackgroundView = UIView(context)
        mOffBackgroundView!!.frame = CGRect(0.0, 0.0, 51.0, 32.0)
        mOffBackgroundView!!.alpha = 1.0f
        mOffBackgroundView!!.wantsLayer = true
        mOffBackgroundView!!.layer.cornerRadius = 16.0
        mOffBackgroundView!!.layer.borderWidth = 2.0
        mOffBackgroundView!!.layer.borderColor = UIColor(0x00 / 255.0, 0x00 / 255.0, 0x00 / 255.0, 0.1)
        mOffBackgroundView!!.layer.backgroundColor = mOffTrackColor

        mOnBackgroundView = UIView(context)
        mOnBackgroundView!!.frame = CGRect(0.0, 0.0, 51.0, 32.0)
        mOnBackgroundView!!.alpha = 0.0f
        mOnBackgroundView!!.wantsLayer = true
        mOnBackgroundView!!.layer.cornerRadius = 16.0
        mOnBackgroundView!!.layer.backgroundColor = mOnTrackColor

        mHandleView = UIView(context)
        mHandleView!!.frame = CGRect(2.0, 2.0, mHandleRadius.toDouble(), mHandleRadius.toDouble())
        mHandleView!!.alpha = 1.0f
        mHandleView!!.wantsLayer = true
        mHandleView!!.layer.shadowX = 2.0
        mHandleView!!.layer.shadowY = 2.0
        mHandleView!!.layer.shadowRadius = 0.5
        mHandleView!!.layer.shadowColor = UIColor(.3, .3, .3, .2)
        mHandleView!!.layer.cornerRadius = 14.0
        mHandleView!!.layer.borderWidth = 0.5
        mHandleView!!.layer.borderColor = UIColor(0x00 / 255.0, 0x00 / 255.0, 0x00 / 255.0, 0.15)
        mHandleView!!.layer.backgroundColor = mOffThumbColor

        addSubview(mOffBackgroundView!!)
        addSubview(mOnBackgroundView!!)
        addSubview(mHandleView!!)
    }

    /* UISwitch UI Details */

    private var mOffBackgroundView: UIView? = null
    private var mOnBackgroundView: UIView? = null
    private var mHandleView: UIView? = null
    private var mHandleRadius = 30f // realRdius_28 + shadowXY_2
    private var mOn: Boolean = false
    private var mActive: Boolean = false
    private var mCurrentAnimation: UIViewAnimation? = null

    private var mOnThumbColor = UIColor.whiteColor
    private var mOnTrackColor = UIColor(0x00 / 255.0, 0xe3 / 255.0, 0x64 / 255.0, 1.0)
    private var mOnBitmap: Bitmap? = null
    private var mOffThumbColor = UIColor.whiteColor
    private var mOffTrackColor = UIColor.whiteColor
    private var mOffBitmap: Bitmap? = null

    override fun layoutSubviews() {
        super.layoutSubviews()
    }

    override fun onEvent(event: UIControl.Event) {
        super.onEvent(event)
        when (event) {
            UIControl.Event.TouchDown ->
                if (!mActive) {
                    setActiveAnimated(true)
                }

            UIControl.Event.TouchDragExit ->
                setOnAnimated(this.mOn)

            UIControl.Event.TouchUpInside ->
                setOnAnimated(!this.mOn)

            UIControl.Event.TouchUpOutside ->
                setOnAnimated(this.mOn)
        }
    }

    /* UISwitch exports methods */

    fun setOnAnimated(isOn: Boolean) {
        this.mOn = isOn
        cancelAnimation()
        mCurrentAnimation = UIViewAnimator.spring(Runnable {
            if (mOn) {
                mOnBackgroundView!!.alpha = 1.0f
                mOnBackgroundView!!.layer.backgroundColor = mOnTrackColor
                mOffBackgroundView!!.alpha = 0.0f
                mHandleView!!.frame = CGRect(21.0, 2.0, mHandleRadius.toDouble(), mHandleRadius.toDouble())
                mHandleView!!.layer.backgroundColor = mOnThumbColor
            } else {
                mOffBackgroundView!!.alpha = 1.0f
                mOffBackgroundView!!.layer.backgroundColor = mOffTrackColor
                mOnBackgroundView!!.alpha = 0.0f
                mHandleView!!.frame = CGRect(2.0, 2.0, mHandleRadius.toDouble(), mHandleRadius.toDouble())
                mHandleView!!.layer.backgroundColor = mOffThumbColor
            }
        }, Runnable { mActive = false })
    }

    fun setOn(isOn: Boolean) {
        if (this.mOn == isOn) {
            return
        }
        this.mOn = isOn
        if (isOn) {
            mOnBackgroundView!!.alpha = 1.0f
            mOnBackgroundView!!.layer.backgroundColor = mOnTrackColor
            mOffBackgroundView!!.alpha = 0.0f
            mHandleView!!.frame = CGRect(21.0, 2.0, mHandleRadius.toDouble(), mHandleRadius.toDouble())
            mHandleView!!.layer.backgroundColor = mOnThumbColor
        } else {
            mOffBackgroundView!!.alpha = 1.0f
            mOffBackgroundView!!.layer.backgroundColor = mOffTrackColor
            mOnBackgroundView!!.alpha = 0.0f
            mHandleView!!.frame = CGRect(2.0, 2.0, mHandleRadius.toDouble(), mHandleRadius.toDouble())
            mHandleView!!.layer.backgroundColor = mOffThumbColor
        }
    }

    fun setOffTraceColor(mOffTraceColor: UIColor) {
        this.mOffTrackColor = mOffTraceColor
    }

    fun setOffThumbColor(mOffThumbColor: UIColor) {
        this.mOffThumbColor = mOffThumbColor
    }

    fun setOnThumbColor(mOnThumbColor: UIColor) {
        this.mOnThumbColor = mOnThumbColor
    }

    fun setOnTraceColor(mOnTraceColor: UIColor) {
        this.mOnTrackColor = mOnTraceColor
    }

    /* UISwitch supports methods */

    private fun setActiveAnimated(isActive: Boolean) {
        this.mActive = isActive
        cancelAnimation()
        mCurrentAnimation = UIViewAnimator.spring(Runnable {
            val widthExpanded = 8f
            if (isActive) {
                if (mOn) {
                    mHandleView!!.frame = CGRect((21 - widthExpanded).toDouble(), 2.0, (mHandleRadius + widthExpanded).toDouble(), mHandleRadius.toDouble())
                } else {
                    mHandleView!!.frame = CGRect(2.0, 2.0, (mHandleRadius + widthExpanded).toDouble(), mHandleRadius.toDouble())
                    mOffBackgroundView!!.layer.backgroundColor = UIColor(0x00 / 255.0, 0x00 / 255.0, 0x00 / 255.0, 0.1) // darken +30%
                }
            } else {
                if (mOn) {
                    mHandleView!!.frame = CGRect(21.0, 2.0, mHandleRadius.toDouble(), mHandleRadius.toDouble())
                } else {
                    mHandleView!!.frame = CGRect(2.0, 2.0, mHandleRadius.toDouble(), mHandleRadius.toDouble())
                    mOffBackgroundView!!.layer.backgroundColor = mOffTrackColor
                }
            }
        }, Runnable { mActive = false })
    }

    private fun cancelAnimation() {
        mCurrentAnimation?.let {
            it.cancel()
        }
    }
}
