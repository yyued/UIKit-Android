package com.yy.codex.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.View
import java.util.*

/**
 * Created by adi on 17/2/8.
 */
class UISegmentedButton : UIButton {
    constructor(context: Context, view: View) : super(context, view)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var bgColors = HashMap<EnumSet<UIControl.State>, UIColor>()

    override fun onEvent(event: UIControl.Event) {
        super.onEvent(event)
        when (event) {
            UIControl.Event.TouchUpInside -> {
                searchSegmentedControl()?.let {
                    it.onSelectWithButton(this)
                }
            }
        }
    }

    override fun tintColorDidChanged() {
        super.tintColorDidChanged()
        resetBackgroundColor()
    }

    override fun resetState() {
        super.resetState()
        resetBackgroundColor()
    }

    // supp

    private fun resetBackgroundColor() {
        setBackgroundColor(currentBackgroundColor())
    }

    private fun currentBackgroundColor(): UIColor? {
        val state = state
        if (bgColors[state] != null) {
            return bgColors[state]
        } else if (bgColors[EnumSet.of(UIControl.State.Normal)] != null) {
            return bgColors[EnumSet.of(UIControl.State.Normal)]
        } else {
            if (state.contains(UIControl.State.Highlighted) && state.contains(UIControl.State.Normal)) {
                searchSegmentedControl()?.let {
                    return it.bgColor.colorWithDarken(0.3)
                }
            }
            if (state.contains(UIControl.State.Selected)){
                searchSegmentedControl()?.let {
                    return it.tintColor ?: it.defaultTinit
                }
            }
            return searchSegmentedControl()?.let {
                return it.bgColor
            }
        }
    }

    override fun currentTitleColor(): UIColor? {
        val state = state
        if (titleColors[state] != null && titleColors[state] != UIColor.clearColor) {
            return titleColors[state]
        } else if (titleColors[EnumSet.of(UIControl.State.Normal)] != null) {
            return titleColors[EnumSet.of(UIControl.State.Normal)]
        } else {
            if (state.contains(UIControl.State.Disabled)) {
                return UIColor(0xdd/255.0, 0xdd/255.0, 0xdd/255.0, 1.0)
            }
            if (state.contains(UIControl.State.Highlighted) && state.contains(UIControl.State.Normal)) {
                return tintColor?.colorWithAlpha(0.3) ?: tintColor
            }
            if (state.contains(UIControl.State.Selected)){
                searchSegmentedControl()?.let {
                    return it.bgColor
                }
            }
            return tintColor
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