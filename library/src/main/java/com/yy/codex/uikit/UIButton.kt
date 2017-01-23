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

    var mTitleLabel: UILabel? = null
        private set

    private fun initTitleLabel() {
        mTitleLabel = UILabel(context)
        mTitleLabel?.let {
            it.numberOfLines = 1
            it.linebreakMode = NSLineBreakMode.ByTruncatingMiddle
            addSubview(it)
        }
    }

    private fun resetTitleLabel() {
        mTitleLabel?.let {
            if (mAttributedTitles.size > 0) {
                it.attributedText = currentAttributedTitle()
            } else {
                val attributedString = NSAttributedString(currentTitle() ?: "", hashMapOf(
                        Pair(NSAttributedString.NSForegroundColorAttributeName, currentTitleColor() ?: UIColor.blackColor),
                        Pair(NSAttributedString.NSFontAttributeName, mFont)
                ))
                it.attributedText = attributedString
            }
            layoutSubviews()
        }
    }

    private val mTitleTexts = HashMap<EnumSet<UIControl.State>, String>()

    fun currentTitle(): String? {
        val state = state
        if (mTitleTexts[state] != null) {
            return mTitleTexts[state]
        } else if (mTitleTexts[EnumSet.of(UIControl.State.Normal)] != null) {
            return mTitleTexts[EnumSet.of(UIControl.State.Normal)]
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
        mTitleTexts.put(state, title)
        resetTitleLabel()
    }

    private val mTitleColors = HashMap<EnumSet<UIControl.State>, UIColor>()

    fun currentTitleColor(): UIColor? {
        val state = state
        if (mTitleColors[state] != null) {
            return mTitleColors[state]
        } else if (mTitleColors[EnumSet.of(UIControl.State.Normal)] != null) {
            return mTitleColors[EnumSet.of(UIControl.State.Normal)]
        } else {
            if (state.contains(UIControl.State.Disabled)) {
                return UIColor.blackColor.colorWithAlpha(0.3)
            }
            if (state.contains(UIControl.State.Highlighted) && state.contains(UIControl.State.Normal)) {
                return tintColor!!.colorWithAlpha(0.3)
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
        mTitleColors.put(state, color)
        resetTitleLabel()
    }

    private var mFont = UIFont(17f)

    fun setFont(font: UIFont) {
        mFont = font
        resetTitleLabel()
    }

    private val mAttributedTitles = HashMap<EnumSet<UIControl.State>, NSAttributedString>()

    fun currentAttributedTitle(): NSAttributedString? {
        val state = state
        if (mAttributedTitles[state] != null) {
            return mAttributedTitles[state]
        } else if (mAttributedTitles[EnumSet.of(UIControl.State.Normal)] != null) {
            return mAttributedTitles[EnumSet.of(UIControl.State.Normal)]
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
        mAttributedTitles.put(state, attributedString)
        resetTitleLabel()
    }

    /* ImageView */

    var mImageView: UIImageView? = null

    private fun initImageView() {
        mImageView = UIImageView(context)
        mImageView?.let {
            addSubview(it)
        }
    }

    private fun resetImageView() {
        mImageView?.let {
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

    private val mImages = HashMap<EnumSet<UIControl.State>, UIImage>()

    fun currentImage(): UIImage? {
        val state = state
        if (mImages[state] != null) {
            return mImages[state]
        } else if (mImages[EnumSet.of(UIControl.State.Normal)] != null) {
            return mImages[EnumSet.of(UIControl.State.Normal)]
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
        mImages.put(state, image)
        resetImageView()
    }

    /* Layouts */

    override fun intrinsicContentSize(): CGSize {
        if (mTitleLabel as? UILabel == null && mImageView as? UIImageView == null) {
            return CGSize(.0, .0)
        }
        val titleLabel = mTitleLabel as UILabel
        val imageView = mImageView as UIImageView
        titleLabel.setMaxWidth(999999.0)
        var contentWidth = titleLabel.intrinsicContentSize().width + imageView.intrinsicContentSize().width
        contentWidth += mContentEdgeInsets.left + mContentEdgeInsets.right + mImageEdgeInsets.left + mImageEdgeInsets.right + mTitleEdgeInsets.left + mTitleEdgeInsets.right
        var contentHeight = titleLabel.intrinsicContentSize().height + imageView.intrinsicContentSize().height
        contentHeight += mContentEdgeInsets.top + mContentEdgeInsets.bottom + Math.max(mTitleEdgeInsets.top + mTitleEdgeInsets.bottom, mImageEdgeInsets.top + mImageEdgeInsets.bottom)
        return CGSize(Math.max(contentWidth, 44.0), Math.max(contentHeight, 44.0))
    }

    private var mContentEdgeInsets = UIEdgeInsets(0.0, 0.0, 0.0, 0.0)
    private var mTitleEdgeInsets = UIEdgeInsets(0.0, 0.0, 0.0, 0.0)
    private var mImageEdgeInsets = UIEdgeInsets(0.0, 0.0, 0.0, 0.0)

    fun setContentEdgeInsets(contentEdgeInsets: UIEdgeInsets) {
        this.mContentEdgeInsets = contentEdgeInsets
        layoutSubviews()
    }

    fun setTitleEdgeInsets(titleEdgeInsets: UIEdgeInsets) {
        this.mTitleEdgeInsets = titleEdgeInsets
        layoutSubviews()
    }

    fun setImageEdgeInsets(imageEdgeInsets: UIEdgeInsets) {
        this.mImageEdgeInsets = imageEdgeInsets
        layoutSubviews()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        if (mTitleLabel as? UILabel == null && mImageView as? UIImageView == null) {
            return
        }
        val titleLabel = mTitleLabel as UILabel
        val imageView = mImageView as UIImageView
        titleLabel.setMaxWidth(frame.size.width - imageView.intrinsicContentSize().width - (mContentEdgeInsets.left + mContentEdgeInsets.right + mImageEdgeInsets.left + mImageEdgeInsets.right + mTitleEdgeInsets.left + mTitleEdgeInsets.right))
        var contentWidth = titleLabel.intrinsicContentSize().width + imageView.intrinsicContentSize().width
        contentWidth += mContentEdgeInsets.left + mContentEdgeInsets.right + mImageEdgeInsets.left + mImageEdgeInsets.right + mTitleEdgeInsets.left + mTitleEdgeInsets.right
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
                    mContentEdgeInsets.left + mImageEdgeInsets.left,
                    imageViewOriginY,
                    imageView.intrinsicContentSize().width,
                    imageView.intrinsicContentSize().height)
            titleLabel.frame = CGRect(
                    mContentEdgeInsets.left + mImageEdgeInsets.left + imageView.frame.size.width + mImageEdgeInsets.right + mTitleEdgeInsets.left,
                    titleLabelOriginY,
                    titleLabel.intrinsicContentSize().width,
                    titleLabel.intrinsicContentSize().height)
        } else if (contentHorizontalAlignment === UIControl.ContentHorizontalAlignment.Center) {
            imageView.frame = CGRect(
                    (frame.size.width - contentWidth) / 2.0 + mContentEdgeInsets.left + mImageEdgeInsets.left,
                    imageViewOriginY,
                    imageView.intrinsicContentSize().width,
                    imageView.intrinsicContentSize().height)
            titleLabel.frame = CGRect(
                    (frame.size.width - contentWidth) / 2.0 + mContentEdgeInsets.left + mImageEdgeInsets.left + imageView.frame.size.width + mImageEdgeInsets.right + mTitleEdgeInsets.left,
                    titleLabelOriginY,
                    titleLabel.intrinsicContentSize().width,
                    titleLabel.intrinsicContentSize().height)
        } else if (contentHorizontalAlignment === UIControl.ContentHorizontalAlignment.Right) {
            imageView.frame = CGRect(
                    frame.size.width - contentWidth + mContentEdgeInsets.left + mImageEdgeInsets.left,
                    imageViewOriginY,
                    imageView.intrinsicContentSize().width,
                    imageView.intrinsicContentSize().height)
            titleLabel.frame = CGRect(
                    frame.size.width - contentWidth + mContentEdgeInsets.left + mImageEdgeInsets.left + imageView.frame.size.width + mImageEdgeInsets.right + mTitleEdgeInsets.left,
                    titleLabelOriginY,
                    titleLabel.intrinsicContentSize().width,
                    titleLabel.intrinsicContentSize().height)
        }
    }

}
