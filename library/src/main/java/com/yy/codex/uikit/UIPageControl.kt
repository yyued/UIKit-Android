package com.yy.codex.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.yy.codex.coreanimation.CALayer

/**
 * Created by adi on 17/2/6.
 */
class UIPageControl : UIControl {
    constructor(context: Context, view: View) : super(context, view)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun init() {
        super.init()

        currentPage = 0
        numberOfPages = 1
        hidesForSinglePage = true
        pageIndicatorColor = UIColor(0.5, 0.3, 1.0, 0.5)
        currentPageIndicatorColor = UIColor.whiteColor
//        defersCurrentPageDispaly = false

        dotView = UIView(context)
        dotView?.let{
            it.wantsLayer = true
            addSubview(it)
        }

    }

    private var dotView: UIView? = null

    var currentPage: Int = 0

    var numberOfPages: Int = 1

    var hidesForSinglePage: Boolean = true

    var pageIndicatorColor: UIColor = UIColor(1.0, 1.0, 1.0, 0.5)

    var currentPageIndicatorColor: UIColor = UIColor.whiteColor

    var defersCurrentPageDispaly: Boolean = false

    private val dotSpacing: Double = 10.0

    private val dotRadius: Double = 5.0

    private var forceUpdate: Boolean = true

    fun updateCurrentPageDisplay() {
        if (hidesForSinglePage && numberOfPages == 1){
            dotView?.let{
                it.hidden = true
            }
            return
        }
        dotView?.let {
            val contentWidth = calcWidthWithPageCount(numberOfPages)
            it.frame = CGRect((frame.width - contentWidth) / 2.0, 0.0, contentWidth, dotRadius * 2)
            it.layer.removeSubLayers()
            it.hidden = false
            for (i in 0..numberOfPages - 1) {
                val x = dotSpacing / 2.0 + i * (dotSpacing + dotRadius * 2)
                val layer = CALayer(CGRect(x, 0.0, dotRadius * 2, dotRadius * 2))
                layer.cornerRadius = dotRadius
                layer.backgroundColor = pageIndicatorColor
                it.layer.addSubLayer(layer)
            }

            if (currentPage < it.layer.sublayers.size){
                it.layer.sublayers[currentPage].backgroundColor = currentPageIndicatorColor
            }
        }
    }

    fun sizeForNumberOfPages(): CGSize {
        return CGSize( numberOfPages * (dotSpacing+dotRadius*2), dotRadius * 2 )
    }

    private fun calcWidthWithPageCount(pageCount: Int): Double {
        return pageCount * ( dotSpacing + dotRadius*2 )
    }

}