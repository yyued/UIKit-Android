package com.yy.codex.uikit;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;

import java.util.HashMap;

/**
 * Created by PonyCui_Home on 2017/1/4.
 */

public class NSAttributedString extends SpannableStringBuilder {

    @NonNull
    public static String NSFontAttributeName = "NSFontAttributeName"; // NSFont, default System 17
    @NonNull
    public static String NSParagraphStyleAttributeName = "NSParagraphStyleAttributeName"; // NSParagraphStyle, default nil
    @NonNull
    public static String NSForegroundColorAttributeName = "NSForegroundColorAttributeName"; // int, default Color.BLACK
    @NonNull
    public static String NSBackgroundColorAttributeName = "NSBackgroundColorAttributeName"; // int, default Color.TRANSPARENT: no background
    @NonNull
    public static String NSKernAttributeName = "NSKernAttributeName"; // double containing floating point value, in points; amount to modify default kerning. 0 means kerning is disabled.
    @NonNull
    public static String NSStrikethroughStyleAttributeName = "NSStrikethroughStyleAttributeName"; // int containing integer, default 0: no strikethrough
    @NonNull
    public static String NSUnderlineStyleAttributeName = "NSUnderlineStyleAttributeName"; // int containing integer, default 0: no underline
    @NonNull
    public static String NSStrokeColorAttributeName = "NSStrokeColorAttributeName";// TODO: 2017/1/9 not implemented.
    @NonNull
    public static String NSStrokeWidthAttributeName = "NSStrokeWidthAttributeName";// TODO: 2017/1/9 not implemented.
    @NonNull
    public static String NSShadowAttributeName = "NSShadowAttributeName"; // NSShadow, default nil: no shadow
    @NonNull
    public static String NSAttachmentAttributeName = "NSAttachmentAttributeName";// TODO: 2017/1/9 not implemented.
    @NonNull
    public static String NSLinkAttributeName = "NSLinkAttributeName";// TODO: 2017/1/9 not implemented.
    @NonNull
    public static String NSBaselineOffsetAttributeName = "NSBaselineOffsetAttributeName";// TODO: 2017/1/9 not implemented.
    @NonNull
    public static String NSUnderlineColorAttributeName = "NSUnderlineColorAttributeName";// TODO: 2017/1/9 not implemented.
    @NonNull
    public static String NSStrikethroughColorAttributeName = "NSStrikethroughColorAttributeName";// TODO: 2017/1/9 not implemented.

    public NSAttributedString(@NonNull String text) {
        super(text);
        this.reset(new HashMap<String, Object>(), new NSRange(0, text.length()));
    }

    public NSAttributedString(@NonNull String text, @NonNull HashMap<String, Object> attributes) {
        super(text);
        this.reset(attributes, new NSRange(0, text.length()));
    }

    public NSAttributedString(@NonNull NSAttributedString attributedString) {
        super(attributedString);
    }

    public NSAttributedString(@NonNull SpannedString spannableString) {
        super(spannableString);
    }

    @NonNull
    public NSMutableAttributedString mutableCopy() {
        return new NSMutableAttributedString(this);
    }

    @Nullable
    public Object getAttribute(String attrName, int atIndex) {
        NSAttributedSpan[] objects = getSpans(atIndex, 1, NSAttributedSpan.class);
        if (objects.length > 0) {
            return objects[0].mAttrs.get(attrName);
        }
        else {
            return null;
        }
    }

    @Nullable
    public HashMap<String, Object> getAttributes(int atIndex) {
        if (atIndex >= length()) {
            return null;
        }
        NSAttributedSpan[] objects = getSpans(atIndex, 1, NSAttributedSpan.class);
        if (objects.length > 0) {
            return objects[0].mAttrs;
        }
        else {
            return null;
        }
    }

    protected void reset(@NonNull final HashMap<String, Object> attrs, @NonNull final NSRange range) {
        setSpan(new NSAttributedSpan(attrs), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (attrs.get(NSFontAttributeName) != null && UIFont.class.isAssignableFrom(attrs.get(NSFontAttributeName).getClass())) {
            UIFont font = (UIFont) attrs.get(NSFontAttributeName);
            setSpan(new TypefaceSpan(font.fontFamily), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setSpan(new AbsoluteSizeSpan((int)font.fontSize, true), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (attrs.get(NSParagraphStyleAttributeName) != null && NSParagraphStyle.class.isAssignableFrom(attrs.get(NSParagraphStyleAttributeName).getClass())) {
            NSParagraphStyle style = (NSParagraphStyle) attrs.get(NSParagraphStyleAttributeName);
            if (style.alignment != Layout.Alignment.ALIGN_NORMAL) {
                setSpan(new AlignmentSpan.Standard(style.alignment), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (attrs.get(NSForegroundColorAttributeName) != null && UIColor.class.isAssignableFrom(attrs.get(NSForegroundColorAttributeName).getClass())) {
            setSpan(new ForegroundColorSpan(((UIColor)(attrs.get(NSForegroundColorAttributeName))).toInt()), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else {
            setSpan(new ForegroundColorSpan(Color.BLACK), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (attrs.get(NSBackgroundColorAttributeName) != null && UIColor.class.isAssignableFrom(attrs.get(NSBackgroundColorAttributeName).getClass())) {
            setSpan(new BackgroundColorSpan(((UIColor)(attrs.get(NSBackgroundColorAttributeName))).toInt()), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (attrs.get(NSKernAttributeName) != null && Number.class.isAssignableFrom(attrs.get(NSKernAttributeName).getClass())) {
            if ((float)attrs.get(NSKernAttributeName) != 0) {
                setSpan(new CharacterStyle() {
                    @Override
                    public void updateDrawState(@NonNull TextPaint textPaint) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textPaint.setLetterSpacing((float)attrs.get(NSKernAttributeName));
                        }
                    }
                }, range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (attrs.get(NSUnderlineStyleAttributeName) != null && Number.class.isAssignableFrom(attrs.get(NSUnderlineStyleAttributeName).getClass())) {
            if ((int)attrs.get(NSUnderlineStyleAttributeName) == 1) {
                setSpan(new UnderlineSpan(), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (attrs.get(NSStrikethroughStyleAttributeName) != null && Number.class.isAssignableFrom(attrs.get(NSStrikethroughStyleAttributeName).getClass())) {
            if ((int)attrs.get(NSStrikethroughStyleAttributeName) == 1) {
                setSpan(new StrikethroughSpan(), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (attrs.get(NSShadowAttributeName) != null && NSShadow.class.isAssignableFrom(attrs.get(NSShadowAttributeName).getClass())) {
            final NSShadow shadow = (NSShadow) attrs.get(NSShadowAttributeName);
            setSpan(new CharacterStyle() {
                @Override
                public void updateDrawState(@NonNull TextPaint textPaint) {
                    textPaint.setShadowLayer((float) shadow.shadowBlurRadius, (float) shadow.shadowOffset.width, (float) shadow.shadowOffset.height, shadow.shadowColor);
                }
            }, range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

}

class NSAttributedSpan extends CharacterStyle {

    public HashMap<String, Object> mAttrs;

    public NSAttributedSpan(HashMap<String, Object> attrs) {
        this.mAttrs = attrs;
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {}

}