package com.yy.codex.uikit

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.*
import android.text.style.*
import java.util.*

/**
 * Created by PonyCui_Home on 2017/1/4.
 */

open class NSAttributedString : SpannableStringBuilder {

    constructor(text: String) : super(text) {
        this.reset(HashMap<String, Any>(), NSRange(0, text.length))
    }

    constructor(text: String, attributes: HashMap<String, Any>) : super(text) {
        this.reset(attributes, NSRange(0, text.length))
    }

    constructor(attributedString: NSAttributedString) : super(attributedString) {}

    constructor(spannableString: SpannedString) : super(spannableString) {}

    fun measure(context: Context, maxWidth: Double): CGSize {
        if (measureLabel == null) {
            measureLabel = UILabel(context)
        }
        measureLabel?.attributedText = this
        measureLabel?.maxWidth = maxWidth
        return measureLabel?.intrinsicContentSize() ?: CGSize(0.0, 0.0)
    }

    fun mutableCopy(): NSMutableAttributedString {
        return NSMutableAttributedString(this)
    }

    fun getAttribute(attrName: String, atIndex: Int): Any? {
        val objects = getSpans(atIndex, 1, NSAttributedSpan::class.java)
        if (objects.size > 0) {
            return objects[0].mAttrs[attrName]
        } else {
            return null
        }
    }

    fun getAttributes(atIndex: Int): HashMap<String, Any>? {
        if (atIndex >= length) {
            return null
        }
        val objects = getSpans(atIndex, 1, NSAttributedSpan::class.java)
        if (objects.size > 0) {
            return objects[0].mAttrs
        } else {
            return null
        }
    }

    fun substring(range: NSRange): NSAttributedString {
        val mutableString = this.mutableCopy()
        mutableString.delete(range.location + range.length, this.length)
        mutableString.delete(0, range.location)
        return mutableString.copy()
    }

    protected fun reset(attrs: HashMap<String, Any>, range: NSRange) {
        if (range.length <= 0) {
            return
        }
        setSpan(NSAttributedSpan(attrs), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        if (attrs[NSFontAttributeName] != null && attrs[NSFontAttributeName] is UIFont) {
            val font = attrs[NSFontAttributeName] as UIFont
            setSpan(TypefaceSpan(font.fontFamily), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(AbsoluteSizeSpan(font.fontSize.toInt(), true), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            font.fontFamily?.let {
                if (it.equals("SystemBold", ignoreCase = true)) {
                    setSpan(NSBoldSpan(), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }
        if (attrs[NSParagraphStyleAttributeName] != null && attrs[NSParagraphStyleAttributeName] is NSParagraphStyle) {
            val style = attrs[NSParagraphStyleAttributeName] as NSParagraphStyle
            if (style.alignment != Layout.Alignment.ALIGN_NORMAL) {
                setSpan(AlignmentSpan.Standard(style.alignment), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        if (attrs[NSForegroundColorAttributeName] != null && attrs[NSForegroundColorAttributeName] is UIColor) {
            setSpan(ForegroundColorSpan((attrs[NSForegroundColorAttributeName] as UIColor).toInt()), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else {
            setSpan(ForegroundColorSpan(Color.BLACK), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        if (attrs[NSBackgroundColorAttributeName] != null && attrs[NSBackgroundColorAttributeName] is UIColor) {
            setSpan(BackgroundColorSpan((attrs[NSBackgroundColorAttributeName] as UIColor).toInt()), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        if (attrs[NSKernAttributeName] != null && attrs[NSKernAttributeName] is Number) {
            if (attrs[NSKernAttributeName] as Float != 0f) {
                setSpan(object : CharacterStyle() {
                    override fun updateDrawState(textPaint: TextPaint) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textPaint.letterSpacing = attrs[NSKernAttributeName] as Float
                        }
                    }
                }, range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        if (attrs[NSUnderlineStyleAttributeName] != null && attrs[NSUnderlineStyleAttributeName] is Number) {
            if (attrs[NSUnderlineStyleAttributeName] as Int == 1) {
                setSpan(UnderlineSpan(), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        if (attrs[NSStrikethroughStyleAttributeName] != null && attrs[NSStrikethroughStyleAttributeName] is Number) {
            if (attrs[NSStrikethroughStyleAttributeName] as Int == 1) {
                setSpan(StrikethroughSpan(), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        if (attrs[NSShadowAttributeName] != null && attrs[NSShadowAttributeName] is NSShadow) {
            val shadow = attrs[NSShadowAttributeName] as NSShadow
            setSpan(object : CharacterStyle() {
                override fun updateDrawState(textPaint: TextPaint) {
                    textPaint.setShadowLayer(shadow.shadowBlurRadius.toFloat(), shadow.shadowOffset.width.toFloat(), shadow.shadowOffset.height.toFloat(), shadow.shadowColor)
                }
            }, range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    companion object {
        private var measureLabel: UILabel? = null
        var NSFontAttributeName = "NSFontAttributeName" // NSFont, default System 17
        var NSParagraphStyleAttributeName = "NSParagraphStyleAttributeName" // NSParagraphStyle, default nil
        var NSForegroundColorAttributeName = "NSForegroundColorAttributeName" // int, default Color.BLACK
        var NSBackgroundColorAttributeName = "NSBackgroundColorAttributeName" // int, default Color.TRANSPARENT: no background
        var NSKernAttributeName = "NSKernAttributeName" // double containing floating point value, in points; amount to modify default kerning. 0 means kerning is disabled.
        var NSStrikethroughStyleAttributeName = "NSStrikethroughStyleAttributeName" // int containing integer, default 0: no strikethrough
        var NSUnderlineStyleAttributeName = "NSUnderlineStyleAttributeName" // int containing integer, default 0: no underline
        var NSStrokeColorAttributeName = "NSStrokeColorAttributeName"// TODO: 2017/1/9 not implemented.
        var NSStrokeWidthAttributeName = "NSStrokeWidthAttributeName"// TODO: 2017/1/9 not implemented.
        var NSShadowAttributeName = "NSShadowAttributeName" // NSShadow, default nil: no shadow
        var NSAttachmentAttributeName = "NSAttachmentAttributeName"// TODO: 2017/1/9 not implemented.
        var NSLinkAttributeName = "NSLinkAttributeName"// TODO: 2017/1/9 not implemented.
        var NSBaselineOffsetAttributeName = "NSBaselineOffsetAttributeName"// TODO: 2017/1/9 not implemented.
        var NSUnderlineColorAttributeName = "NSUnderlineColorAttributeName"// TODO: 2017/1/9 not implemented.
        var NSStrikethroughColorAttributeName = "NSStrikethroughColorAttributeName"// TODO: 2017/1/9 not implemented.
    }

}

