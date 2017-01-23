package com.yy.codex.uikit

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.text.Layout
import android.text.SpannedString
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

import java.util.HashMap

/**
 * Created by cuiminghui on 2017/1/4.
 */

class UILabel : UIView {

    private var mTextView: TextView? = null

    constructor(context: Context, view: View) : super(context, view) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    override fun init() {
        super.init()
        mTextView = TextView(context)
        mTextView!!.maxLines = 1
        resetTextView()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        resetTextView()
    }

    /* category: TextView Props */

    /* Font */

    var font = UIFont(17f)
        set(font) {
            field = font
            updateTextAppearance()
        }

    /* TextColor */

    var textColor = UIColor.blackColor
        set(textColor) {
            field = textColor
            updateTextAppearance()
        }

    /* Number of lines */

    var numberOfLines = 1
        set(numberOfLines) {
            var numberOfLines = numberOfLines
            field = numberOfLines
            if (numberOfLines <= 0) {
                numberOfLines = 99999
            }
            mTextView!!.maxLines = numberOfLines
        }

    /* Line-Break Mode */

    var linebreakMode = NSLineBreakMode.ByWordWrapping
        set(linebreakMode) {
            field = linebreakMode
            when (linebreakMode) {
                NSLineBreakMode.ByTruncatingHead -> mTextView!!.ellipsize = TextUtils.TruncateAt.START
                NSLineBreakMode.ByTruncatingMiddle -> mTextView!!.ellipsize = TextUtils.TruncateAt.MIDDLE
                NSLineBreakMode.ByTruncatingTail -> mTextView!!.ellipsize = TextUtils.TruncateAt.END
                else -> mTextView!!.ellipsize = null
            }
            updateTextAppearance()
        }

    var alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
        set(alignment) {
            field = alignment
            updateTextAppearance()
        }

    /* Text */

    private var mNeedsUpdate = false

    var text: String?
        get() = if (attributedText != null) attributedText!!.toString() else null
        set(text) {
            var text = text
            if (text == null) {
                text = ""
            }
            val paragraphStyle = NSParagraphStyle()
            paragraphStyle.lineBreakMode = linebreakMode
            paragraphStyle.alignment = alignment
            val attributedString = NSAttributedString(text, hashMapOf(
                    Pair(NSAttributedString.NSFontAttributeName, font),
                    Pair(NSAttributedString.NSForegroundColorAttributeName, textColor),
                    Pair(NSAttributedString.NSParagraphStyleAttributeName, paragraphStyle)
            ))
            attributedText = attributedString
            mNeedsUpdate = true
        }

    private fun updateTextAppearance() {
        if (mNeedsUpdate && text != null) {
            var text = text
            text = text
        }
    }

    var attributedText: NSAttributedString?
        get() {
            if (this.mTextView!!.text != null && SpannedString::class.java.isAssignableFrom(this.mTextView!!.text.javaClass)) {
                return NSAttributedString(this.mTextView!!.text as SpannedString)
            }
            return null
        }
        set(attributedText) {
            this.mTextView!!.text = attributedText
            resetTextViewStyles()
            if (constraint != null) {
                constraint!!.setNeedsLayout()
                val superview = superview
                superview?.layoutSubviews()
            }
        }

    private fun resetTextViewStyles() {
        // TODO: 2017/1/10 not implemented.
    }

    /* category: Layouts */

    override var maxWidth: Double
        get() = super.maxWidth
        set(maxWidth) {
            super.maxWidth = maxWidth
            mTextView!!.maxWidth = (maxWidth * UIScreen.mainScreen.scale()).toInt()
        }

    override fun intrinsicContentSize(): CGSize {
        mTextView!!.measure(0, 0)
        val width = mTextView!!.measuredWidth
        val height = mTextView!!.measuredHeight
        return CGSize(Math.ceil(width / UIScreen.mainScreen.scale()), Math.ceil(height / UIScreen.mainScreen.scale()))
    }

    private fun resetTextView() {
        removeView(mTextView)
        addView(mTextView, FrameLayout.LayoutParams((this.frame.size.width * UIScreen.mainScreen.scale()).toInt(), (this.frame.size.height * UIScreen.mainScreen.scale()).toInt()))
    }

}
