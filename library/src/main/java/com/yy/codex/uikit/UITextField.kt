package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.yy.codex.foundation.NSLog

/**
 * Created by PonyCui_Home on 2017/2/3.
 */

class UITextField : UIControl, UITextInput.Delegate {

    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    lateinit var input: UITextInput

    override fun init() {
        super.init()
        input = UITextInput()
        input.view = this
    }

    override fun becomeFirstResponder() {
        super.becomeFirstResponder()
        input.beginEditing()
    }

    override fun resignFirstResponder() {
        if (isFirstResponder()) {
            input.endEditing()
        }
        super.resignFirstResponder()
    }

    override fun onEvent(event: Event) {
        super.onEvent(event)
        if (event == UIControl.Event.TouchUpInside) {
            becomeFirstResponder()
        }
    }

}
