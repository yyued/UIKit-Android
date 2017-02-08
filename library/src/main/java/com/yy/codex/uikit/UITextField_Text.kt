package com.yy.codex.uikit

import java.util.*

/**
 * Created by cuiminghui on 2017/2/7.
 */

internal fun UITextField.resetText(onDelete: Boolean) {
    val text = replaceSecure(input.editor?.text.toString(), onDelete)
    label.text = text
    defaultTextAttributes?.let {
        label.attributedText = NSAttributedString(text, HashMap(it))
    }
    resetCharPositions()
    resetLayouts()
}

internal fun UITextField.resetClearView() {
    if (clearButtonMode == UITextField.ViewMode.Never) {
        return
    }
    if (clearButton == null) {
        val clearButton = UIButton(context)
        clearButton.frame = CGRect(0.0, 0.0, 28.0, 44.0)
        clearButton.setImage(UIImage("iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAAAAXNSR0IArs4c6QAAA/tJREFUWAnNmU1rE0EYx7uraAJeveQQ8SQafCFJTy1U8OwKgoJFFPwECnqsB3sqCpIvUFC0PQhC8wVUiCeb4gut6KW0h168ColiE///zUyY2czs7G5e2oFldp55nv/zy+zs7O7EmxqirK+vn/c8L+h2u+cgU+CBNusp2PZQhQdsW2jXq9XqN/ZlKV7aIAF3D4mvIfZ0yvhtQK8hdjktdGJQABYBtYhEt5HITwmouUOjA41XMC4AeFfrtDScoJubm8dardYTiN+H+HGLTlZzG4G1fD7/uFQq/Y0TiQXd2Ng42el03kJgNk5kBH0N3/evl8vlXzYtK2iz2byAoDpG8ZQteJR2XLEd6AWVSuWrSdcIKiA/AvKEKWhcNsD+hvaMCXYAlJcbgJ8mNZLRH82RxTEdnQba3csbh3PyoCAJzdxkIIv6IzTQdru9iM5x3zhqftv5LFcatbN/6cU6+QOdOdXhAM+5dJ2R66w6ohxNF+Q+5s8zHFdZw38/xQ9JG0sWMoUlHFHxWPyM+aGCS59+TTjckY+kAXE3cL6C46i0Wep/sM9jdN7IfqwsT5HvoWybauTjE+wSH7chGAx8dsdCCqEPqqBIPA8bQWxlAFI4alqmYDKRjX0hHAx8wUhS5qJODlgbJGUGtKLabEs2j4s7Gl9MTgYb5+Qt9RJKH8M0sEIK31XEHpHxcTVG9SKHNohzivRReEUk0roiI+uC5LxOBMkkZPSxuJ7VMrobvHFcsNqNIyUNoy67Ymsyegh+B6/LsZ7mTuuomdyzQgqt97yZwk8Hk7jDZh3ZaNyQkJQrcI5mBaWAE3YEkJyjhXB5YsbDXnwsTfxSzFqc8zSyGmTKQ0aOaFZQJ6SkGgFsZlArJB4gN8W8lJxhPSTsHr6p/O+aorthhSQgLtNrSLjWWWokLriZtjhH64kjeq91SRZz62qgjGziV0Qy+uJDajsJLH7Zc5FIc7csQbGw1NJE7I1t5Oy/5q3Z/bSegVczC6QMssLCYUBLBqk1flDIFq6jGNplGDqqg+VcezVzQEoJG6ymJZ3Vmkxko82THUj6Aud3ZNtS83OCl4yjMQeRB6iTvgVliX2Jy36XLCpoERA/kXzU+0vMk6WYP+5Avgu1WhbFMcXUBFMorz3rc7ncAqyNMSVOI9vgDp8aoIFy64+7apgCO6rTJM+ZmwzRbUgNlEBizydAADesJlpEziC670SIAVAaxUNgZpIjK3IZd/KsoBIWwdM4n8ScbTCXabuRLCzGEe119aYBJvUVtJcg9EfaR1hzCVpiDtPlVvP011HVaDrHA6EI++H9syEKDWD+t3R4/76JArPNXRZAB2JvgB+Jxj/E+M6LJ149bg6a9FXbf9to2cX68ikYAAAAAElFTkSuQmCC", 3.0), UIControl.State.Normal)
        clearButton.contentEdgeInsets = UIEdgeInsets(0.0, -7.0, 0.0, 0.0)
        clearButton.tintColor = UIColor(0xffcccccc.toInt())
        clearButton.addTarget(input, "clear", UIControl.Event.TouchUpInside)
        this.clearButton = clearButton
    }
    rightView = clearButton
    rightViewMode = clearButtonMode
}

private fun UITextField.replaceSecure(text: String, onDelete: Boolean): String {
    if (secureTextEntry) {
        var text = text
        if (onDelete) {
            text = text.replaceRange(0, text.length, ((0 until text.length).map { "●" }).joinToString(""))
        }
        else if (text.length > 1) {
            text = text.replaceRange(0, text.length - 1, ((0 until text.length-1).map { "●" }).joinToString(""))
        }
        return text
    }
    return text
}