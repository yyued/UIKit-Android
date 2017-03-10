package com.yy.codex.uikit

abstract class UITableViewDataSourceObject: UITableView.DataSource {

    override fun titleForHeaderInSection(tableView: UITableView, section: Int): String? {
        return null
    }

    override fun titleForFooterInSection(tableView: UITableView, section: Int): String? {
        return null
    }

}

abstract class UITableViewDelegateObject: UIScrollViewDelegateObject(), UITableView.Delegate {

    override fun heightForRowAtIndexPath(tableView: UITableView, indexPath: NSIndexPath): Double {
        return tableView.rowHeight
    }

    override fun heightForHeaderInSection(tableView: UITableView, section: Int): Double {
        return 28.0
    }

    override fun heightForFooterInSection(tableView: UITableView, section: Int): Double {
        return 28.0
    }

    override fun didSelectRowAtIndexPath(tableView: UITableView, indexPath: NSIndexPath) {}

    override fun didDeselectRowAtIndexPath(tableView: UITableView, indexPath: NSIndexPath) {}

    override fun viewForHeaderInSection(tableView: UITableView, section: Int): UIView? {
        return null
    }

    override fun viewForFooterInSection(tableView: UITableView, section: Int): UIView? {
        return null
    }

    override fun willDisplayCell(tableView: UITableView, cell: UITableViewCell, indexPath: NSIndexPath) {}

    override fun didEndDisplayingCell(tableView: UITableView, cell: UITableViewCell, indexPath: NSIndexPath) {}

    override fun accessoryTypeForRow(tableView: UITableView, indexPath: NSIndexPath): UITableViewCell.AccessoryType? {
        return null
    }

    override fun shouldHighlightRow(tableView: UITableView, indexPath: NSIndexPath): Boolean? {
        return null
    }

    override fun didHighlightRow(tableView: UITableView, indexPath: NSIndexPath) {

    }

    override fun didUnhighlightRow(tableView: UITableView, indexPath: NSIndexPath) {

    }

    override fun editActionsForRow(tableView: UITableView, indexPath: NSIndexPath): List<UITableViewRowAction>? {
        return null
    }

    override fun willBeginEditing(tableView: UITableView, indexPath: NSIndexPath) {}

    override fun didEndEditing(tableView: UITableView, indexPath: NSIndexPath) {}

}
