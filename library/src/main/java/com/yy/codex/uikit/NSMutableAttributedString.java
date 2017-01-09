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

}
