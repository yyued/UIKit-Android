package com.yy.codex.uikit;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by it on 17/1/4.
 */

public class UIGestureRecognizer {

    @Nullable protected WeakReference<UIView> mWeakView;
    @Nullable WeakReference<UIGestureRecognizerLooper> mLooper;
    private boolean mEnabled = true;
    @Nullable private NSInvocation[] mActions;
    @Nullable private Runnable mTriggerBlock;
    @NonNull
    protected UIGestureRecognizerState mState = UIGestureRecognizerState.Possible;

    public UIGestureRecognizer(@NonNull Object target, @NonNull String selector) {
        mActions = new NSInvocation[]{new NSInvocation(target, selector)};
    }

    public UIGestureRecognizer(@NonNull Runnable triggerBlock) {
        mTriggerBlock = triggerBlock;
    }

    public void addTarget(@NonNull Object target, @NonNull String selector) {
        NSInvocation[] actions = new NSInvocation[mActions.length + 1];
        for (int i = 0; i < mActions.length; i++) {
            actions[i] = mActions[i];
        }
        actions[actions.length - 1] = new NSInvocation(target, selector);
    }

    public void removeTarget(@Nullable Object target, @Nullable String selector) {
        if (target == null && selector == null) {
            mActions = new NSInvocation[0];
        }
        ArrayList<NSInvocation> actions = new ArrayList<>();
        for (int i = 0; i < mActions.length; i++) {
            if (target != null && mActions[i].getTarget() == target && selector != null && mActions[i].getSelector() == selector) {
                continue;
            }
            else if (target == null && selector != null && mActions[i].getSelector() == selector) {
                continue;
            }
            else if (target != null && mActions[i].getTarget() == target && selector == null) {
                continue;
            }
            else {
                actions.add(mActions[i]);
            }
        }
        NSInvocation[] actionsArray = new NSInvocation[actions.size()];
        actions.toArray(actionsArray);
        mActions = actionsArray;
    }

    /* Props */

    void didAddToView(@NonNull UIView view) {
        this.mWeakView = new WeakReference<UIView>(view);
    }

    @Nullable
    public UIView getView() {
        UIView view = this.mWeakView != null ? this.mWeakView.get() : null;
        if (view != null) {
            return view;
        }
        return null;
    }

    @NonNull
    public UIGestureRecognizerState getState() {
        return mState;
    }

    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    /* Events */

    public void touchesBegan(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        if (!mEnabled) {
            mState = UIGestureRecognizerState.Failed;
        }
        mLastPoints = touches;
    }

    public void touchesMoved(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        if (!mEnabled) {
            mState = UIGestureRecognizerState.Failed;
        }
        mLastPoints = touches;
    }

    public void touchesEnded(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        if (!mEnabled) {
            mState = UIGestureRecognizerState.Failed;
        }
        mLastPoints = touches;
    }

    public void touchesCancelled(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        mLastPoints = touches;
        mState = UIGestureRecognizerState.Cancelled;
    }

    protected void sendActions() {
        if (mActions != null) {
            for (int i = 0; i < mActions.length; i++) {
                try {
                    mActions[i].invoke(new Object[]{this});
                } catch (Exception e) {
                    System.out.print(e);
                }
            }
        }
        if (mTriggerBlock != null) {
            mTriggerBlock.run();
        }
    }

    /* Points */

    @Nullable protected UITouch[] mLastPoints;

    @NonNull
    public CGPoint location() {
        UIView view = getView();
        if (view != null) {
            return location(getView(), 0);
        }
        return new CGPoint(0, 0);
    }

    @NonNull
    public CGPoint location(@NonNull UIView inView) {
        return location(inView, 0);
    }

    @NonNull
    public CGPoint location(@NonNull UIView inView, int touchIndex) {
        if (mLastPoints != null && touchIndex < mLastPoints.length) {
            return mLastPoints[touchIndex].locationInView(inView);
        }
        return new CGPoint(0, 0);
    }

    public int numberOfTouches() {
        return mLastPoints != null ? mLastPoints.length : 0;
    }

    /* Delegates */

    int gesturePriority() {
        return 0;
    }

    @NonNull protected UIGestureRecognizer[] mGestureRecognizersRequiresFailed = new UIGestureRecognizer[0];
    @Nullable protected Timer mGestureRecognizerRequiresFailedTimer;

    public void requireGestureRecognizerToFail(@NonNull UIGestureRecognizer otherGestureRecognizer) {
        UIGestureRecognizer[] gestureRecognizersRequiresFailed = new UIGestureRecognizer[mGestureRecognizersRequiresFailed.length + 1];
        for (int i = 0; i < mGestureRecognizersRequiresFailed.length; i++) {
            gestureRecognizersRequiresFailed[i] = mGestureRecognizersRequiresFailed[i];
        }
        gestureRecognizersRequiresFailed[gestureRecognizersRequiresFailed.length - 1] = otherGestureRecognizer;
        mGestureRecognizersRequiresFailed = gestureRecognizersRequiresFailed;
    }

    protected void waitOtherGesture(final Runnable runnable) {
        final Handler handler = new Handler();
        if (mGestureRecognizersRequiresFailed.length > 0) {
            if (mGestureRecognizerRequiresFailedTimer == null) {
                mGestureRecognizerRequiresFailedTimer = new Timer();
                mGestureRecognizerRequiresFailedTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mGestureRecognizerRequiresFailedTimer = null;
                        if (mState == UIGestureRecognizerState.Failed) {
                            return;
                        }
                        boolean allFailed = true;
                        for (int i = 0; i < mGestureRecognizersRequiresFailed.length; i++) {
                            if (mGestureRecognizersRequiresFailed[i].mState != UIGestureRecognizerState.Failed) {
                                allFailed = false;
                            }
                        }
                        if (allFailed) {
                            handler.post(runnable);
                        }
                        else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    waitOtherGesture(runnable);
                                }
                            });
                        }
                    }
                }, 350);
            }
        }
        else {
            runnable.run();
        }
    }

}
