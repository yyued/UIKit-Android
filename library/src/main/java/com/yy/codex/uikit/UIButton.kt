package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

import java.util.EnumSet
import java.util.HashMap

/**
 * Created by cuiminghui on 2017/1/16.
 */

class UIButton : UIControl {

    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    init {
        initTitleLabel()
        initImageView()
    }

    /* UIControl */

    override fun resetState() {
        super.resetState()
        resetTitleLabel()
        resetImageView()
    }

    /* UIView */

    override fun tintColorDidChanged() {
        super.tintColorDidChanged()
        resetTitleLabel()
        resetImageView()
    }

    /* Title */

    var titleLabel: UILabel? = null
        private set

    private fun initTitleLabel() {
        titleLabel = UILabel(context)
        titleLabel?.let {
            it.numberOfLines = 1
            it.linebreakMode = NSLineBreakMode.ByTruncatingMiddle
            addSubview(it)
        }
    }

    private fun resetTitleLabel() {
        titleLabel?.let {
            if (attributedTitles.size > 0) {
                it.attributedText = currentAttributedTitle()
            } else {
                val attributedString = NSAttributedString(currentTitle() ?: "", hashMapOf(
                        Pair(NSAttributedString.NSForegroundColorAttributeName, currentTitleColor() ?: UIColor.blackColor),
                        Pair(NSAttributedString.NSFontAttributeName, font)
                ))
                it.attributedText = attributedString
            }
            layoutSubviews()
        }
    }

    private val titleTexts = HashMap<EnumSet<UIControl.State>, String>()

    fun currentTitle(): String? {
        val state = state
        if (titleTexts[state] != null) {
            return titleTexts[state]
        } else if (titleTexts[EnumSet.of(UIControl.State.Normal)] != null) {
            return titleTexts[EnumSet.of(UIControl.State.Normal)]
        } else {
            return ""
        }
    }

    fun setTitle(title: String, state: UIControl.State) {
        setTitle(title, EnumSet.of(UIControl.State.Normal, state))
    }

    fun setTitle(title: String, state: EnumSet<UIControl.State>) {
        if (state.contains(UIControl.State.Selected)) {
            state.remove(UIControl.State.Normal)
        }
        titleTexts.put(state, title)
        resetTitleLabel()
    }

    private val titleColors = HashMap<EnumSet<UIControl.State>, UIColor>()

    fun currentTitleColor(): UIColor? {
        val state = state
        if (titleColors[state] != null) {
            return titleColors[state]
        } else if (titleColors[EnumSet.of(UIControl.State.Normal)] != null) {
            return titleColors[EnumSet.of(UIControl.State.Normal)]
        } else {
            if (state.contains(UIControl.State.Disabled)) {
                return UIColor.blackColor.colorWithAlpha(0.3)
            }
            if (state.contains(UIControl.State.Highlighted) && state.contains(UIControl.State.Normal)) {
                return tintColor?.colorWithAlpha(0.3) ?: tintColor
            }
            return tintColor
        }
    }

    fun setTitleColor(color: UIColor, state: UIControl.State) {
        setTitleColor(color, EnumSet.of(UIControl.State.Normal, state))
    }

    fun setTitleColor(color: UIColor, state: EnumSet<UIControl.State>) {
        if (state.contains(UIControl.State.Selected)) {
            state.remove(UIControl.State.Normal)
        }
        titleColors.put(state, color)
        resetTitleLabel()
    }

    var font = UIFont(17f)
        set(value) {
            field = value
            resetTitleLabel()
        }

    val attributedTitles = HashMap<EnumSet<UIControl.State>, NSAttributedString>()

    fun currentAttributedTitle(): NSAttributedString? {
        val state = state
        if (attributedTitles[state] != null) {
            return attributedTitles[state]
        } else if (attributedTitles[EnumSet.of(UIControl.State.Normal)] != null) {
            return attributedTitles[EnumSet.of(UIControl.State.Normal)]
        } else {
            return null
        }
    }

    fun setAttributedTitle(attributedString: NSAttributedString, state: UIControl.State) {
        setAttributedTitle(attributedString, EnumSet.of(UIControl.State.Normal, state))
    }

    fun setAttributedTitle(attributedString: NSAttributedString, state: EnumSet<UIControl.State>) {
        if (state.contains(UIControl.State.Selected)) {
            state.remove(UIControl.State.Normal)
        }
        attributedTitles.put(state, attributedString)
        resetTitleLabel()
    }

    /* ImageView */

    var imageView: UIImageView? = null
        private set

    private fun initImageView() {
        imageView = UIImageView(context)
        imageView?.let {
            addSubview(it)
        }
    }

    private fun resetImageView() {
        imageView?.let {
            val image = currentImage() ?: return
            if (image.renderingMode != UIImage.RenderingMode.AlwaysOriginal) {
                it.layer.bitmapColor = currentTitleColor()
            } else {
                it.layer.bitmapColor = null
            }
            it.image = image
            it.invalidate()
            layoutSubviews()
        }
    }

    private val images = HashMap<EnumSet<UIControl.State>, UIImage>()

    fun currentImage(): UIImage? {
        val state = state
        if (images[state] != null) {
            return images[state]
        } else if (images[EnumSet.of(UIControl.State.Normal)] != null) {
            return images[EnumSet.of(UIControl.State.Normal)]
        } else {
            return null
        }
    }

    fun setImage(image: UIImage, state: UIControl.State) {
        setImage(image, EnumSet.of(state))
    }

    fun setImage(image: UIImage, state: EnumSet<UIControl.State>) {
        if (state.contains(UIControl.State.Selected)) {
            state.remove(UIControl.State.Normal)
        }
        images.put(state, image)
        resetImageView()
    }

