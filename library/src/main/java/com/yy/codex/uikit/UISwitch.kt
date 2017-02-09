package com.yy.codex.uikit

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

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
        mHandleRadius = 28.0f
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
        mOffBackgroundView?.let {
            it.frame = CGRect(0.0, 0.0, 51.0, 32.0)
            it.alpha = 1.0f
            it.wantsLayer = true
            it.layer.cornerRadius = 16.0
            it.layer.borderWidth = 2.0
            it.layer.borderColor = UIColor(0x00 / 255.0, 0x00 / 255.0, 0x00 / 255.0, 0.1)
            it.layer.backgroundColor = mOffTrackColor
            addSubview(it)
        }


        mOnBackgroundView = UIView(context)
        mOnBackgroundView?.let {
            it.frame = CGRect(0.0, 0.0, 51.0, 32.0)
            it.alpha = 0.0f
            it.wantsLayer = true
            it.layer.cornerRadius = 16.0
            it.layer.backgroundColor = mOnTrackColor
            addSubview(it)
        }


        mHandleView = UIView(context)
        mHandleView?.let {
            it.frame = CGRect(2.0, 2.0, mHandleRadius.toDouble(), mHandleRadius.toDouble())
            it.alpha = 1.0f
            it.wantsLayer = true
            it.layer.shadowX = 2.0
            it.layer.shadowY = 2.0
            it.layer.shadowRadius = 0.5
            it.layer.shadowColor = UIColor(.3, .3, .3, .2)
            it.layer.cornerRadius = 14.0
            it.layer.borderWidth = 0.5
            it.layer.borderColor = UIColor(0x00 / 255.0, 0x00 / 255.0, 0x00 / 255.0, 0.15)
            it.layer.backgroundColor = mOffThumbColor
            it.layer.wantsEnlargerLayer()
            addSubview(it)
        }
    }

    /* UISwitch UI Details */

    private var mOffBackgroundView: UIView? = null
    private var mOnBackgroundView: UIView? = null
    private var mHandleView: UIView? = null
    private var mHandleRadius = 28.0f
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
        cancelAnimation()
        if (this.mOn != isOn){
            this.mOn = isOn
            onEvent(Event.ValueChanged)
        }
        mCurrentAnimation = UIViewAnimator.spring(Runnable {
            if (mOn) {
                mOffBackgroundView?.let {
                    it.alpha = 0.0f
                }
                mOnBackgroundView?.let {
                    it.alpha = 1.0f
                    it.layer.backgroundColor = mOnTrackColor
                }
                mHandleView?.let {
                    it.frame = CGRect(21.0, 2.0, mHandleRadius.toDouble(), mHandleRadius.toDouble())
                    it.layer.backgroundColor = mOnThumbColor
                }
            } else {
                mOffBackgroundView?.let {
                    it.alpha = 1.0f
                    it.layer.backgroundColor = mOffTrackColor
                }
                mOnBackgroundView?.let {
                    it.alpha = 0.0f
                }
                mHandleView?.let {
                    it.frame = CGRect(2.0, 2.0, mHandleRadius.toDouble(), mHandleRadius.toDouble())
                    it.layer.backgroundColor = mOffThumbColor
                }
            }
        }, Runnable { mActive = false })
    }

    fun setOn(isOn: Boolean) {
        if (this.mOn == isOn) {
            return
        }
        this.mOn = isOn
        onEvent(Event.ValueChanged)
        if (isOn) {
            mOffBackgroundView?.let {
                it.alpha = 0.0f
            }
            mOnBackgroundView?.let {
                it.alpha = 1.0f
                it.layer.backgroundColor = mOnTrackColor
            }
            mHandleView?.let {
                it.frame = CGRect(21.0, 2.0, mHandleRadius.toDouble(), mHandleRadius.toDouble())
                it.layer.backgroundColor = mOnThumbColor
            }
        } else {
            mOffBackgroundView?.let {
                it.alpha = 1.0f
                it.layer.backgroundColor = mOffTrackColor
            }
            mOnBackgroundView?.let {
                it.alpha = 0.0f
            }
            mHandleView?.let {
                it.frame = CGRect(2.0, 2.0, mHandleRadius.toDouble(), mHandleRadius.toDouble())
                it.layer.backgroundColor = mOffThumbColor
            }
        }
    }

    fun isOn(): Boolean{
        return this.mOn
    }

    fun setOffTrackColor(mOffTrackColor: UIColor) {
        this.mOffTrackColor = mOffTrackColor
        mOffBackgroundView?.let {
            it.layer.backgroundColor = mOffTrackColor
        }
    }

    fun setOffThumbColor(mOffThumbColor: UIColor) {
        this.mOffThumbColor = mOffThumbColor
        mHandleView?.let {
            if (!mOn){
                it.layer.backgroundColor = mOffThumbColor
            }
        }
    }

    fun setOnThumbColor(mOnThumbColor: UIColor) {
        this.mOnThumbColor = mOnThumbColor
        mHandleView?.let {
            if (mOn){
                it.layer.backgroundColor = mOnThumbColor
            }
        }
    }

    fun setOnTrackColor(mOnTrackColor: UIColor) {
        this.mOnTrackColor = mOnTrackColor
        mOnBackgroundView?.let {
            it.layer.backgroundColor = mOnTrackColor
        }
    }

    /* UISwitch supports methods */

    private fun setActiveAnimated(isActive: Boolean) {
        this.mActive = isActive
        cancelAnimation()
        mCurrentAnimation = UIViewAnimator.spring(Runnable {
            val widthExpanded = 8f
            if (isActive) {
                if (mOn) {
                    mHandleView?.let {
                        it.frame = CGRect((21 - widthExpanded).toDouble(), 2.0, (mHandleRadius + widthExpanded).toDouble(), mHandleRadius.toDouble())
                    }
                } else {
                    mHandleView?.let {
                        it.frame = CGRect(2.0, 2.0, (mHandleRadius + widthExpanded).toDouble(), mHandleRadius.toDouble())
                    }
                    mOffBackgroundView?.let {
                        it.layer.backgroundColor = UIColor(0x00 / 255.0, 0x00 / 255.0, 0x00 / 255.0, 0.1) // darken +30%
                    }
                }
            } else {
                if (mOn) {
                    mHandleView?.let {
                        it.frame = CGRect(21.0, 2.0, mHandleRadius.toDouble(), mHandleRadius.toDouble())
                    }
                } else {
                    mHandleView?.let {
                        it.frame = CGRect(2.0, 2.0, mHandleRadius.toDouble(), mHandleRadius.toDouble())
                    }
                    mOffBackgroundView?.let {
                        it.layer.backgroundColor = mOffTrackColor
                    }
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
