package com.yy.codex.uikit

import android.text.Layout
import com.yy.codex.foundation.NSLog

/**
 * Created by cuiminghui on 2017/2/8.
 */

internal fun UITextView.resetLayouts() {
    label.maxWidth = frame.width - contentInsets.left - contentInsets.right
    var textSize = label.intrinsicContentSize()
    label.frame = CGRect(contentInsets.left, contentInsets.top, Math.min(textSize.width, frame.width - contentInsets.left - contentInsets.right), textSize.height)
    contentSize = CGSize(0.0, contentInsets.top + textSize.height + contentInsets.bottom)
}