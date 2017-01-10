package com.yy.codex.uikit;

import java.util.HashMap;

/**
 * Created by cuiminghui on 2017/1/9.
 */

public class NSMutableAttributedString extends NSAttributedString {

    public NSMutableAttributedString(String text, HashMap<String, Object> attributes) {
        super(text, attributes);
    }

    public NSMutableAttributedString(NSAttributedString attributedString) {
        super(attributedString);
    }

    public NSAttributedString copy() {
        return new NSAttributedString(this);
    }

    public void setAttributes(HashMap<String, Object> attrs, NSRange range) {
        reset(attrs, range);
    }

    public void addAttribute(final String name, final Object value, NSRange range) {
        addAttributes(new HashMap<String, Object>(){{
            put(name, value);
        }}, range);
    }

    public void addAttributes(HashMap<String, Object> attrs, NSRange range) {
        for (int i = 0; i < range.length; i++) {
            HashMap<String, Object> values = getAttributes(range.location + i);
            values.putAll(attrs);
            setAttributes(values, new NSRange(range.location + i, 1));
        }
    }

    public void removeAttribute(final String name, NSRange range) {
        for (int i = 0; i < range.length; i++) {
            HashMap<String, Object> values = getAttributes(range.location + i);
            values.remove(name);
            setAttributes(values, new NSRange(range.location + i, 1));
        }
    }

    public void replaceCharacters(NSRange inRange, NSAttributedString attributedString) {
        delete(inRange.location, inRange.location + inRange.length);
        insert(inRange.location, attributedString);
    }

    public void insertAttributedString(NSAttributedString attributedString, int atIndex) {
        insert(atIndex, attributedString);
    }

    public void appendAttributedString(NSAttributedString attributedString) {
        append(attributedString);
    }

    public void deleteCharacters(NSRange inRange) {
        delete(inRange.location, inRange.location + inRange.length);
    }

    public void setAttributedString(NSAttributedString attributedString) {
        replace(0, length(), attributedString);
    }

}
