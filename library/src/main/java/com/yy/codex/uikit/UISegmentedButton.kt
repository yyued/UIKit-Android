package com.yy.codex.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * Created by adi on 17/2/8.
 */
class UISegmentedButton : UIButton {
    constructor(context: Context, view: View) : super(context, view)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onEvent(event: UIControl.Event) {
        super.onEvent(event)
        when (event) {
            UIControl.Event.TouchUpInside ->
                searchSegmentedControl()?.let {
                    it.onSelectWithButton(this)
                }
        }
    }

    override fun resetState() {
        super.resetState()

        if (select){
            searchSegmentedControl()?.let {
                setTitleColor(it.bgColor, UIControl.State.Selected)
                setBackgroundColor(it.color)
            }
        }
        else if (highlighted){
        }
        else { // normal
            searchSegmentedControl()?.let {
                setTitleColor(it.color, UIControl.State.Normal)
                setBackgroundColor(it.bgColor)
            }
        }
    }

    fun searchSegmentedControl(): UISegmentedControl? {
        var nextResponder = nextResponder
        while (nextResponder != null){
            if (nextResponder is UISegmentedControl){
                return nextResponder
            }
            nextResponder = nextResponder.nextResponder
        }
        return null
    }
}