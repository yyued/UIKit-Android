package com.yy.codex.uikit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by it on 17/1/4.
 */

public class UIGestureRecognizer {

    @Nullable protected WeakReference<UIView> weakView;
    private boolean mEnabled = true;
    @Nullable private NSInvocation[] mActions;
    @Nullable private Runnable mTriggerBlock;
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

    /* UIView Helpers */

    @NonNull
    static ArrayList<UIGestureRecognizer> getGestureRecognizers(@NonNull UIView view) {
        if (!view.isUserInteractionEnabled()) {
            return new ArrayList<>();
        }
        else {
            ArrayList<UIGestureRecognizer> gestureRecognizers = new ArrayList<>(view.getGestureRecognizers());
            UIView superview = view.getSuperview();
            if (superview != null) {
                ArrayList<UIGestureRecognizer> superGestureRecognizers = getGestureRecognizers(superview);
                gestureRecognizers.addAll(superGestureRecognizers);
            }
            return gestureRecognizers;
        }
    }

    @Nullable static ArrayList<UIGestureRecognizer> currentLoopGestureRecognizers = null;

    static void resetCurrentLoopGestureRecognizersState() {
        if (currentLoopGestureRecognizers == null) {
            return;
        }
        for (int i = 0; i < currentLoopGestureRecognizers.size(); i++) {
            currentLoopGestureRecognizers.get(i).mState = UIGestureRecognizerState.Possible;
        }
    }

    static void markOtherGestureRecognizersFailed(@Nullable UIGestureRecognizer excepts) {
        if (currentLoopGestureRecognizers == null) {
            return;
        }
        for (int i = 0; i < currentLoopGestureRecognizers.size(); i++) {
            if (excepts != null && currentLoopGestureRecognizers.get(i) == excepts) {
                continue;
            }
            currentLoopGestureRecognizers.get(i).mState = UIGestureRecognizerState.Failed;
        }
    }

    static void onTouchesBegan(@NonNull ArrayList<UIGestureRecognizer> gestureRecognizers, @NonNull UITouch[] touches, @NonNull UIEvent event) {
        if (currentLoopGestureRecognizers == null) {
            currentLoopGestureRecognizers = gestureRecognizers;
            for (int i = 0; i < gestureRecognizers.size(); i++) {
                gestureRecognizers.get(i).mState = UIGestureRecognizerState.Possible;
            }
        }
        else if (gestureRecognizersByTrimmingInvalid(currentLoopGestureRecognizers).size() == 0) {
            resetCurrentLoopGestureRecognizersState();
            currentLoopGestureRecognizers = null;
            onTouchesBegan(gestureRecognizers, touches, event);
            return;
        }
        if (currentLoopGestureRecognizers == null) {
            return;
        }
        ArrayList<UIGestureRecognizer> gestureRecognizersTrimmingInvalid = gestureRecognizersByTrimmingInvalid(currentLoopGestureRecognizers);
        for (int i = 0; i < gestureRecognizersTrimmingInvalid.size(); i++) {
            if (gestureRecognizersTrimmingInvalid.get(i).mState == UIGestureRecognizerState.Ended) {
                resetCurrentLoopGestureRecognizersState();
                currentLoopGestureRecognizers = null;
                onTouchesBegan(gestureRecognizers, touches, event);
                return;
            }
        }
        for (int i = 0; i < gestureRecognizersTrimmingInvalid.size(); i++) {
            gestureRecognizersTrimmingInvalid.get(i).touchesBegan(touches, event);
            if (gestureRecognizersTrimmingInvalid.get(i).mState == UIGestureRecognizerState.Began || gestureRecognizersTrimmingInvalid.get(i).mState == UIGestureRecognizerState.Changed || gestureRecognizersTrimmingInvalid.get(i).mState == UIGestureRecognizerState.Ended) {
                break;
            }
        }
    }

    static void onTouchesMove(@NonNull ArrayList<UIGestureRecognizer> gestureRecognizers, @NonNull UITouch[] touches, @NonNull UIEvent event) {
        if (currentLoopGestureRecognizers == null) {
            return;
        }
        ArrayList<UIGestureRecognizer> gestureRecognizersTrimmingInvalid = gestureRecognizersByTrimmingInvalid(currentLoopGestureRecognizers);
        for (int i = 0; i < gestureRecognizersTrimmingInvalid.size(); i++) {
            gestureRecognizersTrimmingInvalid.get(i).touchesMoved(touches, event);
            if (gestureRecognizersTrimmingInvalid.get(i).mState == UIGestureRecognizerState.Began || gestureRecognizersTrimmingInvalid.get(i).mState == UIGestureRecognizerState.Changed || gestureRecognizersTrimmingInvalid.get(i).mState == UIGestureRecognizerState.Ended) {
                break;
            }
        }
    }

