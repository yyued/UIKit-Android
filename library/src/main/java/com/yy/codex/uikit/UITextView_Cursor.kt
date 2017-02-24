package com.yy.codex.uikit

import android.content.ClipboardManager
import android.content.Context
import com.yy.codex.foundation.lets

/**
 * Created by cuiminghui on 2017/2/8.
 */

internal fun UITextView.showCursorView() {
    cursorView.hidden = false
    cursorView.setBackgroundColor(tintColor)
    resetCursorLayout()
    setupCursorAnimation()
}

private fun UITextView.setupCursorAnimation() {
    cursorOptID = System.currentTimeMillis()
    removeCursorAnimation()
    runCursorAnimation(true)
}

private fun UITextView.runCursorAnimation(show: Boolean) {
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

internal fun UITextView.hideCursorView() {
    removeCursorAnimation()
    cursorView.hidden = true
}

internal fun UITextView.removeCursorAnimation() {
    cursorViewAnimation?.let(UIViewAnimation::cancel)
}

internal fun UITextView.operateCursor(sender: UILongPressGestureRecognizer) {
    if (isFirstResponder()) {
        if (selection != null) {
            operateSelection(sender)
            return
        }
        val location = sender.location(label)
        moveCursor(location.setY(location.y))
    }
}

internal fun UITextView.operateCursor(sender: UITapGestureRecognizer) {
    if (isFirstResponder()) {
        if (selection != null) {
            operateSelection(sender)
            return
        }
        val location = sender.location(label)
        moveCursor(location.setY(location.y))
    }
}

private fun UITextView.moveCursor(pt: CGPoint) {
    label.textPosition(CGPoint(pt.x, pt.y))?.let {
        input.editor?.setSelection(it)
        resetCursorLayout()
    }
}


internal fun UITextView.showPositionMenu() {
    val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    var menuItems: MutableList<UIMenuItem> = mutableListOf()
    if (input.editor?.length() ?: 0 > 0) {
        menuItems.add(UIMenuItem("选择", this, "onChoose"))
        menuItems.add(UIMenuItem("全选", this, "onChooseAll"))
    }
    if (manager.hasPrimaryClip() && manager.primaryClip.itemCount > 0) {
        menuItems.add(UIMenuItem("粘贴", this, "onPaste"))
    }
    if (menuItems.count() == 0) {
        return
    }
    UIMenuController.sharedMenuController.menuItems = menuItems
    UIMenuController.sharedMenuController.setTargetWithRect(CGRect(0.0, 0.0, 0.0, 0.0), cursorView, this)
    UIMenuController.sharedMenuController.setMenuVisible(true, true)
}

internal fun UITextView.resetSelection() {
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

internal fun UITextView.showSelectionMenu() {
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
        UIMenuController.sharedMenuController.setTargetWithRect(CGRect(0.0, 10.0, selectorRightHandleView.frame.x + 10.0 - selectorLeftHandleView.frame.x, 0.0), selectorLeftHandleView, this)
        postDelayed({
            UIMenuController.sharedMenuController.setMenuVisible(true, true)
        }, 300)
    }
}


internal fun UITextView.operateSelection(sender: UILongPressGestureRecognizer) {
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
                moveCursor(sender.location(label))
            }
        }
        else if (sender.state == UIGestureRecognizerState.Changed) {
            if (selectionOperatingLeft) {
                moveSelectionLeft(touchPoint.x, touchPoint.y)
            }
            else if (selectionOperatingRight) {
                moveSelectionRight(touchPoint.x, touchPoint.y)
            }
        }
        else if (sender.state == UIGestureRecognizerState.Ended) {
            if (selectionOperatingLeft) {
                moveSelectionLeft(touchPoint.x, touchPoint.y)
                selectionOperatingLeft = false
                showSelectionMenu()
            }
            else if (selectionOperatingRight) {
                moveSelectionRight(touchPoint.x, touchPoint.y)
                selectionOperatingRight = false
                showSelectionMenu()
            }
        }
    }
}

private fun UITextView.moveSelectionLeft(x: Double, y: Double) {
    label.textPosition(CGPoint(x, y))?.let {
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

private fun UITextView.moveSelectionRight(x: Double, y: Double) {
    label.textPosition(CGPoint(x, y))?.let {
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

internal fun UITextView.operateSelection(sender: UITapGestureRecognizer) {
    lets(label.selectorLeftHandleView, label.selectorRightHandleView) { selectorLeftHandleView, selectorRightHandleView ->
        var touchPoint = sender.location(label)
        if (UIViewHelpers.pointInside(selectorLeftHandleView, label.convertPoint(touchPoint, selectorLeftHandleView), CGPoint(22.0, 22.0)) ||
                UIViewHelpers.pointInside(selectorRightHandleView, label.convertPoint(touchPoint, selectorRightHandleView), CGPoint(22.0, 22.0))) { }
        else {
            selection = null
            moveCursor(sender.location(label))
        }
    }
}