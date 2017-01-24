package com.yy.codex.uikit

import android.graphics.Rect
import android.graphics.RectF

/**
 * Created by cuiminghui on 2017/1/3.
 */

class CGRect(x: Double, y: Double, width: Double, height: Double) {

    val origin: CGPoint
    val size: CGSize

    init {
        this.origin = CGPoint(x, y)
        this.size = CGSize(width, height)
    }

    fun toRectF(): RectF {
        return RectF(origin.x.toFloat(), origin.y.toFloat(), (origin.x + size.width).toFloat(), (origin.y + size.height).toFloat())
    }

    fun toRectF(point: CGPoint): RectF {
        val top = point.y.toFloat()
        val left = point.x.toFloat()
        val right = size.width.toFloat() + left
        val bottom = size.height.toFloat() + top
        return RectF(left, top, right, bottom)
    }

    fun toRect(): Rect {
        return Rect(origin.x.toInt(), origin.y.toInt(), (origin.x + size.width).toInt(), (origin.y + size.height).toInt())
    }

    fun shrinkToRectF(offset: Float): RectF {
        val top = origin.y.toFloat() + offset
        val left = origin.x.toFloat() + offset
        val right = size.width.toFloat() + left - 2 * offset
        val bottom = size.height.toFloat() + top - 2 * offset
        return RectF(left, top, right, bottom)
    }

    fun shrinkToRectF(offset: Float, point: CGPoint): RectF {
        val top = point.y.toFloat() + offset
        val left = point.x.toFloat() + offset
        val right = size.width.toFloat() + left - 2 * offset
        val bottom = size.height.toFloat() + top - 2 * offset
        return RectF(left, top, right, bottom)
    }

    fun setX(x: Double): CGRect {
        return CGRect(x, this.origin.y, this.size.width, this.size.height)
    }

    val x: Double
        get() = origin.x

    fun setY(y: Double): CGRect {
        return CGRect(this.origin.x, y, this.size.width, this.size.height)
    }

    val y: Double
        get() = origin.y

    fun setWidth(width: Double): CGRect {
        return CGRect(this.origin.x, this.origin.y, width, this.size.height)
    }

    val width: Double
        get() = size.width

    fun setHeight(height: Double): CGRect {
        return CGRect(this.origin.x, this.origin.y, this.size.width, height)
    }

    val height: Double
        get() = size.height

    override fun equals(other: Any?): Boolean {
        var other = other as? CGRect ?: return false
        return origin == other.origin && size == other.size
    }

    override fun toString(): String {
        return "CGRect > origin = " + origin.toString() + ", size = " + size.toString()
    }

}
