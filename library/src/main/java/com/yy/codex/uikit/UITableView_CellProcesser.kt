package com.yy.codex.uikit

import java.util.*

/**
 * Created by cuiminghui on 2017/2/27.
 */

internal fun UITableView._updateCells() {
    val dataSource = dataSource ?: return
    val visiblePositions = _requestVisiblePositions()
    val currentVisibleHash = _computeVisibleHash(visiblePositions)
    if (lastVisibleHash == currentVisibleHash) {
        return
    }
    lastVisibleHash = currentVisibleHash
    _markCellReusable(visiblePositions)
    visiblePositions.forEach {
        val cell = dataSource.tableViewCellForRowAtIndexPath(this, it.indexPath)
        cell.frame = CGRect(0.0, it.value, frame.width, it.height)
        _enqueueCell(cell, it)
        if (cell.superview != this) {
            cell.removeFromSuperview()
            addSubview(cell)
        }
    }
}

internal fun UITableView._dequeueCell(reuseIdentifier: String): UITableViewCell? {
    cellInstances[reuseIdentifier]?.let {
        it.toList().forEach {
            if (!it.isBusy) {
                return it.cell
            }
        }
    }
    return null
}

private fun UITableView._enqueueCell(cell: UITableViewCell, cellPosition: UITableViewCellPosition) {
    cellInstances[cell.reuseIdentifier]?.let {
        var found = false
        it.toList().forEach {
            if (it.cell === cell) {
                it.cellPosition = cellPosition
                it.isBusy = true
                found = true
            }
        }
        if (!found) {
            it.add(UITableViewReusableCell(cell, cellPosition, true))
        }
    }
}

private fun UITableView._markCellReusable(visiblePositions: List<UITableViewCellPosition>) {
    val visibleMapping: HashMap<Int, Boolean> = hashMapOf()
    visiblePositions.forEach {
        visibleMapping[it.hashCode()] = true
    }
    cellInstances.toList().forEach {
        it.toList().forEach {
            (it as? UITableViewReusableCell)?.let {
                it.isBusy = visibleMapping[it.cellPosition.hashCode()] === true
            }
        }
    }
}

internal class UITableViewReusableCell(val cell: UITableViewCell, var cellPosition: UITableViewCellPosition, var isBusy: Boolean)