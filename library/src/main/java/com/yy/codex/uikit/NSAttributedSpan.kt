package com.yy.codex.uikit

import android.text.TextPaint
import android.text.style.CharacterStyle
import java.util.*

internal class NSAttributedSpan(var mAttrs: HashMap<String, Any>) : CharacterStyle() {

    override fun updateDrawState(textPaint: TextPaint) {}

}