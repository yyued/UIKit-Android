package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import com.yy.codex.foundation.NSLog
import com.yy.codex.foundation.lets

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

    enum class AccessoryType {
        None,
        DisclosureIndicator,
        Checkmark,
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

    var accessoryType = AccessoryType.None
        set(value) {
            field = value
            _resetAccessoryView(value)
        }

    var accessoryView: UIView? = null
        set(value) {
            field = value
            if (value == null) {
                _accessoryView.subviews.forEach(UIView::removeFromSuperview)
                _accessoryView.frame = _accessoryView.frame.setWidth(0.0)
                layoutSubviews()
                return
            }
            value?.let {
                _accessoryView.subviews.forEach(UIView::removeFromSuperview)
                _accessoryView.frame = _accessoryView.frame.setWidth(it.frame.width)
                _accessoryView.addSubview(it)
                layoutSubviews()
            }
        }

    var editing = false
        internal set(value) {
            field = value
            _tableView?.editing = value
            lets(_tableView, _indexPath) { tableView, indexPath ->
                if (value) {
                    tableView?.delegate()?.willBeginEditing(tableView, indexPath)
                }
                else {
                    tableView?.delegate()?.didEndEditing(tableView, indexPath)
                }
            }
        }

    open fun prepareForReuse() {}

    fun setSelected(selected: Boolean, animated: Boolean) {
        cellSelected = selected
        if (animated) {
            UIViewAnimator.linear(Runnable {
                _resetSelectedBackgroundViewState()
            })
        }
        else {
            _resetSelectedBackgroundViewState()
        }
        _tableView?._requestPreviousPointCell(this)?.let {
            it._nextCellSelected = cellSelected || cellHighlighted
        }
    }

    fun setHighlighted(highlighted: Boolean, animated: Boolean) {
        cellHighlighted = highlighted
        if (animated) {
            UIViewAnimator.linear(Runnable {
                _resetSelectedBackgroundViewState()
            })
        }
        else {
            _resetSelectedBackgroundViewState()
        }
        _tableView?._requestPreviousPointCell(this)?.let {
            it._nextCellSelected = cellSelected || cellHighlighted
        }
    }

    /**
     * Private
     */

    internal lateinit var _separatorLine: UIPixelLine

    internal lateinit var _accessoryView: UIView

    internal var _indexPath: NSIndexPath? = null

    internal var _nextCellSelected = false
        set(value) {
            field = value
            _updateSeparatorLineHiddenState()
        }

    internal var _tableView: UITableView? = null
        get() {
            return nextResponder as? UITableView
        }

    override fun init() {
        super.init()
        _initControls()
        _initTouches()
    }

    override fun didMoveToSuperview() {
        super.didMoveToSuperview()
        _tableView?.let { it._requestPreviousPointCell(this)?.let(UITableViewCell::_updateSeparatorLineHiddenState) }
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        _updateFrames()
    }

    internal fun _initTouches() {
        _initEditingTouches()
        val longPressGesture = UILongPressGestureRecognizer(this, "onLongPressed:")
        longPressGesture.minimumPressDuration = 0.10
        longPressGesture.stealable = true
        addGestureRecognizer(longPressGesture)
        addGestureRecognizer(UITapGestureRecognizer(this, "onTapped:"))
    }

    private fun onTapped(sender: UITapGestureRecognizer) {
        _onTapped(sender)
    }

    private fun onLongPressed(sender: UILongPressGestureRecognizer) {
        _onLongPressed(sender)
    }

    /**
     * Private - Editing
     */
    internal var _editingPanGesture: UIPanGestureRecognizer? = null
    internal lateinit var _editingView: UITableViewCellActionView
    internal var _editingMaskView: UIView? = null

    private fun onEditingPanned(sender: UIPanGestureRecognizer) {
        _onEditingPanned(sender)
    }

    private fun _endEditing() {
        editing = false
        _editingMaskView?.removeFromSuperview()
        UIViewAnimator.springWithBounciness(1.0, 20.0, Runnable { _updateFrames() }, null)
    }

    internal fun endEditing() {
        _endEditing()
    }

}
