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
    }

    interface UITableViewDelegate: UIScrollView.UIScrollViewDelegate {

        fun tableViewHeightForRowAtIndexPath(tableView: UITableView, indexPath: NSIndexPath): Double
    }

    var rowHeight = 0
    var sectionHeaderHeight = 0
    var sectionFooterHeight = 0

    var numberOfSections = 0
        protected set(value) {
            field = value
        }


    var visibleCells: MutableList<UITableViewCell> = mutableListOf()
    var indexPathsForVisibleRows: List<NSIndexPath> = listOf()

    var reuseableCells: MutableMap<String, UITableViewCell> = mutableMapOf()

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
        numberOfSections = dataSource?.numberOfSectionsInTableView(this) ?: 0
        var contentSizeHeight: Double = 0.00;
        for (i in 0..numberOfSections) {
            contentSizeHeight += delegate()?.tableViewHeightForRowAtIndexPath(this, NSIndexPath()) ?: 0.00
        }
        this.contentSize = CGSize(0.0, contentSizeHeight)

        for (i in 0..numberOfSections) {
            var cell = dataSource?.tableViewCellForRowAtIndexPath(this, NSIndexPath())
            visibleCells.add(cell!!)

            contentView.addSubview(cell!!)

            contentSizeHeight += cell!!.frame.size.height
            if (contentSizeHeight >= frame.size.height) {
                break
            }
        }


    }

    override fun setContentOffset(contentOffset: CGPoint, animated: Boolean) {
        super.setContentOffset(contentOffset, animated)

        var topCell = visibleCells.first()
        var bottomCell = visibleCells.last()

        var actionCell: UITableViewCell? = null;


        //top or bottom cell out off table view
        if (contentOffset.y - contentOffsetY >= topCell.frame.size.height) {
            actionCell = topCell

        }
        else if (contentOffset.y - contentOffsetY >= bottomCell.frame.size.height) {
            actionCell = topCell
        }

        actionCell?.let {
            contentOffsetY = contentOffset.y
            if (!reuseableCells.containsKey(it.reuseIdentifier)) {
                reuseableCells.put(it.reuseIdentifier, it)
            }

            if (visibleCells.contains(it)) {
                visibleCells.remove(it)
            }


            var cell = dataSource?.tableViewCellForRowAtIndexPath(this, NSIndexPath())
            visibleCells.add(cell!!)
            contentView.addSubview(cell!!)
            if (contentOffset.y - contentOffsetY >= topCell.frame.size.height) {
                cell!!.frame = cell!!.frame.setY(it.frame.origin.y + it.frame.size.height)

            }
            else if (contentOffset.y - contentOffsetY >= bottomCell.frame.size.height) {
                cell!!.frame = cell!!.frame.setY(cell!!.frame.origin.y + cell!!.frame.size.height)
            }
        }
    }

    private fun updateContentSize() {

    }
}
