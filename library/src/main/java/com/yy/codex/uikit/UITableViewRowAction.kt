package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

/**
 * Created by cuiminghui on 2017/3/9.
 */
open class UITableViewRowAction {

    interface Handler {
        fun trigger(action: UITableViewRowAction, indexPath: NSIndexPath)
    }

    enum class Style {
        Destructive,
        Normal,
    }

    val style: UITableViewRowAction.Style

    val title: String

    val handler: Handler

    constructor(style: Style, title: String, handler: Handler) {
        this.style = style
        this.title = title
        this.handler = handler
    }

    open fun requestActionView(context: Context, indexPath: NSIndexPath): UITableViewRowActionView {
        val actionView = UITableViewRowActionView(context)
        when (style) {
            Style.Destructive -> actionView.setBackgroundColor(UIColor(0xeb, 0x4d, 0x3d))
            Style.Normal -> actionView.setBackgroundColor(UIColor(0xc7, 0xc7, 0xcb))
        }
        val button = UIButton(context)
        button.font = UIFont(17.0f)
        button.contentEdgeInsets = UIEdgeInsets(0.0, 12.0, 0.0, 12.0)
        button.setTitle(title, UIControl.State.Normal)
        button.setTitleColor(UIColor.whiteColor, UIControl.State.Normal)
        button.addBlock(Runnable {
            this.handler.trigger(this, indexPath)
            var nextResponder = actionView.nextResponder
            while (nextResponder != null) {
                if (nextResponder is UITableViewCell) {
                    nextResponder.endEditing()
                    break
                }
                nextResponder = nextResponder.nextResponder
            }
        }, UIControl.Event.TouchUpInside)
        actionView.addSubview(button)
        actionView.contentWidth = button.intrinsicContentSize().width
        button.constraint = UIConstraint()
        button.constraint?.centerVertically = true
        button.constraint?.centerHorizontally = true
        button.constraint?.width = actionView.contentWidth.toString()
        return actionView
    }

}

open class UITableViewRowActionView: UIView {

    constructor(context: Context, view: View) : super(context, view) {}
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    var contentWidth = 0.0

}