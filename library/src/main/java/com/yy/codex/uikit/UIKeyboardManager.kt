package com.yy.codex.uikit

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import com.yy.codex.foundation.NSLog
import com.yy.codex.foundation.NSNotificationCenter

/**
 * Created by cuiminghui on 2017/2/6.
 */
class UIKeyboardManager(val activity: Activity) {

    private var lastValue = 0.0
        set(value) {
            val oldValue = field
            field = value
            if (oldValue <= 0.0 && value > 0.0) {
                NSNotificationCenter.defaultCenter.postNotification(UIKeyboardDidShowNotification, null, mapOf(
                        Pair(UIKeyboardFrameEndUserInfoKey, CGRect(0.0, UIScreen.mainScreen.bounds().height - value, UIScreen.mainScreen.bounds().width, value))
                ))
            }
            else if (oldValue > 0.0 && value <= 0.0) {
                NSNotificationCenter.defaultCenter.postNotification(UIKeyboardDidHideNotification)
                UIResponder.firstResponder?.resignFirstResponder()
            }
            else if (oldValue != value) {
                NSNotificationCenter.defaultCenter.postNotification(UIKeyboardDidShowNotification, null, mapOf(
                        Pair(UIKeyboardFrameEndUserInfoKey, CGRect(0.0, UIScreen.mainScreen.bounds().height - value, UIScreen.mainScreen.bounds().width, value))
                ))
            }
        }

    private var globalLayoutListener: Any? = null

    internal fun registerListener() {
        val rootView = activity.window.decorView.findViewById(android.R.id.content)
        globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val heightDiff = rootView.bottom - rect.bottom
            lastValue = (heightDiff.toFloat() / rootView.resources.displayMetrics.scaledDensity).toDouble()
        }
        rootView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener as ViewTreeObserver.OnGlobalLayoutListener)
    }

    internal fun unregisterListener() {
        globalLayoutListener?.let {
            val rootView = activity.window.decorView.findViewById(android.R.id.content)
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(it as ViewTreeObserver.OnGlobalLayoutListener)
        }
    }

    companion object {

        val UIKeyboardDidShowNotification = "UIKeyboardDidShowNotification"
        val UIKeyboardDidHideNotification = "UIKeyboardDidHideNotification"
        val UIKeyboardFrameEndUserInfoKey = "UIKeyboardFrameEndUserInfoKey"

    }

}