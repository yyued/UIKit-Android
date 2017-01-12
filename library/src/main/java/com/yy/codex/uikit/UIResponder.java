package com.yy.codex.uikit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

/**
 * Created by it on 17/1/6.
 */

public class UIResponder extends FrameLayout {

    /* UIResponder initialize methods */

    private WeakReference<UIResponder> mNextResponder;

    public void setNextResponder(UIResponder responder) {
        this.mNextResponder = new WeakReference<UIResponder>(responder);
    }

    public UIResponder getNextResponder() {
        UIResponder nextResponder = this.mNextResponder.get();
        if (nextResponder != null) {
            return nextResponder;
        }
        return null;
    }

    public UIResponder(Context context) {
        super(context);
    }

    public UIResponder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UIResponder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UIResponder(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void touchesBegan() {
        UIResponder nextResponder = getNextResponder();
        if (nextResponder != null) {
            nextResponder.touchesBegan();
        }
    }

    public void touchesMoved() {
        UIResponder nextResponder = getNextResponder();
        if (nextResponder != null) {
            nextResponder.touchesMoved();
        }
    }

    public void touchesEnded() {
        UIResponder nextResponder = getNextResponder();
        if (nextResponder != null) {
            nextResponder.touchesEnded();
        }
    }
}
