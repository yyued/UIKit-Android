package com.yy.codex.uikit;

import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * Created by cuiminghui on 2017/1/9.
 */

public class NSMutableAttributedString extends NSAttributedString {

    public NSMutableAttributedString(@NonNull String text, @NonNull HashMap<String, Object> attributes) {
        super(text, attributes);
    }

    public NSMutableAttributedString(@NonNull NSAttributedString attributedString) {
        super(attributedString);
    }

    @NonNull
    public NSAttributedString copy() {
        return new NSAttributedString(this);
    }

    public void setAttributes(@NonNull HashMap<String, Object> attrs, @NonNull NSRange range) {
        reset(attrs, range);
    }

    public void addAttribute(@NonNull final String name, @NonNull final Object value, @NonNull NSRange range) {
        addAttributes(new HashMap<String, Object>(){{
            put(name, value);
        }}, range);
    }

    public void addAttributes(@NonNull HashMap<String, Object> attrs, @NonNull NSRange range) {
        for (int i = 0; i < range.length; i++) {
            HashMap<String, Object> values = getAttributes(range.location + i);
            values.putAll(attrs);
            setAttributes(values, new NSRange(range.location + i, 1));
        }
    }

    public void removeAttribute(@NonNull final String name, @NonNull NSRange range) {
        for (int i = 0; i < range.length; i++) {
            HashMap<String, Object> values = getAttributes(range.location + i);
            values.remove(name);
            setAttributes(values, new NSRange(range.location + i, 1));
        }
    }

    public void replaceCharacters(@NonNull NSRange inRange, @NonNull NSAttributedString attributedString) {
        delete(inRange.location, inRange.location + inRange.length);
        insert(inRange.location, attributedString);
    }

    public void insertAttributedString(@NonNull NSAttributedString attributedString, int atIndex) {
        insert(atIndex, attributedString);
    }

    public void appendAttributedString(@NonNull NSAttributedString attributedString) {
        append(attributedString);
    }

    public void deleteCharacters(@NonNull NSRange inRange) {
        delete(inRange.location, inRange.location + inRange.length);
    }

    public void setAttributedString(@NonNull NSAttributedString attributedString) {
        replace(0, length(), attributedString);
    }

}
