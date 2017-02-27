package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import com.yy.codex.foundation.NSLog
import com.yy.codex.foundation.lets
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

    internal var lastVisibleHash: String = ""

    internal var cellPositions: List<UITableViewCellPosition> = listOf()

    internal var cellInstances: HashMap<String, MutableList<UITableViewReusableCell>> = hashMapOf()

    fun reloadData() {
        _reloadCellCaches()
        _updateCells()
    }

    fun cellForRowAtIndexPath(indexPath: NSIndexPath): UITableViewCell? {
        return null
    }

    override fun didMoveToSuperview() {
        super.didMoveToSuperview()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        _updateCells()
        subviews.forEach {
            (it as? UITableViewCell)?.let { it.frame = it.frame.setWidth(frame.width) }
        }
    }

    override fun touchesBegan(touches: List<UITouch>, event: UIEvent) {
        super.touchesBegan(touches, event)

        if (!decelerating) {
            var touch = touches.first()
            var touchPoint = touch.locationInView(this)

            var middleCount = (visibleCells.count() / 2).toInt()
            var middleCell = visibleCells[middleCount]
            if (touchPoint.y > middleCell.frame.origin.y && touchPoint.y < middleCell.frame.size.height) {
                delegate()?.tableViewDidSelectRowAtIndexPath(this, NSIndexPath(middleCount, 0))
            }

            for (i in 1 until middleCount) {
                var bottom = middleCount + i
                var top = middleCount - i
                var cell = visibleCells[bottom]
                if (touchPoint.y > middleCell.frame.origin.y && touchPoint.y < middleCell.frame.size.height) {
                    delegate()?.tableViewDidSelectRowAtIndexPath(this, NSIndexPath(middleCount, 0))
                }
                cell = visibleCells[top]
                if (touchPoint.y > middleCell.frame.origin.y && touchPoint.y < middleCell.frame.size.height) {
                    delegate()?.tableViewDidSelectRowAtIndexPath(this, NSIndexPath(middleCount, 0))
                }
            }
        }
    }

    private fun updateRowData() {
        updateContentSize()

        var contentSizeHeight: Double = 0.00
        var loadCellComplete = false
        for (i in 0 until numberOfSections) {
            loadingSection = 0

            var headerFooterView = delegateWantsHeaderTitleForSection(i)
            headerFooterView?.let {
                contentView.addSubview(it)
                contentSizeHeight += it.frame.size.height
                it.frame = it.frame.setY(contentSizeHeight)
            }

            var numberOfRowsInSection = dataSource?.tableViewNumberOfRowsInSection(this, i) ?: 0
            willLoadNumberOfRow = numberOfRowsInSection
            for (j in 0 until numberOfRowsInSection) {

                var indexPath = NSIndexPath(j, i)
                var cell = dataSource?.tableViewCellForRowAtIndexPath(this, indexPath) as UITableViewCell
                visibleCells.add(cell)
                indexPathsForVisibleRows.add(indexPath)

                var cellHeight = delegate()?.tableViewHeightForRowAtIndexPath(this, NSIndexPath(0, 0)) ?: 50.00
                cell.frame = CGRect(0.00, contentSizeHeight, frame.size.width, cellHeight + 1)
                addSubview(cell)

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

        var actionCells: MutableList<UITableViewCell> = mutableListOf()
        var actionIndexPaths: MutableList<NSIndexPath> = mutableListOf()

        var isCellOutOfTop = true

        for (i in 0 until visibleCells.count()) {
            var topCell = visibleCells[i]
            var bottomCell = visibleCells[visibleCells.count() - (i + 1)]
            var topIndexPath = indexPathsForVisibleRows[i]
            var bottomIndexPath = indexPathsForVisibleRows[visibleCells.count() - (i + 1)]
            if ((contentOffset.y >= topCell.frame.origin.y + topCell.frame.size.height) && contentOffset.y > contentOffsetY && contentOffset.y < contentSize.height - frame.size.height) {
                actionCells.add(topCell)
                actionIndexPaths.add(NSIndexPath(bottomIndexPath.row + (i + 1), bottomIndexPath.section))
            }
            else if ((contentOffset.y + frame.size.height <= bottomCell.frame.origin.y) && contentOffset.y < contentOffsetY && contentOffset.y > 0) {
                isCellOutOfTop = false
                actionCells.add(bottomCell)
                actionIndexPaths.add(NSIndexPath(topIndexPath.row + (i - 1), bottomIndexPath.section))
            }
        }

        contentOffsetY = contentOffset.y

        if (actionCells.count() > 0) {
            for (i in 0 until actionCells.count()) {
                var actionCell = actionCells[i]
                var actionIndexPath = actionIndexPaths[i]

                var topFirstCell = visibleCells.first()
                var bottomFirstCell = visibleCells.last()

                addReuseableCell(actionCell)

                if (visibleCells.contains(actionCell)) {
                    visibleCells.remove(actionCell)
                }

//                if (actionIndexPath.section < numberOfSections) {
//                    if (actionIndexPath.row >= willLoadNumberOfRow) {
//                        var headerFooterView = delegateWantsHeaderTitleForSection(actionIndexPath.section + 1)
//                        headerFooterView?.let {
//                            contentView.addSubview(it)
//                            it.frame = it.frame.setY(bottomCell.frame.size.height + bottomCell.frame.origin.y)
//                        }
//                    }
//                }

//                if (actionIndexPath.row <= 0 && actionIndexPath.section > 0) {
//                    var headerFooterView = delegateWantsHeaderTitleForSection(actionIndexPath.section - 1)
//                    headerFooterView?.let {
//                        contentView.addSubview(it)
//                        it.frame = it.frame.setY(topCell.frame.size.height + topCell.frame.origin.y)
//                    }
//                }

                var indexPatch = NSIndexPath(actionIndexPath.row, 0)
                var cellHeight = delegate()?.tableViewHeightForRowAtIndexPath(this, indexPatch) ?: 50.00
                var cell = dataSource?.tableViewCellForRowAtIndexPath(this, indexPatch) as UITableViewCell
                if (isCellOutOfTop) {
                    cell.frame = CGRect(0.00, bottomFirstCell.frame.origin.y + bottomFirstCell.frame.size.height, frame.size.width, cellHeight + 1)
                    visibleCells.add(cell)
                }
                else {
                    cell.frame = CGRect(0.00, topFirstCell.frame.origin.y - cellHeight - 1, frame.size.width, cellHeight + 1)
                    visibleCells.add(0, cell)
                }
            }
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
        numberOfSections = dataSource?.numberOfSectionsInTableView(this) ?: 0
        var contentSizeHeight: Double = 0.00
        for (i in 0 until numberOfSections) {
            var numberOfRowsInSection = dataSource?.tableViewNumberOfRowsInSection(this, i) ?: 0
            for (j in 0 until numberOfRowsInSection) {
                contentSizeHeight += delegate()?.tableViewHeightForRowAtIndexPath(this, NSIndexPath(0, 0)) ?: 50.00
                //add line height
                contentSizeHeight += 1.00
            }
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
