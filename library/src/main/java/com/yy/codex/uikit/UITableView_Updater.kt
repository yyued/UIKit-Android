package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/3/10.
 */

internal class UITableViewUpdateOperation {

    val operations: MutableList<OperationEntity> = mutableListOf()

    enum class OperationType {
        Insert,
        Delete,
        Reload,
    }

    internal class OperationEntity(val type: OperationType, val indexPath: NSIndexPath, val rowAnimation: UITableView.RowAnimation)

}

internal fun UITableView._updateData() {
    _reloadData()
}