package com.yy.codex.uikit

import android.content.Context
import android.graphics.Canvas
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
import com.yy.codex.foundation.NSLog

import java.util.HashMap

/**
 * Created by cuiminghui on 2017/1/4.
 */

class UILabel : UIView {

    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    init {
        userInteractionEnabled = false
    }

    /* XML */

    override fun prepareProps(attrs: AttributeSet) {
        super.prepareProps(attrs)
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.UILabel, 0, 0)
        typedArray.getFloat(R.styleable.UILabel_label_fontSize, -1.0f)?.let {
            if (it != -1.0f) {
                initializeAttributes["UILabel.fontSize"] = it
            }
        }
        typedArray.getColor(R.styleable.UILabel_label_textColor, -1)?.let {
            if (it != -1) {
                initializeAttributes["UILabel.textColor"] = UIColor(it)
            }
        }
        typedArray.getInt(R.styleable.UILabel_label_numberOfLines, -1)?.let {
            if (it != -1) {
                initializeAttributes["UILabel.numberOfLines"] = it
            }
        }
        typedArray.getInt(R.styleable.UILabel_label_linebreakMode, -1)?.let {
            if (it != -1) {
                initializeAttributes["UILabel.linebreakMode"] = it
            }
        }
        typedArray.getInt(R.styleable.UILabel_label_alignment, -1)?.let {
            if (it != -1) {
                initializeAttributes["UILabel.alignment"] = it
            }
        }
        typedArray.getString(R.styleable.UILabel_label_text)?.let {
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

    /* category: TextView Props */

    /* Font */

    var font = UIFont(17f)
        set(value) {
            field = value
            resetAttributedText()
        }

    /* TextColor */

    var textColor = UIColor.blackColor
        set(value) {
            field = value
            resetAttributedText()
        }

    /* Number of lines */

    var numberOfLines: Int = 1
        set(value) {
            field = value
            resetAttributedText()
        }

    /* Line-Break Mode */

    var linebreakMode = NSLineBreakMode.ByWordWrapping
        set(value) {
            field = value
            resetAttributedText()
        }

    var alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
        set(value) {
            field = value
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
        set(value) {
            field = value
            invalidate()
            constraint?.setNeedsLayout()
            superview?.layoutIfNeeded()
        }

    override fun drawRect(canvas: Canvas, rect: CGRect) {
        super.drawRect(canvas, rect)
        attributedText?.let {
            val contentWidth = if (maxWidth >= 0) maxWidth else canvas.width.toDouble() / UIScreen.mainScreen.scale()
            it.requestLayout(contentWidth, numberOfLines, linebreakMode).draw(canvas)
        }
    }

    /* category: Layouts */

    override fun intrinsicContentSize(): CGSize {
        attributedText?.let {
            return it.measure(maxWidth, numberOfLines, linebreakMode)
        }
        return CGSize(0.0, 0.0)
    }

}
