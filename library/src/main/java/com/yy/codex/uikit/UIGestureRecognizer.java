package com.yy.codex.uikit;

import java.util.ArrayList;

/**
 * Created by it on 17/1/4.
 */

public class UIGestureRecognizer {

    protected UIView mView;
    private boolean mEnabled = true;
    private NSInvocation[] mActions;
    protected UIGestureRecognizerState mState = UIGestureRecognizerState.Possible;

    public UIGestureRecognizer(Object target, String selector) {
        mActions = new NSInvocation[]{new NSInvocation(target, selector)};
    }

    /* UIView Helpers */

    static ArrayList<UIGestureRecognizer> getGestureRecognizers(UIView view) {
        if (!view.isUserInteractionEnabled()) {
            return null;
        }
        else {
            ArrayList<UIGestureRecognizer> gestureRecognizers = view.getGestureRecognizers();
            if (gestureRecognizers != null) {
                UIView superview = view.getSuperview();
                if (superview != null) {
                    ArrayList<UIGestureRecognizer> superGestureRecognizers = getGestureRecognizers(superview);
                    if (superGestureRecognizers != null) {
                        gestureRecognizers.addAll(superGestureRecognizers);
                    }
                }
                return gestureRecognizers;
            }
            else {
                return null;
            }
        }
    }

    static ArrayList<UIGestureRecognizer> currentLoopGestureRecognizers = null;

    static void resetCurrentLoopGestureRecognizersState() {
        for (int i = 0; i < currentLoopGestureRecognizers.size(); i++) {
            currentLoopGestureRecognizers.get(i).mState = UIGestureRecognizerState.Possible;
        }
    }

    static void onTouchesBegan(ArrayList<UIGestureRecognizer> gestureRecognizers, Object[] touches, Object event) {
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
        }
    }

    static void onTouchesMove(ArrayList<UIGestureRecognizer> gestureRecognizers, Object[] touches, Object event) {
        ArrayList<UIGestureRecognizer> gestureRecognizersTrimmingInvalid = gestureRecognizersByTrimmingInvalid(currentLoopGestureRecognizers);
        for (int i = 0; i < gestureRecognizersTrimmingInvalid.size(); i++) {
            gestureRecognizersTrimmingInvalid.get(i).touchesMoved(touches, event);
        }
    }

    static void onTouchesEnded(ArrayList<UIGestureRecognizer> gestureRecognizers, Object[] touches, Object event) {
        ArrayList<UIGestureRecognizer> gestureRecognizersTrimmingInvalid = gestureRecognizersByTrimmingInvalid(currentLoopGestureRecognizers);
        for (int i = 0; i < gestureRecognizersTrimmingInvalid.size(); i++) {
            gestureRecognizersTrimmingInvalid.get(i).touchesEnded(touches, event);
        }
    }

    static void onTouchesCancelled(ArrayList<UIGestureRecognizer> gestureRecognizers, Object[] touches, Object event) {
        ArrayList<UIGestureRecognizer> gestureRecognizersTrimmingInvalid = gestureRecognizersByTrimmingInvalid(currentLoopGestureRecognizers);
        for (int i = 0; i < gestureRecognizersTrimmingInvalid.size(); i++) {
            gestureRecognizersTrimmingInvalid.get(i).touchesCancelled(touches, event);
        }
    }

    static ArrayList<UIGestureRecognizer> gestureRecognizersByTrimmingInvalid(ArrayList<UIGestureRecognizer> gestureRecognizers) {
        ArrayList<UIGestureRecognizer> filted = new ArrayList<>();
        for (int i = 0; i < gestureRecognizers.size(); i++) {
            if (gestureRecognizers.get(i).mState != UIGestureRecognizerState.Failed) {
                filted.add(gestureRecognizers.get(i));
            }
        }
        return filted;
    }

    /* Props */

    void didAddToView(UIView view) {
        this.mView = view;
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

    public void touchesBegan(Object[] touches, Object event) {

    }

    public void touchesMoved(Object[] touches, Object event) {

    }

    public void touchesEnded(Object[] touches, Object event) {

    }

    public void touchesCancelled(Object[] touches, Object event) {

    }

}
