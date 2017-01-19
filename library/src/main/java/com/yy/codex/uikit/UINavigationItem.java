package com.yy.codex.uikit;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by cuiminghui on 2017/1/18.
 */

public class UINavigationItem {

    protected final Context mContext;

    public UINavigationItem(Context context) {
        mContext = context;
    }

    /* Title */

    private String mTitle;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
        if (mTitleView instanceof UILabel) {
            ((UILabel) mTitleView).setText(mTitle);
            if (mTitleView.getSuperview() != null) {
                mTitleView.getSuperview().layoutSubviews();
            }
        }
    }

    /* FrontView */

    UINavigationItemView mFrontView = null;

    public void setNeedsUpdate() {
        if (mFrontView != null) {
            UIView[] subviews = mFrontView.getSubviews();
            for (int i = 0; i < subviews.length; i++) {
                subviews[i].removeFromSuperview();
            }
            UIView titleView = getTitleView();
            if (titleView != null) {
                mFrontView.addSubview(getTitleView());
                mFrontView.mTitleView = titleView;
            }
            if (mLeftBarButtonItems.length > 0) {
                ArrayList<UIView> views = new ArrayList<>();
                for (int i = 0; i < mLeftBarButtonItems.length; i++) {
                    UIView contentView = mLeftBarButtonItems[i].getContentView(mContext);
                    if (contentView != null) {
                        mFrontView.addSubview(contentView);
                        views.add(contentView);
                    }
                }
                UIView[] viewsArr = new UIView[views.size()];
                views.toArray(viewsArr);
                mFrontView.mLeftViews = viewsArr;
            }
            else {
                mFrontView.mLeftViews = new UIView[0];
            }
            if (mRightBarButtonItems.length > 0) {
                ArrayList<UIView> views = new ArrayList<>();
                for (int i = 0; i < mRightBarButtonItems.length; i++) {
                    UIView contentView = mRightBarButtonItems[i].getContentView(mContext);
                    if (contentView != null) {
                        mFrontView.addSubview(contentView);
                        views.add(contentView);
                    }
                }
                UIView[] viewsArr = new UIView[views.size()];
                views.toArray(viewsArr);
                mFrontView.mRightViews = viewsArr;
            }
            else {
                mFrontView.mRightViews = new UIView[0];
            }
        }
    }

    /* TitleView */

    private UIView mTitleView;

    public UIView getTitleView() {
        if (mTitleView == null) {
            UILabel labelTitleView = new UILabel(mContext);
            labelTitleView.setFont(new UIFont(17));
            labelTitleView.setTextColor(UIColor.blackColor);
            labelTitleView.setText(mTitle);
            mTitleView = labelTitleView;
        }
        return mTitleView;
    }

    public void setTitleView(UIView titleView) {
        if (mTitleView != null) {
            mTitleView.removeFromSuperview();
        }
        mTitleView = titleView;
    }

    /* BarButtonItems */

    protected UIBarButtonItem[] mLeftBarButtonItems = new UIBarButtonItem[0];

    public UIBarButtonItem[] getLeftBarButtonItems() {
        return mLeftBarButtonItems;
    }

    public void setLeftBarButtonItems(UIBarButtonItem[] leftBarButtonItems) {
        this.mLeftBarButtonItems = leftBarButtonItems;
    }

    public void setLeftBarButtonItem(UIBarButtonItem leftBarButtonItem) {
        setLeftBarButtonItems(new UIBarButtonItem[]{leftBarButtonItem});
    }

    protected UIBarButtonItem[] mRightBarButtonItems = new UIBarButtonItem[0];

    public UIBarButtonItem[] getRightBarButtonItems() {
        return mRightBarButtonItems;
    }

    public void setRightBarButtonItems(UIBarButtonItem[] rightBarButtonItems) {
        mRightBarButtonItems = rightBarButtonItems;
    }

    public void setRightBarButtonItem(UIBarButtonItem rightBarButtonItem) {
        setRightBarButtonItems(new UIBarButtonItem[]{rightBarButtonItem});
    }

}
