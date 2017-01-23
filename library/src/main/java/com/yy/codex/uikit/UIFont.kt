package com.yy.codex.uikit

import android.graphics.Typeface

import android.graphics.Typeface.DEFAULT
import android.graphics.Typeface.DEFAULT_BOLD

/**
 * Created by cuiminghui on 2017/1/9.
 */

class UIFont {

    val fontFamily: String?
    val fontSize: Float

    constructor(fontSize: Float) {
        this.fontFamily = "System"
        this.fontSize = fontSize
    }

    constructor(fontFamily: String?, fontSize: Float) {
        this.fontFamily = fontFamily
        this.fontSize = fontSize
    }

    val typeface: Typeface?
        get() {
            if (fontFamily!!.equals("System", ignoreCase = true)) {
                return DEFAULT
            } else if (fontFamily.equals("SystemBold", ignoreCase = true)) {
                return DEFAULT_BOLD
            }
            return Typeface.create(fontFamily, Typeface.NORMAL)
        }

    companion object {

        fun systemBold(fontSize: Float): UIFont {
            return UIFont("SystemBold", fontSize)
        }
    }

}
