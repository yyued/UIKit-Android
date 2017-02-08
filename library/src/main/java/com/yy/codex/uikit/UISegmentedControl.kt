package com.yy.codex.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * Created by adi on 17/2/7.
 */

class UISegmentedItem {
    var title = ""
    var enable = true
    var selected = false

    constructor()

    constructor(title: String){
        this.title = title
    }
}

class UISegmentedControl : UIControl {
    constructor(context: Context, view: View) : super(context, view)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    override fun init() {
        super.init()
        color = UIColor(0x12 / 255.0, 0x6a / 255.0, 1.0, 1.0)
        wantsLayer = true
        layer.borderWidth = 0.5 * UIScreen.mainScreen.scale()
        layer.borderColor = color
        layer.cornerRadius = 1.5 * UIScreen.mainScreen.scale()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        val scale = UIScreen.mainScreen.scale()
        val buttonW = (frame.width - (buttons.size + 1) * scale) / buttons.size
        for (idx in 0..buttons.size-1){
            buttons.get(idx).frame = CGRect((0.5 * scale +  buttonW) * idx , 0.0, buttonW, 15.0 * scale)
        }
        for (idx in 0..divs.size-1){
            divs.get(idx).frame = CGRect(buttonW * (idx + 1), 0.0, 0.5 * scale, 15.0 * scale)
        }
    }

    /* props */

    private var items: List<UISegmentedItem> = listOf()

    private var buttons: List<UIButton> = listOf()

    private var divs: List<UIView> = listOf()

    private var numberOfSegments: Int = 0

    private var activeIndex: Int = 0

    private var color: UIColor = UIColor(0x12 / 255.0, 0x6a / 255.0, 1.0, 1.0)

    /* exports */
    fun setItems(items: List<UISegmentedItem>){
        this.items = items
        numberOfSegments = items.size

        for(button in buttons){
            button.removeFromSuperview()
        }
        for (div in divs){
            div.removeFromSuperview()
        }

        val mutableButtons: MutableList<UIButton> = arrayListOf()
        val mutableDivs: MutableList<UIView> = arrayListOf()
        for ((idx, item) in items.withIndex()){
            val button = UIButton(context)
            button.setTitle(item.title, UIControl.State.Normal)
            button.setTitleColor(UIColor.redColor, UIControl.State.Selected)
            if (idx == 0){
                button.isSelected = true
//                button.state = UIControl.State.Selected
            }
            mutableButtons.add(button)
            addSubview(button)
            if (idx > 0){
                val div = UIView(context)
                div.setBackgroundColor(color)
                mutableDivs.add(div)
                addSubview(div)
            }
        }
        buttons = mutableButtons.toList()
        divs = mutableDivs.toList()

        layoutSubviews()
    }

    fun setActiveIndex(idx: Int){
        activeIndex = idx

    }

    fun insertTitleAtIndex(title: String, atIndex: Int){
        if(atIndex > items.size){
            return
        }
        // updateItems updateUI
    }

    fun setTitleAtIndex(title: String, atIndex: Int){
    }

    fun getTitleAtIndex(atIndex: Int):String{
        return ""
    }
}