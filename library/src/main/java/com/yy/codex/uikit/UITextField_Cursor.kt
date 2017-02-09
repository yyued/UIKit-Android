package com.yy.codex.uikit

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

internal fun UITextField.resetCharPositions() {
    label.attributedText?.let {
        charPositions = (0..it.length)
                .map { i -> it.substring(NSRange(0, i)) }
                .map { it.measure(999999.0) }
                .map { it.width.toInt() }
    }
}

private fun UITextField.moveCursor(x: Double) {
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