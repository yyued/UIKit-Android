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

    constructor(title: String, enable: Boolean){
        this.title = title
        this.enable = enable
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

        defaultTinit = UIColor(0x12 / 255.0, 0x6a / 255.0, 1.0, 1.0)

        contentView = UIView(context)
        addSubview(contentView)

        borderView = UIView(context)
        borderView.userInteractionEnabled = false
        borderView.wantsLayer = true
        borderView.layer.borderWidth = 1.0
        borderView.layer.borderColor = tintColor ?: defaultTinit
        borderView.layer.cornerRadius = 3.0
        addSubview(borderView)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        val borderW = borderView.layer.borderWidth
        val buttonW = (frame.width - (buttons.size + 1) * borderW) / buttons.size
        contentView.frame = CGRect(0.0, 0.0, frame.width, frame.height)
        borderView.frame = CGRect(0.0, 0.0, frame.width, frame.height)
        for (idx in 0..buttons.size-1){
            if (idx == 0){
                buttons.get(0).frame = CGRect(1.0, 1.0, buttonW, 28.0)
            }
            else if (idx == buttons.size-1){
                val ret = if (numberOfSegments % 2 == 0) 1.0 else 0.5
                val w = frame.width - (borderW +  buttonW) * idx - ret
                buttons.get(idx).frame = CGRect((borderW +  buttonW) * idx , 1.0, w, 28.0)
            }
            else {
                buttons.get(idx).frame = CGRect((borderW +  buttonW) * idx , 1.0, buttonW, 28.0)
            }
        }
        for (idx in 0..divs.size-1){
            divs.get(idx).frame = CGRect(buttonW * (idx + 1) + borderW * idx, 0.0, borderW, 30.0)
        }
    }

    override fun tintColorDidChanged() {
        super.tintColorDidChanged()
        borderView.layer.borderColor = tintColor ?: defaultTinit
        divs.map {
            it.setBackgroundColor(tintColor ?: defaultTinit)
        }
    }

    /* props */

    lateinit private var contentView: UIView

    lateinit private var borderView: UIView

    private var items: List<UISegmentedItem> = listOf()

    private var buttons: List<UISegmentedButton> = listOf()

    private var divs: List<UIView> = listOf()

    private var numberOfSegments: Int = 0

    var activeIndex: Int = 0
        private set

    var defaultTinit: UIColor = UIColor(0x12 / 255.0, 0x6a / 255.0, 1.0, 1.0)
        private set

    var bgColor: UIColor = UIColor.whiteColor // @Td should be clearColor

    /* for SegmentedButton */

    fun onSelectWithButton(btn: UISegmentedButton){
        val selectIdx = buttons.indexOf(btn)
        buttons.forEachIndexed { idx, button ->
            if (idx == selectIdx){
                button.select = true
                activeIndex = idx
                onEvent(Event.ValueChanged)
            }
            else {
                button.select = false
            }
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
            button.enable = item.enable
            mutableButtons.add(button)
            contentView.addSubview(button)
            button.select = (idx == 0)
            if (idx > 0){
                val div = UIView(context)
                div.setBackgroundColor(tintColor)
                mutableDivs.add(div)
                contentView.addSubview(div)
            }
        }
        buttons = mutableButtons.toList()
        divs = mutableDivs.toList()

        layoutSubviews()
    }

    fun getTitleAtIndex(atIndex: Int):String{
        return items.get(atIndex).title
    }

    fun insertTitleAtIndex(title: String, atIndex: Int){
        // @Td
    }

    fun setTitleAtIndex(title: String, atIndex: Int){
        // @Td
    }

    override fun setBackgroundColor(color: UIColor?) {
        super.setBackgroundColor(color)
        color?.let {
            bgColor = it
        }
    }
}