package com.yy.codex.uikit;

import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineHeightSpan;
import android.text.style.ParagraphStyle;
import android.text.style.StrikethroughSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;

/**
 * Created by PonyCui_Home on 2017/1/4.
 */

public class NSAttributedString extends SpannableStringBuilder {

    public NSAttributedString(String text) {
        super(text);
    }

    public NSAttributedString(NSAttributedString attributedString) {
        super(attributedString);
    }

    public NSAttributedString attributedStringByAppendingString(NSAttributedString attributedString) {
        return (NSAttributedString) (new NSAttributedString(this).append(attributedString));
    }

    public NSAttributedString setFont(String familyName, double pointSize) {
        setSpan(new TypefaceSpan(familyName), 0, length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setSpan(new AbsoluteSizeSpan((int)pointSize, true), 0, length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public NSAttributedString setTextColor(int color) {
        setSpan(new ForegroundColorSpan(color), 0, length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public NSAttributedString setBackgroundColor(int color) {
        setSpan(new BackgroundColorSpan(color), 0, length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public NSAttributedString setAlignment(Layout.Alignment alignment) {
        setSpan(new AlignmentSpan.Standard(alignment), 0, length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public NSAttributedString setUnderline(boolean underlined) {
        if (underlined) {
            setSpan(new UnderlineSpan(), 0, length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else {
            removeSpan(new UnderlineSpan());
        }
        return this;
    }

    public NSAttributedString setDeleteline(boolean deletelined) {
        if (deletelined) {
            setSpan(new StrikethroughSpan(), 0, length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else {
            removeSpan(new StrikethroughSpan());
        }
        return this;
    }

}
