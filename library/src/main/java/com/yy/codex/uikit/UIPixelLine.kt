package com.yy.codex.uikit

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

/**
 * Created by cuiminghui on 2017/1/18.
 */

class UIPixelLine : UIView {

    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    private var mColor = UIColor.blackColor

    fun setColor(color: UIColor) {
        mColor = color
        invalidate()
    }

    private var mVertical = false

    fun setVertical(vertical: Boolean) {
        mVertical = vertical
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint()
        paint.color = mColor.toInt()
        paint.strokeWidth = 1.0f
        if (mVertical) {
            canvas.drawLine(0f, 0f, 0f, canvas.height.toFloat(), paint)
        } else {
            canvas.drawLine(0f, 0f, canvas.width.toFloat(), 0f, paint)
        }
    }

}
