package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.MotionEvent
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
        fun titleForFooterInSection(tableView: UITableView, section: Int): String?
    }

    interface UITableViewDelegate: UIScrollView.UIScrollViewDelegate {
        fun heightForRowAtIndexPath(tableView: UITableView, indexPath: NSIndexPath): Double
        fun heightForHeaderInSection(tableView: UITableView, section: Int): Double
        fun heightForFooterInSection(tableView: UITableView, section: Int): Double
        fun didSelectRowAtIndexPath(tableView: UITableView, indexPath: NSIndexPath)
        fun didDeselectRowAtIndexPath(tableView: UITableView, indexPath: NSIndexPath)
        fun viewForHeaderInSection(tableView: UITableView, section: Int): UIView?
        fun viewForFooterInSection(tableView: UITableView, section: Int): UIView?
        fun willDisplayCell(tableView: UITableView, cell: UITableViewCell, indexPath: NSIndexPath)
        fun didEndDisplayingCell(tableView: UITableView, cell: UITableViewCell, indexPath: NSIndexPath)
        fun accessoryTypeForRow(tableView: UITableView, indexPath: NSIndexPath): UITableViewCell.AccessoryType?
        fun shouldHighlightRow(tableView: UITableView, indexPath: NSIndexPath): Boolean?
        fun didHighlightRow(tableView: UITableView, indexPath: NSIndexPath)
        fun didUnhighlightRow(tableView: UITableView, indexPath: NSIndexPath)
        fun editActionsForRow(tableView: UITableView, indexPath: NSIndexPath): List<UITableViewRowAction>?
    }

    enum class ScrollPosition {
        None,
        Top,
        Middle,
        Bottom,
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

    var rowHeight = 44.0

    var dataSource: UITableViewDataSource? = null

    fun delegate(): UITableViewDelegate? {
        return delegate as? UITableViewDelegate
    }

    var tableHeaderView: UIView? = null
        set(value) {
            if (field !== value) {
                field?.let(UIView::removeFromSuperview)
            }
            field = value
            _reloadTableHeaderFooterView()
        }

    var tableFooterView: UIView? = null
        set(value) {
            if (field !== value) {
                field?.let(UIView::removeFromSuperview)
            }
            field = value
            _reloadTableHeaderFooterView()
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

    var separatorStyle: UITableViewCell.SeparatorStyle = UITableViewCell.SeparatorStyle.SingleLine
        set(value) {
            field = value
            _allCells().forEach { it._updateAppearance() }
        }

    var separatorInset: UIEdgeInsets = UIEdgeInsets.zero
        set(value) {
            field = value
            _allCells().forEach { it._updateAppearance() }
        }

    var separatorColor: UIColor = UIColor(0xc8, 0xc7, 0xcc)
        set(value) {
            field = value
            _allCells().forEach { it._updateAppearance() }
        }

    var editing = false
        internal set

    fun setEditing(editing: Boolean, animated: Boolean) {
        this.editing = editing
    }

    fun reloadData() {
        _reloadSectionHeaderFooterView()
        _reloadCellCaches()
        _reloadContentSize()
        _updateCells()
        _updateTableHeaderFooterViewFrame()
        _updateSectionHeaderFooterFrame()
    }

    override fun didMoveToSuperview() {
        super.didMoveToSuperview()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        _updateCellsFrame()
        _updateCells()
        _updateTableHeaderFooterViewFrame()
        _updateSectionHeaderFooterFrame()
    }

    override fun setContentOffset(contentOffset: CGPoint, animated: Boolean) {
        super.setContentOffset(contentOffset, animated)
        _updateCells()
        _updateSectionHeaderFooterFrame()
    }

    fun dequeueReusableCellWithIdentifier(reuseIdentifier: String): UITableViewCell? {
        return _dequeueCell(reuseIdentifier)
    }

    fun selectRow(indexPath: NSIndexPath, animated: Boolean) {
        if (!allowsSelection) {
            return
        }
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
        indexPathsForSelectedRows = indexPathsForSelectedRows.filter { it != indexPath }
    }

    fun indexPathForRowAtPoint(point: CGPoint): NSIndexPath? {
        val position = _requestCellPositionWithPoint(point.y)
        if (point.y < position.value) {
            return null
        }
        else if (point.y > position.value + position.height) {
            return null
        }
        else {
            return _requestCellPositionWithPoint(point.y).indexPath
        }
    }

    fun indexPathForCell(cell: UITableViewCell): NSIndexPath? {
        return _allCells().firstOrNull { it === cell }?._indexPath
    }

    fun indexPathsForRowsInRect(rect: CGRect): List<NSIndexPath> {
        return _requestVisiblePositionsWithValues(rect.y, rect.y + rect.height).map { return@map it.indexPath }
    }

    fun cellForRowAtIndexPath(indexPath: NSIndexPath): UITableViewCell? {
        return _allCells().firstOrNull { it._indexPath == indexPath }
    }

    fun visibleCells(): List<UITableViewCell> {
        return _requestVisiblePositions().mapNotNull { return@mapNotNull _requestCell(it.indexPath) }
    }

    fun indexPathsForVisibleRows(): List<NSIndexPath> {
        return _requestVisiblePositions().map { return@map it.indexPath }
    }

    fun scrollToRow(indexPath: NSIndexPath, scrollPosition: ScrollPosition, animated: Boolean) {
        val position = _requestPositionWithIndexPath(indexPath) ?: return
        when (scrollPosition) {
            ScrollPosition.None, ScrollPosition.Top -> {
                setContentOffset(CGPoint(0.0, position.value), animated)
            }
            ScrollPosition.Middle -> {
                setContentOffset(CGPoint(0.0, position.value + position.height - frame.height / 2.0), animated)
            }
            ScrollPosition.Bottom -> {
                setContentOffset(CGPoint(0.0, Math.max(0.0, position.value + position.height - frame.height)), animated)
            }
        }
    }

    /**
     * Private Props
     */

    internal var _lastVisibleHash: String = ""
    internal var _cellPositions: List<UITableViewCellPosition> = listOf()
    internal var _cellInstances: HashMap<String, MutableList<UITableViewReusableCell>> = hashMapOf()
    internal var _sectionsHeaderView: List<UITableViewSectionHeaderFooterViewWrapper?> = listOf()
    internal var _sectionsFooterView: List<UITableViewSectionHeaderFooterViewWrapper?> = listOf()

}
