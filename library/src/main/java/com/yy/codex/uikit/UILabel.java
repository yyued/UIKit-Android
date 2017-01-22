package com.yy.codex.uikit;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Layout;
import android.text.SpannedString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by cuiminghui on 2017/1/4.
 */

public class UILabel extends UIView {

    private TextView mTextView;

    public UILabel(@NonNull Context context, @NonNull View view) {
        super(context, view);
        init();
    }

    public UILabel(@NonNull Context context) {
        super(context);
        init();
    }

    public UILabel(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UILabel(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UILabel(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void init() {
        super.init();
        mTextView = new TextView(getContext());
        mTextView.setMaxLines(1);
        resetTextView();
    }

    @Override
    public void layoutSubviews() {
        super.layoutSubviews();
        resetTextView();
    }

    /* category: TextView Props */

    /* Font */

    @NonNull private UIFont mFont = new UIFont(17);

    @NonNull
    public UIFont getFont() {
        return mFont;
    }

    public void setFont(@NonNull UIFont font) {
        this.mFont = font;
        updateTextAppearance();
    }

    /* TextColor */

    private UIColor mTextColor = UIColor.blackColor;

    public UIColor getTextColor() {
        return mTextColor;
    }

    public void setTextColor(UIColor textColor) {
        this.mTextColor = textColor;
        updateTextAppearance();
    }

    /* Number of lines */

    private int mNumberOfLines = 1;

    public int getNumberOfLines() {
        return mNumberOfLines;
    }

    public void setNumberOfLines(int numberOfLines) {
        this.mNumberOfLines = numberOfLines;
        if (numberOfLines <= 0) {
            numberOfLines = 99999;
        }
        mTextView.setMaxLines(numberOfLines);
    }

    /* Line-Break Mode */

    @NonNull
    private NSLineBreakMode mLinebreakMode = NSLineBreakMode.ByWordWrapping;

    @NonNull
    public NSLineBreakMode getLinebreakMode() {
        return mLinebreakMode;
    }

    public void setLinebreakMode(@NonNull NSLineBreakMode linebreakMode) {
        this.mLinebreakMode = linebreakMode;
        switch (linebreakMode) {
            case ByTruncatingHead:
                mTextView.setEllipsize(TextUtils.TruncateAt.START);
                break;
            case ByTruncatingMiddle:
                mTextView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                break;
            case ByTruncatingTail:
                mTextView.setEllipsize(TextUtils.TruncateAt.END);
                break;
            default:
                mTextView.setEllipsize(null);
                break;
        }
        updateTextAppearance();
    }

    private Layout.Alignment mAlignment = Layout.Alignment.ALIGN_NORMAL;

    public Layout.Alignment getAlignment() {
        return mAlignment;
    }

    public void setAlignment(Layout.Alignment alignment) {
        this.mAlignment = alignment;
        updateTextAppearance();
    }

    /* Text */

    private boolean mNeedsUpdate = false;

    @Nullable
    public String getText() {
        return getAttributedText() != null ? getAttributedText().toString() : null;
    }

    public void setText(@Nullable String text) {
        if (text == null) {
            text = "";
        }
        NSAttributedString attributedString = new NSAttributedString(text, new HashMap(){{
            if (getFont() != null) {
                put(NSAttributedString.NSFontAttributeName, getFont());
            }
            put(NSAttributedString.NSForegroundColorAttributeName, getTextColor());
            NSParagraphStyle paragraphStyle = new NSParagraphStyle();
            paragraphStyle.lineBreakMode = getLinebreakMode();
            paragraphStyle.alignment = getAlignment();
            put(NSAttributedString.NSParagraphStyleAttributeName, paragraphStyle);
        }});
        setAttributedText(attributedString);
        mNeedsUpdate = true;
    }

    private void updateTextAppearance() {
        if (mNeedsUpdate && getText() != null) {
            String text = getText();
            setText(text);
        }
    }

    @Nullable
    public NSAttributedString getAttributedText() {
        if (this.mTextView.getText() != null && SpannedString.class.isAssignableFrom(this.mTextView.getText().getClass())) {
            return new NSAttributedString((SpannedString) this.mTextView.getText());
        }
        return null;
    }

    public void setAttributedText(@Nullable NSAttributedString attributedText) {
        this.mTextView.setText(attributedText);
        resetTextViewStyles();
        if (getConstraint() != null) {
            getConstraint().setNeedsLayout();
            UIView superview = getSuperview();
            if (superview != null) {
                superview.layoutSubviews();
            }
        }
    }

    private void resetTextViewStyles() {
        // TODO: 2017/1/10 not implemented.
    }

    /* category: Layouts */

    @Override
    public void setMaxWidth(double maxWidth) {
        super.setMaxWidth(maxWidth);
        mTextView.setMaxWidth((int) (maxWidth * UIScreen.mainScreen.scale()));
    }

    @Override @NonNull
    public CGSize intrinsicContentSize() {
        mTextView.measure(0, 0);
        int width = mTextView.getMeasuredWidth();
        int height = mTextView.getMeasuredHeight();
        return new CGSize(Math.ceil(width / UIScreen.mainScreen.scale()), Math.ceil(height / UIScreen.mainScreen.scale()));
    }

    private void resetTextView() {
        removeView(mTextView);
        addView(mTextView, new LayoutParams((int)(this.getFrame().getSize().getWidth() * UIScreen.mainScreen.scale()), (int)(this.getFrame().getSize().getHeight() * UIScreen.mainScreen.scale())));
    }

}
