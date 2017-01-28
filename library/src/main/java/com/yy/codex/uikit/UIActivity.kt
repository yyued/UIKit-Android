package com.yy.codex.uikit

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent

/**
 * Created by PonyCui_Home on 2017/1/23.
 */
open class UIActivity : Activity() {

    var main: UIResponder? = null

    var viewController: UIViewController? = null
        set(value) {
            value?.let {
                main = it
                field = it
                setContentView(it.view)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        val event = event ?: return false
        main?.let {
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