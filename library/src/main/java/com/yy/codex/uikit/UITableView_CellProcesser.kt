package com.yy.codex.uikit

import com.yy.codex.foundation.NSLog
import org.jetbrains.annotations.Mutable
import java.util.*

/**
 * Created by cuiminghui on 2017/2/27.
 */

internal fun UITableView._updateCellsFrame() {
    subviews.forEach {
        (it as? UITableViewCell)?.let {
            it.frame = it.frame.setWidth(frame.width)
        }
    }
}

internal fun UITableView._requestCell(indexPath: NSIndexPath): UITableViewCell? {
    _cellInstances.toList().forEach {
        it.second.toList().forEach {
            if (it.cell.indexPath == indexPath) {
                return it.cell
            }
        }
    }
    return null
}

internal fun UITableView._allCells(): List<UITableViewCell> {
    var cells: MutableList<UITableViewCell> = mutableListOf()
    _cellInstances.toList().forEach {
        it.second.toList().forEach {
            cells.add(it.cell)
        }
    }
    return cells.toList()
}

internal fun UITableView._updateCells() {
    val dataSource = dataSource ?: return
    val visiblePositions = _requestVisiblePositions()
    val currentVisibleHash = _computeVisibleHash(visiblePositions)
    if (lastVisibleHash == currentVisibleHash) {
        return
    }
    lastVisibleHash = currentVisibleHash
    _markCellReusable(visiblePositions).forEach {
        val cell = dataSource.cellForRowAtIndexPath(this, it.indexPath)
        cell.indexPath = it.indexPath
        cell.frame = CGRect(0.0, it.value, frame.width, it.height)
        cell.setSelected(_isCellSelected(cell), false)
        _enqueueCell(cell, it)
        if (cell.superview !== this) {
            cell.removeFromSuperview()
            insertSubview(cell, 0)
        }
        cell._updateAppearance()
    }
}

internal fun UITableView._isCellSelected(cell: UITableViewCell): Boolean {
    indexPathsForSelectedRows.forEach {
        if (it == cell.indexPath) {
            return true
        }
    }
    return false
}

internal fun UITableView._dequeueCell(reuseIdentifier: String): UITableViewCell? {
    var cell: UITableViewCell? = null
    _cellInstances[reuseIdentifier]?.let {
        it.toList().forEach {
            if (cell != null) {
                return@forEach
            }
            if (!it.isBusy) {
                cell = it.cell
            }
        }
    }
    cell?.prepareForReuse()
    return cell
}

internal fun UITableView._requestPreviousPointCell(cell: UITableViewCell): UITableViewCell? {
    (this._requestCell(this._requestCellPositionWithPoint(cell.frame.y - 1.0).indexPath))?.let {
        if (it != cell && it.frame.y + it.frame.height >= cell.frame.y - 1.0) {
            return it
        }
    }
    return null
}

internal fun UITableView._requestNextPointCell(cell: UITableViewCell): UITableViewCell? {
    (this._requestCell(this._requestCellPositionWithPoint(cell.frame.y + cell.frame.height + 1.0).indexPath))?.let {
        if (it != cell && it.frame.y <= cell.frame.y + cell.frame.height + 1.0) {
            return it
        }
    }
    return null
}

private fun UITableView._enqueueCell(cell: UITableViewCell, cellPosition: UITableViewCellPosition) {
    val reuseIdentifier = cell.reuseIdentifier ?: return
    if (_cellInstances[reuseIdentifier] == null) {
        _cellInstances[reuseIdentifier] = mutableListOf()
    }
    _cellInstances[reuseIdentifier]?.let {
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
            it.isBusy = visibleMapping[it.cellPosition] == true
            if (it.isBusy) {
                trimmedPositions.remove(it.cellPosition)
            }
        }
    }
    return trimmedPositions.toList()
}

internal class UITableViewReusableCell(val cell: UITableViewCell, var cellPosition: UITableViewCellPosition, var isBusy: Boolean)