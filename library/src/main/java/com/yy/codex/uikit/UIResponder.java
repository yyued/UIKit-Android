package com.yy.codex.uikit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import java.lang.ref.WeakReference;

/**
 * Created by it on 17/1/6.
 */

public interface UIResponder {

    public void setNextResponder(@NonNull UIResponder responder);

    public @Nullable UIResponder getNextResponder();

    public void touchesBegan(@NonNull UITouch[] touches, @NonNull UIEvent event);

    public void touchesMoved(@NonNull UITouch[] touches, @NonNull UIEvent event);

    public void touchesEnded(@NonNull UITouch[] touches, @NonNull UIEvent event);

}
