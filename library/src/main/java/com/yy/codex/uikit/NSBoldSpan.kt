package com.yy.codex.uikit

import android.text.TextPaint
import android.text.style.CharacterStyle

internal class NSBoldSpan : CharacterStyle() {

    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.isFakeBoldText = true
    }

}