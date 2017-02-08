package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import java.util.*

/**
 * Created by cuiminghui on 2017/1/16.
 */

open class UIButton : UIControl {

    /* Props */

    lateinit var titleLabel: UILabel
        private set
    private var titles = HashMap<EnumSet<UIControl.State>, String>()
    private var titleColors = HashMap<EnumSet<UIControl.State>, UIColor>()
    private var attributedTitles = HashMap<EnumSet<UIControl.State>, NSAttributedString>()
    var font = UIFont(17f)
        set(value) {
            field = value
            resetTitleLabel()
        }
    lateinit var imageView: UIImageView
        private set
    private var images = HashMap<EnumSet<UIControl.State>, UIImage>()
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

    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    override fun init() {
        super.init()
        initTitleLabel()
        initImageView()
    }

    override fun prepareProps(attrs: AttributeSet) {
        super.prepareProps(attrs)
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.UIButton, 0, 0)
        typedArray.getString(R.styleable.UIButton_button_title)?.let {
            initializeAttributes.put("UIButton.title", it)
        }
        typedArray.getResourceId(R.styleable.UIButton_button_image, -1)?.let {
            if (it != -1) {
                initializeAttributes.put("UIButton.image", it)
            }
        }
    }

    override fun resetProps() {
        super.resetProps()
        (initializeAttributes["UIButton.title"] as? String)?.let {
            setTitle(it, State.Normal)
        }
        (initializeAttributes["UIButton.image"] as? Int)?.let {
            setImage(UIImage(context, it), State.Normal)
        }
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

    private fun initTitleLabel() {
        titleLabel = UILabel(context)
        titleLabel.numberOfLines = 1
        titleLabel.linebreakMode = NSLineBreakMode.ByTruncatingMiddle
        addSubview(titleLabel)
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

    fun currentTitle(): String? {
        val state = state
        if (titles[state] != null) {
            return titles[state]
        } else if (titles[EnumSet.of(UIControl.State.Normal)] != null) {
            return titles[EnumSet.of(UIControl.State.Normal)]
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
        titles.put(state, title)
        resetTitleLabel()
    }

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

    private fun initImageView() {
        imageView = UIImageView(context)
        addSubview(imageView)
    }

    private fun resetImageView() {
        val image = currentImage() ?: return
        if (image.renderingMode != UIImage.RenderingMode.AlwaysOriginal) {
            imageView.layer.bitmapColor = currentTitleColor()
        } else {
            imageView.layer.bitmapColor = null
        }
        imageView.image = image
        imageView.invalidate()
        layoutSubviews()
    }



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
        titleLabel.maxWidth = 999999.0
        var contentWidth = titleLabel.intrinsicContentSize().width + imageView.intrinsicContentSize().width
        contentWidth += contentEdgeInsets.left + contentEdgeInsets.right + imageEdgeInsets.left + imageEdgeInsets.right + titleEdgeInsets.left + titleEdgeInsets.right
        var contentHeight = titleLabel.intrinsicContentSize().height + imageView.intrinsicContentSize().height
        contentHeight += contentEdgeInsets.top + contentEdgeInsets.bottom + Math.max(titleEdgeInsets.top + titleEdgeInsets.bottom, imageEdgeInsets.top + imageEdgeInsets.bottom)
        return CGSize(Math.max(contentWidth, 44.0), Math.max(contentHeight, 44.0))
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
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
