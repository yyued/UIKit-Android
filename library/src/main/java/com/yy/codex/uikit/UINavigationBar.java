package com.yy.codex.uikit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

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
        setBackgroundColor(getBarTintColor());
//        addSubview(getBottomLine());
    }

    @Override
    public void didMoveToSuperview() {
        super.didMoveToSuperview();
        if (getContext() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
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
        Paint paint = new Paint();
        paint.setColor(getBottomLineColor().toInt());
        canvas.drawLine(0, canvas.getHeight() - 1, canvas.getWidth(), canvas.getHeight() - 1, paint);
    }

    /* Items */

    private UINavigationItem[] mItems = new UINavigationItem[0];
    private UIView[] mItemsView = new UIView[0];

    public void setItems(UINavigationItem[] items, boolean animated) {
        mItems = items;
        resetItemsView();
    }

    public void resetItemsView() {
        for (int i = 0; i < mItemsView.length; i++) {
            mItemsView[i].removeFromSuperview();
        }
        UIView[] itemsView = new UIView[mItems.length];
        for (int i = 0; i < mItems.length; i++) {
            UINavigationItemView frontView = new UINavigationItemView(getContext());
            mItems[i].mFrontView = frontView;
            mItems[i].setNeedsUpdate();
            itemsView[i] = frontView;
            frontView.setConstraint(UIConstraint.full());
            addSubview(frontView);
        }
        mItemsView = itemsView;
        layoutSubviews();
    }

}
