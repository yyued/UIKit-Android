package com.yy.codex.uikit;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by cuiminghui on 2017/1/11.
 */

public class UITouch {

    private long mTimestamp = 0;
    private int mTapCount = 1;
    private boolean mTapCountSetted = false;
    @NonNull private UIView mRelativeView;
    @NonNull private CGPoint mRelativePoint;
    @NonNull private CGPoint mAbsolutePoint;

    public UITouch(@NonNull UIView relativeView, @NonNull CGPoint relativePoint, @NonNull CGPoint absolutePoint) {
        mRelativeView = relativeView;
        mRelativePoint = relativePoint;
        mAbsolutePoint = absolutePoint;
        mTimestamp = System.currentTimeMillis();
    }

    static ArrayList<UITouch> tapCountStore = new ArrayList<>();

    public void resetTapCount() {
        if (mTapCountSetted) {
            return;
        }
        mTapCountSetted = true;
        ArrayList<UITouch> newTapCountStore = new ArrayList<>();
        boolean found = false;
        for (int i = 0; i < tapCountStore.size(); i++) {
            if (tapCountStore.get(i).mRelativeView == this.mRelativeView &&
                tapCountStore.get(i).mRelativePoint.inRange(22.0, 22.0, this.mRelativePoint) &&
                tapCountStore.get(i).mTimestamp > System.currentTimeMillis() - 300) {
                mTapCount = tapCountStore.get(i).mTapCount + 1;
                newTapCountStore.add(this);
                found = true;
                break;
            }
        }
        if (!found) {
            newTapCountStore.add(this);
        }
        tapCountStore = newTapCountStore;
    }

    @NonNull
    public UIView getHitTestedView() {
        return mRelativeView;
    }

    @NonNull
    public CGPoint getRelativePoint() {
        return mRelativePoint;
    }

    @NonNull
    public CGPoint getAbsolutePoint() {
        return mAbsolutePoint;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    @NonNull
    public CGPoint locationInView(UIView view) {
        UIView hitTestedView = getHitTestedView();
        if (hitTestedView != null) {
            if (hitTestedView == view) {
                return getRelativePoint();
            }
            else {
                hitTestedView.convertPoint(getRelativePoint(), view);
            }
        }
        return new CGPoint(0, 0);
    }

    public int getTapCount() {
        return mTapCount;
    }

    @Override
    public String toString() {
        return "UITouch { RelativeView=" + this.mRelativeView + " x=" + this.mRelativePoint.getX() + " y=" + this.mRelativePoint.getY();
    }
}
