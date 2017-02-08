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
        layer.borderWidth = 1.0
        layer.borderColor = color
        layer.cornerRadius = 3.0
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        val buttonW = (frame.width - (buttons.size + 1) * layer.borderWidth) / buttons.size
        for (idx in 0..buttons.size-1){
            if (idx == 0){
                buttons.get(0).frame = CGRect(1.0, 1.0, buttonW, 28.0)
            }
            else if (idx == buttons.size-1){
                val ret = if (numberOfSegments % 2 == 0) 1.0 else 0.5
                val w = frame.width - (layer.borderWidth +  buttonW) * idx - ret
                buttons.get(idx).frame = CGRect((layer.borderWidth +  buttonW) * idx , 1.0, w, 28.0)
            }
            else {
                buttons.get(idx).frame = CGRect((layer.borderWidth +  buttonW) * idx , 1.0, buttonW, 28.0)
            }
        }
        for (idx in 0..divs.size-1){
            divs.get(idx).frame = CGRect(buttonW * (idx + 1) + layer.borderWidth * idx, 0.0, layer.borderWidth, 30.0)
        }
    }

    /* props */

    private var items: List<UISegmentedItem> = listOf()

    private var buttons: List<UISegmentedButton> = listOf()

    private var divs: List<UIView> = listOf()

    private var numberOfSegments: Int = 0

    private var activeIndex: Int = 0

    var color: UIColor = UIColor(0x12 / 255.0, 0x6a / 255.0, 1.0, 1.0)

    var bgColor: UIColor = UIColor.whiteColor

    /* for SegmentedButton */

    fun onSelectWithButton(btn: UISegmentedButton){
        val selectIdx = buttons.indexOf(btn)
        buttons.forEachIndexed { idx, button ->
            button.select = idx == selectIdx
        }
    }

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

        val mutableButtons: MutableList<UISegmentedButton> = arrayListOf()
        val mutableDivs: MutableList<UIView> = arrayListOf()
        for ((idx, item) in items.withIndex()){
            val button = UISegmentedButton(context)
            button.setTitle(item.title, UIControl.State.Normal)
            button.setTitleColor(UIColor.clearColor, UIControl.State.Selected)
            mutableButtons.add(button)
            addSubview(button)
            button.select = (idx == 0)
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

    override fun setBackgroundColor(color: UIColor?) {
        super.setBackgroundColor(color)
        color?.let {
            bgColor = it
        }
    }
}