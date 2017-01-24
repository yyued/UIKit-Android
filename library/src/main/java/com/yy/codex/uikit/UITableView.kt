package com.yy.codex.uikit

import android.content.Context
import com.yy.codex.foundation.lets

/**
 * Created by it on 17/1/23.
 */
open class UITableView(context: Context) : UIScrollView(context) {

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

    var rowHeight = 0
    var sectionHeaderHeight = 0
    var sectionFooterHeight = 0

    var numberOfSections = 0
        protected set(value) {
            field = value
        }

    var loadingSection = 0
    var willLoadNumberOfRow= 0

    var visibleCells: MutableList<UITableViewCell> = mutableListOf()
    var indexPathsForVisibleRows: MutableList<NSIndexPath> = mutableListOf()

    var reuseableCells: MutableMap<String, MutableList<UITableViewCell>?> = mutableMapOf()

    var dataSource: UITableViewDataSource? = null

    private var contentView = UIView(context)
    private var contentOffsetY = 0.0

    fun delegate(): UITableViewDelegate? {
        return delegate as? UITableViewDelegate
    }


    init {
        addSubview(contentView)
    }

    fun reloadData() {

    }

    fun cellForRowAtIndexPath(indexPath: NSIndexPath): UITableViewCell? {
        return null
    }

    override fun didMoveToSuperview() {
        super.didMoveToSuperview()

        updateRowData()
    }

    private fun updateRowData() {
        updateContentSize()

        var contentSizeHeight: Double = 0.00
        var loadCellComplete = false
        for (i in 0..numberOfSections) {
            loadingSection = 0

            var headerFooterView = delegateWantsHeaderTitleForSection(i)
            headerFooterView?.let {
                contentView.addSubview(it)
                contentSizeHeight += it.frame.size.height
                it.frame = it.frame.setY(contentSizeHeight)
            }

            var numberOfRowsInSection = dataSource?.tableViewNumberOfRowsInSection(this, i) ?: 0
            willLoadNumberOfRow = numberOfRowsInSection
            for (j in 0..numberOfRowsInSection) {

                var indexPath = NSIndexPath(j, i)
                var cell = dataSource?.tableViewCellForRowAtIndexPath(this, indexPath) as UITableViewCell
                visibleCells.add(cell)
                indexPathsForVisibleRows.add(indexPath)

                cell.frame = cell.frame.setY(contentSizeHeight)
                contentView.addSubview(cell)

                contentSizeHeight += cell.frame.size.height
                loadCellComplete = contentSizeHeight >= frame.size.height
                if (loadCellComplete) {
                    break
                }
            }
            if (loadCellComplete) {
                break
            }
        }
    }

    private fun delegateWantsHeaderTitleForSection(section: Int): UITableViewHeaderFooterView? {
        var titleForSection = dataSource?.tableViewTitleForHeaderInSection(this, section)
        titleForSection.let {
            var headerFooterView = UITableViewHeaderFooterView(context)
            var heightForHeaderInSection = delegate()?.tableViewHeightForHeaderInSection(this, section)
            heightForHeaderInSection?.let {
                headerFooterView.frame = headerFooterView.frame.setHeight(it)
            }
            headerFooterView.label.text = titleForSection
            return headerFooterView
        }
        return null
    }

    override fun setContentOffset(contentOffset: CGPoint, animated: Boolean) {
        super.setContentOffset(contentOffset, animated)

        var topCell = visibleCells.first()
        var bottomCell = visibleCells.last()

        var topIndexPath = indexPathsForVisibleRows.first()
        var bottomIndexPath = indexPathsForVisibleRows.last()

        var actionCell: UITableViewCell? = null;
        var actionIndexPath: NSIndexPath? = null;

        //top or bottom cell out off table view
        if (Math.abs(contentOffset.y) - contentOffsetY >= topCell.frame.size.height) {
            actionCell = topCell
            actionIndexPath = NSIndexPath(bottomIndexPath.row + 1, bottomIndexPath.section)
        }
        else if (Math.abs(contentOffset.y) - contentOffsetY >= bottomCell.frame.size.height) {
            actionCell = bottomCell
            actionIndexPath = NSIndexPath(bottomIndexPath.row - 1, bottomIndexPath.section)
        }

        lets(actionCell, actionIndexPath) { actionCell, actionIndexPath ->
            contentOffsetY = Math.abs(contentOffset.y)
            addReuseableCell(actionCell)

            if (visibleCells.contains(actionCell)) {
                visibleCells.remove(actionCell)
            }

            if (actionIndexPath.section < numberOfSections) {
                if (actionIndexPath.row >= willLoadNumberOfRow) {
                    var headerFooterView = delegateWantsHeaderTitleForSection(actionIndexPath.section + 1)
                    headerFooterView?.let {
                        contentView.addSubview(it)
                        it.frame = it.frame.setY(bottomCell.frame.size.height + bottomCell.frame.origin.y)
                    }
                }
            }

            if (actionIndexPath.row <= 0 && actionIndexPath.section > 0) {
                var headerFooterView = delegateWantsHeaderTitleForSection(actionIndexPath.section - 1)
                headerFooterView?.let {
                    contentView.addSubview(it)
                    it.frame = it.frame.setY(topCell.frame.size.height + topCell.frame.origin.y)
                }
            }

            var indexPatch = NSIndexPath(actionIndexPath.row, 0)
            var cell = dataSource?.tableViewCellForRowAtIndexPath(this, indexPatch) as UITableViewCell
            if (Math.abs(contentOffset.y) - contentOffsetY >= topCell.frame.size.height) {
                cell.frame = cell.frame.setY(actionCell.frame.origin.y + actionCell.frame.size.height)

            }
            else if (Math.abs(contentOffset.y) - contentOffsetY >= bottomCell.frame.size.height) {
                cell.frame = cell.frame.setY(cell.frame.origin.y + cell.frame.size.height)
            }

            visibleCells.add(cell)
            contentView.addSubview(cell)
        }
    }

    private fun addReuseableCell(cell: UITableViewCell) {
        if (!reuseableCells.containsKey(cell.reuseIdentifier)) {
            var cells: MutableList<UITableViewCell> = mutableListOf()
            cells.add(cell)
            reuseableCells.put(cell.reuseIdentifier, cells)
        }
        else {
            var cells: MutableList<UITableViewCell> = reuseableCells.get(cell.reuseIdentifier)!!
            if (!cells.contains(cell)) {
                cells.add(cell)
            }
            reuseableCells.put(cell.reuseIdentifier, cells)
        }
    }

    private fun updateContentSize() {
        var contentSizeHeight: Double = 0.00
        for (i in 0..numberOfSections) {
            contentSizeHeight += delegate()?.tableViewHeightForRowAtIndexPath(this, NSIndexPath(0, 0)) ?: 0.00
        }
        this.contentSize = CGSize(0.0, contentSizeHeight)
    }

    public fun dequeueReusableCellWithIdentifier(identifier: String): UITableViewCell? {
        var cells: MutableList<UITableViewCell>? = reuseableCells.get(identifier)
        var cell = cells?.first()
        cell.let {
            cells?.remove(it)
        }
        return cell
    }

    public fun numberOfRowsInSection(section: Int): Int {
        return 0
    }

    public fun dequeueReusableHeaderFooterViewWithIdentifier(identifier: String) {

    }
}
