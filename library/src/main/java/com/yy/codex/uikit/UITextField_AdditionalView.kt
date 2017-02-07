package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/2/7.
 */

internal fun UITextField.setupPlaceholder() {
    if (input?.editor?.text?.toString()?.length == 0 && (placeholder != null || attributedPlaceholder != null)) {
        if (attributedPlaceholder != null) {
            label.attributedText = attributedPlaceholder
        }
        else {
            placeholder?.let {
                label.attributedText = NSAttributedString(it, hashMapOf(
                        Pair(NSAttributedString.NSFontAttributeName, label.font),
                        Pair(NSAttributedString.NSForegroundColorAttributeName, UIColor.blackColor.colorWithAlpha(0.3))
                ))
            }
        }
        resetLayouts()
    }
}

internal fun UITextField.removePlaceholder() {
    if ((placeholder != null || attributedPlaceholder != null) && label.text == placeholder) {
        label.text = ""
    }
}

internal fun UITextField.resetLeftView() {
    leftView?.let {
        addSubview(it)
        resetLayouts()
    }
}

internal fun UITextField.resetRightView() {
    rightView?.let {
        addSubview(it)
        resetLayouts()
    }
}