package com.yy.codex.uikit

import java.util.*

/**
 * Created by cuiminghui on 2017/2/8.
 */

internal fun UITextView.resetText(onDelete: Boolean) {
    val text = input.editor?.text.toString()
    label.text = text
    defaultTextAttributes?.let {
        label.attributedText = NSAttributedString(text, HashMap(it))
    }
//    resetCharPositions()
    resetLayouts()
}