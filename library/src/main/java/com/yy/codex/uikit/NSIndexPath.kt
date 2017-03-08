package com.yy.codex.uikit

/**
 * Created by it on 17/1/23.
 */

open class NSIndexPath(val section: Int, val row: Int) {

    override fun equals(other: Any?): Boolean {
        return (other as? NSIndexPath)?.section == section && (other as? NSIndexPath)?.row == row
    }

}