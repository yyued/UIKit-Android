package com.yy.codex.uikit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cuiminghui on 2017/1/17.
 */

public class UISwitch extends UIControl {

    public UISwitch(@NonNull Context context, @NonNull View view) {
        super(context, view);
    }

    public UISwitch(@NonNull Context context) {
        super(context);
    }

    public UISwitch(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public UISwitch(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UISwitch(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private UIView mOffBackgroundView;
    private UIView mOnBackgroundView;
    private UIView mHandleView;

    @Override
    protected void init() {
        super.init();
        mOffBackgroundView = new UIView(getContext());
        mOffBackgroundView.setFrame(new CGRect(0, 0, 51, 32));
        mOffBackgroundView.setAlpha(1.0f);
        mOffBackgroundView.setWantsLayer(true);
        mOffBackgroundView.getLayer().setCornerRadius(16);
        mOffBackgroundView.getLayer().setBorderColor(UIColor.blackColor);
        mOffBackgroundView.getLayer().setBorderWidth(2.0);
        mOffBackgroundView.getLayer().setBackgroundColor(UIColor.greenColor);

        mOnBackgroundView = new UIView(getContext());
        mOnBackgroundView.setFrame(new CGRect(0, 0, 51, 32));
        mOnBackgroundView.setAlpha(0.0f);

        mHandleView = new UIView(getContext());
        mHandleView.setFrame(new CGRect(2, 2, 28, 28));
        mHandleView.setAlpha(1.0f);
        mHandleView.setWantsLayer(true);
        mHandleView.getLayer().setCornerRadius(14);
        mHandleView.getLayer().setBackgroundColor(UIColor.blackColor);
        mOffBackgroundView.setWantsLayer(true);
        mOffBackgroundView.getLayer().setCornerRadius(14);
        mOffBackgroundView.getLayer().setBorderColor(new UIColor(0x00/255.0, 0x00/255.0, 0x00/255.0, 0.1));
        mOffBackgroundView.getLayer().setBorderWidth(0.5);
        mOffBackgroundView.getLayer().setBackgroundColor(UIColor.whiteColor);

        addSubview(mOffBackgroundView);
        addSubview(mOnBackgroundView);
        addSubview(mHandleView);
    }

    @Override
    public void layoutSubviews() {
        super.layoutSubviews();
    }

}
