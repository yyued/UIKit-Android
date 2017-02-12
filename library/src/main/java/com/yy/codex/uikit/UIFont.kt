package com.yy.codex.uikit

import android.graphics.Typeface

import android.graphics.Typeface.DEFAULT
import android.graphics.Typeface.DEFAULT_BOLD
import android.text.TextPaint

/**
 * Created by cuiminghui on 2017/1/9.
 */

class UIFont(val fontFamily: String?, val fontSize: Float) {

    constructor(fontSize: Float): this(null, fontSize)

    val typeface: Typeface?
        get() {
            val fontFamily = fontFamily ?: return Typeface.create(fontFamily, Typeface.NORMAL)
            if (fontFamily.equals("System", ignoreCase = true)) {
                return DEFAULT
            }
            else if (fontFamily.equals("SystemBold", ignoreCase = true)) {
                return DEFAULT_BOLD
            }
            else {
                return Typeface.create(fontFamily, Typeface.NORMAL)
            }
        }

    private val textPaint = TextPaint()

    var lineHeight: Double = 0.0
        get() {
            typeface?.let {
                textPaint.typeface = it
                textPaint.textSize = (fontSize * UIScreen.mainScreen.scale()).toFloat()
                return (Math.abs(textPaint.fontMetrics.ascent).toDouble() + Math.abs(textPaint.fontMetrics.descent).toDouble()) / UIScreen.mainScreen.scale()
            }
            return 0.0
        }

    companion object {

        fun systemBold(fontSize: Float): UIFont {
            return UIFont("SystemBold", fontSize)
        }

    }

}
