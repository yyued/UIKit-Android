package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/1/3.
 */

class CGSize(val width: Double, val height: Double) {

    fun setWidth(width: Double): CGSize {
        return CGSize(width, height)
    }

    fun setHeight(height: Double): CGSize {
        return CGSize(width, height)
    }

    override fun equals(other: Any?): Boolean {
        var other = other as? CGSize ?: return false
        return Math.abs(width - other.width) < 0.01 && Math.abs(height - other.height) < 0.01
    }

    override fun toString(): String {
        return "CGSize{width=$width,height=$height}"
    }

    companion object {
        val zero = CGSize(0.0, 0.0)
    }

}
