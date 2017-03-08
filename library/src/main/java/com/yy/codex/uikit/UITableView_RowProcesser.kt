package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/2/27.
 */

internal fun UITableView._reloadCellCaches() {
    _reloadCellPositionCaches()
}

internal fun UITableView._reloadContentSize() {
    if (_cellPositions.size > 0) {
        val maxY = _cellPositions.last().value + _cellPositions.last().height + (tableFooterView?.frame?.height ?: 0.0)
        contentSize = CGSize(0.0, maxY)
    }
}

internal fun UITableView._requestVisiblePositions(): List<UITableViewCellPosition> {
    val results: MutableList<UITableViewCellPosition> = mutableListOf()
    val startPosition = _requestCellPositionWithPoint(contentOffset.y)
    val endPosition = _requestCellPositionWithPoint(contentOffset.y + frame.height)
    results.add(startPosition)
    if (endPosition !== startPosition) {
        _cellPositions.indexOf(startPosition)?.let {
            val startIndex= it
            _cellPositions.indexOf(endPosition)?.let {
                val endIndex = it
                (startIndex + 1 until endIndex).forEach { results.add(_cellPositions[it]) }
            }
        }
        results.add(endPosition)
    }
    return results
}

internal fun UITableView._computeVisibleHash(visiblePositions: List<UITableViewCellPosition>): String {
    var hash = ""
    visiblePositions.forEach { hash += it.indexPath.section.toString() + "_" + it.indexPath.row.toString() + "," }
    return hash
}

internal fun UITableView._requestCellPositionWithPoint(atPoint: Double): UITableViewCellPosition {
    var left = 0
    var right = _cellPositions.size - 1
    if (atPoint <= _cellPositions[left].value) {
        return _cellPositions[left]
    }
    else if (atPoint >= _cellPositions[right].value) {
        return _cellPositions[right]
    }
    while (true) {
        if (right - left < 2) {
            return _cellPositions[left]
        }
        val mid = Math.ceil((left + right) / 2.0).toInt()
        if (atPoint < _cellPositions[mid].value) {
            right = mid
        }
        else if (atPoint > _cellPositions[mid].value) {
            left = mid
        }
        else {
            return _cellPositions[mid]
        }
    }
}

private fun UITableView._reloadCellPositionCaches() {
    dataSource?.let {
        val cellPositions: MutableList<UITableViewCellPosition> = mutableListOf()
        val dataSource = it
        val sectionCount = dataSource.numberOfSections(this)
        var currentY = 0.0
        currentY += tableHeaderView?.frame?.height ?: 0.0
        (0 until sectionCount).forEach {
            val section = it
            val sectionHeader = _sectionsHeaderView[section]
            sectionHeader?.startY = currentY
            currentY += sectionHeader?.headerHeight ?: 0.0
            val rowCount = dataSource.numberOfRowsInSection(this, it)
            (0 until rowCount).forEach {
                val row = it
                val rowHeight = delegate()?.heightForRowAtIndexPath(this, NSIndexPath(section, row)) ?: rowHeight
                cellPositions.add(UITableViewCellPosition(currentY, rowHeight, NSIndexPath(section, row)))
                currentY += rowHeight
            }
            sectionHeader?.endY = currentY
        }
        this._cellPositions = cellPositions.toList()
    }
}

internal class UITableViewCellPosition(val value: Double, val height: Double, val indexPath: NSIndexPath)