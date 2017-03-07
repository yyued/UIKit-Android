package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

/**
 * Created by it on 17/1/23.
 */

open class UITableViewCell : UIView {

    constructor(context: Context, view: View) : super(context, view) {}
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    constructor(context: Context, reuseIdentifier: String): super(context) {
        this.reuseIdentifier = reuseIdentifier
    }

    var reuseIdentifier: String? = null
        internal set

    var separatorInset: UIEdgeInsets = UIEdgeInsets.zero
        set(value) {
            field = value
            _updateSeparatorLineFrame()
        }

    lateinit var contentView: UIView
        internal set

    open fun prepareForReuse() {

    }

    /**
     * Private
     */

    internal lateinit var separatorLine: UIPixelLine

    override fun init() {
        super.init()
        _initContentView()
        addSubview(contentView)
        _initSeparatorLine()
        addSubview(separatorLine)
    }

    internal fun _initSeparatorLine() {
        separatorLine = UIPixelLine(context)
        separatorLine.color = UIColor(0xc8, 0xc7, 0xcc)
        separatorLine.contentInsets = UIEdgeInsets(0.0, 0.0, 1.0, 0.0)
    }

    internal fun _updateSeparatorLineFrame() {
        separatorLine.frame = CGRect(separatorInset.left, frame.height - 2.0, frame.width - separatorInset.left - separatorInset.right, 2.0)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        _updateSeparatorLineFrame()
    }

    internal fun _initContentView() {
        contentView = UIView(context)
        contentView.setBackgroundColor(UIColor.whiteColor)
        contentView.constraint = UIConstraint.full()
    }

}