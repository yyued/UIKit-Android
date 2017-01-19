package com.yy.codex.uikit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cuiminghui on 2017/1/18.
 */

class UINavigationItemView extends UIView {

    public UINavigationItemView(@NonNull Context context, @NonNull View view) {
        super(context, view);
    }

    public UINavigationItemView(@NonNull Context context) {
        super(context);
    }

    public UINavigationItemView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public UINavigationItemView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UINavigationItemView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    UIView[] mLeftViews;
    UIView   mTitleView;
    UIView[] mRightViews;

    @Override
    public void layoutSubviews() {
        super.layoutSubviews();
        if (mTitleView != null) {
            CGSize iSize = mTitleView.intrinsicContentSize();
            if (iSize.width > 0 && iSize.height > 0) {
                mTitleView.setFrame(new CGRect(
                        (getFrame().size.width - iSize.width) / 2.0,
                        (getFrame().size.height - iSize.height) / 2.0,
                        iSize.width,
                        iSize.height)
                );
            }
            else {
                mTitleView.setFrame(new CGRect(
                        (getFrame().size.width - mTitleView.getFrame().size.width) / 2.0,
                        (getFrame().size.height - mTitleView.getFrame().size.height) / 2.0,
                        mTitleView.getFrame().size.width,
                        mTitleView.getFrame().size.height)
                );
            }
        }
        if (mLeftViews != null && mLeftViews.length > 0) {
            double x = 0;
            for (int i = 0; i < mLeftViews.length; i++) {
                UIView contentView = mLeftViews[i];
                if (contentView != null) {
                    x += contentView.getMarginInsets().left;
                    CGSize iSize = contentView.intrinsicContentSize();
                    if (iSize.width > 0 && iSize.height > 0) {
                        contentView.setFrame(new CGRect(
                                x,
                                0,
                                iSize.width,
                                getFrame().getHeight())
                        );
                        x += iSize.width + contentView.getMarginInsets().right;
                    }
                    else {
                        contentView.setFrame(new CGRect(
                                x,
                                (getFrame().size.height - contentView.getFrame().size.height) / 2.0,
                                contentView.getFrame().size.width,
                                contentView.getFrame().size.height)
                        );
                        x += contentView.getFrame().size.width + contentView.getMarginInsets().right;
                    }
                }
            }
        }
        if (mRightViews != null && mRightViews.length > 0) {
            double rx = getFrame().size.width;
            for (int i = 0; i < mRightViews.length; i++) {
                UIView contentView = mRightViews[i];
                if (contentView != null) {
                    CGSize iSize = contentView.intrinsicContentSize();
                    if (iSize.width > 0 && iSize.height > 0) {
                        contentView.setFrame(new CGRect(
                                rx - iSize.width - contentView.getMarginInsets().right,
                                0,
                                iSize.width,
                                getFrame().getHeight())
                        );
                        rx -= iSize.width + contentView.getMarginInsets().right + contentView.getMarginInsets().left;
                    }
                    else {
                        contentView.setFrame(new CGRect(
                                rx - contentView.getFrame().size.width - contentView.getMarginInsets().right,
                                (getFrame().size.height - contentView.getFrame().size.height) / 2.0,
                                contentView.getFrame().size.width,
                                contentView.getFrame().size.height)
                        );
                        rx -= contentView.getFrame().size.width + contentView.getMarginInsets().right + contentView.getMarginInsets().left;;
                    }
                }
            }
        }
    }

    void animateFromFrontToBack(boolean reset) {
        if (!reset) {
            layoutSubviews();
            mTitleView.setAlpha(1);
            for (int i = 0; i < mLeftViews.length; i++) {
                mLeftViews[i].setAlpha(1);
            }
            for (int i = 0; i < mRightViews.length; i++) {
                mRightViews[i].setAlpha(1);
            }
        }
        else {
            layoutSubviews();
            mTitleView.setFrame(new CGRect(22, mTitleView.getFrame().getY(), mTitleView.getFrame().getWidth(), mTitleView.getFrame().getHeight()));
            mTitleView.setAlpha(0);
            for (int i = 0; i < mLeftViews.length; i++) {
                mLeftViews[i].setAlpha(0);
            }
            for (int i = 0; i < mRightViews.length; i++) {
                mRightViews[i].setAlpha(0);
            }
        }
    }

    void animateToFront(boolean reset) {
        if (!reset) {
            layoutSubviews();
            mTitleView.setFrame(new CGRect(getFrame().getWidth() - mTitleView.getFrame().getWidth(), mTitleView.getFrame().getY(), mTitleView.getFrame().getWidth(), mTitleView.getFrame().getHeight()));
            mTitleView.setAlpha(0);
            for (int i = 0; i < mLeftViews.length; i++) {
                mLeftViews[i].setAlpha(0);
            }
            for (int i = 0; i < mRightViews.length; i++) {
                mRightViews[i].setAlpha(0);
            }
        }
        else {
            layoutSubviews();
            mTitleView.setAlpha(1);
            for (int i = 0; i < mLeftViews.length; i++) {
                mLeftViews[i].setAlpha(1);
            }
            for (int i = 0; i < mRightViews.length; i++) {
                mRightViews[i].setAlpha(1);
            }
        }
    }

    void animateFromBackToFront(boolean reset) {
        if (!reset) {
            layoutSubviews();
            mTitleView.setFrame(new CGRect(22, mTitleView.getFrame().getY(), mTitleView.getFrame().getWidth(), mTitleView.getFrame().getHeight()));
            mTitleView.setAlpha(0);
            for (int i = 0; i < mLeftViews.length; i++) {
                mLeftViews[i].setAlpha(0);
            }
            for (int i = 0; i < mRightViews.length; i++) {
                mRightViews[i].setAlpha(0);
            }
        }
        else {
            layoutSubviews();
            mTitleView.setAlpha(1);
            for (int i = 0; i < mLeftViews.length; i++) {
                mLeftViews[i].setAlpha(1);
            }
            for (int i = 0; i < mRightViews.length; i++) {
                mRightViews[i].setAlpha(1);
            }
        }
    }

    void animateToGone(boolean reset) {
        if (!reset) {
            layoutSubviews();
            mTitleView.setAlpha(1);
            for (int i = 0; i < mLeftViews.length; i++) {
                mLeftViews[i].setAlpha(1);
            }
            for (int i = 0; i < mRightViews.length; i++) {
                mRightViews[i].setAlpha(1);
            }
        }
        else {
            layoutSubviews();
            mTitleView.setFrame(new CGRect(getFrame().getWidth() - mTitleView.getFrame().getWidth(), mTitleView.getFrame().getY(), mTitleView.getFrame().getWidth(), mTitleView.getFrame().getHeight()));
            mTitleView.setAlpha(0);
            for (int i = 0; i < mLeftViews.length; i++) {
                mLeftViews[i].setAlpha(0);
            }
            for (int i = 0; i < mRightViews.length; i++) {
                mRightViews[i].setAlpha(0);
            }
        }
    }

}
