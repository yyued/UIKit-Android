package com.yy.codex.uikit

import android.content.Context

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

internal fun UITableView._updateSectionHeaderFooterFrame() {
    _sectionsHeaderView.forEach {
        it?.let {
            if (contentOffset.y > it.startY && contentOffset.y < it.endY) {
                it.view.frame = CGRect(0.0, contentOffset.y, frame.width, it.headerHeight)
            }
            else {
                it.view.frame = CGRect(0.0, it.startY, frame.width, it.headerHeight)
            }
        }
    }
}

internal fun UITableView._reloadSectionHeaderFooterView() {
    _sectionsHeaderView.forEach { it?.let { it.view.removeFromSuperview() } }
    dataSource?.let {
        _sectionsHeaderView = (0 until it.numberOfSectionsInTableView(this)).map {
            val idx = it
            _requestSectionHeaderView(it)?.let {
                return@map UITableViewSectionHeaderViewWrapper(it, 0.0, 0.0, _requestSectionHeaderHeight(idx))
            }
            return@map null
        }
        _sectionsHeaderView.forEach { it?.let { addSubview(it.view) } }
    }

}

internal fun UITableView._requestSectionHeaderView(section: Int): UIView? {
    dataSource?.let {
        it.tableViewTitleForHeaderInSection(this, section)?.let {
            val view = UITableViewSectionHeaderView(context)
            return view
        }
    }
    return null
}

internal fun UITableView._requestSectionHeaderHeight(section: Int): Double {
    return 28.0
}

internal class UITableViewSectionHeaderView(context: Context) : UIView(context) {

    lateinit internal var textLabel: UILabel

    override fun init() {
        super.init()
        setBackgroundColor(UIColor.blueColor)
        textLabel = UILabel(context)
        textLabel.constraint = UIConstraint()
        textLabel.constraint?.left = "15"
        textLabel.constraint?.centerVertically = true
        addSubview(textLabel)
    }

}

internal class UITableViewSectionHeaderViewWrapper(val view: UIView, var startY: Double, var endY: Double, var headerHeight: Double)