package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.text.Editable
import android.text.Layout
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import com.yy.codex.foundation.NSLog
import java.util.*

/**
 * Created by PonyCui_Home on 2017/2/3.
 */

class UITextField : UIControl, UITextInput.Delegate, UITextInputTraits {

    enum class ViewMode {
        Never,
        WhileEditing,
        UnlessEditing,
        Always
    }

    enum class BorderStyle {
        None,
        Line,
        RoundedRect
    }

    interface Delegate {
        fun textFieldShouldBeginEditing(textField: UITextField): Boolean
        fun textFieldDidBeginEditing(textField: UITextField)
        fun textFieldShouldEndEditing(textField: UITextField): Boolean
        fun textFieldDidEndEditing(textField: UITextField)
        fun textFieldShouldChangeCharactersInRange(textField: UITextField, inRange: NSRange, replacementString: String): Boolean
        fun textFieldShouldClear(textField: UITextField): Boolean
        fun textFieldShouldReturn(textField: UITextField): Boolean
    }

    constructor(context: Context, view: View) : super(context, view) {}
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    var delegate: Delegate? = null

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

    var alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
        set(value) {
            field = value
            resetLayouts()
        }

    var borderStyle: BorderStyle = BorderStyle.None
        set(value) {
            field = value
            when (value) {
                BorderStyle.None -> wantsLayer = false
                BorderStyle.Line -> {
                    wantsLayer = true
                    layer.borderWidth = 1.0
                    layer.borderColor = UIColor.blackColor
                }
                BorderStyle.RoundedRect -> {
                    wantsLayer = true
                    layer.borderWidth = 1.0
                    layer.borderColor = UIColor.blackColor.colorWithAlpha(0.3)
                    layer.cornerRadius = 6.0
                }
            }
        }

    var defaultTextAttributes: Map<String, Any>? = null

    var placeholder: String? = null
        set(value) {
            field = value
            setupPlaceholder()
        }

    var attributedPlaceholder: NSAttributedString? = null

    var clearsOnBeginEditing = false

    var editing: Boolean = false
        get() = isFirstResponder()

    var clearButton: UIView? = null
        internal set
    var clearButtonMode = ViewMode.Never
        set(value) {
            field = value
            resetClearView()
        }

    var leftView: UIView? = null
        set(value) {
            if (value == null) {
                field?.let(UIView::removeFromSuperview)
            }
            field = value
            resetLeftView()
        }
    var leftViewMode = ViewMode.Never
        set(value) {
            field = value
            resetLayouts()
        }

    var rightView: UIView? = null
        set(value) {
            if (value == null) {
                field?.let(UIView::removeFromSuperview)
            }
            field = value
            resetRightView()
        }
    var rightViewMode = ViewMode.Never
        set(value) {
            field = value
            resetLayouts()
        }

    override var keyboardType: UIKeyboardType = UIKeyboardType.Default

    override var returnKeyType: UIReturnKeyType = UIReturnKeyType.Default

    override var secureTextEntry: Boolean = false

    override fun init() {
        super.init()
        input = UITextInput()
        wrapper = UIView(context)
        addSubview(wrapper)
        label = UILabel(context)
        wrapper.addSubview(label)
        cursorView = UIView(context)
        cursorView.hidden = true
        label.addSubview(cursorView)
    }

    override fun didMoveToSuperview() {
        super.didMoveToSuperview()
        input.view = this
    }

    override fun becomeFirstResponder() {
        delegate?.let {
            if (!it.textFieldShouldBeginEditing(this)) {
                return
            }
        }
        super.becomeFirstResponder()
        removePlaceholder()
        if (clearsOnBeginEditing) {
            text = ""
        }
        input.beginEditing()
        showCursorView()
        resetLayouts()
        delegate?.let {
            it.textFieldDidBeginEditing(this)
        }
    }

    override fun resignFirstResponder() {
        delegate?.let {
            if (!it.textFieldShouldEndEditing(this)) {
                return
            }
        }
        if (isFirstResponder()) {
            setupPlaceholder()
            input.endEditing()
            hideCursorView()
        }
        super.resignFirstResponder()
        resetLayouts()
        delegate?.let {
            it.textFieldDidEndEditing(this)
        }
    }

    override fun onEvent(event: Event) {
        super.onEvent(event)
        if (event == UIControl.Event.TouchUpInside) {
            if (!isFirstResponder()) {
                becomeFirstResponder()
            }
        }
    }

    override fun onLongPressed(sender: UILongPressGestureRecognizer) {
        super.onLongPressed(sender)
        operateCursor(sender)
    }

    override fun onTapped(sender: UITapGestureRecognizer) {
        super.onTapped(sender)
        operateCursor(sender)
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

    override fun textDidChanged(onDelete: Boolean) {
        resetText(onDelete)
    }

    override fun textShouldChange(range: NSRange, replacementString: String): Boolean {
        if (replacementString == "\n") {
            return false
        }
        delegate?.let {
            return it.textFieldShouldChangeCharactersInRange(this, range, replacementString)
        }
        return true
    }

    override fun textShouldClear(): Boolean {
        delegate?.let {
            return it.textFieldShouldClear(this)
        }
        return true
    }

    override fun textShouldReturn(): Boolean {
        delegate?.let {
            return it.textFieldShouldReturn(this)
        }
        return false
    }

    /* Content Props */
    lateinit internal var input: UITextInput
    lateinit internal var wrapper: UIView
    lateinit internal var label: UILabel
//    internal var charPositions: List<Int> = listOf()

    /* Cursor Private Props */
    lateinit internal var cursorView: UIView
    internal var cursorOptID: Long = 0
    internal var cursorViewAnimation: UIViewAnimation? = null
    internal var cursorMoveNextTiming: Long = 0
    internal var cursorMovingPrevious = false
    internal var cursorMovingNext = false

}
