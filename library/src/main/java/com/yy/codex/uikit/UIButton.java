package com.yy.codex.uikit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.util.EnumSet;
import java.util.HashMap;

/**
 * Created by cuiminghui on 2017/1/16.
 */

public class UIButton extends UIControl {

    public UIButton(@NonNull Context context, @NonNull View view) {
        super(context, view);
    }

    public UIButton(@NonNull Context context) {
        super(context);
    }

    public UIButton(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public UIButton(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UIButton(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init() {
        super.init();
        initTitleLabel();
        initImageView();
    }

    /* UIControl */

    @Override
    protected void resetState() {
        super.resetState();
        resetTitleLabel();
        resetImageView();
    }

    /* UIView */

    @Override
    public void tintColorDidChanged() {
        super.tintColorDidChanged();
        resetTitleLabel();
    }

    /* Title */

    @NonNull private UILabel mTitleLabel;

    private void initTitleLabel() {
        mTitleLabel = new UILabel(getContext());
        mTitleLabel.setNumberOfLines(1);
        mTitleLabel.setLinebreakMode(NSLineBreakMode.ByTruncatingMiddle);
        addSubview(mTitleLabel);
    }

    private void resetTitleLabel() {
        if (mAttributedTitles.size() > 0) {
            mTitleLabel.setAttributedText(currentAttributedTitle());
        }
        else {
            NSAttributedString attributedString = new NSAttributedString(currentTitle(), new HashMap(){{
                put(NSAttributedString.NSForegroundColorAttributeName, currentTitleColor());
                put(NSAttributedString.NSFontAttributeName, mFont);
            }});
            mTitleLabel.setAttributedText(attributedString);
        }
        layoutSubviews();
    }

    private HashMap<EnumSet<State>, String> mTitleTexts = new HashMap<>();

    @NonNull
    protected String currentTitle() {
        EnumSet<State> state = getState();
        if (mTitleTexts.get(state) != null) {
            return mTitleTexts.get(state);
        }
        else if (mTitleTexts.get(EnumSet.of(State.Normal)) != null){
            return mTitleTexts.get(EnumSet.of(State.Normal));
        }
        else {
            return "";
        }
    }

    public void setTitle(@NonNull String title, State state) {
        setTitle(title, EnumSet.of(State.Normal, state));
    }

    public void setTitle(@NonNull String title, EnumSet<State> state) {
        if (state.contains(State.Selected)) {
            state.remove(State.Normal);
        }
        mTitleTexts.put(state, title);
        resetTitleLabel();
    }

    private HashMap<EnumSet<State>, UIColor> mTitleColors = new HashMap<>();

    @NonNull
    protected UIColor currentTitleColor() {
        EnumSet<State> state = getState();
        if (mTitleColors.get(state) != null) {
            return mTitleColors.get(state);
        }
        else if (mTitleColors.get(EnumSet.of(State.Normal)) != null){
            return mTitleColors.get(EnumSet.of(State.Normal));
        }
        else {
            if (state.contains(State.Disabled)) {
                return UIColor.blackColor.colorWithAlpha(0.3);
            }
            if (state.contains(State.Highlighted) && state.contains(State.Normal)) {
                return getTintColor().colorWithAlpha(0.3);
            }
            return getTintColor();
        }
    }

    public void setTitleColor(@NonNull UIColor color, State state) {
        setTitleColor(color, EnumSet.of(State.Normal, state));
    }

    public void setTitleColor(@NonNull final UIColor color, EnumSet<State> state) {
        if (state.contains(State.Selected)) {
            state.remove(State.Normal);
        }
        mTitleColors.put(state, color);
        resetTitleLabel();
    }

    private UIFont mFont = new UIFont(17);

    public void setFont(@NonNull final UIFont font) {
        mFont = font;
        resetTitleLabel();
    }

    @NonNull private HashMap<EnumSet<State>, NSAttributedString> mAttributedTitles = new HashMap<>();

    @Nullable
    public NSAttributedString currentAttributedTitle() {
        EnumSet<State> state = getState();
        if (mAttributedTitles.get(state) != null) {
            return mAttributedTitles.get(state);
        }
        else if (mAttributedTitles.get(EnumSet.of(State.Normal)) != null){
            return mAttributedTitles.get(EnumSet.of(State.Normal));
        }
        else {
            return null;
        }
    }

    public void setAttributedTitle(@NonNull NSAttributedString attributedString, State state) {
        setAttributedTitle(attributedString, EnumSet.of(State.Normal, state));
    }

    public void setAttributedTitle(@NonNull NSAttributedString attributedString, EnumSet<State> state) {
        if (state.contains(State.Selected)) {
            state.remove(State.Normal);
        }
        mAttributedTitles.put(state, attributedString);
        resetTitleLabel();
    }

    /* ImageView */

    @NonNull UIImageView mImageView;

    private void initImageView() {
        mImageView = new UIImageView(getContext());
        addSubview(mImageView);
    }

    private void resetImageView() {
        UIImage image = currentImage();
        if (image == null) {
            return;
        }
        if (image.getRenderingMode() != UIImage.RenderingMode.AlwaysOriginal) {
            mImageView.getLayer().setBitmapColor(currentTitleColor());
        }
        else {
            mImageView.getLayer().setBitmapColor(null);
        }
        mImageView.setImage(image);
        mImageView.invalidate();
        layoutSubviews();
    }

    @NonNull private HashMap<EnumSet<State>, UIImage> mImages = new HashMap<>();

    @Nullable
    public UIImage currentImage() {
        EnumSet<State> state = getState();
        if (mImages.get(state) != null) {
            return mImages.get(state);
        }
        else if (mImages.get(EnumSet.of(State.Normal)) != null){
            return mImages.get(EnumSet.of(State.Normal));
        }
        else {
            return null;
        }
    }

    public void setImage(UIImage image, State state) {
        setImage(image, EnumSet.of(state));
    }

    public void setImage(@NonNull UIImage image, EnumSet<State> state) {
        if (state.contains(State.Selected)) {
            state.remove(State.Normal);
        }
        mImages.put(state, image);
        resetImageView();
    }

    /* Layouts */

    private UIEdgeInsets mContentEdgeInsets = new UIEdgeInsets(0, 0, 0, 0);
    private UIEdgeInsets mTitleEdgeInsets = new UIEdgeInsets(0, 0, 0, 0);
    private UIEdgeInsets mImageEdgeInsets = new UIEdgeInsets(0, 0, 0, 0);

    public void setContentEdgeInsets(UIEdgeInsets contentEdgeInsets) {
        this.mContentEdgeInsets = contentEdgeInsets;
        layoutSubviews();
    }

    public void setTitleEdgeInsets(UIEdgeInsets titleEdgeInsets) {
        this.mTitleEdgeInsets = titleEdgeInsets;
        layoutSubviews();
    }

    public void setImageEdgeInsets(UIEdgeInsets imageEdgeInsets) {
        this.mImageEdgeInsets = imageEdgeInsets;
        layoutSubviews();
    }

    @Override
    public void layoutSubviews() {
        super.layoutSubviews();
        mTitleLabel.setMaxWidth(getFrame().size.width - mImageView.intrinsicContentSize().width - (mContentEdgeInsets.left + mContentEdgeInsets.right + mImageEdgeInsets.left + mImageEdgeInsets.right + mTitleEdgeInsets.left + mTitleEdgeInsets.right));
        double contentWidth = mTitleLabel.intrinsicContentSize().width + mImageView.intrinsicContentSize().width;
        contentWidth += mContentEdgeInsets.left + mContentEdgeInsets.right + mImageEdgeInsets.left + mImageEdgeInsets.right + mTitleEdgeInsets.left + mTitleEdgeInsets.right;
        double imageViewOriginY = 0;
        double titleLabelOriginY = 0;
        if (getContentVerticalAlignment() == ContentVerticalAlignment.Center) {
            imageViewOriginY = (getFrame().size.height - mImageView.intrinsicContentSize().height) / 2.0;
            titleLabelOriginY = (getFrame().size.height - mTitleLabel.intrinsicContentSize().height) / 2.0;
        }
        else if (getContentVerticalAlignment() == ContentVerticalAlignment.Bottom) {
            imageViewOriginY = getFrame().size.height - mImageView.intrinsicContentSize().height;
            titleLabelOriginY = getFrame().size.height - mTitleLabel.intrinsicContentSize().height;
        }
        if (getContentHorizontalAlignment() == ContentHorizontalAlignment.Left) {
            mImageView.setFrame(new CGRect(
                    mContentEdgeInsets.left + mImageEdgeInsets.left,
                    imageViewOriginY,
                    mImageView.intrinsicContentSize().width,
                    mImageView.intrinsicContentSize().height)
            );
            mTitleLabel.setFrame(new CGRect(
                    mContentEdgeInsets.left + mImageEdgeInsets.left + mImageView.getFrame().size.width + mImageEdgeInsets.right + mTitleEdgeInsets.left,
                    titleLabelOriginY,
                    mTitleLabel.intrinsicContentSize().width,
                    mTitleLabel.intrinsicContentSize().height)
            );
        }
        else if (getContentHorizontalAlignment() == ContentHorizontalAlignment.Center) {
            mImageView.setFrame(new CGRect(
                    ((getFrame().size.width - contentWidth) / 2.0) + mContentEdgeInsets.left + mImageEdgeInsets.left,
                    imageViewOriginY,
                    mImageView.intrinsicContentSize().width,
                    mImageView.intrinsicContentSize().height)
            );
            mTitleLabel.setFrame(new CGRect(
                    ((getFrame().size.width - contentWidth) / 2.0) + mContentEdgeInsets.left + mImageEdgeInsets.left + mImageView.getFrame().size.width + mImageEdgeInsets.right + mTitleEdgeInsets.left,
                    titleLabelOriginY,
                    mTitleLabel.intrinsicContentSize().width,
                    mTitleLabel.intrinsicContentSize().height)
            );
        }
        else if (getContentHorizontalAlignment() == ContentHorizontalAlignment.Right) {
            mImageView.setFrame(new CGRect(
                    (getFrame().size.width - contentWidth) + mContentEdgeInsets.left + mImageEdgeInsets.left,
                    imageViewOriginY,
                    mImageView.intrinsicContentSize().width,
                    mImageView.intrinsicContentSize().height)
            );
            mTitleLabel.setFrame(new CGRect(
                    (getFrame().size.width - contentWidth) + mContentEdgeInsets.left + mImageEdgeInsets.left + mImageView.getFrame().size.width + mImageEdgeInsets.right + mTitleEdgeInsets.left,
                    titleLabelOriginY,
                    mTitleLabel.intrinsicContentSize().width,
                    mTitleLabel.intrinsicContentSize().height)
            );
        }
    }
}
