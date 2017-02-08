package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

/**
 * Created by cuiminghui on 2017/2/8.
 */
class UITextView(context: Context) : UIScrollView(context), UITextInput.Delegate {

    override fun init() {
        super.init()
        alwaysBounceVertical = true
        input = UITextInput()
        label = UILabel(context)
        label.numberOfLines = 0
        addSubview(label)
        addGestureRecognizer(UITapGestureRecognizer(this, "becomeFirstResponder"))
    }

    override fun didMoveToSuperview() {
        super.didMoveToSuperview()
        input.view = this
    }

    override fun becomeFirstResponder() {
        input.beginEditing()
    }

    override fun resignFirstResponder() {
        if (isFirstResponder()) {
            input.endEditing()
        }
        super.resignFirstResponder()
        resetLayouts()
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
    internal var charPositions: List<Int> = listOf()

    /* Cursor Private Props */
    lateinit internal var cursorView: UIView
    internal var cursorOptID: Long = 0
    internal var cursorViewAnimation: UIViewAnimation? = null
    internal var cursorMoveNextTiming: Long = 0
    internal var cursorMovingPrevious = false
    internal var cursorMovingNext = false

}