package com.yy.codex.uikit

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.yy.codex.foundation.lets

/**
 * Created by PonyCui_Home on 2017/2/3.
 */

class UITextInput {

    interface Delegate {
        fun textDidChanged()
        fun textShouldChange(range: NSRange, replacementString: String): Boolean
    }

    internal var editor: EditText? = null
    internal var cursorPosition: Int = 0
        get() = editor?.selectionEnd ?: 0
    internal var currentOperationRange: NSRange? = null

    var view: Delegate? = null
        set(value) {
            field = value
            (field as? UIView)?.let {
                val delegate = value as Delegate
                editor = EditText(it.context)
                editor?.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {
                        lets(currentOperationRange, p0) { currentOperationRange, p0 ->
                            if (!delegate.textShouldChange(currentOperationRange, p0.substring(currentOperationRange.location, currentOperationRange.location + currentOperationRange.length))) {
                                p0.delete(currentOperationRange.location, currentOperationRange.location + currentOperationRange.length)
                            }
                        }
                        delegate.textDidChanged()
                    }
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        currentOperationRange = NSRange(p1, p3)
                    }
                })
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

    fun clear() {
        editor?.let {
            val editable = it.text
            editable.clear()
        }
    }

    fun delete() {
        editor?.let {
            val editable = it.text
            if (editable.length > 0) {
                if (it.selectionStart < it.selectionEnd) {
                    editable.delete(it.selectionStart, it.selectionEnd)
                }
                else {
                    if (it.selectionEnd > 0) {
                        editable.delete(it.selectionEnd - 1, it.selectionEnd)
                    }
                }
            }
        }
    }

}
