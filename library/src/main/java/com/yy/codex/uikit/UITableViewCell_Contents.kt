package com.yy.codex.uikit

import android.content.Context
import android.view.MotionEvent
import com.yy.codex.foundation.lets

/**
 * Created by cuiminghui on 2017/3/9.
 */

internal fun UITableViewCell._initControls() {
    _initBackgroundView()
    addSubview(backgroundView)
    _initSelectedBackgroundView()
    addSubview(selectedBackgroundView)
    _initAccessoryView()
    addSubview(_accessoryView)
    _initContentView()
    addSubview(contentView)
    _initActionsView()
    addSubview(_actionsView)
    _initSeparatorLine()
    addSubview(_separatorLine)
}

internal fun UITableViewCell._updateAppearance() {
    _updateSeparatorLineStyle()
    _updateSeparatorLineFrame()
    _updateSeparatorLineHiddenState()
    _updateAccessoryView()
    _enableActionsView()
}

internal fun UITableViewCell._updateFrames() {
    _updateContentViewFrame()
    _updateBackgroundViewFrame()
    _updateSelectedBackgroundViewFrame()
    _updateSeparatorLineFrame()
    _updateAccessoryViewFrame()
    _updateActionsViewFrame()
}

internal fun UITableViewCell._editingMovement(): Double {
    if (editingPanGesture?.state == UIGestureRecognizerState.Began || editingPanGesture?.state == UIGestureRecognizerState.Changed) {
        return Math.ceil(editingPanGesture?.translation()?.x ?: 0.0)
    }
    else if (editing) {
        return -_actionsView.contentWidth
    }
    return 0.0
}

/**
 * ContentView
 */

internal fun UITableViewCell._initContentView() {
    contentView = UIView(context)
}

internal fun UITableViewCell._updateContentViewFrame() {
    contentView.frame = CGRect(_editingMovement(), 0.0, frame.width, frame.height)
}

/**
 * BackgroundView
 */

internal fun UITableViewCell._initBackgroundView() {
    backgroundView = UIView(context)
    backgroundView.constraint = UIConstraint.full()
    backgroundView.setBackgroundColor(UIColor.whiteColor)
}

internal fun UITableViewCell._updateBackgroundViewFrame() {
    backgroundView.frame = CGRect(_editingMovement(), 0.0, frame.width, frame.height)
}

/**
 * SelectedBackgroundView
 */

internal fun UITableViewCell._initSelectedBackgroundView() {
    selectedBackgroundView = UIView(context)
    selectedBackgroundView.constraint = UIConstraint.full()
    selectedBackgroundView.alpha = 0.0f
    selectedBackgroundView.setBackgroundColor(UIColor(0xd9, 0xd9, 0xd9))
}

internal fun UITableViewCell._updateSelectedBackgroundViewFrame() {
    selectedBackgroundView.frame = CGRect(_editingMovement(), 0.0, frame.width, frame.height)
}

internal fun UITableViewCell._resetHighlightedView() {
    selectedBackgroundView.alpha = if (cellSelected || cellHighlighted) 1.0f else 0.0f
}

/**
 * AccessoryView
 */

internal fun UITableViewCell._initAccessoryView() {
    _accessoryView = UIView(context)
}

internal fun UITableViewCell._updateAccessoryView() {
    lets(_tableView, _indexPath) { _tableView, _indexPath ->
        _tableView.delegate()?.accessoryTypeForRow(_tableView, _indexPath)?.let {
            accessoryType = it
        }
    }
}

internal fun UITableViewCell._resetAccessoryView(accessoryType: UITableViewCell.AccessoryType) {
    when (accessoryType) {
        UITableViewCell.AccessoryType.None -> {
            accessoryView = null
        }
        UITableViewCell.AccessoryType.DisclosureIndicator -> {
            val imageView = UIImageView(context)
            imageView.image = UIImage("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaBAMAAADKhlwxAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAwUExURUdwTMzMzNra2sjIzMjIzMfHzMnJzMfHzMjIzsjIzf///8fHzOLi4sfHzMjIzMfHzAy8PhMAAAAPdFJOUwAKB7+1/V/7VF0Eqwmyp9PtpQYAAABuSURBVFjD7datDYBAAIPRKgIIwgi3Ah7FIijmQDIBEoFGMgBDgS83wtXwk/TTzdMFnHPuGy1BGBd9J6xnXkGgyXQ8i2sBH2jc+A/xKa7b1HE+kmet0I1p02/QlUKjPAQa2AQ64rtAA6svg3PugW6LboLTwj02WgAAAABJRU5ErkJggg==", 3.0)
            imageView.frame = CGRect(0, 0, 30, 30)
            accessoryView = imageView
        }
        UITableViewCell.AccessoryType.Checkmark -> {
            val checkmarkButton = UIButton(context)
            checkmarkButton.setImage(UIImage("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaAgMAAABFxqmRAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAMUExURUdwTBV/+xd/+xV++0vg/PkAAAADdFJOUwCAQLcpHQUAAAB3SURBVEjH7ZOxDcAgDATtNB7DGYURskGWSM+KDEOLPgscRbog8eUVJ+sEZnt7y++Y8CjMszGvHbFrsH7CU+ypaqxX+bn+nOgf1vtgfaigPtVQn+p4fWjg9S7dGKdKGCfFcUIcx/WtvSW3t+D25n3ylK/9y/eW3wvxMknyMSaeYAAAAABJRU5ErkJggg==", 3.0), UIControl.State.Normal)
            checkmarkButton.frame = CGRect(0, 0, 30, 30)
            accessoryView = checkmarkButton
        }
    }
}

