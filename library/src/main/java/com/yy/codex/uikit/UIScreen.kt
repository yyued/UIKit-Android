package com.yy.codex.uikit

import android.content.Context
import android.graphics.Rect
import android.view.WindowManager

/**
 * Created by cuiminghui on 2017/1/13.
 */

class UIScreen {

    private var mContext: Context? = null

    fun setContext(context: Context) {
        mContext = context
    }

    fun scale(): Double {
        val scale: Double = mContext?.resources?.displayMetrics?.scaledDensity?.toDouble() ?: 1.0
        if (scale == 0.0) {
            return 1.0
        }
        else {
            return scale
        }
    }

    fun bounds(): CGRect {
        if (mContext != null && scale() > 0.0) {
            val windowManager = mContext?.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            val rect = Rect()
            windowManager?.defaultDisplay?.getRectSize(rect)
            return CGRect(rect.left / scale(), rect.top / scale(), rect.width() / scale(), rect.height() / scale())
        } else {
            return CGRect(0.0, 0.0, 0.0, 0.0)
        }
    }

    companion object {
        var mainScreen = UIScreen()
    }

}
