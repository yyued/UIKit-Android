package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/2/27.
 */

internal fun UITableView._reloadCellCaches() {
    _reloadCellPositionCaches()
}

internal fun UITableView._requestVisiblePositions(): List<UITableViewCellPosition> {
    val results: MutableList<UITableViewCellPosition> = mutableListOf()
    val startPosition = _requestCellPositionWithPoint(contentOffset.y)
    val endPosition = _requestCellPositionWithPoint(contentOffset.y + frame.height)
    results.add(startPosition)
    if (endPosition != startPosition) {
        cellPositions.indexOf(startPosition)?.let {
            val startIndex= it
            cellPositions.indexOf(endPosition)?.let {
                val endIndex = it
                (startIndex + 1 until endIndex).forEach { results.add(cellPositions[it]) }
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

private fun UITableView._requestCellPositionWithPoint(atPoint: Double): UITableViewCellPosition {
    var left = 0
    var right = cellPositions.size - 1
    while (true) {
        if (right - left < 2) {
            return cellPositions[left]
        }
        val mid = Math.ceil((left + right) / 2.0).toInt()
        if (atPoint < cellPositions[mid].value) {
            right = mid
        }
        else if (atPoint > cellPositions[mid].value) {
            left = mid
        }
        else {
            return cellPositions[mid]
        }
    }
}

private fun UITableView._reloadCellPositionCaches() {
    dataSource?.let {
        val cellPositions: MutableList<UITableViewCellPosition> = mutableListOf()
        val dataSource = it
        val sectionCount = dataSource.numberOfSectionsInTableView(this)
        var currentY = 0.0
        (0 until sectionCount).forEach {
            val section = it
            val rowCount = dataSource.tableViewNumberOfRowsInSection(this, it)
            (0 until rowCount).forEach {
                val row = it
                val rowHeight = delegate()?.tableViewHeightForRowAtIndexPath(this, NSIndexPath(section, row)) ?: rowHeight
                cellPositions.add(UITableViewCellPosition(currentY, rowHeight, NSIndexPath(section, row)))
                currentY += rowHeight
            }
        }
        this.cellPositions = cellPositions.toList()
    }
}

internal class UITableViewCellPosition(val value: Double, val height: Double, val indexPath: NSIndexPath)