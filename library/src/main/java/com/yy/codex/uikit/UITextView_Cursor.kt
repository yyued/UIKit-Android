package com.yy.codex.uikit

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
        val location = sender.location(label)
        moveCursor(location.setY(location.y + contentOffset.y))
    }
}

internal fun UITextView.operateCursor(sender: UITapGestureRecognizer) {
    if (isFirstResponder()) {
        val location = sender.location(label)
        moveCursor(location.setY(location.y + contentOffset.y))
    }
}

private fun UITextView.moveCursor(pt: CGPoint) {
    label.textPosition(CGPoint(pt.x, pt.y))?.let {
        input.editor?.setSelection(it)
        resetCursorLayout()
    }
}