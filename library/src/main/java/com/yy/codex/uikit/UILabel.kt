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

    /* XML */

    override fun prepareProps(attrs: AttributeSet) {
        super.prepareProps(attrs)
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.UILabel, 0, 0)
        typedArray.getFloat(R.styleable.UILabel_fontSize, -1.0f)?.let {
            if (it != -1.0f) {
                initializeAttributes["UILabel.fontSize"] = it
            }
        }
        typedArray.getColor(R.styleable.UILabel_textColor, -1)?.let {
            if (it != -1) {
                initializeAttributes["UILabel.textColor"] = UIColor(it)
            }
        }
        typedArray.getInt(R.styleable.UILabel_numberOfLines, -1)?.let {
            if (it != -1) {
                initializeAttributes["UILabel.numberOfLines"] = it
            }
        }
        typedArray.getInt(R.styleable.UILabel_linebreakMode, -1)?.let {
            if (it != -1) {
                initializeAttributes["UILabel.linebreakMode"] = it
            }
        }
        typedArray.getInt(R.styleable.UILabel_alignment, -1)?.let {
            if (it != -1) {
                initializeAttributes["UILabel.alignment"] = it
            }
        }
        typedArray.getString(R.styleable.UILabel_text)?.let {
            initializeAttributes["UILabel.text"] = it
        }
        typedArray.recycle()
    }

    override fun resetProps() {
        super.resetProps()
        (initializeAttributes["UILabel.fontSize"] as? Float)?.let {
            font = UIFont(it)
        }
        (initializeAttributes["UILabel.textColor"] as? UIColor)?.let {
            textColor = it
        }
        (initializeAttributes["UILabel.numberOfLines"] as? Int)?.let {
            numberOfLines = it
        }
        (initializeAttributes["UILabel.linebreakMode"] as? Int)?.let {
            when (it) {
                0 -> linebreakMode = NSLineBreakMode.ByWordWrapping
                1 -> linebreakMode = NSLineBreakMode.ByTruncatingHead
                2 -> linebreakMode = NSLineBreakMode.ByTruncatingMiddle
                3 -> linebreakMode = NSLineBreakMode.ByTruncatingTail
            }
        }
        (initializeAttributes["UILabel.alignment"] as? Int)?.let {
            when (it) {
                0 -> alignment = Layout.Alignment.ALIGN_NORMAL
                1 -> alignment = Layout.Alignment.ALIGN_CENTER
                2 -> alignment = Layout.Alignment.ALIGN_OPPOSITE
            }
        }
        (initializeAttributes["UILabel.text"] as? String)?.let {
            text = it
        }
    }

    /* Layout */

    override fun layoutSubviews() {
        super.layoutSubviews()
        resetTextView()
    }

    /* category: TextView Props */

    /* Font */

    var font = UIFont(17f)
        set(font) {
            field = font
            resetAttributedText()
        }

    /* TextColor */

    var textColor = UIColor.blackColor
        set(textColor) {
            field = textColor
            resetAttributedText()
        }

    /* Number of lines */

    var numberOfLines = 1
        set(numberOfLines) {
            var numberOfLines = numberOfLines
            field = numberOfLines
            if (numberOfLines <= 0) {
                numberOfLines = 99999
            }
            textView?.let { it.maxLines = numberOfLines }
        }

    /* Line-Break Mode */

    var linebreakMode = NSLineBreakMode.ByWordWrapping
        set(linebreakMode) {
            field = linebreakMode
            when (linebreakMode) {
                NSLineBreakMode.ByTruncatingHead -> textView?.ellipsize = TextUtils.TruncateAt.START
                NSLineBreakMode.ByTruncatingMiddle -> textView?.ellipsize = TextUtils.TruncateAt.MIDDLE
                NSLineBreakMode.ByTruncatingTail -> textView?.ellipsize = TextUtils.TruncateAt.END
                else -> textView?.ellipsize = null
            }
            resetAttributedText()
        }

    var alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
        set(alignment) {
            field = alignment
            resetAttributedText()
        }

    /* Text */

    var text: String? = null
        set(value) {
            field = value
            resetAttributedText()
        }

    private fun resetAttributedText() {
        val text = text ?: ""
        val paragraphStyle = NSParagraphStyle()
        paragraphStyle.lineBreakMode = linebreakMode
        paragraphStyle.alignment = alignment
        val attributedString = NSAttributedString(text, hashMapOf(
                Pair(NSAttributedString.NSFontAttributeName, font),
                Pair(NSAttributedString.NSForegroundColorAttributeName, textColor),
                Pair(NSAttributedString.NSParagraphStyleAttributeName, paragraphStyle)
        ))
        attributedText = attributedString
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
            textView?.maxWidth = (maxWidth * UIScreen.mainScreen.scale()).toInt()
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
