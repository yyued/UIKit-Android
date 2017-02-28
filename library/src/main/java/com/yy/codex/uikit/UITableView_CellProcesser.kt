package com.yy.codex.uikit

import com.yy.codex.foundation.NSLog
import org.jetbrains.annotations.Mutable
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
    _markCellReusable(visiblePositions).forEach {
        val cell = dataSource.tableViewCellForRowAtIndexPath(this, it.indexPath)
        cell.frame = CGRect(0.0, it.value, frame.width, it.height)
        _enqueueCell(cell, it)
        if (cell.superview !== this) {
            cell.removeFromSuperview()
            addSubview(cell)
        }
    }
}

internal fun UITableView._dequeueCell(reuseIdentifier: String): UITableViewCell? {
    var cell: UITableViewCell? = null
    _cellInstances[reuseIdentifier]?.let {
        it.toList().forEach {
            if (!it.isBusy) {
                cell = it.cell
            }
        }
    }
    return cell
}

private fun UITableView._enqueueCell(cell: UITableViewCell, cellPosition: UITableViewCellPosition) {
    if (_cellInstances[cell.reuseIdentifier] == null) {
        _cellInstances[cell.reuseIdentifier] = mutableListOf()
    }
    _cellInstances[cell.reuseIdentifier]?.let {
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

private fun UITableView._markCellReusable(visiblePositions: List<UITableViewCellPosition>): List<UITableViewCellPosition> {
    val trimmedPositions: MutableList<UITableViewCellPosition> = visiblePositions.toMutableList()
    val visibleMapping: HashMap<UITableViewCellPosition, Boolean> = hashMapOf()
    visiblePositions.forEach {
        visibleMapping[it] = true
    }
    _cellInstances.toList().forEach {
        it.second.toList().forEach {
            it.isBusy = visibleMapping[it.cellPosition] === true
            if (it.isBusy) {
                trimmedPositions.remove(it.cellPosition)
            }
        }
    }
    return trimmedPositions.toList()
}

internal class UITableViewReusableCell(val cell: UITableViewCell, var cellPosition: UITableViewCellPosition, var isBusy: Boolean)