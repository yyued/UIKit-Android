package com.yy.codex.uikit;

import android.content.Context;

import com.yy.codex.foundation.NSInvocation;
import com.yy.codex.uikit.UIControl;
import com.yy.codex.uikit.UIEdgeInsets;
import com.yy.codex.uikit.UIImage;
import com.yy.codex.uikit.UIView;

import java.util.EnumSet;
import java.util.HashMap;

/**
 * Created by cuiminghui on 2017/1/18.
 */

public class UIBarItem {

    public UIBarItem() {

    }

    protected boolean mEnabled = true;

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    protected String mTitle = null;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    protected UIImage mImage = null;

    public UIImage getImage() {
        return mImage;
    }

    public void setImage(UIImage image) {
        mImage = image;
    }

    protected UIEdgeInsets mImageInsets = new UIEdgeInsets(0, 0, 0, 0);

    public UIEdgeInsets getImageInsets() {
        return mImageInsets;
    }

    public void setImageInsets(UIEdgeInsets imageInsets) {
        this.mImageInsets = imageInsets;
    }

    protected int mTag = 0;

    public int getTag() {
        return mTag;
    }

    public void setTag(int tag) {
        this.mTag = tag;
    }

    protected HashMap<EnumSet<UIControl.State>, HashMap<String, Object>> mTitleTextAttributes = new HashMap<>();

    public void setTitleTextAttributes(HashMap<EnumSet<UIControl.State>, HashMap<String, Object>> titleTextAttributes) {
        this.mTitleTextAttributes = titleTextAttributes;
    }

    public HashMap<String, Object> titleTextAttributes(EnumSet<UIControl.State> state) {
        return mTitleTextAttributes.get(state);
    }

    protected UIView mView;

    public UIView getContentView(Context context) {
        return mView;
    }

}
