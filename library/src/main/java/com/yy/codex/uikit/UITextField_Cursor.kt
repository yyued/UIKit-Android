package com.yy.codex.uikit

import android.content.ClipboardManager
import android.content.Context
import com.yy.codex.foundation.lets

/**
 * Created by cuiminghui on 2017/2/7.
 */

internal fun UITextField.operateCursor(sender: UILongPressGestureRecognizer) {
    cursorMovingPrevious = false
    cursorMovingNext = false
    if (isFirstResponder()) {
        if (selection != null) {
            operateSelection(sender)
            return
        }
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
        if (selection != null) {
            operateSelection(sender)
            return
        }
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
    menuItems.add(UIMenuItem("选择", this, "onChoose"))
    menuItems.add(UIMenuItem("全选", this, "onChooseAll"))
    if (manager.hasPrimaryClip() && manager.primaryClip.itemCount > 0) {
        menuItems.add(UIMenuItem("粘贴", this, "onPaste"))
    }
    UIMenuController.sharedMenuController.menuItems = menuItems
    UIMenuController.sharedMenuController.setTargetWithRect(CGRect(0.0, 0.0, 0.0, 0.0), cursorView, this)
    UIMenuController.sharedMenuController.setMenuVisible(true, true)
}

internal fun UITextField.resetSelection() {
    if (selection == null) {
        label.selectText(null)
        showCursorView()
        UIMenuController.sharedMenuController.setMenuVisible(false, true)
        return
    }
    selection?.let {
        hideCursorView()
        label.selectText(it)
        showSelectionMenu()
        input.editor?.setSelection(it.location, it.location + it.length)
    }
}

internal fun UITextField.showSelectionMenu() {
    if (selectionOperatingLeft || selectionOperatingRight) {
        return
    }
    lets(label.selectorLeftHandleView, label.selectorRightHandleView) { selectorLeftHandleView, selectorRightHandleView ->
        val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        var menuItems: MutableList<UIMenuItem> = mutableListOf()
        menuItems.add(UIMenuItem("剪切", this, "onCrop"))
        menuItems.add(UIMenuItem("拷贝", this, "onCopy"))
        if (manager.hasPrimaryClip() && manager.primaryClip.itemCount > 0) {
            menuItems.add(UIMenuItem("粘贴", this, "onPaste"))
        }
        UIMenuController.sharedMenuController.menuItems = menuItems
        UIMenuController.sharedMenuController.setTargetWithRect(CGRect(0.0, selectorLeftHandleView.frame.y + 18.0, selectorRightHandleView.frame.x + 10.0 - selectorLeftHandleView.frame.x, 0.0), selectorLeftHandleView, this)
        postDelayed({
            UIMenuController.sharedMenuController.setMenuVisible(true, true)
        }, 300)
    }
}

internal fun UITextField.operateSelection(sender: UILongPressGestureRecognizer) {
    lets(label.selectorLeftHandleView, label.selectorRightHandleView) { selectorLeftHandleView, selectorRightHandleView ->
        var touchPoint = sender.location(label)
        if (sender.state == UIGestureRecognizerState.Began) {
            selectionOperatingLeft = false
            selectionOperatingRight = false
            if (UIViewHelpers.pointInside(selectorLeftHandleView, label.convertPoint(touchPoint, selectorLeftHandleView), CGPoint(22.0, 22.0))) {
                selectionOperatingLeft = true
                UIMenuController.sharedMenuController.setMenuVisible(false, true)
            }
            else if (UIViewHelpers.pointInside(selectorRightHandleView, label.convertPoint(touchPoint, selectorRightHandleView), CGPoint(22.0, 22.0))) {
                selectionOperatingRight = true
                UIMenuController.sharedMenuController.setMenuVisible(false, true)
            }
            else {
                selection = null
                moveCursor(sender.location(label).x)
            }
        }
        else if (sender.state == UIGestureRecognizerState.Changed) {
            if (selectionOperatingLeft) {
                moveSelectionLeft(touchPoint.x)
            }
            else if (selectionOperatingRight) {
                moveSelectionRight(touchPoint.x)
            }
        }
        else if (sender.state == UIGestureRecognizerState.Ended) {
            if (selectionOperatingLeft) {
                moveSelectionLeft(touchPoint.x)
                selectionOperatingLeft = false
                showSelectionMenu()
            }
            else if (selectionOperatingRight) {
                moveSelectionRight(touchPoint.x)
                selectionOperatingRight = false
                showSelectionMenu()
            }
        }
    }
}

private fun UITextField.moveSelectionLeft(x: Double) {
    label.textPosition(CGPoint(x, 0.0))?.let {
        val newPosition = it
        selection?.let {
            if (it.location + it.length - newPosition <= 0) {
                selection = NSRange(it.location + it.length - 1, 1)
            }
            else {
                selection = NSRange(newPosition, it.location + it.length - newPosition)
            }
        }
    }
}

private fun UITextField.moveSelectionRight(x: Double) {
    label.textPosition(CGPoint(x, 0.0))?.let {
        val newPosition = it
        selection?.let {
            if (newPosition <= it.location) {
                selection = NSRange(it.location, 1)
            }
            else {
                selection = NSRange(it.location, newPosition - it.location)
            }
        }
    }
}

internal fun UITextField.operateSelection(sender: UITapGestureRecognizer) {
    lets(label.selectorLeftHandleView, label.selectorRightHandleView) { selectorLeftHandleView, selectorRightHandleView ->
        var touchPoint = sender.location(label)
        if (UIViewHelpers.pointInside(selectorLeftHandleView, label.convertPoint(touchPoint, selectorLeftHandleView), CGPoint(22.0, 22.0)) ||
                UIViewHelpers.pointInside(selectorRightHandleView, label.convertPoint(touchPoint, selectorRightHandleView), CGPoint(22.0, 22.0))) { }
        else {
            selection = null
            moveCursor(sender.location(label).x)
        }
    }
}