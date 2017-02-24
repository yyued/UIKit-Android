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
    label.textRect(input.cursorPosition - 1)?.let {
        cursorView.frame = CGRect(it.x + it.width, it.y, 2.0, it.height)
        scrollToVisible(CGRect(cursorView.frame.x - 1.0, cursorView.frame.y + cursorView.frame.height - 1.0, 1.0, 1.0), true)
    }
}