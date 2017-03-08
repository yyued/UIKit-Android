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
        fun numberOfRowsInSection(tableView: UITableView, section: Int): Int
        fun cellForRowAtIndexPath(tableView: UITableView, indexPath: NSIndexPath): UITableViewCell
        fun numberOfSections(tableView: UITableView): Int
        fun titleForHeaderInSection(tableView: UITableView, section: Int): String?
    }

    interface UITableViewDelegate: UIScrollView.UIScrollViewDelegate {
        fun heightForRowAtIndexPath(tableView: UITableView, indexPath: NSIndexPath): Double
        fun didSelectRowAtIndexPath(tableView: UITableView, indexPath: NSIndexPath)
        fun didDeselectRowAtIndexPath(tableView: UITableView, indexPath: NSIndexPath)
        fun heightForHeaderInSection(tableView: UITableView, section: Int): Double
        fun viewForHeaderInSection(tableView: UITableView, section: Int): UIView?
    }

    constructor(context: Context, view: View) : super(context, view) {}
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    /**
     * Public
     */

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

    var allowsSelection = true

    var allowsMultipleSelection = false

    var indexPathsForSelectedRow: NSIndexPath? = null
        get() {
            if (indexPathsForSelectedRows.count() > 0) {
                return indexPathsForSelectedRows.first()
            }
            else {
                return null
            }
        }
        private set

    var indexPathsForSelectedRows: List<NSIndexPath> = listOf()
        internal set

    /**
     * Private
     */

    internal var lastVisibleHash: String = ""

    internal var _cellPositions: List<UITableViewCellPosition> = listOf()

    internal var _cellInstances: HashMap<String, MutableList<UITableViewReusableCell>> = hashMapOf()

    internal var _sectionsHeaderView: List<UITableViewSectionHeaderViewWrapper?> = listOf()

    fun reloadData() {
        _reloadSectionHeaderFooterView()
        _reloadCellCaches()
        _reloadContentSize()
        _updateCells()
        _updateHeaderFooterViewFrame()
        _updateSectionHeaderFooterFrame()
    }

    override fun didMoveToSuperview() {
        super.didMoveToSuperview()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        _updateCellsFrame()
        _updateCells()
        _updateHeaderFooterViewFrame()
        _updateSectionHeaderFooterFrame()
    }

    override fun setContentOffset(contentOffset: CGPoint, animated: Boolean) {
        super.setContentOffset(contentOffset, animated)
        _updateCells()
        _updateSectionHeaderFooterFrame()
    }

    fun dequeueReusableCellWithIdentifier(reuseIdentifier: String): UITableViewCell? {
        val cell = _dequeueCell(reuseIdentifier)
        return cell
    }

    fun selectRow(indexPath: NSIndexPath, animated: Boolean) {
        if (!allowsMultipleSelection) {
            indexPathsForSelectedRows?.forEach {
                val indexPath = it
                deselectRow(indexPath, false)
                this.delegate()?.let {
                    it.didDeselectRowAtIndexPath(this, indexPath)
                }
            }
            indexPathsForSelectedRows = listOf()
        }
        _requestCell(indexPath)?.let {
            it.setSelected(true, animated)
        }
        indexPathsForSelectedRows = indexPathsForSelectedRows.plus(indexPath)
    }

    fun deselectRow(indexPath: NSIndexPath, animated: Boolean) {
        _requestCell(indexPath)?.let {
            it.setSelected(false, animated)
        }
        indexPathsForSelectedRows = indexPathsForSelectedRows.filter { it !== indexPath }
    }

}
