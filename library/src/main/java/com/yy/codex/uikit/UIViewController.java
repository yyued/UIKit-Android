package com.yy.codex.uikit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yy.codex.foundation.NSLog;

import java.lang.ref.WeakReference;

/**
 * Created by cuiminghui on 2017/1/19.
 */

public class UIViewController implements UIResponder {

    protected Context mContext;

    public UIViewController() {
        // Context will be set by ParentViewController.
    }

    public UIViewController(Context context) {
        mContext = context;
    }

    /* UIResponder */

    private WeakReference<UIResponder> mNextResponder;

    @Override
    public void setNextResponder(@NonNull UIResponder responder) {
        this.mNextResponder = new WeakReference<>(responder);
    }

    @Nullable
    @Override
    public UIResponder getNextResponder() {
        UIResponder nextResponder = this.mNextResponder != null ? this.mNextResponder.get() : null;
        if (nextResponder != null) {
            return nextResponder;
        }
        return null;
    }

    @Override
    public void touchesBegan(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        if (getNextResponder() != null) {
            getNextResponder().touchesBegan(touches, event);
        }
    }

    @Override
    public void touchesMoved(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        if (getNextResponder() != null) {
            getNextResponder().touchesMoved(touches, event);
        }
    }

    @Override
    public void touchesEnded(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        if (getNextResponder() != null) {
            getNextResponder().touchesEnded(touches, event);
        }
    }

    /* View Manager */

    private UIView mView;
    private boolean mViewLoaded = false;

    public UIView getView() {
        if (mView == null) {
            loadView();
        }
        return mView;
    }

    public void setView(UIView view) {
        mView = view;
        if (!mViewLoaded) {
            mViewLoaded = true;
            viewDidLoad();
        }
    }

    public void loadView() {
        if (mView == null && mContext != null) {
            mView = new UIView(mContext);
            mView.setBackgroundColor(UIColor.whiteColor);
        }
    }

    public void loadViewIfNeeded() {
        loadView();
    }

    public boolean isViewLoaded() {
        return mViewLoaded;
    }

    public void viewDidLoad() {
    }

    public void viewWillAppear(boolean animated) {
    }

    public void viewDidAppear(boolean animated) {

    }

    public void viewWillDisappear(boolean animated) {

    }

    public void viewDidDisappear(boolean animated) {

    }

    public void viewWillLayoutSubviews() {

    }

    public void viewDidLayoutSubviews() {

    }

    /* ChildViewControllers */

    private UIViewController[] childViewControllers = new UIViewController[0];

    public void addChildViewController(UIViewController childController) {

    }

    public void removeFromParentViewController() {

    }

}
