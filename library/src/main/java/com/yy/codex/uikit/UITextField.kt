package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
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
            if (!isFirstResponder()) {
                becomeFirstResponder()
            }
        }
    }

    override fun onLongPressed(sender: UILongPressGestureRecognizer) {
        super.onLongPressed(sender)
        movingPrevious = false
        movingNext = false
        if (isFirstResponder()) {
            if (sender.location(wrapper).x < 12.0) {
                movingPrevious = true
                moveCursorToPrevious()
            }
            else if (sender.location(wrapper).x > wrapper.frame.width - 12.0) {
                movingNext = true
                moveCursorToNext()
            }
            else {
                moveCursor(sender.location(label).x)
            }
        }
    }

    override fun onTapped(sender: UITapGestureRecognizer) {
        super.onTapped(sender)
        if (isFirstResponder()) {
            moveCursor(sender.location(label).x)
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
        val onBounds = label.frame.x == 0.0 || -label.frame.x == label.frame.width - wrapper.frame.width - 2.0
        val onCenter = input.cursorPosition < input.editor?.length() ?: 0 && input.cursorPosition > 0
        var lastX = label.frame.x
        label.text = input.editor?.text.toString()
        resetCharPositions()
        resetLayouts()
        if (!onBounds || onCenter) {
            if (cursorView.frame.x > -lastX + wrapper.frame.width) {
                lastX = -(cursorView.frame.x - wrapper.frame.width + 22.0)
            }
            else if (label.frame.width < wrapper.frame.width) {
                lastX = 0.0
            }
            else if (-lastX > label.frame.width - wrapper.frame.width) {
                lastX = - (label.frame.width - wrapper.frame.width)
            }
            label.frame = label.frame.setX(lastX)
        }
    }

    override fun textShouldChange(range: NSRange, replacementString: String): Boolean {
        return true
    }

    lateinit internal var input: UITextInput
    lateinit internal var wrapper: UIView
    lateinit internal var label: UILabel
    lateinit private var cursorView: UIView
    private var cursorViewAnimation: UIViewAnimation? = null
    internal var charPositions: List<Int> = listOf()

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
        var labelX = 0.0
        if (input.cursorPosition < charPositions.count() && wrapper.frame.width < textSize.width) {
            labelX = (-charPositions[input.cursorPosition] + wrapper.frame.width) - 2.0
            if (labelX > 0.0) {
                labelX = 0.0
            }
        }
        label.frame = CGRect(labelX, (wrapper.frame.height - textSize.height) / 2.0, textSize.width, textSize.height)
        resetCursorLayout()
    }

    private fun resetCursorLayout() {
        cursorView.frame = CGRect(0.0, 0.0, 2.0, label.frame.height)
        label.attributedText?.let {
            val substring = it.substring(NSRange(0, input.cursorPosition))
            val cursorPosition = substring.measure(context, 999999.0)
            cursorView.frame = CGRect(Math.max(0.0, cursorPosition.width - 1.0), 0.0, 2.0, label.frame.height)
        }
    }

    private fun resetCharPositions() {
        label.attributedText?.let {
            val mutableList: MutableList<Int> = mutableListOf()
            for (i in 0..it.length) {
                val substring = it.substring(NSRange(0, i))
                val cursorPosition = substring.measure(context, 999999.0)
                mutableList.add(cursorPosition.width.toInt())
            }
            charPositions = mutableList.toList()
        }
    }

    private fun moveCursor(x: Double) {
        if (charPositions.count() >= 2) {
            var left = 0
            var right = charPositions.count() - 1
            var target = 0
            while (true) {
                if (x > charPositions[left] && x < charPositions[right]) {
                    if (right - left <= 1) {
                        val midValue = (charPositions[left] + charPositions[right]) / 2.0
                        if (x > midValue) {
                            target = right
                        }
                        else {
                            target = left
                        }
                        break
                    }
                    val mid = (left + right) / 2
                    if (x > charPositions[mid]) {
                        left = mid
                    }
                    else if (x < charPositions[mid]) {
                        right = mid
                    }
                    else {
                        target = mid
                        break
                    }
                }
                else if (x < charPositions[left]) {
                    target = left
                    break
                }
                else if (x > charPositions[right]) {
                    target = right
                    break
                }
            }
            input.editor?.setSelection(target)
            resetCursorLayout()
        }
        else if (charPositions.count() == 1) {
            if (x > charPositions[0]) {
                input.editor?.setSelection(1)
            }
            else {
                input.editor?.setSelection(0)
            }
            resetCursorLayout()
        }
        else if (charPositions.count() == 0) {
            input.editor?.setSelection(0)
            resetCursorLayout()
        }
    }

    private var moveNextTiming: Long = 0

    private var movingPrevious = false
    private fun moveCursorToPrevious() {
        if (System.currentTimeMillis() < moveNextTiming) {
            return
        }
        moveNextTiming = System.currentTimeMillis() + 128
        val current = input.editor?.selectionEnd ?: 0
        if (current > 0) {
            input.editor?.setSelection(current - 1)
            resetLayouts()
        }
        postDelayed({
            if (tracking && movingPrevious){
                moveCursorToPrevious()
            }
        }, 128)
    }

    private var movingNext = false
    private fun moveCursorToNext() {
        if (System.currentTimeMillis() < moveNextTiming) {
            return
        }
        moveNextTiming = System.currentTimeMillis() + 128
        val current = input.editor?.selectionEnd ?: 0
        if (current < input.editor?.length() ?: 0) {
            input.editor?.setSelection(current + 1)
            resetLayouts()
        }
        postDelayed({
            if (tracking && movingNext){
                moveCursorToNext()
            }
        }, 128)
    }

}
