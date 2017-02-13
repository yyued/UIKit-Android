package com.yy.codex.uikit

import android.content.ClipboardManager
import android.content.Context

/**
 * Created by cuiminghui on 2017/2/7.
 */

internal fun UITextField.operateCursor(sender: UILongPressGestureRecognizer) {
    cursorMovingPrevious = false
    cursorMovingNext = false
    if (isFirstResponder()) {
        if (sender.location(wrapper).x < 12.0) {
            cursorMovingPrevious = true
            moveCursorToPrevious()
        }
        else if (sender.location(wrapper).x > wrapper.frame.width - 12.0) {
            cursorMovingNext = true
            moveCursorToNext()
        }
        else {
            moveCursor(sender.location(label).x)
        }
    }
}

internal fun UITextField.operateCursor(sender: UITapGestureRecognizer) {
    if (isFirstResponder()) {
        moveCursor(sender.location(label).x)
    }
}

internal fun UITextField.showCursorView() {
    cursorView.hidden = false
    cursorView.setBackgroundColor(tintColor)
    resetLayouts()
    setupCursorAnimation()
}

private fun UITextField.setupCursorAnimation() {
    cursorOptID = System.currentTimeMillis()
    removeCursorAnimation()
    runCursorAnimation(true)
}

private fun UITextField.runCursorAnimation(show: Boolean) {
    val currentID = cursorOptID
    if (show) {
        cursorViewAnimation = UIViewAnimator.linear(0.15, Runnable {
            cursorView.alpha = 1.0f
        }, Runnable {
            postDelayed({
                if (currentID != cursorOptID) {
                    return@postDelayed
                }
                runCursorAnimation(false)
            }, 500)
        })
    }
    else {
        cursorViewAnimation = UIViewAnimator.linear(0.15, Runnable {
            cursorView.alpha = 0.0f
        }, Runnable {
            postDelayed({
                if (currentID != cursorOptID) {
                    return@postDelayed
                }
                runCursorAnimation(true)
            }, 500)
        })
    }
}

internal fun UITextField.hideCursorView() {
    removeCursorAnimation()
    cursorView.hidden = true
}

internal fun UITextField.removeCursorAnimation() {
    cursorViewAnimation?.let(UIViewAnimation::cancel)
}

private fun UITextField.moveCursor(x: Double) {
    label.textPosition(CGPoint(x, 0.0))?.let {
        input.editor?.setSelection(it)
        resetCursorLayout()
    }
}

private fun UITextField.moveCursorToPrevious() {
    if (System.currentTimeMillis() < cursorMoveNextTiming) {
        return
    }
    cursorMoveNextTiming = System.currentTimeMillis() + 64
    val current = input.editor?.selectionEnd ?: 0
    if (current > 0) {
        input.editor?.setSelection(current - 1)
        resetLayouts()
    }
    postDelayed({
        if (tracking && cursorMovingPrevious){
            moveCursorToPrevious()
        }
    }, 64)
}

private fun UITextField.moveCursorToNext() {
    if (System.currentTimeMillis() < cursorMoveNextTiming) {
        return
    }
    cursorMoveNextTiming = System.currentTimeMillis() + 64
    val current = input.editor?.selectionEnd ?: 0
    if (current < input.editor?.length() ?: 0) {
        input.editor?.setSelection(current + 1)
        resetLayouts()
    }
    postDelayed({
        if (tracking && cursorMovingNext){
            moveCursorToNext()
        }
    }, 64)
}

internal fun UITextField.showPositionMenu() {
    val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    var menuItems: MutableList<UIMenuItem> = mutableListOf()
    menuItems.add(UIMenuItem("选择", this, "onCopy"))
    if (manager.hasPrimaryClip() && manager.primaryClip.itemCount > 0) {
        menuItems.add(UIMenuItem("粘贴", this, "onPaste"))
    }
    UIMenuController.sharedMenuController.menuItems = menuItems
    UIMenuController.sharedMenuController.setTargetWithRect(CGRect(0.0, 0.0, 0.0, 0.0), cursorView)
    UIMenuController.sharedMenuController.setMenuVisible(true, true)
}

