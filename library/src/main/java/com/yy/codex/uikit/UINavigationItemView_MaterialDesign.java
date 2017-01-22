package com.yy.codex.uikit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cuiminghui on 2017/1/19.
 */

public class UINavigationItemView_MaterialDesign extends UINavigationItemView {

    public UINavigationItemView_MaterialDesign(@NonNull Context context, @NonNull View view) {
        super(context, view);
    }

    public UINavigationItemView_MaterialDesign(@NonNull Context context) {
        super(context);
    }

    public UINavigationItemView_MaterialDesign(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public UINavigationItemView_MaterialDesign(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UINavigationItemView_MaterialDesign(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void layoutSubviews() {
        super.layoutSubviews();
        double x = 0.0;
        if (mLeftViews != null && mLeftViews.length > 0) {
            for (int i = 0; i < mLeftViews.length; i++) {
                UIView contentView = mLeftViews[i];
                if (contentView != null) {
                    x += contentView.getMarginInsets().left;
                    CGSize iSize = contentView.intrinsicContentSize();
                    if (iSize.getWidth() > 0 && iSize.getHeight() > 0) {
                        contentView.setFrame(new CGRect(
                                x,
                                0,
                                iSize.getWidth(),
                                getFrame().getHeight())
                        );
                        x += iSize.getWidth() + contentView.getMarginInsets().right;
                    }
                    else {
                        contentView.setFrame(new CGRect(
                                x,
                                (getFrame().getSize().getHeight() - contentView.getFrame().getSize().getHeight()) / 2.0,
                                contentView.getFrame().getSize().getWidth(),
                                contentView.getFrame().getSize().getHeight())
                        );
                        x += contentView.getFrame().getSize().getWidth() + contentView.getMarginInsets().right;
                    }
                }
            }
        }
        if (mTitleView != null) {
            if (mLeftViews != null && mLeftViews.length > 0) {
                x += 0.0;
            }
            else {
                x += 12.0;
            }
            CGSize iSize = mTitleView.intrinsicContentSize();
            if (iSize.getWidth() > 0 && iSize.getHeight() > 0) {
                mTitleView.setFrame(new CGRect(
                        x,
                        (getFrame().getSize().getHeight() - iSize.getHeight()) / 2.0,
                        iSize.getWidth(),
                        iSize.getHeight())
                );
            }
            else {
                mTitleView.setFrame(new CGRect(
                        x,
                        (getFrame().getSize().getHeight() - mTitleView.getFrame().getSize().getHeight()) / 2.0,
                        mTitleView.getFrame().getSize().getWidth(),
                        mTitleView.getFrame().getSize().getHeight())
                );
            }
        }
    }

    @Override
    void animateFromBackToFront(boolean reset) {
        if (!reset) {
            setAlpha(0);
        }
        else {
            setAlpha(1);
        }
    }

    @Override
    void animateToFront(boolean reset) {
        if (!reset) {
            setAlpha(0);
        }
        else {
            setAlpha(1);
        }
    }

    @Override
    void animateFromFrontToBack(boolean reset) {
        if (!reset) {
            setAlpha(1);
        }
        else {
            setAlpha(0);
        }
    }

    @Override
    void animateToGone(boolean reset) {
        if (!reset) {
            setAlpha(1);
        }
        else {
            setAlpha(0);
        }
    }

}
