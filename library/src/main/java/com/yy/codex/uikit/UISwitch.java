package com.yy.codex.uikit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

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
    private float mHandleRadius = 30; // realRdius_28 + shadowXY_2
    private boolean mOn;
    private boolean mActive;
    private UIViewAnimation mCurrentAnimation = null;

    // configuable
    private UIColor mOnThumbColor = UIColor.Companion.getWhiteColor();
    private UIColor mOnTrackColor = new UIColor(0x00/255.0, 0xe3/255.0, 0x64/255.0, 1);
    private @Nullable Bitmap mOnBitmap = null;
    private UIColor mOffThumbColor = UIColor.Companion.getWhiteColor();
    private UIColor mOffTrackColor = UIColor.Companion.getWhiteColor();
    private @Nullable Bitmap mOffBitmap = null;

    public void setOffTraceColor(UIColor mOffTraceColor) {
        this.mOffTrackColor = mOffTraceColor;
    }

    public void setOffThumbColor(UIColor mOffThumbColor) {
        this.mOffThumbColor = mOffThumbColor;
    }

    public void setOnThumbColor(UIColor mOnThumbColor) {
        this.mOnThumbColor = mOnThumbColor;
    }

    public void setOnTraceColor(UIColor mOnTraceColor) {
        this.mOnTrackColor = mOnTraceColor;
    }

    private void defaultValue(){
        mHandleRadius = 30;
        mOnThumbColor = UIColor.Companion.getWhiteColor();
        mOnTrackColor = new UIColor(0x00/255.0, 0xe3/255.0, 0x64/255.0, 1);
        mOnBitmap = null;
        mOffThumbColor = UIColor.Companion.getWhiteColor();
        mOffTrackColor = UIColor.Companion.getWhiteColor();
        mOffBitmap = null;
    }

    @Override
    protected void init() {
        super.init();
        defaultValue();
        mOffBackgroundView = new UIView(getContext());
        mOffBackgroundView.setFrame(new CGRect(0, 0, 51, 32));
        mOffBackgroundView.setAlpha(1.0f);
        mOffBackgroundView.setWantsLayer(true);
        mOffBackgroundView.getLayer().setCornerRadius(16);
        mOffBackgroundView.getLayer().setBorderWidth(2);
        mOffBackgroundView.getLayer().setBorderColor(new UIColor(0x00/255.0, 0x00/255.0, 0x00/255.0, 0.1));
        mOffBackgroundView.getLayer().setBackgroundColor(mOffTrackColor);

        mOnBackgroundView = new UIView(getContext());
        mOnBackgroundView.setFrame(new CGRect(0, 0, 51, 32));
        mOnBackgroundView.setAlpha(0.0f);
        mOnBackgroundView.setWantsLayer(true);
        mOnBackgroundView.getLayer().setCornerRadius(16);
        mOnBackgroundView.getLayer().setBackgroundColor(mOnTrackColor);

        mHandleView = new UIView(getContext());
        mHandleView.setFrame(new CGRect(2, 2, mHandleRadius, mHandleRadius));
        mHandleView.setAlpha(1.0f);
        mHandleView.setWantsLayer(true);
        mHandleView.getLayer().setShadowX(2);
        mHandleView.getLayer().setShadowY(2);
        mHandleView.getLayer().setShadowRadius(0.5);
        mHandleView.getLayer().setShadowColor(new UIColor(.3, .3, .3, .2));
        mHandleView.getLayer().setCornerRadius(14);
        mHandleView.getLayer().setBorderWidth(0.5);
        mHandleView.getLayer().setBorderColor(new UIColor(0x00/255.0, 0x00/255.0, 0x00/255.0, 0.15));
        mHandleView.getLayer().setBackgroundColor(mOffThumbColor);

        addSubview(mOffBackgroundView);
        addSubview(mOnBackgroundView);
        addSubview(mHandleView);
    }

    @Override
    public void layoutSubviews() {
        super.layoutSubviews();
    }


    @Override
    protected void onEvent(Event event) {
        super.onEvent(event);
        switch (event){
            case TouchDown:
//                NSLog.warn("---");
//                NSLog.warn("touch down");
                if (!mActive){
                    setActiveAnimated(true);
                }
                break;
            case TouchDragExit:
//                NSLog.warn("drag exit");
                setOnAnimated(this.mOn);
                break;
            case TouchUpInside:
//                NSLog.warn("touch upInside");
                setOnAnimated(!this.mOn);
                break;
            case TouchUpOutside:
//                NSLog.warn("touch upOutside");
                setOnAnimated(this.mOn);
                break;
        }
    }

    private void setActiveAnimated(final boolean isActive){
        this.mActive = isActive;
        cancelAnimation();
        mCurrentAnimation = UIViewAnimator.INSTANCE.spring(new Runnable() {
            @Override
            public void run() {
                float widthExpanded = 8;
                if (isActive) {
                    if (mOn) {
                        mHandleView.setFrame(new CGRect(21 - widthExpanded, 2, mHandleRadius + widthExpanded, mHandleRadius));
                    } else {
                        mHandleView.setFrame(new CGRect(2, 2, mHandleRadius + widthExpanded, mHandleRadius));
                        mOffBackgroundView.getLayer().setBackgroundColor(new UIColor(0x00 / 255.0, 0x00 / 255.0, 0x00 / 255.0, 0.1)); // darken +30%
                    }
                } else {
                    if (mOn) {
                        mHandleView.setFrame(new CGRect(21, 2, mHandleRadius, mHandleRadius));
                    } else {
                        mHandleView.setFrame(new CGRect(2, 2, mHandleRadius, mHandleRadius));
                        mOffBackgroundView.getLayer().setBackgroundColor(mOffTrackColor);
                    }
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                mActive = false;
            }
        });
    }

    public void setOnAnimated(boolean isOn){
        this.mOn = isOn;
        cancelAnimation();
        mCurrentAnimation = UIViewAnimator.INSTANCE.spring(new Runnable() {
            @Override
            public void run() {
                if (mOn) {
                    mOnBackgroundView.setAlpha(1.0f);
                    mOnBackgroundView.getLayer().setBackgroundColor(mOnTrackColor);
                    mOffBackgroundView.setAlpha(0.0f);
                    mHandleView.setFrame(new CGRect(21, 2, mHandleRadius, mHandleRadius));
                    mHandleView.getLayer().setBackgroundColor(mOnThumbColor);
                } else {
                    mOffBackgroundView.setAlpha(1.0f);
                    mOffBackgroundView.getLayer().setBackgroundColor(mOffTrackColor);
                    mOnBackgroundView.setAlpha(0.0f);
                    mHandleView.setFrame(new CGRect(2, 2, mHandleRadius, mHandleRadius));
                    mHandleView.getLayer().setBackgroundColor(mOffThumbColor);
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                mActive = false;
            }
        });
    }

    private void cancelAnimation(){
        if (mCurrentAnimation != null){
            mCurrentAnimation.cancel();
        }
    }

    public void setOn(boolean isOn){
        if (this.mOn == isOn) {
            return;
        }
        this.mOn = isOn;
        if (isOn){
            mOnBackgroundView.setAlpha(1.0f);
            mOnBackgroundView.getLayer().setBackgroundColor(mOnTrackColor);
            mOffBackgroundView.setAlpha(0.0f);
            mHandleView.setFrame(new CGRect(21, 2, mHandleRadius, mHandleRadius));
            mHandleView.getLayer().setBackgroundColor(mOnThumbColor);
        }
        else {
            mOffBackgroundView.setAlpha(1.0f);
            mOffBackgroundView.getLayer().setBackgroundColor(mOffTrackColor);
            mOnBackgroundView.setAlpha(0.0f);
            mHandleView.setFrame(new CGRect(2, 2, mHandleRadius, mHandleRadius));
            mHandleView.getLayer().setBackgroundColor(mOffThumbColor);
        }
    }
}
