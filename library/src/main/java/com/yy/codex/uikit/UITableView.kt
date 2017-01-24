package com.yy.codex.uikit

import android.content.Context

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

    fun cellForRowAtIndexPath(indexPath: NSIndexPath): UITableViewCell {
        return UITableViewCell(context)
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

            var headerFooterView = delegateWantsHeaderTitleForSection(i)
            headerFooterView?.let {
                contentView.addSubview(it)
                contentSizeHeight += it.frame.size.height
            }

            var numberOfRowsInSection = dataSource?.tableViewNumberOfRowsInSection(this, i) ?: 0
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

        var actionCell: UITableViewCell? = null;

        //top or bottom cell out off table view
        if (Math.abs(contentOffset.y) - contentOffsetY >= topCell.frame.size.height) {
            actionCell = topCell

        }
        else if (Math.abs(contentOffset.y) - contentOffsetY >= bottomCell.frame.size.height) {
            actionCell = bottomCell
        }

        actionCell?.let {
            contentOffsetY = Math.abs(contentOffset.y)
            addReuseableCell(it)

            if (visibleCells.contains(it)) {
                visibleCells.remove(it)
            }

            var indexPatch = NSIndexPath(0, 0)
            var cell = dataSource?.tableViewCellForRowAtIndexPath(this, indexPatch) as UITableViewCell
            if (Math.abs(contentOffset.y) - contentOffsetY >= topCell.frame.size.height) {
                cell.frame = cell.frame.setY(it.frame.origin.y + it.frame.size.height)

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
}
