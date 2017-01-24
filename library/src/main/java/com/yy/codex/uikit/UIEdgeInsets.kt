package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/1/16.
 */

class UIEdgeInsets(val top: Double, val left: Double, val bottom: Double, val right: Double) {

    override fun equals(other: Any?): Boolean {
        var other = other as? UIEdgeInsets ?: return false
        return Math.abs(top - other.top) < 0.01 && Math.abs(left - other.left) < 0.01 && Math.abs(bottom - other.bottom) < 0.01 && Math.abs(right - other.right) < 0.01
    }

    companion object {
        var zero = UIEdgeInsets(0.0, 0.0, 0.0, 0.0)
    }

}
