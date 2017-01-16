package com.yy.codex.uikit;

/**
 * Created by cuiminghui on 2017/1/16.
 */

public class UIViewAnimation {

    private boolean mCancelled = false;
    private boolean mFinished = false;

    public boolean isCancelled() {
        return mCancelled;
    }

    public boolean isFinished() {
        return mFinished;
    }

    public void cancel() {
        mCancelled = true;
    }

    public void markFinished() {
        mFinished = true;
    }

}