    /* Layouts */

    override fun intrinsicContentSize(): CGSize {
        if (titleLabel as? UILabel == null && imageView as? UIImageView == null) {
            return CGSize(.0, .0)
        }
        val titleLabel = titleLabel as UILabel
        val imageView = imageView as UIImageView
        titleLabel.maxWidth = 999999.0
        var contentWidth = titleLabel.intrinsicContentSize().width + imageView.intrinsicContentSize().width
        contentWidth += contentEdgeInsets.left + contentEdgeInsets.right + imageEdgeInsets.left + imageEdgeInsets.right + titleEdgeInsets.left + titleEdgeInsets.right
        var contentHeight = titleLabel.intrinsicContentSize().height + imageView.intrinsicContentSize().height
        contentHeight += contentEdgeInsets.top + contentEdgeInsets.bottom + Math.max(titleEdgeInsets.top + titleEdgeInsets.bottom, imageEdgeInsets.top + imageEdgeInsets.bottom)
        return CGSize(Math.max(contentWidth, 44.0), Math.max(contentHeight, 44.0))
    }

    var contentEdgeInsets = UIEdgeInsets(0.0, 0.0, 0.0, 0.0)
        set(value) {
            field = value
            layoutSubviews()
        }
    var titleEdgeInsets = UIEdgeInsets(0.0, 0.0, 0.0, 0.0)
        set(value) {
            field = value
            layoutSubviews()
        }
    var imageEdgeInsets = UIEdgeInsets(0.0, 0.0, 0.0, 0.0)
        set(value) {
            field = value
            layoutSubviews()
        }

    override fun layoutSubviews() {
        super.layoutSubviews()
        if (titleLabel as? UILabel == null && imageView as? UIImageView == null) {
            return
        }
        val titleLabel = titleLabel as UILabel
        val imageView = imageView as UIImageView
        titleLabel.maxWidth = frame.size.width - imageView.intrinsicContentSize().width - (contentEdgeInsets.left + contentEdgeInsets.right + imageEdgeInsets.left + imageEdgeInsets.right + titleEdgeInsets.left + titleEdgeInsets.right)
        var contentWidth = titleLabel.intrinsicContentSize().width + imageView.intrinsicContentSize().width
        contentWidth += contentEdgeInsets.left + contentEdgeInsets.right + imageEdgeInsets.left + imageEdgeInsets.right + titleEdgeInsets.left + titleEdgeInsets.right
        var imageViewOriginY = 0.0
        var titleLabelOriginY = 0.0
        if (contentVerticalAlignment === UIControl.ContentVerticalAlignment.Center) {
            imageViewOriginY = (frame.size.height - imageView.intrinsicContentSize().height) / 2.0
            titleLabelOriginY = (frame.size.height - titleLabel.intrinsicContentSize().height) / 2.0
            imageViewOriginY = Math.ceil(imageViewOriginY)
            titleLabelOriginY = Math.ceil(titleLabelOriginY)
        } else if (contentVerticalAlignment === UIControl.ContentVerticalAlignment.Bottom) {
            imageViewOriginY = frame.size.height - imageView.intrinsicContentSize().height
            titleLabelOriginY = frame.size.height - titleLabel.intrinsicContentSize().height
            imageViewOriginY = Math.ceil(imageViewOriginY)
            titleLabelOriginY = Math.ceil(titleLabelOriginY)
        }
        if (contentHorizontalAlignment === UIControl.ContentHorizontalAlignment.Left) {
            imageView.frame = CGRect(
                    contentEdgeInsets.left + imageEdgeInsets.left,
                    imageViewOriginY,
                    imageView.intrinsicContentSize().width,
                    imageView.intrinsicContentSize().height)
            titleLabel.frame = CGRect(
                    contentEdgeInsets.left + imageEdgeInsets.left + imageView.frame.size.width + imageEdgeInsets.right + titleEdgeInsets.left,
                    titleLabelOriginY,
                    titleLabel.intrinsicContentSize().width,
                    titleLabel.intrinsicContentSize().height)
        } else if (contentHorizontalAlignment === UIControl.ContentHorizontalAlignment.Center) {
            imageView.frame = CGRect(
                    (frame.size.width - contentWidth) / 2.0 + contentEdgeInsets.left + imageEdgeInsets.left,
                    imageViewOriginY,
                    imageView.intrinsicContentSize().width,
                    imageView.intrinsicContentSize().height)
            titleLabel.frame = CGRect(
                    (frame.size.width - contentWidth) / 2.0 + contentEdgeInsets.left + imageEdgeInsets.left + imageView.frame.size.width + imageEdgeInsets.right + titleEdgeInsets.left,
                    titleLabelOriginY,
                    titleLabel.intrinsicContentSize().width,
                    titleLabel.intrinsicContentSize().height)
        } else if (contentHorizontalAlignment === UIControl.ContentHorizontalAlignment.Right) {
            imageView.frame = CGRect(
                    frame.size.width - contentWidth + contentEdgeInsets.left + imageEdgeInsets.left,
                    imageViewOriginY,
                    imageView.intrinsicContentSize().width,
                    imageView.intrinsicContentSize().height)
            titleLabel.frame = CGRect(
                    frame.size.width - contentWidth + contentEdgeInsets.left + imageEdgeInsets.left + imageView.frame.size.width + imageEdgeInsets.right + titleEdgeInsets.left,
                    titleLabelOriginY,
                    titleLabel.intrinsicContentSize().width,
                    titleLabel.intrinsicContentSize().height)
        }
    }

}
