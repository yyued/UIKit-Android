package com.yy.codex.uikit

import android.content.ClipData
import android.content.ClipboardManager
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

    interface Delegate: UIScrollViewDelegate {
        fun textViewShouldBeginEditing(textView: UITextView): Boolean
        fun textViewShouldEndEditing(textView: UITextView): Boolean
        fun textViewDidBeginEditing(textView: UITextView)
        fun textViewDidEndEditing(textView: UITextView)
        fun textViewShouldChangeTextInRange(textView: UITextView, inRange: NSRange, replacementString: String): Boolean
        fun textViewDidChange(textView: UITextView)
    }

    constructor(context: Context, view: View) : super(context, view) {}
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

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
        if (!editable) {
            return
        }
        (delegate as? Delegate)?.let {
            if (!it.textViewShouldBeginEditing(this)) {
                return
            }
        }
        super.becomeFirstResponder()
        input.beginEditing()
        showCursorView()
        (delegate as? Delegate)?.let {
            it.textViewDidBeginEditing(this)
        }
    }

    override fun resignFirstResponder() {
        (delegate as? Delegate)?.let {
            if (!it.textViewShouldEndEditing(this)) {
                return
            }
        }
        if (isFirstResponder()) {
            selection = null
            input.endEditing()
            hideCursorView()
        }
        super.resignFirstResponder()
        resetLayouts()
        (delegate as? Delegate)?.let {
            it.textViewDidEndEditing(this)
        }
    }

    fun onLongPressed(sender: UILongPressGestureRecognizer) {
        if (sender.state == UIGestureRecognizerState.Began && isFirstResponder()) {
            touchStartTimestamp = System.currentTimeMillis()
            touchStartInputPosition = input.cursorPosition
            UIMenuController.sharedMenuController.setMenuVisible(false, true)
            operateCursor(sender)
        }
        if (sender.state == UIGestureRecognizerState.Changed && isFirstResponder()) {
            UIMenuController.sharedMenuController.setMenuVisible(false, true)
            operateCursor(sender)
        }
        else if (sender.state == UIGestureRecognizerState.Ended) {
            if (UIViewHelpers.pointInside(this, sender.location(this))) {
                if (!isFirstResponder()) {
                    becomeFirstResponder()
                    operateCursor(sender)
                }
                else {
                    operateCursor(sender)
                    if (selection == null && touchStartTimestamp + 300 > System.currentTimeMillis()) {
                        if (touchStartInputPosition == input.cursorPosition) {
                            showPositionMenu()
                        }
                    }
                }
            }
        }
    }

    fun onTapped(sender: UITapGestureRecognizer) {
        if (!isFirstResponder()) {
            becomeFirstResponder()
            operateCursor(sender)
        }
        else {
            val menuVisible = UIMenuController.sharedMenuController.menuVisible
            if (!menuVisible) {
                touchStartInputPosition = input.cursorPosition
            }
            else {
                UIMenuController.sharedMenuController.setMenuVisible(false, true)
            }
            operateCursor(sender)
            if (!menuVisible && selection == null) {
                if (touchStartInputPosition == input.cursorPosition) {
                    showPositionMenu()
                }
            }
        }
    }

    override fun keyboardPressDown(event: UIKeyEvent) {
        super.keyboardPressDown(event)
        if (event.keyCode == KeyEvent.KEYCODE_DEL) {
            input.delete(selection)
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

    var editable = true

    var selectable = true

    var selection: NSRange? = null
        set(value) {
            field = value
            resetSelection()
        }

    override fun textDidChanged(onDelete: Boolean) {
        resetText(onDelete)
        selection?.let {
            selection = null
        }
        (delegate as? Delegate)?.let {
            it.textViewDidChange(this)
        }
    }

    override fun textShouldChange(range: NSRange, replacementString: String): Boolean {
        (delegate as? Delegate)?.let {
            return it.textViewShouldChangeTextInRange(this, range, replacementString)
        }
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
    lateinit private var tapGestureRecognizer: UITapGestureRecognizer
    lateinit private var longPressGestureRecognizer: UILongPressGestureRecognizer

    /* Cursor Private Props */
    lateinit internal var cursorView: UIView
    internal var cursorOptID: Long = 0
    internal var cursorViewAnimation: UIViewAnimation? = null
    internal var cursorMoveNextTiming: Long = 0
    internal var cursorMovingPrevious = false
    internal var cursorMovingNext = false
    internal var touchStartTimestamp: Long = 0
    internal var touchStartInputPosition: Int = 0
    internal var selectionOperatingLeft = false
    internal var selectionOperatingRight = false

    private fun onChoose() {
        val textLength = label.text?.length ?: return
        if (input.cursorPosition >= 2) {
            selection = NSRange(input.cursorPosition - 2, 2)
        }
        else if (textLength > 0) {
            onChooseAll()
        }
    }

    private fun onChooseAll() {
        selection = NSRange(0, label.text?.length ?: 0)
    }

    private fun onCrop() {
        selection?.let {
            text?.substring(it.location, it.location + it.length)?.let {
                val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                manager.primaryClip = ClipData.newPlainText(it, it)
            }
            input.delete(it)
        }
    }

    private fun onCopy() {
        selection?.let {
            text?.substring(it.location, it.location + it.length)?.let {
                val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                manager.primaryClip = ClipData.newPlainText(it, it)
            }
        }
    }

    private fun onPaste() {
        val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (manager.hasPrimaryClip() && manager.primaryClip.itemCount > 0) {
            manager.primaryClip.getItemAt(0).text?.let {
                input.editor?.text?.insert(input.cursorPosition, it)
            }
        }
    }

}