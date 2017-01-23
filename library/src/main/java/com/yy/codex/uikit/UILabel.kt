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

    var textView: TextView? = null
        private set

    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    init {
        textView = TextView(context)
        textView?.let { it.maxLines = 1 }
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
            textView!!.maxLines = numberOfLines
        }

    /* Line-Break Mode */

    var linebreakMode = NSLineBreakMode.ByWordWrapping
        set(linebreakMode) {
            field = linebreakMode
            when (linebreakMode) {
                NSLineBreakMode.ByTruncatingHead -> textView!!.ellipsize = TextUtils.TruncateAt.START
                NSLineBreakMode.ByTruncatingMiddle -> textView!!.ellipsize = TextUtils.TruncateAt.MIDDLE
                NSLineBreakMode.ByTruncatingTail -> textView!!.ellipsize = TextUtils.TruncateAt.END
                else -> textView!!.ellipsize = null
            }
            updateTextAppearance()
        }

    var alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
        set(alignment) {
            field = alignment
            updateTextAppearance()
        }

    /* Text */

    private var needsUpdate = false

    var text: String? = null
        get() = if (attributedText != null) attributedText!!.toString() else null
        set(value) {
            field = value
            val text = value ?: ""
            val paragraphStyle = NSParagraphStyle()
            paragraphStyle.lineBreakMode = linebreakMode
            paragraphStyle.alignment = alignment
            val attributedString = NSAttributedString(text, hashMapOf(
                    Pair(NSAttributedString.NSFontAttributeName, font),
                    Pair(NSAttributedString.NSForegroundColorAttributeName, textColor),
                    Pair(NSAttributedString.NSParagraphStyleAttributeName, paragraphStyle)
            ))
            attributedText = attributedString
            needsUpdate = true
        }

    private fun updateTextAppearance() {
        if (needsUpdate && text != null) {
            this.text = text
        }
    }

    var attributedText: NSAttributedString? = null
        get() {
            val text = textView?.text as? SpannedString
            if (text != null) {
                return NSAttributedString(text)
            }
            return null
        }
        set(attributedText) {
            field = attributedText
            textView?.let {
                it.text = attributedText
                resetTextViewStyles()
                constraint?.let(UIConstraint::setNeedsLayout)
                superview?.let(UIView::layoutSubviews)
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
            textView!!.maxWidth = (maxWidth * UIScreen.mainScreen.scale()).toInt()
        }

    override fun intrinsicContentSize(): CGSize {
        val textView = textView ?: return CGSize(.0, .0)
        textView.measure(0, 0)
        val width = textView.measuredWidth
        val height = textView.measuredHeight
        return CGSize(Math.ceil(width / UIScreen.mainScreen.scale()), Math.ceil(height / UIScreen.mainScreen.scale()))
    }

    private fun resetTextView() {
        textView?.let {
            removeView(textView)
            addView(textView, FrameLayout.LayoutParams((this.frame.size.width * UIScreen.mainScreen.scale()).toInt(), (this.frame.size.height * UIScreen.mainScreen.scale()).toInt()))
        }
    }

}
