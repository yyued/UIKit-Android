package com.yy.codex.uikit

import android.content.Context

/**
 * Created by cuiminghui on 2017/2/28.
 */

internal fun UITableView._reloadTableHeaderFooterView() {
    tableHeaderView?.let {
        if (it.superview != this) {
            addSubview(it)
        }
    }
    tableFooterView?.let {
        if (it.superview != this) {
            addSubview(it)
        }
    }
    _reloadCellCaches()
    _reloadContentSize()
    _updateTableHeaderFooterViewFrame()
}

internal fun UITableView._updateTableHeaderFooterViewFrame() {
    tableHeaderView?.let {
        it.frame = CGRect(0.0, 0.0, frame.width, it.frame.height)
    }
    tableFooterView?.let {
        it.frame = CGRect(0.0, contentSize.height - it.frame.height, frame.width, it.frame.height)
    }
}

internal fun UITableView._updateSectionHeaderFooterFrame() {
    _sectionsHeaderView.forEach {
        it?.let {
            if (contentOffset.y >= it.startY && contentOffset.y < it.endY) {
                if (contentOffset.y > it.endY - it.headerHeight) {
                    it.view.frame = CGRect(0.0, scrollY / UIScreen.mainScreen.scale() - (contentOffset.y - (it.endY - it.headerHeight)), frame.width, it.headerHeight)
                }
                else {
                    it.view.frame = CGRect(0.0, scrollY / UIScreen.mainScreen.scale(), frame.width, it.headerHeight)
                }
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
        _sectionsHeaderView = (0 until it.numberOfSections(this)).map {
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
        it.titleForHeaderInSection(this, section)?.let {
            val view = UITableViewSectionHeaderView(context)
            view.textLabel.text = it
            return view
        }
    }
    return null
}

internal fun UITableView._requestSectionHeaderHeight(section: Int): Double {
    delegate()?.let {
        return it.heightForHeaderInSection(this, section)
    }
    return 28.0
}

internal class UITableViewSectionHeaderView(context: Context) : UIView(context) {

    lateinit internal var textLabel: UILabel

    override fun init() {
        super.init()
        textLabel = UILabel(context)
        textLabel.constraint = UIConstraint()
        textLabel.constraint?.left = "15"
        textLabel.constraint?.centerVertically = true
        addSubview(textLabel)
        setBackgroundColor(UIColor(0xf2 / 255.0, 0xf2 / 255.0, 0xf2 / 255.0, 1.0))
    }

}

internal class UITableViewSectionHeaderViewWrapper(val view: UIView, var startY: Double, var endY: Double, var headerHeight: Double)