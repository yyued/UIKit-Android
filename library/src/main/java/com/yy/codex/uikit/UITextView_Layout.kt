package com.yy.codex.uikit

import android.text.Layout
import com.yy.codex.foundation.NSLog

/**
 * Created by cuiminghui on 2017/2/8.
 */

internal fun UITextView.resetLayouts() {
    label.maxWidth = frame.width - contentInsets.left - contentInsets.right
    var textSize = label.intrinsicContentSize()
    label.frame = CGRect(contentInsets.left, contentInsets.top, Math.min(textSize.width, frame.width - contentInsets.left - contentInsets.right) + 4.0, textSize.height)
    contentSize = CGSize(0.0, contentInsets.top + textSize.height + contentInsets.bottom)
    resetCursorLayout()
}

internal fun UITextView.resetCursorLayout() {
    cursorView.frame = CGRect(0.0, 0.0, 2.0, 20.0)
    label.attributedText?.let {
        if (input.cursorPosition < charPositions.count()) {
            val cursorPosition = charPositions[input.cursorPosition]
            cursorView.frame = CGRect(cursorPosition.x, cursorPosition.y, 2.0, cursorPosition.height - 2.0)
        }
        else {
            cursorView.frame = CGRect(0.0, 0.0, 2.0, 20.0)
        }
    }
}