internal fun UITableViewCell._updateAccessoryViewFrame() {
    _accessoryView.frame = CGRect(frame.width - _accessoryView.frame.width + _editingMovement(), 0.0, _accessoryView.frame.width, frame.height)
    _accessoryView.subviews.firstOrNull()?.let {
        it.frame = it.frame.setY((frame.height - it.frame.height) / 2.0)
    }
}

/**
 * ActionsView
 */

internal fun UITableViewCell._initActionsView() {
    _actionsView = UITableViewCellActionView(context)
}

internal fun UITableViewCell._enableActionsView() {
    editingPanGesture?.enabled = false
    _actionsView.clearViews()
    lets(_tableView, _indexPath) { tableView, indexPath ->
        tableView.delegate()?.editActionsForRow(tableView, indexPath)?.let {
            editingPanGesture?.enabled = it.size > 0
        }
    }
}

internal fun UITableViewCell._resetActionsView() {
    _actionsView.clearViews()
    lets(_tableView, _indexPath) { tableView, indexPath ->
        tableView.delegate()?.editActionsForRow(tableView, indexPath)?.let {
            _actionsView.resetViews(it.map {
                return@map it.requestActionView(context, indexPath)
            })
        }
    }
}

internal fun UITableViewCell._onEditingPanned(sender: UIPanGestureRecognizer) {
    when (sender.state) {
        UIGestureRecognizerState.Began -> {
            _resetActionsView()
        }
        UIGestureRecognizerState.Changed -> {
            layoutSubviews()
        }
        UIGestureRecognizerState.Ended, UIGestureRecognizerState.Cancelled, UIGestureRecognizerState.Failed -> {
            editing = editingPanGesture?.velocity()?.x ?: 0.0 < -200.0 || -Math.ceil(editingPanGesture?.translation()?.x ?: 0.0) > _actionsView.contentWidth * 2 / 3
            UIViewAnimator.springWithBounciness(1.0, 20.0, Runnable { _updateFrames() }, null)
            if (editing) {
                val maskView = UITableViewCellActionMaskView(context)
                maskView.addGestureRecognizer(UITapGestureRecognizer(this, "endEditing"))
                maskView.touchesView = _actionsView
                maskView.frame = _tableView?.frame ?: CGRect(0, 0, 0, 0)
                _tableView?.superview?.addSubview(maskView)
                this._actionsViewMaskView = maskView
            }
        }
    }
}

internal fun UITableViewCell._updateActionsViewFrame() {
    _actionsView.frame = CGRect(frame.width + _editingMovement(), 0.0 , Math.max(0.0, -_editingMovement() + 1.0), frame.height)
}

internal class UITableViewCellActionView(context: Context) : UIView(context) {

    var contentWidth = 0.0

    fun clearViews() {
        subviews.forEach(UIView::removeFromSuperview)
    }

    fun resetViews(views: List<UITableViewRowActionView>) {
        views.forEach {
            addSubview(it)
        }
        contentWidth = views.sumByDouble { it.contentWidth }
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        if (contentWidth <= 0.0) {
            return
        }
        val percentage = frame.width / contentWidth
        var currentX = 0.0
        subviews.forEachIndexed { idx, subview ->
            val subview = (subview as? UITableViewRowActionView) ?: return@forEachIndexed
            subview.frame = CGRect(currentX, 0.0, subview.contentWidth * percentage + 1.0, frame.height)
            currentX += subview.contentWidth * percentage
        }
    }

}

private class UITableViewCellActionMaskView(context: Context): UIView(context) {

    internal var touchesView: UIView? = null

    override fun hitTest(point: CGPoint, event: MotionEvent): UIView? {
        touchesView?.let {
            if (UIViewHelpers.pointInside(it, this.convertPoint(point, it))) {
                return null
            }
        }
        return super.hitTest(point, event)
    }

}

/**
 * Separator
 */

internal fun UITableViewCell._initSeparatorLine() {
    _separatorLine = UIPixelLine(context)
    _separatorLine.color = UIColor(0xc8, 0xc7, 0xcc)
    _separatorLine.contentInsets = UIEdgeInsets(0.0, 0.0, 1.0, 0.0)
}

internal fun UITableViewCell._updateSeparatorLineHiddenState() {
    if (_tableView?._requestNextPointCell(this) == null) {
        _separatorLine.hidden = true
        return
    }
    _separatorLine.hidden = selectionStyle == UITableViewCell.SelectionStyle.Gray && (cellSelected || cellHighlighted || _nextCellSelected)
}

internal fun UITableViewCell._updateSeparatorLineStyle() {
    (separatorStyle ?: (nextResponder as? UITableView)?.separatorStyle ?: UITableViewCell.SeparatorStyle.SingleLine)?.let {
        when (it) {
            UITableViewCell.SeparatorStyle.None -> _separatorLine.alpha = 0.0f
            UITableViewCell.SeparatorStyle.SingleLine -> _separatorLine.alpha = 1.0f
        }
    }
    ((nextResponder as? UITableView)?.separatorColor ?: UIColor(0xc8, 0xc7, 0xcc))?.let {
        _separatorLine.color = it
    }
}

internal fun UITableViewCell._updateSeparatorLineFrame() {
    (separatorInset ?: (nextResponder as? UITableView)?.separatorInset ?: UIEdgeInsets.zero)?.let {
        _separatorLine.frame = CGRect(it.left, frame.height - 2.0, frame.width - it.left - it.right + _editingMovement(), 2.0)
    }
}
