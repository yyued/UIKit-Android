package com.yy.codex.uikit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;
import java.util.Set;

/**
 * Created by it on 17/1/6.
 */

public class UIResponder extends FrameLayout {

    /* UIResponder initialize methods */

    private WeakReference<UIResponder> mNextResponder;

    public void setNextResponder(@NonNull UIResponder responder) {
        this.mNextResponder = new WeakReference<>(responder);
    }

    @Nullable
    public UIResponder getNextResponder() {
        UIResponder nextResponder = this.mNextResponder != null ? this.mNextResponder.get() : null;
        if (nextResponder != null) {
            return nextResponder;
        }
        return null;
    }

    public UIResponder(@NonNull Context context) {
        super(context);
    }

    public UIResponder(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public UIResponder(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UIResponder(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void touchesBegan(@NonNull Set<UITouch> touches, @NonNull UIEvent event) {
        UIResponder nextResponder = getNextResponder();
        if (nextResponder != null) {
            nextResponder.touchesBegan(touches, event);
        }
    }

    public void touchesMoved(@NonNull Set<UITouch> touches, @NonNull UIEvent event) {
        UIResponder nextResponder = getNextResponder();
        if (nextResponder != null) {
            nextResponder.touchesMoved(touches, event);
        }
    }

    public void touchesEnded(@NonNull Set<UITouch> touches, @NonNull UIEvent event) {
        UIResponder nextResponder = getNextResponder();
        if (nextResponder != null) {
            nextResponder.touchesEnded(touches, event);
        }
    }
}
