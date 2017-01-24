package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/1/3.
 */

class CGPoint(val x: Double, val y: Double) {

    fun setX(x: Double): CGPoint {
        return CGPoint(x, y)
    }

    fun setY(y: Double): CGPoint {
        return CGPoint(x, y)
    }

    fun inRange(xRange: Double, yRange: Double, toPoint: CGPoint): Boolean {
        return Math.abs(toPoint.x - this.x) < xRange && Math.abs(toPoint.y - this.y) < yRange
    }

    override fun equals(other: Any?): Boolean {
        var other = other as? CGPoint ?: return false
        return Math.abs(x - other.x) < 0.01 && Math.abs(y - other.y) < 0.01
    }

    override fun toString(): String {
        return "CGPoint{x=$x,y=$y}"
    }

}
