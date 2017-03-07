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

    var color = UIColor.blackColor
        set(value) {
            field = value
            invalidate()
        }

    var vertical = false
        set(value) {
            field = value
            invalidate()
        }

    var contentInsets: UIEdgeInsets = UIEdgeInsets.zero
        set(value) {
            field = value
            invalidate()
        }

    override fun prepareProps(attrs: AttributeSet) {
        super.prepareProps(attrs)
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.UIPixelLine, 0, 0)
        typedArray.getBoolean(R.styleable.UIPixelLine_pixelline_vertical, false)?.let {
            initializeAttributes.put("UIPixelLine.vertical", it)
        }
        typedArray.getColor(R.styleable.UIPixelLine_pixelline_color, -1)?.let {
            if (it != -1) {
                initializeAttributes.put("UIPixelLine.color", UIColor(it))
            }
        }
    }

    override fun resetProps() {
        super.resetProps()
        initializeAttributes?.let {
            (it["UIPixelLine.vertical"] as? Boolean)?.let {
                vertical = it
            }
            (it["UIPixelLine.color"] as? UIColor)?.let {
                color = it
            }
        }
    }

    private val paint = Paint()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = color.toInt()
        paint.strokeWidth = 1.0f
        if (vertical) {
            if (contentInsets.right > 0) {
                canvas.drawLine((canvas.width - 1.0).toFloat(), 0f, (canvas.width - 1.0).toFloat(), canvas.height.toFloat(), paint)
            }
            else {
                canvas.drawLine(0f, 0f, 0f, canvas.height.toFloat(), paint)
            }
        }
        else {
            if (contentInsets.bottom > 0) {
                canvas.drawLine(0f, (canvas.height - 1.0).toFloat(), canvas.width.toFloat(), (canvas.height - 1.0).toFloat(), paint)
            }
            else {
                canvas.drawLine(0f, 0f, canvas.width.toFloat(), 0f, paint)
            }
        }
    }

}
