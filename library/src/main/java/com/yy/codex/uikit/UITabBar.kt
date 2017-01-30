package com.yy.codex.uikit

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import com.yy.codex.foundation.NSLog
import java.util.*

/**
 * Created by saiakirahui on 2017/1/28.
 */
class UITabBar : UIView {

    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    override fun init() {
        super.init()
        val constraint = UIConstraint()
        constraint.centerHorizontally = true
        constraint.bottom = "0"
        constraint.width = "100%"
        constraint.height = "50"
        this.constraint = constraint
    }

    override fun willMoveToSuperview(newSuperview: UIView?) {
        super.willMoveToSuperview(newSuperview)
        setBackgroundColor(barTintColor)
    }

    /* Layout Length */

    fun length(): Double {
        return 50.0
    }

    /* BarTintColor */

    var barTintColor: UIColor = UIColor(0xf8 / 255.0, 0xf8 / 255.0, 0xf8 / 255.0, 1.0)
        set(barTintColor) {
            field = barTintColor
            setBackgroundColor(barTintColor)
        }

    /* Title Attributes */

    var titleTextAttributes: HashMap<String, Any>? = null
        set(titleTextAttributes) {
            field = titleTextAttributes
        }

    /* Line Color */

    var topLineColor: UIColor = UIColor(0xb2 / 255.0, 0xb2 / 255.0, 0xb2 / 255.0, 0.5)
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!materialDesign) {
            val paint = Paint()
            paint.color = topLineColor.toInt()
            canvas.drawLine(0f, 0f, canvas.width.toFloat(), 0f, paint)
        }
    }

    /* Items */

    var items: List<UITabBarItem> = listOf()
        set(value) {
            field = value
            selectedItem = value.first()
            resetItemsView()
        }

    var selectedItem: UITabBarItem? = null
        set(value) {
            field = value
            resetSelected()
        }

    private fun resetItemsView() {
        subviews.forEach(UIView::removeFromSuperview)
        for ((idx, item) in items.withIndex()) {
            item.itemIndex = idx
            item.attachTabBar(this)
            item.getContentView(context)?.let {
                addSubview(it)
                it.constraint = UIConstraint.horizonStack(idx, items.size)
            }
        }
    }

    private fun resetSelected() {
        items.indexOf(selectedItem)?.let {
            for ((idx, item) in items.withIndex()) {
                item.setSelected(it == idx)
            }
        }
    }

}