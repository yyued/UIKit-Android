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

    private TextView textView;

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

    /* Font */

    @NonNull private UIFont font = new UIFont(17);

    @NonNull
    public UIFont getFont() {
        return font;
    }

    public void setFont(@NonNull UIFont font) {
        this.font = font;
        updateTextAppearance();
    }

    /* TextColor */

    private int textColor = Color.BLACK;

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        updateTextAppearance();
    }

    /* Number of lines */

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

    /* Line-Break Mode */

    @NonNull
    private NSLineBreakMode linebreakMode = NSLineBreakMode.ByWordWrapping;

    @NonNull
    public NSLineBreakMode getLinebreakMode() {
        return linebreakMode;
    }

    public void setLinebreakMode(@NonNull NSLineBreakMode linebreakMode) {
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
        updateTextAppearance();
    }

    private Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;

    public Layout.Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Layout.Alignment alignment) {
        this.alignment = alignment;
        updateTextAppearance();
    }

    /* Text */

    private boolean needsUpdate = false;

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
        needsUpdate = true;
    }

    private void updateTextAppearance() {
        if (needsUpdate && getText() != null) {
            String text = getText();
            setText(text);
        }
    }

    @Nullable
    public NSAttributedString getAttributedText() {
        if (this.textView.getText() != null && SpannedString.class.isAssignableFrom(this.textView.getText().getClass())) {
            return new NSAttributedString((SpannedString) this.textView.getText());
        }
        return null;
    }

    public void setAttributedText(@Nullable NSAttributedString attributedText) {
        this.textView.setText(attributedText);
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
        float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        textView.setMaxWidth((int) (maxWidth * scaledDensity));
    }

    @Override @NonNull
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
