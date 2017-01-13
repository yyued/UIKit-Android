package com.yy.codex.uikit;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by cuiminghui on 2017/1/13.
 */

class UIGestureRecognizerLooper {

    @NonNull UIView mHitTestedView;
    @NonNull ArrayList<UIGestureRecognizer> mGestureRecognizers;
    boolean mFinished = false;

    static boolean isHitTestedView(@NonNull Set<UITouch> touches ,UIView theView) {
        if (touches.size() > 0) {
            UITouch[] arr = new UITouch[touches.size()];
            touches.toArray(arr);
            return arr[0].getHitTestedView() == theView;
        }
        return false;
    }

    UIGestureRecognizerLooper(UIView hitTestedView) {
        mHitTestedView = hitTestedView;
        mGestureRecognizers = getGestureRecognizers(hitTestedView);
        Collections.sort(mGestureRecognizers, new Comparator<UIGestureRecognizer>() {
            @Override
            public int compare(UIGestureRecognizer gestureRecognizer, UIGestureRecognizer t1) {
                return gestureRecognizer.gesturePriority() > t1.gesturePriority() ? 1 : -1;
            }
        });
        resetState();
    }

    boolean isFinished() {
        return mFinished;
    }

    void onTouchesBegan(@NonNull Set<UITouch> touches, @NonNull UIEvent event) {
        ArrayList<UIGestureRecognizer> copyList = new ArrayList<>(mGestureRecognizers);
        for (int i = 0; i < copyList.size(); i++) {
            if (checkState(copyList.get(i))) {
                UITouch[] arr = new UITouch[touches.size()];
                touches.toArray(arr);
                copyList.get(i).touchesBegan(arr, event);
                checkState(copyList.get(i));
            }
        }
        markFailed();
    }

    void onTouchesMoved(@NonNull Set<UITouch> touches, @NonNull UIEvent event) {
        ArrayList<UIGestureRecognizer> copyList = new ArrayList<>(mGestureRecognizers);
        for (int i = 0; i < copyList.size(); i++) {
            if (checkState(copyList.get(i))) {
                UITouch[] arr = new UITouch[touches.size()];
                touches.toArray(arr);
                copyList.get(i).touchesMoved(arr, event);
                checkState(copyList.get(i));
            }
        }
        markFailed();
    }

    void onTouchesEnded(@NonNull Set<UITouch> touches, @NonNull UIEvent event) {
        ArrayList<UIGestureRecognizer> copyList = new ArrayList<>(mGestureRecognizers);
        for (int i = 0; i < copyList.size(); i++) {
            if (checkState(copyList.get(i))) {
                UITouch[] arr = new UITouch[touches.size()];
                touches.toArray(arr);
                copyList.get(i).touchesEnded(arr, event);
                checkState(copyList.get(i));
            }
        }
        markFailed();
    }

    boolean checkState(UIGestureRecognizer gestureRecognizer) {
        if (gestureRecognizer.mState == UIGestureRecognizerState.Failed || gestureRecognizer.mState == UIGestureRecognizerState.Cancelled) {
            mGestureRecognizers.remove(gestureRecognizer);
            return false;
        }
        else if (gestureRecognizer.mState == UIGestureRecognizerState.Ended) {
            mFinished = true;
        }
        return true;
    }

    private void markFailed() {
        boolean hasRecognized = false;
        for (int i = 0; i < mGestureRecognizers.size(); i++) {
            hasRecognized = mGestureRecognizers.get(i).mState == UIGestureRecognizerState.Began || mGestureRecognizers.get(i).mState == UIGestureRecognizerState.Changed || mGestureRecognizers.get(i).mState == UIGestureRecognizerState.Ended;
            if (hasRecognized) {
                break;
            }
        }
        if (hasRecognized) {
            for (int i = 0; i < mGestureRecognizers.size(); i++) {
                if (mGestureRecognizers.get(i).mState == UIGestureRecognizerState.Began || mGestureRecognizers.get(i).mState == UIGestureRecognizerState.Changed || mGestureRecognizers.get(i).mState == UIGestureRecognizerState.Ended) {
                    continue;
                }
                mGestureRecognizers.get(i).mState = UIGestureRecognizerState.Failed;
            }
        }
    }

    @NonNull
    private ArrayList<UIGestureRecognizer> getGestureRecognizers(@NonNull UIView view) {
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

    private void resetState() {
        for (int i = 0; i < mGestureRecognizers.size(); i++) {
            mGestureRecognizers.get(i).mState = UIGestureRecognizerState.Possible;
        }
    }

}
