package com.yy.codex.foundation

import android.util.Log

import com.yy.codex.uikit.UIView

/**
 * Created by cuiminghui on 2017/1/12.
 */

object NSLog {

    fun log(`object`: Any?) {
        println("NSLog: " + `object`!!.toString())
    }

    fun warn(`object`: Any?) {
        Log.w("NSLog", `object`!!.toString())
    }

}