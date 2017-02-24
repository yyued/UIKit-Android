package com.yy.codex.uikit

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.yy.codex.foundation.lets

/**
 * Created by PonyCui_Home on 2017/2/3.
 */

class UITextInput {

    interface Delegate {
        fun textDidChanged(onDelete: Boolean)
        fun textShouldChange(range: NSRange, replacementString: String): Boolean
        fun textShouldClear(): Boolean
        fun textShouldReturn(): Boolean
    }

    internal var editor: EditText? = null
    internal var cursorPosition: Int = 0
        get() = editor?.selectionEnd ?: 0
    private var currentOperationDeleting = false
    private var currentOperationRange: NSRange? = null

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
                        delegate.textDidChanged(currentOperationDeleting)
                    }
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        currentOperationRange = NSRange(p1, p3)
                    }
                })
                editor?.setOnEditorActionListener { p0, p1, p2 ->
                    return@setOnEditorActionListener delegate.textShouldReturn()
                }
                editor?.alpha = 0.0f
                editor?.clearFocus()
                var rootView = it.superview
                while (rootView?.superview != null) {
                    rootView = rootView?.superview
                }
                if (rootView != null) {
                    rootView.addView(editor, 0, 0)
                }
                else {
                    (editor?.parent as? ViewGroup)?.removeView(editor)
                }
            }
        }

    fun beginEditing() {
        resetKeyboardType()
        resetReturnType()
        editor?.let {
            it.setSingleLine((view as? UITextInputTraits)?.returnKeyType != UIReturnKeyType.Default)
            (it.context as? Activity)?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            it.requestFocus()
            (it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun endEditing() {
        editor?.let {
            it.clearFocus()
            (it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun resetKeyboardType() {
        val view = (this.view as? UITextInputTraits) ?: return
        editor?.let {
            when (view.keyboardType) {
                UIKeyboardType.Default -> it.inputType = EditorInfo.TYPE_CLASS_TEXT
                UIKeyboardType.Password -> it.inputType = EditorInfo.TYPE_CLASS_TEXT.xor(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
                UIKeyboardType.EmailAddress -> it.inputType = EditorInfo.TYPE_CLASS_TEXT.xor(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                UIKeyboardType.URL -> it.inputType = EditorInfo.TYPE_CLASS_TEXT.xor(EditorInfo.TYPE_TEXT_VARIATION_URI)
                UIKeyboardType.DecimalPad -> it.inputType = EditorInfo.TYPE_CLASS_NUMBER.xor(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL)
                UIKeyboardType.NumberPad -> it.inputType = EditorInfo.TYPE_CLASS_NUMBER
                UIKeyboardType.PhonePad -> it.inputType = EditorInfo.TYPE_CLASS_PHONE
            }
        }
    }

    fun resetReturnType() {
        val view = (this.view as? UITextInputTraits) ?: return
        editor?.let {
            when (view.returnKeyType) {
                UIReturnKeyType.Default -> it.imeOptions = EditorInfo.IME_ACTION_UNSPECIFIED
                UIReturnKeyType.Go -> it.imeOptions = EditorInfo.IME_ACTION_GO
                UIReturnKeyType.Next -> it.imeOptions = EditorInfo.IME_ACTION_NEXT
                UIReturnKeyType.Search -> it.imeOptions = EditorInfo.IME_ACTION_SEARCH
                UIReturnKeyType.Send -> it.imeOptions = EditorInfo.IME_ACTION_SEND
                UIReturnKeyType.Done -> it.imeOptions = EditorInfo.IME_ACTION_DONE
            }
        }
    }

    fun clear() {
        view?.let {
            if (it.textShouldClear()) {
                editor?.let {
                    val editable = it.text
                    editable.clear()
                }
            }
        }
    }

    fun delete(range: NSRange? = null) {
        currentOperationDeleting = true
        editor?.let {
            val editable = it.text
            if (editable.length > 0) {
                val range = range ?: NSRange(it.selectionStart, it.selectionEnd - it.selectionStart)
                if (range.length > 0) {
                    editable.delete(range.location, range.location + range.length)
                }
                else {
                    if (range.location > 0) {
                        editable.delete(range.location - 1, range.location)
                    }
                }
            }
        }
        currentOperationDeleting = false
    }

}
