package com.yy.codex.uikit

import android.content.Context
import android.graphics.Matrix
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.yy.codex.coreanimation.CAShapeLayer
import java.util.*

/**
 * Created by adi on 17/2/7.
 */

class UISegmentedItem(val title: String) {

    var enabled = true

}

class UISegmentedControl : UIControl {

    constructor(context: Context, view: View) : super(context, view)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun init() {
        super.init()
        contentView = UIView(context)
        contentView.constraint = UIConstraint.full()
        addSubview(contentView)
        initBorderView()
        addSubview(borderView)
    }

    override fun intrinsicContentSize(): CGSize {
        return CGSize(0.0, 28.0);
    }

    override fun tintColorDidChanged() {
        super.tintColorDidChanged()
        borderView.layer.borderColor = tintColor ?: UIColor.clearColor
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        resetItemsView()
    }

    /* public */

    var items: List<UISegmentedItem> = listOf()
        set(value) {
            field = value
            selectedIndex = 0
            resetItemsView()
        }

    var selectedColor: UIColor = UIColor.whiteColor

    var selectedIndex = 0
        set(value) {
            field = value
            if (field < contentButtons.count()) {
                contentButtons.forEach { it.select = false }
                contentButtons[field].select = true
            }
        }

    /* props */

    lateinit private var contentView: UIView
    lateinit private var borderView: UIView
    private var contentButtons: List<UIButton> = listOf()

    private fun initBorderView() {
        borderView = UIView(context)
        borderView.constraint = UIConstraint.full()
        borderView.userInteractionEnabled = false
        borderView.wantsLayer = true
        borderView.layer.borderWidth = 1.0
        borderView.layer.borderColor = tintColor ?: UIColor.clearColor
        borderView.layer.cornerRadius = 6.0
    }

    private fun createLeftMask(width: Float): Path {
        val maskPath = Path()
        maskPath.moveTo(width, 0.0f)
        maskPath.cubicTo(width, 0.0f, width, 2.5f, width, 6f)
        maskPath.cubicTo(width, 10.72f, width, 17.28f, width, 22.0f)
        maskPath.cubicTo(width, 25.5f, width, 28.0f, width, 28.0f)
        maskPath.lineTo(6.45f, 28.0f)
        maskPath.cubicTo(2.89f, 28.0f, 0.0f, 25.31f, 0.0f, 22.0f)
        maskPath.lineTo(0.0f, 6.0f)
        maskPath.cubicTo(0.0f, 2.69f, 2.89f, 0.0f, 6.45f, 0.0f)
        maskPath.lineTo(width, 0.0f)
        maskPath.close()
        return maskPath
    }

    private fun createRightMask(width: Float): Path {
        val maskPath = Path()
        maskPath.moveTo(0.0f, 0.0f)
        maskPath.cubicTo(0.0f, 0.0f, 0.0f, 2.5f, 0.0f, 6f)
        maskPath.cubicTo(0.0f, 10.72f, 0.0f, 17.28f, 0.0f, 22.0f)
        maskPath.cubicTo(0.0f, 25.5f, 0.0f, 28.0f, 0.0f, 28.0f)
        maskPath.lineTo(width - 6.45f, 28.0f)
        maskPath.cubicTo(width - 2.89f, 28.0f, width, 25.31f, width, 22.0f)
        maskPath.lineTo(width, 6.0f)
        maskPath.cubicTo(width, 2.69f, width - 2.89f, 0.0f, width - 6.45f, 0.0f)
        maskPath.lineTo(0.0f, 0.0f)
        maskPath.close()
        return maskPath
    }

    private fun resetItemsView() {
        contentView.subviews.forEach(UIView::removeFromSuperview)
        val buttons = items.mapIndexed { idx, it ->
            val button = UIButton(context)
            button.tag = idx
            button.addTarget(this, "onItemButtonTouchUpInside:", Event.TouchUpInside)
            button.wantsLayer = true
            if (idx == 0) {
                val maskLayer = CAShapeLayer()
                maskLayer.frame = CGRect(0.0, 0.0, frame.width / items.count() + 1, frame.height)
                maskLayer.path = createLeftMask((frame.width / items.count() + 1).toFloat())
                maskLayer.fillColor = UIColor.blackColor
                button.layer.mask = maskLayer
                button.layer.clipToBounds = true
            }
            else if (idx == items.count() - 1) {
                val maskLayer = CAShapeLayer()
                maskLayer.frame = CGRect(0.0, 0.0, frame.width / items.count() + 1, frame.height)
                maskLayer.path = createRightMask((frame.width / items.count() + 1).toFloat())
                maskLayer.fillColor = UIColor.blackColor
                button.layer.mask = maskLayer
                button.layer.clipToBounds = true
            }
            button.constraint = UIConstraint.horizonStack(idx, items.count())
            button.constraint?.width = button.constraint?.width + " + 2"
            button.setTitle(it.title, State.Normal)
            button.setTitleColor(tintColor ?: UIColor.clearColor, State.Normal)
            button.setTitleColor(tintColor ?: UIColor.clearColor, EnumSet.of(State.Normal, State.Highlighted))
            button.setTitleColor(selectedColor, State.Selected)
            button.setTitleColor(selectedColor, EnumSet.of(State.Selected, State.Highlighted))
            button.setBackgroundColor(UIColor.clearColor, State.Normal)
            button.setBackgroundColor((tintColor ?: UIColor.clearColor).colorWithAlpha(0.25), EnumSet.of(State.Normal, State.Highlighted))
            button.setBackgroundColor((tintColor ?: UIColor.clearColor).colorWithAlpha(0.75), EnumSet.of(State.Selected, State.Highlighted))
            button.setBackgroundColor(tintColor ?: UIColor.clearColor, State.Selected)
            button.select = idx == selectedIndex
            if (idx > 0) {
                val line = UIPixelLine(context)
                line.vertical = true
                line.frame = CGRect(0.0,0.0,1.0,28.0)
                line.color = tintColor ?: UIColor.clearColor
                button.addSubview(line)
            }
            return@mapIndexed button
        }
        buttons.forEach { contentView.addSubview(it) }
        contentButtons = buttons
    }

    private fun onItemButtonTouchUpInside(sender: UIButton) {
        (sender.tag as? Int)?.let {
            selectedIndex = it
        }
    }

}