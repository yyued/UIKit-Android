package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/3/8.
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
        _separatorLine.frame = CGRect(it.left, frame.height - 2.0, frame.width - it.left - it.right, 2.0)
    }
}

