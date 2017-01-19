package com.yy.codex.uikit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.yy.codex.foundation.NSLog;

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
    private boolean mOn;
    private boolean mActive;
    private UIViewAnimation mCurrentAnimation = null;

    // configuable
    private UIColor mOnThumbColor = UIColor.whiteColor;
    private UIColor mOnTraceColor = UIColor.greenColor;
    private @Nullable Bitmap mOnBitmap = null;
    private UIColor mOffThumbColor = UIColor.whiteColor;
    private UIColor mOffTraceColor = UIColor.whiteColor;
    private @Nullable Bitmap mOffBitmap = null;

    public void setOffTraceColor(UIColor mOffTraceColor) {
        this.mOffTraceColor = mOffTraceColor;
    }

    public void setOffThumbColor(UIColor mOffThumbColor) {
        this.mOffThumbColor = mOffThumbColor;
    }

    public void setOnThumbColor(UIColor mOnThumbColor) {
        this.mOnThumbColor = mOnThumbColor;
    }

    public void setOnTraceColor(UIColor mOnTraceColor) {
        this.mOnTraceColor = mOnTraceColor;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void init() {
        super.init();
        defaultValue();
        mOffBackgroundView = new UIView(getContext());
        mOffBackgroundView.setFrame(new CGRect(0, 0, 51, 32));
        mOffBackgroundView.setAlpha(1.0f);
        mOffBackgroundView.setWantsLayer(true);
        mOffBackgroundView.getLayer().setCornerRadius(16).setBorderWidth(2).setBorderColor(new UIColor(0x00/255.0, 0x00/255.0, 0x00/255.0, 0.1));
        mOffBackgroundView.getLayer().setBackgroundColor(mOffTraceColor);

        mOnBackgroundView = new UIView(getContext());
        mOnBackgroundView.setFrame(new CGRect(0, 0, 51, 32));
        mOnBackgroundView.setAlpha(0.0f);
        mOnBackgroundView.setWantsLayer(true);
        mOnBackgroundView.getLayer().setCornerRadius(16);
        mOnBackgroundView.getLayer().setBackgroundColor(mOnTraceColor);

        mHandleView = new UIView(getContext());
        mHandleView.setFrame(new CGRect(2, 2, 28, 28));
        mHandleView.setAlpha(1.0f);
        mHandleView.setWantsLayer(true);
        mHandleView.getLayer().setCornerRadius(14).setBorderWidth(0.5).setBorderColor(new UIColor(0x00/255.0, 0x00/255.0, 0x00/255.0, 0.15));
        mHandleView.getLayer().setBackgroundColor(mOffThumbColor);

        // mHandleView's shadow
        mHandleView.setElevation(3);
        mHandleView.setTranslationZ(8);
        mHandleView.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int shapeSize = 28 * 2;
                outline.setRoundRect(0, 0, shapeSize, shapeSize, shapeSize/2);
            }
        });

        addSubview(mOffBackgroundView);
        addSubview(mOnBackgroundView);
        addSubview(mHandleView);
    }

    private void defaultValue(){
        mOnThumbColor = UIColor.whiteColor;
        mOnTraceColor = new UIColor(0x2a/0xff, 0xd7/0xff, 0x68/0xff, 1);
        mOnBitmap = null;
        mOffThumbColor = UIColor.whiteColor;
        mOffTraceColor = UIColor.whiteColor;
        mOffBitmap = null;
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
        mCurrentAnimation = UIView.animator.spring(new Runnable() {
            @Override
            public void run() {
                float widthExpanded = 8;
                if (isActive) {
                    if (mOn) {
                        mHandleView.setFrame(new CGRect(21 - widthExpanded, 2, 28 + widthExpanded, 28));
                    } else {
                        mHandleView.setFrame(new CGRect(2, 2, 28 + widthExpanded, 28));
                        mOffBackgroundView.getLayer().setBackgroundColor(new UIColor(0x00 / 255.0, 0x00 / 255.0, 0x00 / 255.0, 0.1)); // darken +30%
                    }
                } else {
                    if (mOn) {
                        mHandleView.setFrame(new CGRect(21, 2, 28, 28));
                    } else {
                        mHandleView.setFrame(new CGRect(2, 2, 28, 28));
                        mOffBackgroundView.getLayer().setBackgroundColor(mOffTraceColor);
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
        mCurrentAnimation = UIView.animator.spring(new Runnable() {
            @Override
            public void run() {
                if (mOn) {
                    mOnBackgroundView.setAlpha(1.0f);
                    mOnBackgroundView.getLayer().setBackgroundColor(mOnTraceColor);
                    mOffBackgroundView.setAlpha(0.0f);
                    mHandleView.setFrame(new CGRect(21, 2, 28, 28));
                    mHandleView.getLayer().setBackgroundColor(mOnThumbColor);
                } else {
                    mOffBackgroundView.setAlpha(1.0f);
                    mOffBackgroundView.getLayer().setBackgroundColor(mOffTraceColor);
                    mOnBackgroundView.setAlpha(0.0f);
                    mHandleView.setFrame(new CGRect(2, 2, 28, 28));
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
            mOnBackgroundView.getLayer().setBackgroundColor(mOnTraceColor);
            mOffBackgroundView.setAlpha(0.0f);
            mHandleView.setFrame(new CGRect(21, 2, 28, 28));
            mHandleView.getLayer().setBackgroundColor(mOnThumbColor);
        }
        else {
            mOffBackgroundView.setAlpha(1.0f);
            mOffBackgroundView.getLayer().setBackgroundColor(mOffTraceColor);
            mOnBackgroundView.setAlpha(0.0f);
            mHandleView.setFrame(new CGRect(2, 2, 28, 28));
            mHandleView.getLayer().setBackgroundColor(mOffThumbColor);
        }
    }
}
