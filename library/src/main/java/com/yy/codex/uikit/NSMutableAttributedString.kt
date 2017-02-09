package com.yy.codex.uikit

import android.text.BoringLayout
import android.text.DynamicLayout
import android.text.Layout
import android.text.StaticLayout
import java.util.HashMap

/**
 * Created by cuiminghui on 2017/1/9.
 */

class NSMutableAttributedString : NSAttributedString {

    constructor(text: String, attributes: HashMap<String, Any>) : super(text, attributes) {}

    constructor(attributedString: NSAttributedString) : super(attributedString) {}

    fun copy(): NSAttributedString {
        return NSAttributedString(this)
    }

    fun setAttributes(attrs: HashMap<String, Any>, range: NSRange) {
        boringLayoutCache.clear()
        layoutCache.clear()
        reset(attrs, range)
    }

    fun addAttribute(name: String, value: Any, range: NSRange) {
        boringLayoutCache.clear()
        layoutCache.clear()
        addAttributes(object : HashMap<String, Any>() {
            init {
                put(name, value)
            }
        }, range)
    }

    fun addAttributes(attrs: HashMap<String, Any>, range: NSRange) {
        boringLayoutCache.clear()
        layoutCache.clear()
        for (i in 0..range.length - 1) {
            val values = getAttributes(range.location + i) ?: HashMap<String, Any>()
            values.putAll(attrs)
            setAttributes(values, NSRange(range.location + i, 1))
        }
    }

    fun removeAttribute(name: String, range: NSRange) {
        boringLayoutCache.clear()
        layoutCache.clear()
        for (i in 0..range.length - 1) {
            val values = getAttributes(range.location + i) ?: HashMap<String, Any>()
            values.remove(name)
            setAttributes(values, NSRange(range.location + i, 1))
        }
    }

    fun replaceCharacters(inRange: NSRange, attributedString: NSAttributedString) {
        boringLayoutCache.clear()
        layoutCache.clear()
        delete(inRange.location, inRange.location + inRange.length)
        insert(inRange.location, attributedString)
    }

    fun insertAttributedString(attributedString: NSAttributedString, atIndex: Int) {
        boringLayoutCache.clear()
        layoutCache.clear()
        insert(atIndex, attributedString)
    }

    fun appendAttributedString(attributedString: NSAttributedString) {
        boringLayoutCache.clear()
        layoutCache.clear()
        append(attributedString)
    }

    fun deleteCharacters(inRange: NSRange) {
        boringLayoutCache.clear()
        layoutCache.clear()
        delete(inRange.location, inRange.location + inRange.length)
    }

    fun setAttributedString(attributedString: NSAttributedString) {
        boringLayoutCache.clear()
        layoutCache.clear()
        replace(0, length, attributedString)
    }

}
