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

    override fun toString(): String {
        return "CGSize{mWidth=$width,height=$height}"
    }

}
