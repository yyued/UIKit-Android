package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/2/28.
 */

internal fun UITableView._reloadHeaderFooterView() {
    headerView?.let {
        if (it.superview != this) {
            addSubview(it)
        }
    }
    footerView?.let {
        if (it.superview != this) {
            addSubview(it)
        }
    }
    _reloadCellCaches()
    _reloadContentSize()
    _updateHeaderFooterViewFrame()
}

internal fun UITableView._updateHeaderFooterViewFrame() {
    headerView?.let {
        it.frame = CGRect(0.0, 0.0, frame.width, it.frame.height)
    }
    footerView?.let {
        it.frame = CGRect(0.0, contentSize.height - it.frame.height, frame.width, it.frame.height)
    }
}
