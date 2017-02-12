package com.yy.codex.uikit

import android.text.Layout
import com.yy.codex.foundation.NSLog

/**
 * Created by cuiminghui on 2017/2/7.
 */

internal fun UITextField.resetLayouts() {
    when (alignment) {
        Layout.Alignment.ALIGN_NORMAL -> resetNormalLayout()
        Layout.Alignment.ALIGN_OPPOSITE -> resetOppositeLayout()
        Layout.Alignment.ALIGN_CENTER -> resetCenterLayout()
    }
}

private fun UITextField.resetNormalLayout() {
    var leftViewWidth = resetLeftViewLayout()
    var rightViewWidth = resetRightViewLayout()
    wrapper.frame = CGRect(leftViewWidth + contentInsets.left, contentInsets.top, frame.width - leftViewWidth - contentInsets.left - contentInsets.right - rightViewWidth, frame.height - contentInsets.top - contentInsets.bottom)
    var textSize = label.intrinsicContentSize()
    textSize = textSize.setWidth(textSize.width + 4.0)
    val minX = -label.frame.x
    val maxX = -label.frame.x + wrapper.frame.width - 2.0
    val mostX = label.lineBounds(0)?.width ?: 0.0 + 2.0
    label.frame = CGRect(-computeMinimumX(minX, maxX, mostX), (wrapper.frame.height - textSize.height) / 2.0, textSize.width, textSize.height)
    resetCursorLayout()
}

private fun UITextField.resetOppositeLayout() {
    var leftViewWidth = resetLeftViewLayout()
    var rightViewWidth = resetRightViewLayout()
    wrapper.frame = CGRect(leftViewWidth + contentInsets.left, contentInsets.top, frame.width - leftViewWidth - contentInsets.left - contentInsets.right - rightViewWidth, frame.height - contentInsets.top - contentInsets.bottom)
    var textSize = label.intrinsicContentSize()
    textSize = textSize.setWidth(textSize.width + 4.0)
    if (textSize.width < wrapper.frame.width) {
        label.frame = CGRect(wrapper.frame.width - textSize.width, (wrapper.frame.height - textSize.height) / 2.0, textSize.width, textSize.height)
    }
    else {
        val minX = -label.frame.x
        val maxX = -label.frame.x + wrapper.frame.width - 2.0
        val mostX = label.lineBounds(0)?.width ?: 0.0 + 2.0
        label.frame = CGRect(-computeMinimumX(minX, maxX, mostX), (wrapper.frame.height - textSize.height) / 2.0, textSize.width, textSize.height)
    }
    resetCursorLayout()
}

private fun UITextField.resetCenterLayout() {
    var leftViewWidth = resetLeftViewLayout()
    var rightViewWidth = resetRightViewLayout()
    wrapper.frame = CGRect(leftViewWidth + contentInsets.left, contentInsets.top, frame.width - leftViewWidth - contentInsets.left - contentInsets.right - rightViewWidth, frame.height - contentInsets.top - contentInsets.bottom)
    var textSize = label.intrinsicContentSize()
    textSize = textSize.setWidth(textSize.width + 4.0)
    if (textSize.width < wrapper.frame.width) {
        label.frame = CGRect((wrapper.frame.width - textSize.width) / 2.0, (wrapper.frame.height - textSize.height) / 2.0, textSize.width, textSize.height)
    }
    else {
        val minX = -label.frame.x
        val maxX = -label.frame.x + wrapper.frame.width - 2.0
        val mostX = label.lineBounds(0)?.width ?: 0.0 + 2.0
        label.frame = CGRect(-computeMinimumX(minX, maxX, mostX), (wrapper.frame.height - textSize.height) / 2.0, textSize.width, textSize.height)
    }
    resetCursorLayout()
}

private fun UITextField.computeMinimumX(min: Double, max: Double, most: Double): Double {
    val textRect = label.textRect(input.cursorPosition - 1) ?: CGRect(0.0, 0.0, 0.0, 0.0)
    val target: Double = textRect.x + textRect.width
    if (target < min) {
        return Math.max(0.0, min - 22.0)
    }
    else if (target > max) {
        if (max > most) {
            return most - (max - min)
        }
        else {
            return target - (max - min)
        }
    }
    else {
        if (max > most) {
            return Math.max(0.0, most - (max - min))
        }
        return min
    }
}

private fun UITextField.resetLeftViewLayout(): Double {
    leftView?.let {
        var width = it.frame.width
        it.hidden = false
        if (leftViewMode == UITextField.ViewMode.Never) {
            width = 0.0
            it.hidden = true
        }
        else if (leftViewMode == UITextField.ViewMode.WhileEditing) {
            if (!editing) {
                width = 0.0
                it.hidden = true
            }
        }
        else if (leftViewMode == UITextField.ViewMode.UnlessEditing) {
            if (editing) {
                width = 0.0
                it.hidden = true
            }
        }
        it.frame = it.frame.setY((frame.height - it.frame.height) / 2.0)
        return width
    }
    return 0.0
}

private fun UITextField.resetRightViewLayout(): Double {
    rightView?.let {
        var width = it.frame.width
        it.hidden = false
        if (rightViewMode == UITextField.ViewMode.Never) {
            width = 0.0
            it.hidden = true
        }
        else if (rightViewMode == UITextField.ViewMode.WhileEditing) {
            if (!editing) {
                width = 0.0
                it.hidden = true
            }
            else if (it === clearButton && input.editor?.length() == 0) {
                width = 0.0
                it.hidden = true
            }
        }
        else if (rightViewMode == UITextField.ViewMode.UnlessEditing) {
            if (editing) {
                width = 0.0
                it.hidden = true
            }
        }
        it.frame = it.frame.setY((frame.height - it.frame.height) / 2.0).setX(frame.width - width)
        return width
    }
    return 0.0
}

internal fun UITextField.resetCursorLayout() {
    cursorView.frame = CGRect(0.0, 0.0, 2.0, label.frame.height)
    label.attributedText?.let {
        val substring = it.substring(NSRange(0, input.cursorPosition))
        val cursorPosition = substring.measure(999999.0)
        cursorView.frame = CGRect(Math.max(0.0, cursorPosition.width - 1.0), 0.0, 2.0, label.frame.height)
    }
}