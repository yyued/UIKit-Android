package com.yy.codex.uikit

import android.graphics.Color

/**
 * Created by cuiminghui on 2017/1/16.
 */

class UIColor(val r: Double, val g: Double, val b: Double, val a: Double) {

    constructor(int: Int): this(Color.red(int) / 255.0, Color.green(int) / 255.0, Color.blue(int) / 255.0, Color.alpha(int) / 255.0)

    fun colorWithDarken(darken: Double): UIColor {
        return UIColor(r + darken * (255.0 - r), g + darken * (255.0 - g), b + darken * (255.0 - b), a )
    }

    fun colorWithAlpha(alpha: Double): UIColor {
        return UIColor(r, g, b, a * alpha)
    }

    fun toInt(): Int {
        if (a == 0.0) {
            return Color.TRANSPARENT
        }
        return Color.argb((a * 255).toInt(), (r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt())
    }

    override fun equals(other: Any?): Boolean {
        var other = other as? UIColor ?: return false
        return Math.abs(r - other.r) < 0.01 && Math.abs(g - other.g) < 0.01 && Math.abs(b - other.b) < 0.01 && Math.abs(a - other.a) < 0.01
    }

    override fun toString(): String {
        return "UIColor, r = $r, g = $g, b = $b, a = $a"
    }

    companion object {
        val blackColor = UIColor(0.0, 0.0, 0.0, 1.0)
        val whiteColor = UIColor(1.0, 1.0, 1.0, 1.0)
        val clearColor = UIColor(0.0, 0.0, 0.0, 0.0)
        val redColor = UIColor(1.0, 0.0, 0.0, 1.0)
        val greenColor = UIColor(0.0, 1.0, 0.0, 1.0)
        val blueColor = UIColor(0.0, 0.0, 1.0, 1.0)
        val grayColor = UIColor(0.3, 0.3, 0.3, 1.0)
        val orangeColor = UIColor(1.0, 0.38, 0.0, 1.0)
        val yellowColor = UIColor(1.0, 1.0, 0.0, 1.0)
    }

}
