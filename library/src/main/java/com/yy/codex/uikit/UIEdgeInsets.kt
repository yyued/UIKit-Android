package com.yy.codex.uikit

import android.content.res.TypedArray

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

        fun create(attributes: TypedArray, target: UIView, type: String): UIEdgeInsets {
            if (type.contentEquals("margin")) {
                return UIEdgeInsets(
                    attributes.getFloat(R.styleable.UIView_marginInset_top, 0.0f).toDouble(),
                    attributes.getFloat(R.styleable.UIView_marginInset_left, 0.0f).toDouble(),
                    attributes.getFloat(R.styleable.UIView_marginInset_bottom, 0.0f).toDouble(),
                    attributes.getFloat(R.styleable.UIView_marginInset_right, 0.0f).toDouble()
                )
            }
            return zero
        }

    }

}
