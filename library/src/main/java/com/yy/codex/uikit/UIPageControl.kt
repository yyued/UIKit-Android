package com.yy.codex.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.yy.codex.coreanimation.CALayer

/**
 * Created by adi on 17/2/6.
 */
class UIPageControl : UIView {

    constructor(context: Context, view: View) : super(context, view)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun init() {
        super.init()
        dotView = UIView(context)
        dotView.wantsLayer = true
        addSubview(dotView)
    }

    override fun prepareProps(attrs: AttributeSet) {
        super.prepareProps(attrs)
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.UIPageControl, 0, 0)
        typedArray.getInteger(R.styleable.UIPageControl_pagecontrol_currentPage, 0)?.let {
            initializeAttributes.put("UIPageControl.currentPage", it)
        }
        typedArray.getInteger(R.styleable.UIPageControl_pagecontrol_numberOfPages, 0)?.let {
            initializeAttributes.put("UIPageControl.numberOfPages", it)
        }
        typedArray.getBoolean(R.styleable.UIPageControl_pagecontrol_hidesForSinglePage, false)?.let {
            initializeAttributes.put("UIPageControl.hidesForSinglePage", it)
        }
        typedArray.getColor(R.styleable.UIPageControl_pagecontrol_pageIndicatorColor, -1)?.let {
            if (it != -1) {
                initializeAttributes.put("UIPageControl.pageIndicatorColor", UIColor(it))
            }
        }
        typedArray.getColor(R.styleable.UIPageControl_pagecontrol_currentPageIndicatorColor, -1)?.let {
            if (it != -1) {
                initializeAttributes.put("UIPageControl.currentPageIndicatorColor", UIColor(it))
            }
        }
    }

    override fun resetProps() {
        super.resetProps()
        initializeAttributes?.let {
            (it["UIPageControl.currentPage"] as? Int)?.let {
                currentPage = it
            }
            (it["UIPageControl.numberOfPages"] as? Int)?.let {
                numberOfPages = it
            }
            (it["UIPageControl.hidesForSinglePage"] as? Boolean)?.let {
                hidesForSinglePage = it
            }
            (it["UIPageControl.pageIndicatorColor"] as? UIColor)?.let {
                pageIndicatorColor = it
            }
            (it["UIPageControl.currentPageIndicatorColor"] as? UIColor)?.let {
                currentPageIndicatorColor = it
            }
        }
    }

    override fun intrinsicContentSize(): CGSize {
        return CGSize(0.0, 16.0)
    }

    private val dotSpacing: Double = 10.0

    private val dotRadius: Double = 5.0

    private lateinit var dotView: UIView

    var currentPage: Int = 0
        set(value) {
            if (field != value){
                field = value
                updatePagesColor()
            }
        }

    var numberOfPages: Int = 3
        set(value) {
            if (field != value){
                field = value
                updatePagesLayout()
                updatePagesColor()
            }
        }

    var hidesForSinglePage: Boolean = false
        set(value) {
            if (field != value){
                field = value
                updatePagesLayout()
            }
        }

    var pageIndicatorColor: UIColor = (tintColor ?: UIColor(0x12 / 255.0, 0x6a / 255.0, 1.0, 1.0)).colorWithAlpha(0.50)
        set(value) {
            if (field != value){
                field = value
                updatePagesColor()
            }
        }

    var currentPageIndicatorColor: UIColor = tintColor ?: UIColor(0x12 / 255.0, 0x6a / 255.0, 1.0, 1.0)
        set(value) {
            if (field != value){
                field = value
                updatePagesColor()
            }
        }

    override fun layoutSubviews() {
        super.layoutSubviews()
        updatePagesLayout()
        updatePagesColor()
    }

    private fun updatePagesLayout(){
        if (hidesForSinglePage && numberOfPages == 1){
            dotView.hidden = true
            return
        }
        val contentWidth = numberOfPages * ( dotSpacing + dotRadius*2 )
        dotView.frame = CGRect((frame.width - contentWidth) / 2.0, 0.0, contentWidth, dotRadius * 2)
        dotView.layer.removeSubLayers()
        dotView.hidden = false
        for (i in 0..numberOfPages - 1) {
            val x = dotSpacing / 2.0 + i * (dotSpacing + dotRadius * 2)
            val layer = CALayer(CGRect(x, 0.0, dotRadius * 2, dotRadius * 2))
            layer.cornerRadius = dotRadius
            dotView.layer.addSubLayer(layer)
        }
    }

    private fun updatePagesColor(){
        dotView.layer.sublayers.forEachIndexed { idx, dotLayer ->
            dotLayer.backgroundColor = if (currentPage == idx) currentPageIndicatorColor else pageIndicatorColor
        }
    }

    fun sizeForNumberOfPages(): CGSize {
        return CGSize( numberOfPages * (dotSpacing+dotRadius*2), dotRadius * 2 )
    }

}