package com.yy.codex.uikit;

import android.content.Context;

/**
 * Created by PonyCui_Home on 2017/1/20.
 */

public class UINavigationController extends UIViewController {

    public UINavigationController(Context context) {
        super(context);
    }

    public void setRootViewController(UIViewController rootViewController) {
        setViewControllers(new UIViewController[]{rootViewController});
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        getView().addSubview(getNavigationBar());
    }

    public void setViewControllers(UIViewController[] viewControllers) {
        UIViewController[] oldValue = getChildViewControllers();
        for (int i = 0; i < oldValue.length; i++) {
            oldValue[i].removeFromParentViewController();
        }
        for (int i = 0; i < viewControllers.length; i++) {
            addChildViewController(viewControllers[i]);
        }
        resetNavigationItems();
        resetChildViews();
    }

    private boolean beingPush = false;

    public void pushViewController(UIViewController viewController, boolean animated) {
        beingPush = true;
        addChildViewController(viewController);
        resetNavigationItems();
        resetChildViews();
        beingPush = false;
    }

    private boolean beingPop = false;

    public void popViewController(boolean animated) {

    }

    protected void resetNavigationItems() {
        UIViewController[] viewControllers = getChildViewControllers();
        UINavigationItem[] navigationItems = new UINavigationItem[viewControllers.length];
        for (int i = 0; i < viewControllers.length; i++) {
            navigationItems[i] = viewControllers[i].getNavigationItem();
        }
        getNavigationBar().setItems(navigationItems, false);
    }

    protected void resetChildViews() {
        if (beingPush) {
            UIViewController[] childViewControllers = getChildViewControllers();
            if (childViewControllers.length > 0) {
                UIView currentView = childViewControllers[childViewControllers.length - 1].getView();
                currentView.setFrame(new CGRect(0, topLayoutLength(), getView().getFrame().getWidth(), getView().getFrame().getHeight() - topLayoutLength()));
                getView().addSubview(currentView);
            }
        }
        else if (beingPop) {
            UIViewController[] childViewControllers = getChildViewControllers();
            if (childViewControllers.length > 0) {
                UIView currentView = childViewControllers[childViewControllers.length - 1].getView();
                currentView.removeFromSuperview();
            }
        }
        else {
            UIView[] subviews = getView().getSubviews();
            for (int i = 0; i < subviews.length; i++) {
                if (subviews[i] instanceof UINavigationBar) {
                    continue;
                }
                subviews[i].removeFromSuperview();
            }
            UIViewController[] childViewControllers = getChildViewControllers();
            for (int i = 0; i < childViewControllers.length; i++) {
                UIView currentView = childViewControllers[i].getView();
                currentView.setFrame(new CGRect(0, topLayoutLength(), getView().getFrame().getWidth(), getView().getFrame().getHeight() - topLayoutLength()));
                getView().addSubview(currentView);
            }
        }
        getView().bringSubviewToFront(getNavigationBar());
    }

    @Override
    public void viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews();
        UIViewController[] childViewControllers = getChildViewControllers();
        for (int i = 0; i < childViewControllers.length; i++) {
            UIView currentView = childViewControllers[i].getView();
            currentView.setFrame(new CGRect(0, topLayoutLength(), getView().getFrame().getWidth(), getView().getFrame().getHeight() - topLayoutLength()));
        }
    }

    /* NavigationBar */

    private UINavigationBar mNavigationBar;

    public UINavigationBar getNavigationBar() {
        if (mNavigationBar == null) {
            mNavigationBar = new UINavigationBar(getContext());
        }
        return mNavigationBar;
    }

    public void setNavigationBar(UINavigationBar navigationBar) {
        mNavigationBar = navigationBar;
    }

}
