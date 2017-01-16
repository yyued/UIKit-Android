package com.yy.codex.uikit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

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

    private UILabel mTitleLabel;

    private void initTitleLabel() {
        mTitleLabel = new UILabel(getContext());
        UIConstraint constraint = new UIConstraint();
        constraint.centerHorizontally = true;
        constraint.centerVertically = true;
        mTitleLabel.setConstraint(constraint);
        mTitleLabel.setText("Hello");
        mTitleLabel.setTextColor(getTintColor());
        addSubview(mTitleLabel);
    }

    @Override
    public void layoutSubviews() {
        super.layoutSubviews();
        mTitleLabel.setMaxWidth(getFrame().getWidth());
    }

}
