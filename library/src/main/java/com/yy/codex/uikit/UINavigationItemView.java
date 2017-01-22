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
            if (iSize.getWidth() > 0 && iSize.getHeight() > 0) {
                mTitleView.setFrame(new CGRect(
                        (getFrame().getSize().getWidth() - iSize.getWidth()) / 2.0,
                        (getFrame().getSize().getHeight() - iSize.getHeight()) / 2.0,
                        iSize.getWidth(),
                        iSize.getHeight())
                );
            }
            else {
                mTitleView.setFrame(new CGRect(
                        (getFrame().getSize().getWidth() - mTitleView.getFrame().getSize().getWidth()) / 2.0,
                        (getFrame().getSize().getHeight() - mTitleView.getFrame().getSize().getHeight()) / 2.0,
                        mTitleView.getFrame().getSize().getWidth(),
                        mTitleView.getFrame().getSize().getHeight())
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
        if (mRightViews != null && mRightViews.length > 0) {
            double rx = getFrame().getSize().getWidth();
            for (int i = 0; i < mRightViews.length; i++) {
                UIView contentView = mRightViews[i];
                if (contentView != null) {
                    CGSize iSize = contentView.intrinsicContentSize();
                    if (iSize.getWidth() > 0 && iSize.getHeight() > 0) {
                        contentView.setFrame(new CGRect(
                                rx - iSize.getWidth() - contentView.getMarginInsets().right,
                                0,
                                iSize.getWidth(),
                                getFrame().getHeight())
                        );
                        rx -= iSize.getWidth() + contentView.getMarginInsets().right + contentView.getMarginInsets().left;
                    }
                    else {
                        contentView.setFrame(new CGRect(
                                rx - contentView.getFrame().getSize().getWidth() - contentView.getMarginInsets().right,
                                (getFrame().getSize().getHeight() - contentView.getFrame().getSize().getHeight()) / 2.0,
                                contentView.getFrame().getSize().getWidth(),
                                contentView.getFrame().getSize().getHeight())
                        );
                        rx -= contentView.getFrame().getSize().getWidth() + contentView.getMarginInsets().right + contentView.getMarginInsets().left;;
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
