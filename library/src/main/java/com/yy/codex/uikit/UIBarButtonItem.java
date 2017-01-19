package com.yy.codex.uikit;

import android.content.Context;

import com.yy.codex.UIBarItem;
import com.yy.codex.foundation.NSInvocation;

/**
 * Created by cuiminghui on 2017/1/18.
 */

public class UIBarButtonItem extends UIBarItem {

    protected NSInvocation mInvocation = null;

    public UIBarButtonItem() {

    }

    public UIBarButtonItem(UIImage image, Object target, String action) {
        mImage = image;
        if (target != null && action != null) {
            mInvocation = new NSInvocation(target, action);
        }
    }

    public UIBarButtonItem(String title, Object target, String action) {
        mTitle = title;
        if (target != null && action != null) {
            mInvocation = new NSInvocation(target, action);
        }
    }

    protected UIView mCustomView = null;

    public UIView getCustomView() {
        return mCustomView;
    }

    public void setCustomView(UIView customView) {
        this.mCustomView = customView;
    }

    protected double mWidth = 0.0;

    public double getWidth() {
        return mWidth;
    }

    public void setWidth(double width) {
        this.mWidth = width;
    }

    protected UIEdgeInsets mInsets;

    public UIEdgeInsets getInsets() {
        if (mInsets == null) {
            mInsets = new UIEdgeInsets(0, 8, 0, 8);
        }
        return mInsets;
    }

    public void setInsets(UIEdgeInsets insets) {
        mInsets = insets;
    }

    @Override
    public UIView getContentView(Context context) {
        if (mCustomView != null) {
            if (mWidth > 0.0) {
                mCustomView.setFrame(new CGRect(0, 0, mWidth, Math.min(44.0, mCustomView.getFrame().getHeight())));
            }
            mCustomView.setMarginInsets(getInsets());
            return mCustomView;
        }
        else if (mView == null) {
            UIButton button = new UIButton(context);
            if (mTitle != null) {
                button.setFont(new UIFont(17));
                button.setTitle(mTitle, UIControl.State.Normal);
            }
            if (mImage != null) {
                button.setImage(mImage, UIControl.State.Normal);
            }
            button.setFrame(new CGRect(0, 0, button.intrinsicContentSize().width, 44));
            mView = button;
            mView.setMarginInsets(getInsets());
        }
        return super.getContentView(context);
    }

}
