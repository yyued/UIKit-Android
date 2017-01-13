package com.yy.codex.uikit;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by cuiminghui on 2017/1/11.
 */

public class UITouch {

    private long mTimestamp = 0;
    private int mTapCount = 1;
    private boolean mTapCountAdded = false;
    @NonNull private UIView mRelativeView;
    @NonNull private CGPoint mRelativePoint;
    @NonNull private CGPoint mAbsolutePoint;

    public UITouch(@NonNull UIView relativeView, @NonNull CGPoint relativePoint, @NonNull CGPoint absolutePoint) {
        mRelativeView = relativeView;
        mRelativePoint = relativePoint;
        mAbsolutePoint = absolutePoint;
        mTimestamp = System.currentTimeMillis();
        resetTapCount();
    }

    @NonNull
    static ArrayList<UITouch> tapCountStore = new ArrayList<>();

    public void addTapCount() {
        if (mTapCountAdded) {
            return;
        }
        mTapCountAdded = true;
        ArrayList<UITouch> newTapCountStore = new ArrayList<>();
        boolean found = false;
        for (int i = 0; i < tapCountStore.size(); i++) {
            if (tapCountStore.get(i).mAbsolutePoint.inRange(22.0, 22.0, this.mAbsolutePoint) &&
                tapCountStore.get(i).mTimestamp > System.currentTimeMillis() - 300) {
                mTapCount = tapCountStore.get(i).mTapCount + 1;
                newTapCountStore.add(this);
                found = true;
                break;
            }
        }
        if (!found) {
            mTapCount = 1;
            newTapCountStore.add(this);
        }
        tapCountStore = newTapCountStore;
    }

    protected void resetTapCount() {
        for (int i = 0; i < tapCountStore.size(); i++) {
            if (tapCountStore.get(i).mAbsolutePoint.inRange(22.0, 22.0, this.mAbsolutePoint)) {
                mTapCount = tapCountStore.get(i).mTapCount;
                break;
            }
        }
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
    public CGPoint locationInView(@NonNull UIView view) {
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

    @NonNull
    @Override
    public String toString() {
        return "UITouch { RelativeView=" + this.mRelativeView + " x=" + this.mRelativePoint.getX() + " y=" + this.mRelativePoint.getY();
    }
}
