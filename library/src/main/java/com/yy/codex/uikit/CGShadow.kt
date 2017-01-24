package com.yy.codex.uikit

/**
 * Created by adi on 17/1/17.
 */

class CGShadow(val radius: Double, val offsetX: Double, val offsetY: Double, val color: UIColor) {

    fun setRadius(radius: Double): CGShadow {
        return CGShadow(radius, this.offsetX, this.offsetY, this.color)
    }

    fun setOffsetX(offsetX: Double): CGShadow {
        return CGShadow(this.radius, offsetX, this.offsetY, this.color)
    }

    fun setOffsetY(offsetY: Double): CGShadow {
        return CGShadow(this.radius, this.offsetX, offsetY, this.color)
    }

    fun setOffsetX(color: UIColor): CGShadow {
        return CGShadow(this.radius, offsetX, this.offsetY, color)
    }

    override fun equals(other: Any?): Boolean {
        var other = other as? CGShadow ?: return false
        return Math.abs(radius - other.radius) < 0.01 && Math.abs(offsetX - other.offsetX) < 0.01 && Math.abs(offsetY - other.offsetY) < 0.01 && color == other.color
    }

}
