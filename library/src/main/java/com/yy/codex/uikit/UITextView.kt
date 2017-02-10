package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View

/**
 * Created by cuiminghui on 2017/2/8.
 */
class UITextView : UIScrollView, UITextInput.Delegate {

    constructor(context: Context, view: View) : super(context, view) {}
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    private lateinit var tapGestureRecognizer: UITapGestureRecognizer
    private lateinit var longPressGestureRecognizer: UILongPressGestureRecognizer

    override fun init() {
        super.init()
        input = UITextInput()
        label = UILabel(context)
        label.numberOfLines = 0
        addSubview(label)
        tapGestureRecognizer = UITapGestureRecognizer(this, "onTapped:")
        longPressGestureRecognizer = UILongPressGestureRecognizer(this, "onLongPressed:")
        longPressGestureRecognizer.minimumPressDuration = 0.20
        addGestureRecognizer(tapGestureRecognizer)
        addGestureRecognizer(longPressGestureRecognizer)
        cursorView = UIView(context)
        cursorView.hidden = true
        label.addSubview(cursorView)
    }

    override fun didMoveToSuperview() {
        super.didMoveToSuperview()
        input.view = this
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
        resetLayouts()
    }

    fun onLongPressed(sender: UILongPressGestureRecognizer) {
        if (isFirstResponder()) {
            operateCursor(sender)
        }
        else if (sender.state == UIGestureRecognizerState.Ended) {
            if (UIViewHelpers.pointInside(this, sender.location(this))) {
                becomeFirstResponder()
                operateCursor(sender)
            }
        }
    }

    fun onTapped(sender: UITapGestureRecognizer) {
        if (!isFirstResponder()) {
            becomeFirstResponder()
            operateCursor(sender)
        }
        else {
            operateCursor(sender)
        }
    }

    override fun keyboardPressDown(event: UIKeyEvent) {
        super.keyboardPressDown(event)
        if (event.keyCode == KeyEvent.KEYCODE_DEL) {
            input.delete()
        }
    }

    var contentInsets: UIEdgeInsets = UIEdgeInsets(0.0, 6.0, 0.0, 6.0)
        set(value) {
            field = value
            resetLayouts()
        }

    var text: String?
        get() = input?.editor?.text.toString()
        set(value) {
            label.text = value
            input.editor?.text?.clear()
            input.editor?.text?.append(text)
            input.editor?.setSelection(value?.length ?: 0)
        }

    var attributedText: NSAttributedString?
        get() = label.attributedText
        set(value) { label.attributedText = value }

    var textColor: UIColor?
        get() = label.textColor
        set(value) { value?.let { label.textColor = it } }

    var font: UIFont?
        get() = label.font
        set(value) { value?.let { label.font = it } }

    var defaultTextAttributes: Map<String, Any>? = null

    override fun textDidChanged(onDelete: Boolean) {
        resetText(onDelete)
    }

    override fun textShouldChange(range: NSRange, replacementString: String): Boolean {
        return true
    }

    override fun textShouldClear(): Boolean {
        return true
    }

    override fun textShouldReturn(): Boolean {
        return true
    }

    /* Content Props */
    lateinit internal var input: UITextInput
    lateinit internal var label: UILabel

    /* Cursor Private Props */
    lateinit internal var cursorView: UIView
    internal var cursorOptID: Long = 0
    internal var cursorViewAnimation: UIViewAnimation? = null
    internal var cursorMoveNextTiming: Long = 0
    internal var cursorMovingPrevious = false
    internal var cursorMovingNext = false

}