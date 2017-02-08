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

internal fun UITextView.resetCharPositions() {
    val maxWidth = frame.width - contentInsets.left - contentInsets.right
    label.attributedText?.let {
        val mutablePositions: MutableList<CGRect> = mutableListOf()
        var lastHeight = 0.0
        var currentLineStartIndex = 0
        var currentLineHeight = 0.0
        for (index in 0..it.length) {
            if (index == 0) {
                mutablePositions.add(CGRect(0.0, 0.0, 0.0, 0.0))
                continue
            }
            val lineStr = it.substring(NSRange(0, index))
            val lineSize = lineStr.measure(context, maxWidth)
            var letterStr = it.substring(NSRange(currentLineStartIndex, index - currentLineStartIndex))
            var letterSize = letterStr.measure(context, maxWidth)
            if (lineSize.height > lastHeight) {
                currentLineStartIndex = index - 1
                currentLineHeight = lineSize.height - lastHeight
                lastHeight = lineSize.height
                letterStr = it.substring(NSRange(currentLineStartIndex, index - currentLineStartIndex))
                letterSize = letterStr.measure(context, maxWidth)
            }
            mutablePositions.add(CGRect(letterSize.width, lastHeight - currentLineHeight, 0.0, currentLineHeight))
        }
        charPositions = mutablePositions.toList()
    }
}