package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import com.yy.codex.foundation.NSLog

/**
 * Created by it on 17/1/23.
 */

open class UITableViewCell : UIView {

    enum class SelectionStyle {
        None,
        Gray,
    }

    enum class SeparatorStyle {
        None,
        SingleLine,
    }

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

    var separatorInset: UIEdgeInsets? = null
        set(value) {
            field = value
            _updateAppearance()
        }

    lateinit var contentView: UIView
        internal set

    lateinit var backgroundView: UIView
        internal set

    lateinit var selectedBackgroundView: UIView
        internal set

    var selectionStyle: SelectionStyle = SelectionStyle.Gray
        set(value) {
            field = value
            when (value) {
                SelectionStyle.None -> selectedBackgroundView.setBackgroundColor(UIColor.clearColor)
                SelectionStyle.Gray -> selectedBackgroundView.setBackgroundColor(UIColor(0xd9, 0xd9, 0xd9))
            }
        }

    var separatorStyle: SeparatorStyle? = null
        set(value) {
            field = value
            _updateAppearance()
        }

    var cellSelected = false
        internal set(value) {
            field = value
            _updateSeparatorLineHiddenState()
        }

    var cellHighlighted = false
        internal set(value) {
            field = value
            _updateSeparatorLineHiddenState()
        }

    open fun prepareForReuse() {}

    fun setSelected(selected: Boolean, animated: Boolean) {
        cellSelected = selected
        if (animated) {
            UIViewAnimator.linear(Runnable {
                _resetHighlightedView()
            })
        }
        else {
            _resetHighlightedView()
        }
        tableView?._requestPreviousPointCell(this)?.let {
            it.nextCellSelected = cellSelected || cellHighlighted
        }
    }

    fun setHighlighted(highlighted: Boolean, animated: Boolean) {
        cellHighlighted = highlighted
        if (animated) {
            UIViewAnimator.linear(Runnable {
                _resetHighlightedView()
            })
        }
        else {
            _resetHighlightedView()
        }
        tableView?._requestPreviousPointCell(this)?.let {
            it.nextCellSelected = cellSelected || cellHighlighted
        }
    }

    /**
     * Private
     */

    internal lateinit var separatorLine: UIPixelLine

    internal var indexPath: NSIndexPath? = null

    internal var nextCellSelected = false
        set(value) {
            field = value
            _updateSeparatorLineHiddenState()
        }

    internal var tableView: UITableView? = null
        get() {
            return nextResponder as? UITableView
        }

    override fun init() {
        super.init()
        _initBackgroundView()
        addSubview(backgroundView)
        _initSelectedBackgroundView()
        addSubview(selectedBackgroundView)
        _initContentView()
        addSubview(contentView)
        _initSeparatorLine()
        addSubview(separatorLine)
        _initTouches()
    }

    override fun didMoveToSuperview() {
        super.didMoveToSuperview()
        tableView?.let { it._requestPreviousPointCell(this)?.let(UITableViewCell::_updateSeparatorLineHiddenState) }
    }

    internal fun _updateAppearance() {
        _updateSeparatorLineStyle()
        _updateSeparatorLineFrame()
        _updateSeparatorLineHiddenState()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        _updateSeparatorLineFrame()
    }

    internal fun _initContentView() {
        contentView = UIView(context)
        contentView.constraint = UIConstraint.full()
    }

    internal fun _initBackgroundView() {
        backgroundView = UIView(context)
        backgroundView.constraint = UIConstraint.full()
        backgroundView.setBackgroundColor(UIColor.whiteColor)
    }

    internal fun _initSelectedBackgroundView() {
        selectedBackgroundView = UIView(context)
        selectedBackgroundView.constraint = UIConstraint.full()
        selectedBackgroundView.alpha = 0.0f
        selectedBackgroundView.setBackgroundColor(UIColor(0xd9, 0xd9, 0xd9))
    }

    internal fun _initTouches() {
        val longPressGesture = UILongPressGestureRecognizer(this, "onLongPressed:")
        longPressGesture.minimumPressDuration = 0.10
        longPressGesture.stealable = true
        addGestureRecognizer(longPressGesture)
        addGestureRecognizer(UITapGestureRecognizer(this, "onTapped:"))
    }

    internal fun _resetHighlightedView() {
        selectedBackgroundView.alpha = if (cellSelected || cellHighlighted) 1.0f else 0.0f
    }

    private fun onTapped(sender: UITapGestureRecognizer) {
        val indexPath = indexPath ?: return
        val tableView = (nextResponder as? UITableView) ?: return
        if (!tableView.allowsSelection) {
            return
        }
        tableView.selectRow(indexPath, false)
        tableView.delegate()?.let {
            it.didSelectRowAtIndexPath(tableView, indexPath)
        }
    }

    private fun onLongPressed(sender: UILongPressGestureRecognizer) {
        val indexPath = indexPath ?: return
        val tableView = (nextResponder as? UITableView) ?: return
        if (!tableView.allowsSelection) {
            return
        }
        if (sender.state == UIGestureRecognizerState.Began) {
            setHighlighted(true, false)
        }
        else if (sender.state == UIGestureRecognizerState.Ended) {
            setHighlighted(false, false)
            tableView.selectRow(indexPath, false)
            tableView.delegate()?.let {
                it.didSelectRowAtIndexPath(tableView, indexPath)
            }
        }
        else if (sender.state == UIGestureRecognizerState.Cancelled) {
            setHighlighted(false, false)
        }
    }

}