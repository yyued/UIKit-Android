package com.yy.codex.uikit

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

import com.yy.codex.foundation.NSInvocation

import java.util.HashMap

/**
 * Created by cuiminghui on 2017/1/18.
 */

class UINavigationBar : UIView {

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
        constraint.top = "0"
        constraint.width = "100%"
        constraint.height = "44"
        this.constraint = constraint
    }

    override fun willMoveToSuperview(newSuperview: UIView?) {
        super.willMoveToSuperview(newSuperview)
        setBackgroundColor(barTintColor)
    }

    override fun didMoveToSuperview() {
        super.didMoveToSuperview()
        if (context != null && context is Activity) {
            val invocation = NSInvocation(context, "getSupportActionBar")
            val actionBar = invocation.invoke()
            if (actionBar != null) {
                val actionBarInvocation = NSInvocation(actionBar, "hide")
                actionBarInvocation.invoke()
            } else {
                val _invocation = NSInvocation(context, "getActionBar")
                val _actionBar = _invocation.invoke()
                if (_actionBar != null) {
                    val _actionBarInvocation = NSInvocation(_actionBar, "hide")
                    _actionBarInvocation.invoke()
                }
            }
        }
    }

    /* Layout Length */

    fun length(): Double {
        if (isMaterialDesign) {
            return 48.0
        }
        return 44.0
    }

    /* Material Design */

    private var mMaterialDesignInitialized = false

    override fun materialDesignDidChanged() {
        super.materialDesignDidChanged()
        if (!mMaterialDesignInitialized && isMaterialDesign) {
            mMaterialDesignInitialized = true
            barTintColor = UIColor(0x3f / 255.0, 0x51 / 255.0, 0xb5 / 255.0, 1.0)
            tintColor = UIColor.whiteColor
            titleTextAttributes = object : HashMap<String, Any>() {
                init {
                    put(NSAttributedString.NSForegroundColorAttributeName, UIColor.whiteColor)
                    put(NSAttributedString.NSFontAttributeName, UIFont.systemBold(17f))
                }
            }
            if (constraint != null) {
                constraint!!.height = "48"
            }
            layoutSubviews()
            resetItemsView()
            invalidate()
        }
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
            resetItemsView()
        }

    /* Line Color */

    var bottomLineColor: UIColor = UIColor(0xb2 / 255.0, 0xb2 / 255.0, 0xb2 / 255.0, 0.5)
        set(bottomLineColor) {
            field = bottomLineColor
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isMaterialDesign) {
            val paint = Paint()
            paint.color = bottomLineColor.toInt()
            canvas.drawLine(0f, (canvas.height - 1).toFloat(), canvas.width.toFloat(), (canvas.height - 1).toFloat(), paint)
        }
    }

    /* Items */

    private var items: List<UINavigationItem> = listOf()
    private var itemsView: List<UINavigationItemView> = listOf()

    fun setItems(items: List<UINavigationItem>, animated: Boolean) {
        this.items = items
        resetItemsProps()
        resetBackItems()
        resetItemsView()
    }

    fun pushNavigationItem(item: UINavigationItem, animated: Boolean) {
        val mutableItems = items.toMutableList()
        mutableItems.add(item)
        this.items = mutableItems.toList()
        resetItemsProps()
        resetBackItems()
        resetItemsView()
        if (animated) {
            doPushAnimation()
        }
    }

    fun doPushAnimation() {
        if (itemsView.size >= 2) {
            val topItemView = itemsView[itemsView.size - 1]
            val backItemView = itemsView[itemsView.size - 2]
            topItemView.alpha = 1f
            topItemView.animateToFront(false)
            backItemView.alpha = 1f
            backItemView.animateFromFrontToBack(false)
            UIViewAnimator.linear(0.75, Runnable {
                topItemView.animateToFront(true)
                backItemView.animateFromFrontToBack(true)
            }, Runnable { backItemView.alpha = 0f })
        }
    }

    fun popNavigationItem(animated: Boolean) {
        if (animated) {
            doPopAnimation(Runnable { popNavigationItem(false) })
        } else {
            val mutableItems = items.toMutableList()
            mutableItems.removeAt(mutableItems.count() - 1)
            this.items = mutableItems.toList()
            resetItemsView()
        }
    }

    fun doPopAnimation(completion: Runnable) {
        if (itemsView.size >= 2) {
            val topItemView = itemsView[itemsView.size - 1]
            val backItemView = itemsView[itemsView.size - 2]
            topItemView.alpha = 1f
            backItemView.alpha = 1f
            topItemView.animateToGone(false)
            backItemView.animateFromBackToFront(false)
            UIViewAnimator.linear(0.75, Runnable {
                topItemView.animateToGone(true)
                backItemView.animateFromBackToFront(true)
            }, Runnable {
                topItemView.alpha = 0f
                completion.run()
            })
        }
    }

    fun resetItemsProps() {
        for (item in items) {
            item.navigationBar = this
        }
    }

    fun resetBackItems() {
        for ((index, item) in items.withIndex()) {
            if (index > 0) {
                if (item.leftBarButtonItems.count() <= 0 || item.leftBarButtonItems.first().isSystemBackItem) {
                    item.setLeftBarButtonItem(items[index - 1].backBarButtonItem)
                }
            }
            else {
                if (item.leftBarButtonItems.count() == 1 && item.leftBarButtonItems.first().isSystemBackItem) {
                    item.setLeftBarButtonItem(null)
                }
            }
        }
    }

    fun resetItemsView() {
        for (itemView in itemsView) {
            itemView.removeFromSuperview()
        }
        val theItemsView: MutableList<UINavigationItemView> = mutableListOf()
        for ((index, item) in items.withIndex()) {
            val frontView = if (isMaterialDesign) UINavigationItemView_MaterialDesign(context) else UINavigationItemView(context)
            item.frontView = frontView
            item.setNeedsUpdate()
            theItemsView.add(frontView)
            frontView.constraint = UIConstraint.full()
            addSubview(frontView)
            if (index < items.count() - 1) {
                frontView.alpha = 0f
            }
            else {
                frontView.alpha = 1f
            }
        }
        this.itemsView = theItemsView
        layoutSubviews()
    }

}
