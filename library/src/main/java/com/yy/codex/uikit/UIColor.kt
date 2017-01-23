package com.yy.codex.uikit

import android.graphics.Color

/**
 * Created by cuiminghui on 2017/1/16.
 */

class UIColor(val r: Double, val g: Double, val b: Double, val a: Double) {

    fun colorWithAlpha(alpha: Double): UIColor {
        return UIColor(r, g, b, a * alpha)
    }

    fun toInt(): Int {
        return Color.argb((a * 255).toInt(), (r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt())
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
        val grayColor = UIColor(.3, .3, .3, 1.0)
        val orangeColor = UIColor(1.0, .38, 0.0, 1.0)
        val yellowColor = UIColor(1.0, 1.0, 0.0, 1.0)
    }

}
