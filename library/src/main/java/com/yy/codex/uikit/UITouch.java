package com.yy.codex.uikit;

import java.util.ArrayList;

/**
 * Created by cuiminghui on 2017/1/11.
 */

public class UITouch {

    private long mTimestamp = 0;
    private int mTapCount = 1;
    private UIView mRelativeView;
    private CGPoint mRelativePoint;

    public UITouch(UIView relativeView, CGPoint relativePoint) {
        mRelativeView = relativeView;
        mRelativePoint = relativePoint;
        mTimestamp = System.currentTimeMillis();
    }

    static ArrayList<UITouch> tapCountStore = new ArrayList<>();

    public void resetTapCount() {
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

    public CGPoint getRelativePoint() {
        return mRelativePoint;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public CGPoint locationInView(UIView view) {
        return new CGPoint(0, 0);
    }

    public int getTapCount() {
        return mTapCount;
    }

}
