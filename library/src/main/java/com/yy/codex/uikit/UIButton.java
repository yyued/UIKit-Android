package com.yy.codex.uikit;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
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
        init();
    }

    public UIButton(@NonNull Context context) {
        super(context);
        init();
    }

    public UIButton(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UIButton(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UIButton(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void init() {
        super.init();
        initTitleLabel();
    }

    /* State Handler */

    @Override
    protected void resetState() {
        super.resetState();
        resetTitleLabel();
    }

    @Override
    protected void onLongPressed(UILongPressGestureRecognizer sender) {
        super.onLongPressed(sender);
    }

    @Override
    protected void onTapped(UITapGestureRecognizer sender) {
        super.onTapped(sender);
    }

    /* Title */

    @NonNull private HashMap<EnumSet<State>, NSAttributedString> mAttributedTitles = new HashMap<>();
    @NonNull private UILabel mTitleLabel;
    @NonNull private UIFont mFont = new UIFont(17);

    private void initTitleLabel() {
        mTitleLabel = new UILabel(getContext());
        UIConstraint constraint = new UIConstraint();
        constraint.centerHorizontally = true;
        constraint.centerVertically = true;
        mTitleLabel.setConstraint(constraint);
        addSubview(mTitleLabel);
    }

    private void resetTitleLabel() {
        EnumSet<State> state = getState();
        NSAttributedString attributedTitle = mAttributedTitles.get(state);
        if (attributedTitle != null) {
            mTitleLabel.setAttributedText(attributedTitle);
        }
        else {
            if (state.contains(State.Highlighted)) {
                NSAttributedString defaultAttributedTitle = null;
                if (state.contains(State.Selected)) {
                    defaultAttributedTitle = mAttributedTitles.get(EnumSet.of(State.Selected));
                }
                if (defaultAttributedTitle == null) {
                    defaultAttributedTitle = mAttributedTitles.get(EnumSet.of(State.Normal));
                }
                if (defaultAttributedTitle != null) {
                    NSMutableAttributedString mutableAttributedString = defaultAttributedTitle.mutableCopy();
                    final Object vColor = mutableAttributedString.getAttribute(NSAttributedString.NSForegroundColorAttributeName, 0);
                    if (vColor != null && vColor instanceof Number) {
                        mutableAttributedString.setAttributes(new HashMap<String, Object>(){{
                            put(NSAttributedString.NSForegroundColorAttributeName, 0x50007AFF);
                        }}, new NSRange(0, mutableAttributedString.length()));
                        defaultAttributedTitle = mutableAttributedString.copy();
                    }
                    mTitleLabel.setAttributedText(defaultAttributedTitle);
                }
            }
        }
    }

    public void setTitle(String title, State state) {
        setTitle(title, EnumSet.of(state));
    }

    public void setTitle(String title, EnumSet<State> state) {
        NSAttributedString attributedString = mAttributedTitles.get(state);
        if (attributedString == null) {
            attributedString = new NSAttributedString(title, new HashMap(){{
                put(NSAttributedString.NSForegroundColorAttributeName, getTintColor());
                put(NSAttributedString.NSFontAttributeName, mFont);
            }});
        }
        else {
            attributedString = new NSAttributedString(title, attributedString.getAttributes(0));
        }
        mAttributedTitles.put(state, attributedString);
        resetTitleLabel();
    }

    public void setTitleColor(int color, State state) {
        setTitleColor(color, EnumSet.of(state));
    }

    public void setTitleColor(final int color, EnumSet<State> state) {
        NSAttributedString attributedString = mAttributedTitles.get(state);
        if (attributedString == null) {
            attributedString = new NSAttributedString("", new HashMap(){{
                put(NSAttributedString.NSForegroundColorAttributeName, color);
                put(NSAttributedString.NSFontAttributeName, mFont);
            }});
        }
        else {
            NSMutableAttributedString mutableAttributedString = attributedString.mutableCopy();
            mutableAttributedString.addAttribute(NSAttributedString.NSForegroundColorAttributeName, color, new NSRange(0, mutableAttributedString.length()));
            attributedString = mutableAttributedString.copy();
        }
        mAttributedTitles.put(state, attributedString);
        resetTitleLabel();
    }

    public void setFont(@NonNull UIFont font) {
        this.mFont = font;
    }

}
