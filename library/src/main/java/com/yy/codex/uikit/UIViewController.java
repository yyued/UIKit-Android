package com.yy.codex.uikit;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by cuiminghui on 2017/1/19.
 */

public class UIViewController implements UIResponder {

    @NonNull private Context mContext;

    public Context getContext() {
        return mContext;
    }

    public void postDelay(Runnable runnable, long millisecond) {
        if (getView() != null) {
            getView().postDelayed(runnable, millisecond);
        }
    }

    public void runOnUIThread(Runnable runnable) {
        if (mContext instanceof Activity) {
            ((Activity) mContext).runOnUiThread(runnable);
        }
    }

    public UIViewController(@NonNull Context context) {
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

    /* Props */

    @Nullable private String mTitle;

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(@Nullable String title) {
        mTitle = title;
        getNavigationItem().setTitle(title);
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
        view.setNextResponder(this);
        mView = view;
        if (!mViewLoaded) {
            mViewLoaded = true;
            viewDidLoad();
        }
    }

    public void loadView() {
        if (mView == null && getContext() != null) {
            setView(new UIView(getContext()));
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
        for (int i = 0; i < mChildViewControllers.length; i++) {
            mChildViewControllers[i].loadViewIfNeeded();
        }
    }

    public void viewWillAppear(boolean animated) {
        for (int i = 0; i < mChildViewControllers.length; i++) {
            mChildViewControllers[i].viewWillAppear(animated);
        }
    }

    public void viewDidAppear(boolean animated) {
        for (int i = 0; i < mChildViewControllers.length; i++) {
            mChildViewControllers[i].viewDidAppear(animated);
        }
    }

    public void viewWillDisappear(boolean animated) {
        for (int i = 0; i < mChildViewControllers.length; i++) {
            mChildViewControllers[i].viewWillDisappear(animated);
        }
    }

    public void viewDidDisappear(boolean animated) {
        for (int i = 0; i < mChildViewControllers.length; i++) {
            mChildViewControllers[i].viewDidDisappear(animated);
        }
    }

    public void viewWillLayoutSubviews() {
        for (int i = 0; i < mChildViewControllers.length; i++) {
            mChildViewControllers[i].viewWillLayoutSubviews();
        }
    }

    public void viewDidLayoutSubviews() {
        for (int i = 0; i < mChildViewControllers.length; i++) {
            mChildViewControllers[i].viewDidLayoutSubviews();
        }
    }

    /* ChildViewControllers */

    @NonNull private UIViewController[] mChildViewControllers = new UIViewController[0];
    @Nullable private WeakReference<UIViewController> mParentViewController;

    public UIViewController[] getChildViewControllers() {
        return mChildViewControllers;
    }

    @Nullable
    public UIViewController getParentViewController() {
        return mParentViewController.get();
    }

    public void addChildViewController(@NonNull UIViewController childController) {
        if (childController == null) {
            return;
        }
        childController.mParentViewController = new WeakReference<UIViewController>(this);
        childController.setNextResponder(this);
        boolean contains = false;
        for (int i = 0; i < mChildViewControllers.length; i++) {
            if (mChildViewControllers[i] == childController) {
                contains = true;
            }
        }
        if (!contains) {
            UIViewController[] childViewControllers = new UIViewController[mChildViewControllers.length + 1];
            for (int i = 0; i < mChildViewControllers.length; i++) {
                childViewControllers[i] = mChildViewControllers[i];
            }
            childViewControllers[childViewControllers.length - 1] = childController;
            mChildViewControllers = childViewControllers;
        }
    }

    public void removeFromParentViewController() {
        UIViewController parentViewController = mParentViewController.get();
        if (parentViewController != null) {
            ArrayList<UIViewController> viewControllers = new ArrayList<>();
            for (int i = 0; i < parentViewController.mChildViewControllers.length; i++) {
                if (parentViewController.mChildViewControllers[i] != this) {
                    viewControllers.add(parentViewController.mChildViewControllers[i]);
                }
            }
            parentViewController.mChildViewControllers = new UIViewController[viewControllers.size()];
            viewControllers.toArray(parentViewController.mChildViewControllers);
            parentViewController = null;
        }
    }

    /* NavigationController */

    @Nullable
    public UINavigationController navigationController() {
        UIResponder nextResponder = getNextResponder();
        while (nextResponder != null) {
            if (nextResponder instanceof UINavigationController) {
                return (UINavigationController) nextResponder;
            }
            nextResponder = nextResponder.getNextResponder();
        }
        return null;
    }

    private UINavigationItem mNavigationItem;

    public UINavigationItem getNavigationItem() {
        if (mNavigationItem == null) {
            mNavigationItem = new UINavigationItem(getContext());
        }
        return mNavigationItem;
    }

}
