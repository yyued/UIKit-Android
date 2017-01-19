package com.yy.codex.uikit;

import android.content.Context;
import android.support.annotation.Nullable;

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

    @Nullable
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

    @Nullable
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

    @Nullable
    protected UIBarButtonItem mBackBarButtonItem = null;

    public UIBarButtonItem getBackBarButtonItem() {
        if (mBackBarButtonItem == null) {
            mBackBarButtonItem = new UIBarButtonItem(mTitle == null ? "Back" : mTitle, null, null);
            if (UIScreen.mainScreen.scale() == 1.0) {
                UIImage arrowImage = new UIImage("iVBORw0KGgoAAAANSUhEUgAAABoAAAAsCAYAAAB7aah+AAAAAXNSR0IArs4c6QAAATpJREFUWAm92LsNwjAQANAzFIzATJQwCAPQABJiAMagoUCCHegYgwkQlfEFQkLikPviJo5t3fNHyiUB8Cj7OGyGHTQb1Pe7OIIrHGERl/VYoX6jriNygwNEmLxjrWAb1li3g9pIOe8Cs4G6kQ/WOrSyh3ztR3Df7roV0ZAzjGEqhxgIzMNDBjERPAY+JED4kBDhQQqEDikRGmSA9ENGyG/IEOmGjJE85IC0ISfkG3JEKsgZeUF/QBCivpxEHKwpr6c3bVWnlMBmmFskYJUmaFiRLSVYBeE0HbFvyBFrQ05YHnLAuiFj7DdkiPVDRhgNMsDokBLjQQqMDwkxGSTA5BAT00EMjJr4MGS+YH5KH1rpu+ScH5BaA1z0Kyqjd6WYAaxhE1Z2EIJN7I1gly1Ux9J24Uqwya9kftE8AedMD8V9MQduAAAAAElFTkSuQmCC");
                arrowImage.setScale(1.0);
                mBackBarButtonItem.setImage(arrowImage);
            }
            else if (UIScreen.mainScreen.scale() == 2.0) {
                UIImage arrowImage = new UIImage("iVBORw0KGgoAAAANSUhEUgAAABoAAAAsCAYAAAB7aah+AAAAAXNSR0IArs4c6QAAATpJREFUWAm92LsNwjAQANAzFIzATJQwCAPQABJiAMagoUCCHegYgwkQlfEFQkLikPviJo5t3fNHyiUB8Cj7OGyGHTQb1Pe7OIIrHGERl/VYoX6jriNygwNEmLxjrWAb1li3g9pIOe8Cs4G6kQ/WOrSyh3ztR3Df7roV0ZAzjGEqhxgIzMNDBjERPAY+JED4kBDhQQqEDikRGmSA9ENGyG/IEOmGjJE85IC0ISfkG3JEKsgZeUF/QBCivpxEHKwpr6c3bVWnlMBmmFskYJUmaFiRLSVYBeE0HbFvyBFrQ05YHnLAuiFj7DdkiPVDRhgNMsDokBLjQQqMDwkxGSTA5BAT00EMjJr4MGS+YH5KH1rpu+ScH5BaA1z0Kyqjd6WYAaxhE1Z2EIJN7I1gly1Ux9J24Uqwya9kftE8AedMD8V9MQduAAAAAElFTkSuQmCC");
                arrowImage.setScale(2.0);
                mBackBarButtonItem.setImage(arrowImage);
            }
            else {
                UIImage arrowImage = new UIImage("iVBORw0KGgoAAAANSUhEUgAAACcAAABCCAYAAADUms/cAAAAAXNSR0IArs4c6QAAAbFJREFUaAXt2stNxDAQBuDfSNQBpWwVcORRBhHSXqALkOBCG1SCdkUfeD2CKC8ncex5+MAcR5b8aRxLmUmA/1iowKO/QuMvYivOYkm1XONv8IMPeHzGgE4NMt6IYMBrgLUFOsBhhyd3bJfa4Kaw1jMA6uPmYROgLm4dNgDq4dJhLfBLB7cV5sIdBu7lcZmwcGvfZHEFMDpbOVwhTA7HAJPBMcH4cYwwXhwzjA8nAOPBCcHKcYKwMpwwLB+nAMvDKcG24xRh23DKsHScASwNZwRbxxnClnHGsHlcBbA4rhLYFFcRbIirDNbhKoQR7nfC4zO6MB/mQ8LRtYY51fO4w7N7lzJ2ONqhMuAQlwsMcw0aH3BXcIrLBQoccRxXCXAeVwFwGWcMXMcZAtNwRsB0nAFwG04ZuB2nCMzDKQHzcQrAMpwwsBwnCOTBCQH5cAJAXhwzkB/HCJTB5QJHb9RyOAagLK4QKI8rAOrgCPjgb0Pr/tL7VYOyS6H0Gb0lpPbFDkecY6dXuVTgHwx7d9DHEXKugj0YLbPBxYAjmC2uDwS+6Rmjo6R0PdH4a+z9ZT2gRMkJbMiClVJCMU0AAAAASUVORK5CYII=");
                arrowImage.setScale(3.0);
                mBackBarButtonItem.setImage(arrowImage);
            }
            mBackBarButtonItem.setImageInsets(new UIEdgeInsets(0, 0, 0, 5));
            mBackBarButtonItem.mSystemBackItem = true;
        }
        return mBackBarButtonItem;
    }

    public void setBackBarButtonItem(UIBarButtonItem backBarButtonItem) {
        mBackBarButtonItem = backBarButtonItem;
    }
}
