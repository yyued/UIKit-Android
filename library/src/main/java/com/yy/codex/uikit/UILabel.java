package com.yy.codex.uikit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by cuiminghui on 2017/1/4.
 */

public class UILabel extends UIView {

    private TextView textView;

    public UILabel(Context context, View view) {
        super(context, view);
        init();
    }

    public UILabel(Context context) {
        super(context);
        init();
    }

    public UILabel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UILabel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UILabel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        textView = new TextView(getContext());
        textView.setMaxLines(1);
        resetTextView();
    }

    @Override
    public void layoutSubviews() {
        super.layoutSubviews();
        resetTextView();
    }

    /* category: TextView Props */

    private NSAttributedString attributedText;
    private String fontFamilyName;
    private double fontPointSize = 17;
    private int textColor = Color.BLACK;
    private Layout.Alignment textAlignment = Layout.Alignment.ALIGN_NORMAL;

    public String getFontFamilyName() {
        return fontFamilyName;
    }

    public void setFontFamilyName(String fontFamilyName) {
        this.fontFamilyName = fontFamilyName;
    }

    public double getFontPointSize() {
        return fontPointSize;
    }

    public void setFontPointSize(double fontPointSize) {
        this.fontPointSize = fontPointSize;
    }

    public String getText() {
        return attributedText.toString();
    }

    public void setText(String text) {
//        NSAttributedString attributedString = new NSAttributedString(text, new HashMap(){{
//        }});
//        setAttributedText(attributedString);
    }

    public void setAttributedText(NSAttributedString attributedText) {
        this.attributedText = attributedText;
        this.textView.setText(attributedText);
        if (getConstraint() != null) {
            getConstraint().setNeedsLayout();
            UIView superview = getSuperview();
            if (superview != null) {
                superview.layoutSubviews();
            }
        }
    }

    private int numberOfLines = 1;

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public void setNumberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
        if (numberOfLines <= 0) {
            numberOfLines = 99999;
        }
        textView.setMaxLines(numberOfLines);
    }

    private NSLineBreakMode linebreakMode = NSLineBreakMode.ByWordWrapping;

    public NSLineBreakMode getLinebreakMode() {
        return linebreakMode;
    }

    public void setLinebreakMode(NSLineBreakMode linebreakMode) {
        this.linebreakMode = linebreakMode;
        switch (linebreakMode) {
            case ByTruncatingHead:
                textView.setEllipsize(TextUtils.TruncateAt.START);
                break;
            case ByTruncatingMiddle:
                textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                break;
            case ByTruncatingTail:
                textView.setEllipsize(TextUtils.TruncateAt.END);
                break;
            default:
                textView.setEllipsize(null);
                break;
        }
    }

    /* category: Layouts */

    @Override
    public void setMaxWidth(double maxWidth) {
        super.setMaxWidth(maxWidth);
        float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        textView.setMaxWidth((int) (maxWidth * scaledDensity));
    }

    @Override
    public CGSize intrinsicContentSize() {
        textView.measure(0, 0);
        int width = textView.getMeasuredWidth();
        int height = textView.getMeasuredHeight();
        float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        return new CGSize(width / scaledDensity, height / scaledDensity);
    }

    private void resetTextView() {
        removeView(textView);
        float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        addView(textView, new LayoutParams((int)(this.getFrame().size.getWidth() * scaledDensity), (int)(this.getFrame().size.getHeight() * scaledDensity)));
    }

}
