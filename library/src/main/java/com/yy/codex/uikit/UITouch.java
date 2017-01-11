package com.yy.codex.uikit;

/**
 * Created by cuiminghui on 2017/1/11.
 */

public class UITouch {

    private double mTimestamp = 0;
    private int mTapCount = 0;
    private UIView mRelativeView;
    private CGPoint mRelativePoint;

    public CGPoint locationInView(UIView view) {
        return new CGPoint(0, 0);
    }

    public int getTapCount() {
        return mTapCount;
    }

}