    static void onTouchesEnded(@NonNull ArrayList<UIGestureRecognizer> gestureRecognizers, @NonNull UITouch[] touches, @NonNull UIEvent event) {
        if (currentLoopGestureRecognizers == null) {
            return;
        }
        ArrayList<UIGestureRecognizer> gestureRecognizersTrimmingInvalid = gestureRecognizersByTrimmingInvalid(currentLoopGestureRecognizers);
        for (int i = 0; i < gestureRecognizersTrimmingInvalid.size(); i++) {
            gestureRecognizersTrimmingInvalid.get(i).touchesEnded(touches, event);
            if (gestureRecognizersTrimmingInvalid.get(i).mState == UIGestureRecognizerState.Began || gestureRecognizersTrimmingInvalid.get(i).mState == UIGestureRecognizerState.Changed || gestureRecognizersTrimmingInvalid.get(i).mState == UIGestureRecognizerState.Ended) {
                break;
            }
        }
    }

    static void onTouchesCancelled(@NonNull ArrayList<UIGestureRecognizer> gestureRecognizers, @NonNull UITouch[] touches, @NonNull UIEvent event) {
        if (currentLoopGestureRecognizers == null) {
            return;
        }
        ArrayList<UIGestureRecognizer> gestureRecognizersTrimmingInvalid = gestureRecognizersByTrimmingInvalid(currentLoopGestureRecognizers);
        for (int i = 0; i < gestureRecognizersTrimmingInvalid.size(); i++) {
            gestureRecognizersTrimmingInvalid.get(i).touchesCancelled(touches, event);
        }
    }

    static ArrayList<UIGestureRecognizer> gestureRecognizersByTrimmingInvalid(@NonNull ArrayList<UIGestureRecognizer> gestureRecognizers) {
        ArrayList<UIGestureRecognizer> filtered = new ArrayList<>();
        for (int i = 0; i < gestureRecognizers.size(); i++) {
            if(gestureRecognizers.get(i).mState == UIGestureRecognizerState.Began || gestureRecognizers.get(i).mState == UIGestureRecognizerState.Changed || gestureRecognizers.get(i).mState == UIGestureRecognizerState.Ended) {
                filtered.add(gestureRecognizers.get(i));
                break;
            }
            else if (gestureRecognizers.get(i).mState != UIGestureRecognizerState.Failed) {
                filtered.add(gestureRecognizers.get(i));
            }
        }
        return filtered;
    }

    /* Props */

    void didAddToView(@NonNull UIView view) {
        this.weakView = new WeakReference<UIView>(view);
    }

    @Nullable
    public UIView getView() {
        UIView view = this.weakView != null ? this.weakView.get() : null;
        if (view != null) {
            return view;
        }
        return null;
    }

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
        lastPoints = touches;
    }

    public void touchesMoved(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        if (!mEnabled) {
            mState = UIGestureRecognizerState.Failed;
        }
        lastPoints = touches;
    }

    public void touchesEnded(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        if (!mEnabled) {
            mState = UIGestureRecognizerState.Failed;
        }
        lastPoints = touches;
    }

    public void touchesCancelled(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        lastPoints = touches;
        mState = UIGestureRecognizerState.Cancelled;
    }

    protected void sendActions() {
        if (mActions != null) {
            for (int i = 0; i < mActions.length; i++) {
                try {
                    mActions[i].invoke(new Object[]{this});
                } catch (Exception e) {}
            }
        }
        if (mTriggerBlock != null) {
            mTriggerBlock.run();
        }
    }

    /* Points */

    @Nullable protected UITouch[] lastPoints;

    @NonNull
    public CGPoint location() {
        UIView view = getView();
        if (view != null) {
            return location(getView(), 0);
        }
        return new CGPoint(0, 0);
    }

    @NonNull
    public CGPoint location(UIView inView) {
        return location(inView, 0);
    }

    @NonNull
    public CGPoint location(UIView inView, int touchIndex) {
        if (lastPoints != null && touchIndex < lastPoints.length) {
            return lastPoints[touchIndex].locationInView(inView);
        }
        return new CGPoint(0, 0);
    }

    public int numberOfTouches() {
        return lastPoints != null ? lastPoints.length : 0;
    }

}
