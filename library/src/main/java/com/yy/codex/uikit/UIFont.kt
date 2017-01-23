package com.yy.codex.uikit

import android.graphics.Typeface

import android.graphics.Typeface.DEFAULT
import android.graphics.Typeface.DEFAULT_BOLD

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

    companion object {

        fun systemBold(fontSize: Float): UIFont {
            return UIFont("SystemBold", fontSize)
        }

    }

}
