package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import java.util.*

/**
 * Created by it on 17/1/23.
 */
open class UITableView : UIScrollView {

    interface UITableViewDataSource {
        fun tableViewNumberOfRowsInSection(tableView: UITableView, section: Int): Int
        fun tableViewCellForRowAtIndexPath(tableView: UITableView, indexPath: NSIndexPath): UITableViewCell
        fun numberOfSectionsInTableView(tableView: UITableView): Int
        fun tableViewTitleForHeaderInSection(tableView: UITableView, section: Int): String?
    }

    interface UITableViewDelegate: UIScrollView.UIScrollViewDelegate {
        fun tableViewHeightForRowAtIndexPath(tableView: UITableView, indexPath: NSIndexPath): Double
        fun tableViewDidSelectRowAtIndexPath(tableView: UITableView, indexPath: NSIndexPath)
        fun tableViewHeightForHeaderInSection(tableView: UITableView, section: Int): Double
    }

    constructor(context: Context, view: View) : super(context, view) {}
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    var rowHeight = 0.0

    var dataSource: UITableViewDataSource? = null

    fun delegate(): UITableViewDelegate? {
        return delegate as? UITableViewDelegate
    }

    var headerView: UIView? = null
        set(value) {
            if (field !== value) {
                field?.let(UIView::removeFromSuperview)
            }
            field = value
            _reloadHeaderFooterView()
        }

    var footerView: UIView? = null
        set(value) {
            if (field !== value) {
                field?.let(UIView::removeFromSuperview)
            }
            field = value
            _reloadHeaderFooterView()
        }

    internal var lastVisibleHash: String = ""

    internal var _cellPositions: List<UITableViewCellPosition> = listOf()

    internal var _cellInstances: HashMap<String, MutableList<UITableViewReusableCell>> = hashMapOf()

    fun reloadData() {
        _reloadCellCaches()
        _reloadContentSize()
        _updateCells()
        _updateHeaderFooterViewFrame()
    }

    override fun didMoveToSuperview() {
        super.didMoveToSuperview()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        _updateCellsFrame()
        _updateCells()
        _updateHeaderFooterViewFrame()
    }

    override fun setContentOffset(contentOffset: CGPoint, animated: Boolean) {
        super.setContentOffset(contentOffset, animated)
        _updateCells()
    }

    fun dequeueReusableCellWithIdentifier(reuseIdentifier: String): UITableViewCell? {
        val cell = _dequeueCell(reuseIdentifier)
        return cell
    }

}
