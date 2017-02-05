package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
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

    var contentInsets: UIEdgeInsets = UIEdgeInsets.zero
        set(value) {
            field = value
            resetLayouts()
        }

    override fun init() {
        super.init()
        input = UITextInput()
        input.view = this
        wrapper = UIView(context)
        addSubview(wrapper)
        label = UILabel(context)
        wrapper.addSubview(label)
        cursorView = UIView(context)
        cursorView.hidden = true
        label.addSubview(cursorView)
    }

    override fun becomeFirstResponder() {
        super.becomeFirstResponder()
        input.beginEditing()
        showCursorView()
    }

    override fun resignFirstResponder() {
        if (isFirstResponder()) {
            input.endEditing()
            hideCursorView()
        }
        super.resignFirstResponder()
    }

    override fun onEvent(event: Event) {
        super.onEvent(event)
        if (event == UIControl.Event.TouchUpInside) {
            becomeFirstResponder()
        }
    }

    override fun keyboardPressDown(event: UIKeyEvent) {
        super.keyboardPressDown(event)
        if (event.keyCode == KeyEvent.KEYCODE_DEL) {
            input.delete()
        }
    }

    override fun keyboardPressUp(event: UIKeyEvent) {
        super.keyboardPressUp(event)
    }

    override fun textDidChanged() {
        label.text = input.editor?.text.toString()
        resetLayouts()
    }

    override fun textShouldChange(range: NSRange, replacementString: String): Boolean {
        return true
    }

    lateinit internal var input: UITextInput
    lateinit internal var wrapper: UIView
    lateinit internal var label: UILabel
    lateinit private var cursorView: UIView
    private var cursorViewAnimation: UIViewAnimation? = null

    private fun showCursorView() {
        cursorView.hidden = false
        cursorView.setBackgroundColor(tintColor)
        resetLayouts()
        setupCursorAnimation()
    }

    private fun setupCursorAnimation() {
        postDelayed({
            runCursorAnimation(false)
        }, 500)
    }

    private fun runCursorAnimation(show: Boolean) {
        if (show) {
            cursorViewAnimation = UIViewAnimator.linear(0.15, Runnable {
                cursorView.alpha = 1.0f
            }, Runnable {
                postDelayed({
                    runCursorAnimation(false)
                }, 500)
            })
        }
        else {
            cursorViewAnimation = UIViewAnimator.linear(0.15, Runnable {
                cursorView.alpha = 0.0f
            }, Runnable {
                postDelayed({
                    runCursorAnimation(true)
                }, 500)
            })
        }
    }

    private fun hideCursorView() {
        removeCursorAnimation()
        cursorView.hidden = true
    }

    private fun removeCursorAnimation() {
        cursorViewAnimation?.let(UIViewAnimation::cancel)
    }

    private fun resetLayouts() {
        wrapper.frame = CGRect(contentInsets.left, contentInsets.top, frame.width - contentInsets.left - contentInsets.right, frame.height - contentInsets.top - contentInsets.bottom)
        var textSize = label.intrinsicContentSize()
        textSize = textSize.setWidth(textSize.width + 4.0)
        label.frame = CGRect(Math.min(0.0, wrapper.frame.width - textSize.width), (wrapper.frame.height - textSize.height) / 2.0, textSize.width, textSize.height)
        cursorView.frame = CGRect(0.0, 0.0, 2.0, label.frame.height)
        label.attributedText?.let {
            val substring = it.substring(NSRange(0, input.cursorPosition))
            val cursorPosition = substring.measure(context, 999999.0)
            cursorView.frame = CGRect(cursorPosition.width, 0.0, 2.0, label.frame.height)
        }
    }

}
