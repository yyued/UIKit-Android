package com.yy.codex.uikit

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import com.yy.codex.foundation.NSLog

/**
 * Created by PonyCui_Home on 2017/1/23.
 */
open class UIActivity : Activity() {

    var mainResponder: UIResponder? = null
    lateinit internal var keyboardManager: UIKeyboardManager

    var viewController: UIViewController? = null
        set(value) {
            value?.let {
                mainResponder = it
                field = it
                setContentView(it.view)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keyboardManager = UIKeyboardManager(this)
        keyboardManager.registerListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        keyboardManager.unregisterListener()
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        val event = event ?: return false
        mainResponder?.let {
            if (UIResponder.firstResponder == null) {
                UIResponder.firstResponder = UIResponder.findFirstResponder(it)
            }
            UIResponder.firstResponder?.let {
                when (event.action) {
                    KeyEvent.ACTION_DOWN -> it.keyboardPressDown(UIKeyEvent(event.keyCode))
                    KeyEvent.ACTION_UP -> if (!event.isCanceled) it.keyboardPressUp(UIKeyEvent(event.keyCode))
                }
            }
        }
        return true
    }

}