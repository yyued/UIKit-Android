package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/3/9.
 */

internal fun UITableViewCell._onTapped(sender: UITapGestureRecognizer) {
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

internal fun UITableViewCell._onLongPressed(sender: UILongPressGestureRecognizer) {
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