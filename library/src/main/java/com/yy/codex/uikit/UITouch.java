package com.yy.codex.uikit;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by cuiminghui on 2017/1/11.
 */

public class UITouch {

    public enum Phase {
        Began,             // whenever a finger touches the surface.
        Moved,             // whenever a finger moves on the surface.
        Stationary,        // whenever a finger is touching the surface but hasn't moved since the previous event.
        Ended,             // whenever a finger leaves the surface.
        Cancelled,         // whenever a touch doesn't end but we need to stop tracking (e.g. putting device to face)
    }

    private long mTimestamp = 0;
    private int mTapCount = 1;
    private long mEventID = 0;
    @NonNull private UIView mRelativeView;
    @NonNull private CGPoint mRelativePoint;
    @NonNull private CGPoint mAbsolutePoint;

    private Phase mPhase;

    public UITouch(@NonNull UIView relativeView, @NonNull CGPoint relativePoint, @NonNull CGPoint absolutePoint, Phase phase, long eventID) {
        mRelativeView = relativeView;
        mRelativePoint = relativePoint;
        mAbsolutePoint = absolutePoint;
        mTimestamp = System.currentTimeMillis();
        mPhase = phase;
        mEventID = eventID;
        resetTapCount();
    }

    @NonNull
    static ArrayList<UITouch> tapCountStore = new ArrayList<>();

    public void resetTapCount() {
        ArrayList<UITouch> newTapCountStore = new ArrayList<>();
        boolean found = false;
        for (int i = 0; i < tapCountStore.size(); i++) {
            if (tapCountStore.get(i).mAbsolutePoint.inRange(22.0, 22.0, this.mAbsolutePoint)) {
                if (mPhase == Phase.Began) {
                    if (tapCountStore.get(i).mEventID != this.mEventID) {
                        if (tapCountStore.get(i).mTimestamp <= System.currentTimeMillis() - 300) {
                            mTapCount = 1;
                        }
                        else {
                            mTapCount = tapCountStore.get(i).mTapCount + 1;
                        }
                    }
                    else {
                        mTapCount = tapCountStore.get(i).mTapCount;
                    }
                }
                else {
                    mTapCount = tapCountStore.get(i).mTapCount;
                }
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
