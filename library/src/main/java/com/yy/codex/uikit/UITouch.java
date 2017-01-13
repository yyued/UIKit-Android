package com.yy.codex.uikit;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by cuiminghui on 2017/1/11.
 */

enum UITouchPhase {
    UITouchPhaseBegan,             // whenever a finger touches the surface.
    UITouchPhaseMoved,             // whenever a finger moves on the surface.
    UITouchPhaseStationary,        // whenever a finger is touching the surface but hasn't moved since the previous event.
    UITouchPhaseEnded,             // whenever a finger leaves the surface.
    UITouchPhaseCancelled,         // whenever a touch doesn't end but we need to stop tracking (e.g. putting device to face)
}

public class UITouch {

    private long mTimestamp = 0;
    private int mTapCount = 1;
    private boolean mTapCountAdded = false;
    @NonNull private UIView mRelativeView;
    @NonNull private CGPoint mRelativePoint;
    @NonNull private CGPoint mAbsolutePoint;

    private UITouchPhase mPhase;

    public UITouch(@NonNull UIView relativeView, @NonNull CGPoint relativePoint, @NonNull CGPoint absolutePoint, UITouchPhase phase) {
        mRelativeView = relativeView;
        mRelativePoint = relativePoint;
        mAbsolutePoint = absolutePoint;
        mTimestamp = System.currentTimeMillis();
        mPhase = phase;
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
        return "UITouch { RelativeView=" + this.mRelativeView + " x=" + this.mRelativePoint.getX() + " y=" + this.mRelativePoint.getY() + " Phase=" + this.mPhase;
    }
}
