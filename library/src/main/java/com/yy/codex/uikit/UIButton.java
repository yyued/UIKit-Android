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
        UIConstraint constraint = new UIConstraint();
        constraint.centerHorizontally = true;
        constraint.centerVertically = true;
        mTitleLabel.setConstraint(constraint);
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

}
