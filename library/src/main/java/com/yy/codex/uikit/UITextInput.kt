package com.yy.codex.uikit

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * Created by PonyCui_Home on 2017/2/3.
 */

class UITextInput {

    interface Delegate

    private var editor: EditText? = null

    var view: Delegate? = null
        set(value) {
            field = value
            (field as? UIView)?.let {
                editor = EditText(it.context)
                editor?.alpha = 0.0f
                editor?.clearFocus()
                it.addView(editor)
            }
        }

    fun beginEditing() {
        editor?.let {
            it.requestFocus()
            (it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
        }
    }

    fun endEditing() {
        editor?.let {
            it.clearFocus()
            (it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }
    }

}
