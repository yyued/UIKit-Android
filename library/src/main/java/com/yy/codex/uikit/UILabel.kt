package com.yy.codex.uikit

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.Rect
import android.os.Build
import android.support.annotation.RequiresApi
import android.text.Layout
import android.text.SpannedString
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.yy.codex.coreanimation.CAShapeLayer
import com.yy.codex.foundation.NSLog
import com.yy.codex.foundation.lets

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
            val contentWidth = if (maxWidth > 0) maxWidth else (canvas.width.toDouble() / UIScreen.mainScreen.scale())
            it.requestLayout(contentWidth, numberOfLines, linebreakMode).draw(canvas)
        }
    }

    /* category: Layouts */

    override fun intrinsicContentSize(): CGSize {
        if (attributedText == null || attributedText?.length == 0) {
            return CGSize(0.0, font.lineHeight)
        }
        attributedText?.let {
            return it.measure(maxWidth, numberOfLines, linebreakMode)
        }
        return CGSize(0.0, 0.0)
    }

    /* Text Position */

    fun textPosition(inPoint: CGPoint): Int? {
        val lastLayout = attributedText?.requestLayout(maxWidth, numberOfLines, linebreakMode)
        lastLayout?.let {
            val line = it.getLineForVertical((inPoint.y * UIScreen.mainScreen.scale()).toInt())
            if (line < it.lineCount) {
                return it.getOffsetForHorizontal(line, (inPoint.x * UIScreen.mainScreen.scale()).toFloat())
            }
        }
        return null
    }

    fun textRect(letterIndex: Int): CGRect? {
        val lastLayout = attributedText?.requestLayout(maxWidth, numberOfLines, linebreakMode)
        lastLayout?.let {
            if (letterIndex < 0) {
                return CGRect(0.0, 0.0, 0.0, font.lineHeight)
            }
            else if (letterIndex >= it.text.length) {
                return null
            }
            val lineIndex = it.getLineForOffset(letterIndex)
            val top = it.getLineTop(lineIndex) / UIScreen.mainScreen.scale()
            val left = it.getPrimaryHorizontal(letterIndex) / UIScreen.mainScreen.scale()
            val bottom = it.getLineBottom(lineIndex) / UIScreen.mainScreen.scale()
            var right = if (letterIndex + 1 < it.text.length) it.getSecondaryHorizontal(letterIndex + 1) / UIScreen.mainScreen.scale() else it.getLineWidth(lineIndex) / UIScreen.mainScreen.scale()
            if (right < left) {
                right = it.getLineWidth(lineIndex) / UIScreen.mainScreen.scale()
            }
            return CGRect(left, top, (right - left), (bottom - top))
        }
        return null
    }

    fun lineBounds(line: Int): CGRect? {
        val lastLayout = attributedText?.requestLayout(maxWidth, numberOfLines, linebreakMode)
        lastLayout?.let {
            val top = it.getLineTop(line) / UIScreen.mainScreen.scale()
            val left = it.getLineLeft(line) / UIScreen.mainScreen.scale()
            val bottom = it.getLineBottom(line) / UIScreen.mainScreen.scale()
            val right = left + it.getLineWidth(line) / UIScreen.mainScreen.scale()
            return CGRect(left, top, (right - left), (bottom - top))
        }
        return CGRect(0.0, 0.0, 0.0, 0.0)
    }

    /* Text Selector */

    internal var selectorBackgroundViews: List<UIView> = listOf()
    internal var selectorLeftHandleView: UIButton? = null
    internal var selectorRightHandleView: UIButton? = null

    private fun createLeftSelectorHandles(textHeight: Double) {
        if (selectorLeftHandleView == null) {
            selectorLeftHandleView = UIButton(context)
            selectorLeftHandleView?.wantsLayer = true
            val roundShape = CAShapeLayer()
            val roundPath = Path()
            roundPath.addCircle(5.0f, 5.0f, 5.0f, Path.Direction.CW)
            roundShape.path = roundPath
            roundShape.fillColor = tintColor
            selectorLeftHandleView?.layer?.addSubLayer(roundShape)
            val rectShape = CAShapeLayer()
            val rectPath = Path()
            rectPath.addRect(4.0f, 9.0f, 6.0f, textHeight.toFloat(), Path.Direction.CW)
            rectShape.path = rectPath
            rectShape.fillColor = tintColor
            selectorLeftHandleView?.layer?.addSubLayer(rectShape)
            selectorLeftHandleView?.frame = CGRect(0.0, 0.0, 10.0, textHeight)
        }
    }

    private fun createRightSelectorHandles(textHeight: Double) {
        if (selectorRightHandleView == null) {
            selectorRightHandleView = UIButton(context)
            selectorRightHandleView?.wantsLayer = true
            val roundShape = CAShapeLayer()
            val roundPath = Path()
            roundPath.addCircle(5.0f, (textHeight - 5.0f).toFloat(), 5.0f, Path.Direction.CW)
            roundShape.path = roundPath
            roundShape.fillColor = tintColor
            selectorRightHandleView?.layer?.addSubLayer(roundShape)
            val rectShape = CAShapeLayer()
            val rectPath = Path()
            rectPath.addRect(4.0f, 0.0f, 6.0f, (textHeight - 4.0f).toFloat(), Path.Direction.CW)
            rectShape.path = rectPath
            rectShape.fillColor = tintColor
            selectorRightHandleView?.layer?.addSubLayer(rectShape)
            selectorRightHandleView?.frame = CGRect(0.0, 0.0, 10.0, textHeight)
        }
    }

    fun selectText(range: NSRange?) {
        selectorBackgroundViews.forEach(UIView::removeFromSuperview)
        selectorLeftHandleView?.let(UIView::removeFromSuperview)
        selectorRightHandleView?.let(UIView::removeFromSuperview)
        selectorLeftHandleView = null
        selectorRightHandleView = null
        if (range == null || range?.length == 0) {
            return
        }
        val lastLayout = attributedText?.requestLayout(maxWidth, numberOfLines, linebreakMode)
        lets(range, lastLayout) { range, lastLayout ->
            var startLine = lastLayout.getLineForOffset(range.location)
            var endLine = lastLayout.getLineForOffset(range.location + range.length - 1)
            selectorBackgroundViews = (startLine..endLine).map {
                var x = 0.0
                var width = 0.0
                var y = lastLayout.getLineTop(it) / UIScreen.mainScreen.scale()
                var height = (lastLayout.getLineBottom(it) - lastLayout.getLineTop(it)).toDouble() / UIScreen.mainScreen.scale()
                var lineStartLetterIndex = lastLayout.getOffsetForHorizontal(it, 0.0f)
                var lineEndLetterIndex = lastLayout.getOffsetForHorizontal(it, lastLayout.getLineWidth(it))
                if (lineStartLetterIndex < range.location) {
                    x = textRect(range.location)?.x ?: 0.0
                }
                if (range.location + range.length - 1 < lineEndLetterIndex) {
                    textRect(range.location + range.length - 1)?.let {
                        width = it.x + it.width - x
                    }
                }
                else {
                    width = lastLayout.getLineWidth(it) / UIScreen.mainScreen.scale() - x
                }
                val view = UIView(context)
                view.frame = CGRect(x, y, width, height)
                view.setBackgroundColor(tintColor?.colorWithAlpha(0.30) ?: UIColor(0x14 / 255.0, 0x6D / 255.0, 0xDE / 255.0, 0.22))
                return@map view
            }
            selectorBackgroundViews.forEach {
                insertSubview(it, 0)
            }
        }
        textRect(range.location)?.let {
            val x = Math.max(0.0, it.x - 5.0)
            val y = it.y - 10.0
            val height = it.height + 10.0
            createLeftSelectorHandles(height)
            selectorLeftHandleView?.let {
                it.frame = CGRect(if (x <= 4.0) -4.0 else x, y, 10.0, height)
                addSubview(it)
            }
        }
        textRect(range.location + range.length - 1)?.let {
            val x = Math.max(0.0, it.x + it.width - 5.0)
            val y = it.y
            val height = it.height + 10.0
            createRightSelectorHandles(height)
            selectorRightHandleView?.let {
                it.frame = CGRect(if (x <= 4.0) -4.0 else x, y, 10.0, height)
                addSubview(it)
            }
        }
    }

}
