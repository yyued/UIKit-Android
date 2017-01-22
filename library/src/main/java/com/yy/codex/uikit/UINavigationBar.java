package com.yy.codex.uikit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.yy.codex.foundation.NSInvocation;

import java.util.HashMap;

/**
 * Created by cuiminghui on 2017/1/18.
 */

public class UINavigationBar extends UIView {

    public UINavigationBar(@NonNull Context context, @NonNull View view) {
        super(context, view);
    }

    public UINavigationBar(@NonNull Context context) {
        super(context);
    }

    public UINavigationBar(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public UINavigationBar(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UINavigationBar(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init() {
        super.init();
        UIConstraint constraint = new UIConstraint();
        constraint.centerHorizontally = true;
        constraint.top = "0";
        constraint.width = "100%";
        constraint.height = "44";
        setConstraint(constraint);
    }

    @Override
    public void willMoveToSuperview(@Nullable UIView newSuperview) {
        super.willMoveToSuperview(newSuperview);
        setBackgroundColor(getBarTintColor());
        setTintColor(getTintColor());
    }

    @Override
    public void didMoveToSuperview() {
        super.didMoveToSuperview();
        if (getContext() != null && getContext() instanceof Activity) {
            NSInvocation invocation = new NSInvocation(getContext(), "getSupportActionBar");
            Object actionBar = invocation.invoke();
            if (actionBar != null) {
                NSInvocation actionBarInvocation = new NSInvocation(actionBar, "hide");
                actionBarInvocation.invoke();
            }
            else {
                NSInvocation _invocation = new NSInvocation(getContext(), "getActionBar");
                Object _actionBar = _invocation.invoke();
                if (_actionBar != null) {
                    NSInvocation _actionBarInvocation = new NSInvocation(_actionBar, "hide");
                    _actionBarInvocation.invoke();
                }
            }
        }
    }

    /* Material Design */

    private boolean mMaterialDesignInitialized = false;

    @Override
    public void materialDesignDidChanged() {
        super.materialDesignDidChanged();
        if (!mMaterialDesignInitialized && isMaterialDesign()) {
            mMaterialDesignInitialized = true;
            setBarTintColor(new UIColor(0x3f/255.0, 0x51/255.0, 0xb5/255.0, 1.0));
            setTintColor(UIColor.whiteColor);
            setTitleTextAttributes(new HashMap<String, Object>(){{
                put(NSAttributedString.NSForegroundColorAttributeName, UIColor.whiteColor);
                put(NSAttributedString.NSFontAttributeName, UIFont.systemBold(17));
            }});
            if (getConstraint() != null) {
                getConstraint().height = "48";
            }
            layoutSubviews();
            resetItemsView();
            invalidate();
        }
    }

    /* BarTintColor */

    private UIColor mBarTintColor;

    public UIColor getBarTintColor() {
        if (mBarTintColor == null) {
            mBarTintColor = new UIColor(0xf8/255.0, 0xf8/255.0, 0xf8/255.0, 1.0);
        }
        return mBarTintColor;
    }

    public void setBarTintColor(UIColor barTintColor) {
        mBarTintColor = barTintColor;
        setBackgroundColor(barTintColor);
    }

    /* Title Attributes */

    @Nullable private HashMap<String, Object> mTitleTextAttributes;

    @Nullable
    public HashMap<String, Object> getTitleTextAttributes() {
        return mTitleTextAttributes;
    }

    public void setTitleTextAttributes(@Nullable HashMap<String, Object> titleTextAttributes) {
        mTitleTextAttributes = titleTextAttributes;
        resetItemsView();
    }

    /* Line Color */

    private UIColor mBottomLineColor;

    public UIColor getBottomLineColor() {
        if (mBottomLineColor == null) {
            mBottomLineColor = new UIColor(0xb2/255.0, 0xb2/255.0, 0xb2/255.0, 0.5);
        }
        return mBottomLineColor;
    }

    public void setBottomLineColor(UIColor bottomLineColor) {
        mBottomLineColor = bottomLineColor;
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (!isMaterialDesign()) {
            Paint paint = new Paint();
            paint.setColor(getBottomLineColor().toInt());
            canvas.drawLine(0, canvas.getHeight() - 1, canvas.getWidth(), canvas.getHeight() - 1, paint);
        }
    }

    /* Items */

    private UINavigationItem[] mItems = new UINavigationItem[0];
    private UINavigationItemView[] mItemsView = new UINavigationItemView[0];

    public void setItems(UINavigationItem[] items, boolean animated) {
        mItems = items;
        resetItemsProps();
        resetBackItems();
        resetItemsView();
    }

    public void pushNavigationItem(UINavigationItem item, boolean animated) {
        UINavigationItem[] items = new UINavigationItem[mItems.length + 1];
        for (int i = 0; i < mItems.length; i++) {
            items[i] = mItems[i];
        }
        items[items.length - 1] = item;
        mItems = items;
        resetItemsProps();
        resetBackItems();
        resetItemsView();
        if (animated) {
            doPushAnimation();
        }
    }

    protected void doPushAnimation() {
        if (mItemsView.length >= 2) {
            final UINavigationItemView topItemView = mItemsView[mItemsView.length - 1];
            final UINavigationItemView backItemView = mItemsView[mItemsView.length - 2];
            topItemView.setAlpha(1);
            backItemView.setAlpha(1);
            topItemView.animateToFront(false);
            backItemView.animateFromFrontToBack(false);
            UIView.animator.linear(0.75, new Runnable() {
                @Override
                public void run() {
                    topItemView.animateToFront(true);
                    backItemView.animateFromFrontToBack(true);
                }
            }, new Runnable() {
                @Override
                public void run() {
                    backItemView.setAlpha(0);
                }
            });
        }
    }

    public void popNavigationItem(boolean animated) {
        if (animated) {
            doPopAnimation(new Runnable() {
                @Override
                public void run() {
                    popNavigationItem(false);
                }
            });
        }
        else {
            if (mItems.length <= 0) {
                return;
            }
            UINavigationItem[] items = new UINavigationItem[mItems.length - 1];
            for (int i = 0; i < mItems.length; i++) {
                if (i < items.length) {
                    items[i] = mItems[i];
                }
            }
            mItems = items;
            resetItemsView();
        }
    }

    protected void doPopAnimation(@NonNull final Runnable completion) {
        if (mItemsView.length >= 2) {
            final UINavigationItemView topItemView = mItemsView[mItemsView.length - 1];
            final UINavigationItemView backItemView = mItemsView[mItemsView.length - 2];
            topItemView.setAlpha(1);
            backItemView.setAlpha(1);
            topItemView.animateToGone(false);
            backItemView.animateFromBackToFront(false);
            UIView.animator.linear(0.75, new Runnable() {
                @Override
                public void run() {
                    topItemView.animateToGone(true);
                    backItemView.animateFromBackToFront(true);
                }
            }, new Runnable() {
                @Override
                public void run() {
                    topItemView.setAlpha(0);
                    if (completion != null) {
                        completion.run();
                    }
                }
            });
        }
    }

    protected void resetItemsProps() {
        for (int i = 0; i < mItems.length; i++) {
            mItems[i].setNavigationBar(this);
        }
    }

    protected void resetBackItems() {
        for (int i = 0; i < mItems.length; i++) {
            if (i > 0) {
                if (mItems[i].getLeftBarButtonItems().length <= 0 || mItems[i].getLeftBarButtonItems()[0].isSystemBackItem()) {
                    mItems[i].setLeftBarButtonItem(mItems[i - 1].getBackBarButtonItem());
                }
            }
        }
    }

    protected void resetItemsView() {
        for (int i = 0; i < mItemsView.length; i++) {
            mItemsView[i].removeFromSuperview();
        }
        UINavigationItemView[] itemsView = new UINavigationItemView[mItems.length];
        for (int i = 0; i < mItems.length; i++) {
            UINavigationItemView frontView = isMaterialDesign() ? new UINavigationItemView_MaterialDesign(getContext()) : new UINavigationItemView(getContext());
            mItems[i].mFrontView = frontView;
            mItems[i].setNeedsUpdate();
            itemsView[i] = frontView;
            frontView.setConstraint(UIConstraint.full());
            addSubview(frontView);
            if (i < mItems.length - 1) {
                frontView.setAlpha(0);
            }
            else {
                frontView.setAlpha(1);
            }
        }
        mItemsView = itemsView;
        layoutSubviews();
    }

}
