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
    _initEditingView()
    addSubview(_editingView)
    _initSeparatorLine()
    addSubview(_separatorLine)
}

internal fun UITableViewCell._updateAppearance() {
    _updateSeparatorLineStyle()
    _updateSeparatorLineHiddenState()
    _updateAccessoryView()
    _enableEditingView()
    _updateFrames()
}

internal fun UITableViewCell._updateFrames() {
    contentView.frame = CGRect(_editingMovement(), 0.0, frame.width, frame.height)
    backgroundView.frame = CGRect(_editingMovement(), 0.0, frame.width, frame.height)
    selectedBackgroundView.frame = CGRect(_editingMovement(), 0.0, frame.width, frame.height)
    (separatorInset ?: (nextResponder as? UITableView)?.separatorInset ?: UIEdgeInsets.zero)?.let {
        _separatorLine.frame = CGRect(it.left, frame.height - 2.0, frame.width - it.left - it.right + _editingMovement(), 2.0)
    }
    _accessoryView.frame = CGRect(frame.width - _accessoryView.frame.width + _editingMovement(), 0.0, _accessoryView.frame.width, frame.height)
    _accessoryView.subviews.firstOrNull()?.let {
        it.frame = it.frame.setY((frame.height - it.frame.height) / 2.0)
    }
    _editingView.frame = CGRect(frame.width + _editingMovement(), 0.0 , Math.max(0.0, -_editingMovement() + 1.0), frame.height)
}

private fun UITableViewCell._editingMovement(): Double {
    if (_editingPanGesture?.state == UIGestureRecognizerState.Began || _editingPanGesture?.state == UIGestureRecognizerState.Changed) {
        return Math.ceil(_editingPanGesture?.translation()?.x ?: 0.0)
    }
    else if (editing) {
        return -_editingView.contentWidth
    }
    return 0.0
}

/**
 * ContentView
 */

private fun UITableViewCell._initContentView() {
    contentView = UIView(context)
}

/**
 * BackgroundView
 */

private fun UITableViewCell._initBackgroundView() {
    backgroundView = UIView(context)
    backgroundView.constraint = UIConstraint.full()
    backgroundView.setBackgroundColor(UIColor.whiteColor)
}

/**
 * SelectedBackgroundView
 */

private fun UITableViewCell._initSelectedBackgroundView() {
    selectedBackgroundView = UIView(context)
    selectedBackgroundView.constraint = UIConstraint.full()
    selectedBackgroundView.alpha = 0.0f
    selectedBackgroundView.setBackgroundColor(UIColor(0xd9, 0xd9, 0xd9))
}

internal fun UITableViewCell._resetSelectedBackgroundViewState() {
    selectedBackgroundView.alpha = if (cellSelected || cellHighlighted) 1.0f else 0.0f
}

/**
 * AccessoryView
 */

private fun UITableViewCell._initAccessoryView() {
    _accessoryView = UIView(context)
}

private fun UITableViewCell._updateAccessoryView() {
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

/**
 * Separator
 */

private fun UITableViewCell._initSeparatorLine() {
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

private fun UITableViewCell._updateSeparatorLineStyle() {
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
