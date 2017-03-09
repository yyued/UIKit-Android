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
                _accessoryView.constraint?.width = "0"
                _accessoryView.constraint?.setNeedsLayout()
                contentView.constraint?.right = "0"
                contentView.constraint?.setNeedsLayout()
                layoutSubviews()
                return
            }
            value?.let {
                _accessoryView.subviews.forEach(UIView::removeFromSuperview)
                _accessoryView.constraint?.width = it.frame.width.toString()
                _accessoryView.constraint?.setNeedsLayout()
                contentView.constraint?.right = it.frame.width.toString()
                contentView.constraint?.setNeedsLayout()
                _accessoryView.addSubview(it)
                layoutSubviews()
            }
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
        _tableView?._requestPreviousPointCell(this)?.let {
            it._nextCellSelected = cellSelected || cellHighlighted
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
        _initBackgroundView()
        addSubview(backgroundView)
        _initSelectedBackgroundView()
        addSubview(selectedBackgroundView)
        _initAccessoryView()
        addSubview(_accessoryView)
        _initContentView()
        addSubview(contentView)
        _initSeparatorLine()
        addSubview(_separatorLine)
        _initTouches()
    }

    override fun didMoveToSuperview() {
        super.didMoveToSuperview()
        _tableView?.let { it._requestPreviousPointCell(this)?.let(UITableViewCell::_updateSeparatorLineHiddenState) }
    }

    internal fun _updateAppearance() {
        _updateSeparatorLineStyle()
        _updateSeparatorLineFrame()
        _updateSeparatorLineHiddenState()
        _updateAccessoryView()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        _updateSeparatorLineFrame()
        _updateAccessoryViewFrame()
    }

    internal fun _initContentView() {
        contentView = UIView(context)
        contentView.constraint = UIConstraint()
        contentView.constraint?.top = "0"
        contentView.constraint?.left = "0"
        contentView.constraint?.bottom = "0"
        contentView.constraint?.right = "0"
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

    internal fun _initAccessoryView() {
        _accessoryView = UIView(context)
        _accessoryView.constraint = UIConstraint()
        _accessoryView.constraint?.height = "100%"
        _accessoryView.constraint?.width = "0"
        _accessoryView.constraint?.right = "0"
        _accessoryView.constraint?.centerVertically = true
    }

    internal fun _updateAccessoryView() {
        lets(_tableView, _indexPath) { _tableView, _indexPath ->
            _tableView.delegate()?.accessoryTypeForRow(_tableView, _indexPath)?.let {
                accessoryType = it
            }
        }
    }

    internal fun _resetAccessoryView(accessoryType: AccessoryType) {
        when (accessoryType) {
            AccessoryType.None -> {
                accessoryView = null
            }
            AccessoryType.DisclosureIndicator -> {
                val imageView = UIImageView(context)
                imageView.image = UIImage("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaBAMAAADKhlwxAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAwUExURUdwTMzMzNra2sjIzMjIzMfHzMnJzMfHzMjIzsjIzf///8fHzOLi4sfHzMjIzMfHzAy8PhMAAAAPdFJOUwAKB7+1/V/7VF0Eqwmyp9PtpQYAAABuSURBVFjD7datDYBAAIPRKgIIwgi3Ah7FIijmQDIBEoFGMgBDgS83wtXwk/TTzdMFnHPuGy1BGBd9J6xnXkGgyXQ8i2sBH2jc+A/xKa7b1HE+kmet0I1p02/QlUKjPAQa2AQ64rtAA6svg3PugW6LboLTwj02WgAAAABJRU5ErkJggg==", 3.0)
                imageView.frame = CGRect(0, 0, 30, 30)
                accessoryView = imageView
            }
            AccessoryType.Checkmark -> {
                val checkmarkButton = UIButton(context)
                checkmarkButton.setImage(UIImage("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaAgMAAABFxqmRAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAMUExURUdwTBV/+xd/+xV++0vg/PkAAAADdFJOUwCAQLcpHQUAAAB3SURBVEjH7ZOxDcAgDATtNB7DGYURskGWSM+KDEOLPgscRbog8eUVJ+sEZnt7y++Y8CjMszGvHbFrsH7CU+ypaqxX+bn+nOgf1vtgfaigPtVQn+p4fWjg9S7dGKdKGCfFcUIcx/WtvSW3t+D25n3ylK/9y/eW3wvxMknyMSaeYAAAAABJRU5ErkJggg==", 3.0), UIControl.State.Normal)
                checkmarkButton.frame = CGRect(0, 0, 30, 30)
                accessoryView = checkmarkButton
            }
        }
    }

    internal fun _updateAccessoryViewFrame() {
        _accessoryView.subviews.firstOrNull()?.let {
            it.frame = it.frame.setY((frame.height - it.frame.height) / 2.0)
        }
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
        val indexPath = _indexPath ?: return
        val tableView = (nextResponder as? UITableView) ?: return
        if (!tableView.allowsSelection) {
            return
        }
        if (!cellSelected || !tableView.allowsMultipleSelection) {
            tableView.selectRow(indexPath, false)
            tableView.delegate()?.let {
                it.didSelectRowAtIndexPath(tableView, indexPath)
            }
        }
        else {
            tableView.deselectRow(indexPath, false)
            tableView.delegate()?.let {
                it.didDeselectRowAtIndexPath(tableView, indexPath)
            }
        }
    }

    private fun onLongPressed(sender: UILongPressGestureRecognizer) {
        val indexPath = _indexPath ?: return
        val tableView = (nextResponder as? UITableView) ?: return
        if (!tableView.allowsSelection) {
            return
        }
        if (sender.state == UIGestureRecognizerState.Began) {
            setHighlighted(tableView.delegate()?.shouldHighlightRow(tableView, indexPath) ?: true, false)
            if (cellHighlighted) {
                tableView.delegate()?.didHighlightRow(tableView, indexPath)
            }
        }
        else if (sender.state == UIGestureRecognizerState.Ended) {
            setHighlighted(false, false)
            if (!cellSelected || !tableView.allowsMultipleSelection) {
                tableView.selectRow(indexPath, false)
                tableView.delegate()?.let {
                    it.didSelectRowAtIndexPath(tableView, indexPath)
                }
            }
            else {
                tableView.deselectRow(indexPath, false)
                tableView.delegate()?.let {
                    it.didDeselectRowAtIndexPath(tableView, indexPath)
                }
            }
        }
        else if (sender.state == UIGestureRecognizerState.Cancelled) {
            if (cellHighlighted) {
                setHighlighted(false, false)
                tableView.delegate()?.didUnhighlightRow(tableView, indexPath)
            }
        }
    }

}