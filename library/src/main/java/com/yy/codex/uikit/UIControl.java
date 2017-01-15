package com.yy.codex.uikit;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuiminghui on 2017/1/13.
 */

public class UIControl extends UIView {

    public enum Event {
        TouchDown,
        TouchDownRepeat,
        TouchDragInside,
        TouchDragOutside,
        TouchDragEnter,
        TouchDragExit,
        TouchUpInside,
        TouchUpOutside,
        TouchCancel,
        ValueChanged
    }

    public UIControl(@NonNull Context context, @NonNull View view) {
        super(context, view);
        init();
    }

    public UIControl(@NonNull Context context) {
        super(context);
        init();
    }

    public UIControl(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UIControl(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UIControl(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private HashMap<Event, NSInvocation[]> mInvocations = new HashMap<>();
    private HashMap<Event, Runnable[]> mRunnable = new HashMap<>();

    public void addTarget(@NonNull Object target, @NonNull String selector, @NonNull Event event) {
        if (!mInvocations.containsKey(event)) {
            mInvocations.put(event, new NSInvocation[0]);
        }
        NSInvocation[] invocations = new NSInvocation[mInvocations.get(event).length + 1];
        for (int i = 0; i < mInvocations.get(event).length; i++) {
            invocations[i] = mInvocations.get(event)[i];
        }
        invocations[invocations.length - 1] = new NSInvocation(target, selector);
        mInvocations.put(event, invocations);
    }

    public void removeTarget(@Nullable Object target, @Nullable String selector, @Nullable Event event) {
        if (target == null && selector == null && event == null) {
            mInvocations.clear();
        }
        else if (target == null && selector == null && event != null) {
            mInvocations.remove(event);
        }
        else if (target != null && selector == null && event == null) {
            for (Map.Entry<Event, NSInvocation[]> entry: mInvocations.entrySet()) {
                NSInvocation[] invocations = entry.getValue();
                ArrayList<NSInvocation> arrayList = new ArrayList<>();
                for (int i = 0; i < invocations.length; i++) {
                    if (invocations[i].getTarget() != target) {
                        arrayList.add(invocations[i]);
                    }
                }
                NSInvocation[] newInvocations = new NSInvocation[arrayList.size()];
                arrayList.toArray(newInvocations);
                mInvocations.put(entry.getKey(), newInvocations);
            }
        }
        else if (target != null && selector != null && event == null) {
            for (Map.Entry<Event, NSInvocation[]> entry: mInvocations.entrySet()) {
                NSInvocation[] invocations = entry.getValue();
                ArrayList<NSInvocation> arrayList = new ArrayList<>();
                for (int i = 0; i < invocations.length; i++) {
                    if (invocations[i].getTarget() != target || invocations[i].getSelector() != selector) {
                        arrayList.add(invocations[i]);
                    }
                }
                NSInvocation[] newInvocations = new NSInvocation[arrayList.size()];
                arrayList.toArray(newInvocations);
                mInvocations.put(entry.getKey(), newInvocations);
            }
        }
        else if (target == null && selector != null && event == null) {
            for (Map.Entry<Event, NSInvocation[]> entry: mInvocations.entrySet()) {
                NSInvocation[] invocations = entry.getValue();
                ArrayList<NSInvocation> arrayList = new ArrayList<>();
                for (int i = 0; i < invocations.length; i++) {
                    if (invocations[i].getSelector() != selector) {
                        arrayList.add(invocations[i]);
                    }
                }
                NSInvocation[] newInvocations = new NSInvocation[arrayList.size()];
                arrayList.toArray(newInvocations);
                mInvocations.put(entry.getKey(), newInvocations);
            }
        }
        else if (target == null && selector != null && event != null) {
            if (!mInvocations.containsKey(event)) {
                return;
            }
            NSInvocation[] invocations = mInvocations.get(event);
            ArrayList<NSInvocation> arrayList = new ArrayList<>();
            for (int i = 0; i < invocations.length; i++) {
                if (invocations[i].getTarget() != target) {
                    arrayList.add(invocations[i]);
                }
            }
            NSInvocation[] newInvocations = new NSInvocation[arrayList.size()];
            arrayList.toArray(newInvocations);
            mInvocations.put(event, newInvocations);
        }
        else if (target != null && selector != null && event != null) {
            if (!mInvocations.containsKey(event)) {
                return;
            }
            NSInvocation[] invocations = mInvocations.get(event);
            ArrayList<NSInvocation> arrayList = new ArrayList<>();
            for (int i = 0; i < invocations.length; i++) {
                if (invocations[i].getTarget() != target || invocations[i].getSelector() != selector) {
                    arrayList.add(invocations[i]);
                }
            }
            NSInvocation[] newInvocations = new NSInvocation[arrayList.size()];
            arrayList.toArray(newInvocations);
            mInvocations.put(event, newInvocations);
        }
    }

    public void addBlock(@NonNull Runnable runnable,@NonNull Event event) {
        if (!mRunnable.containsKey(event)) {
            mRunnable.put(event, new Runnable[0]);
        }
        Runnable[] runnables = new Runnable[mRunnable.get(event).length + 1];
        for (int i = 0; i < mRunnable.get(event).length; i++) {
            runnables[i] = mRunnable.get(event)[i];
        }
        runnables[runnables.length - 1] = runnable;
        mRunnable.put(event, runnables);
    }

    protected void init() {
        setUserInteractionEnabled(true);
        UILongPressGestureRecognizer longPressGestureRecognizer = new UILongPressGestureRecognizer(this, "onLongPressed:");
        longPressGestureRecognizer.minimumPressDuration = 0.10;
        addGestureRecognizer(longPressGestureRecognizer);
        UITapGestureRecognizer tapGestureRecognizer = new UITapGestureRecognizer(this, "onTapped:");
        addGestureRecognizer(tapGestureRecognizer);
    }

    private boolean mInside = true;

    protected void onLongPressed(UILongPressGestureRecognizer sender) {
        if (sender.mState == UIGestureRecognizerState.Began) {
            mInside = true;
            onEvent(Event.TouchDown);
        }
        else if (sender.mState == UIGestureRecognizerState.Changed) {
            if (isPointInside(sender.location(this))) {
                onEvent(Event.TouchDragInside);
                if (!mInside) {
                    onEvent(Event.TouchDragEnter);
                    mInside = true;
                }
            }
            else {
                onEvent(Event.TouchDragOutside);
                if (mInside) {
                    onEvent(Event.TouchDragExit);
                    mInside = false;
                }
            }
        }
        else if (sender.mState == UIGestureRecognizerState.Ended) {
            if (isPointInside(sender.location(this))) {
                onEvent(Event.TouchUpInside);
            }
            else {
                onEvent(Event.TouchUpOutside);
            }
        }
    }

    protected void onTapped(UITapGestureRecognizer sender) {
        onEvent(Event.TouchUpInside);
    }

    protected void onEvent(Event event) {
        NSInvocation[] invocations = mInvocations.get(event);
        if (invocations != null) {
            for (int i = 0; i < invocations.length; i++) {
                try {
                    invocations[i].invoke(new Object[]{this});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Runnable[] runnables = mRunnable.get(event);
        if (runnables != null) {
            for (int i = 0; i < runnables.length; i++) {
                runnables[i].run();
            }
        }
    }

    protected boolean isPointInside(CGPoint point) {
        double xRange = this.getFrame().getWidth() / 2.0;
        double yRange = this.getFrame().getHeight() / 2.0;
        return point.inRange(xRange * 2, yRange * 2, new CGPoint(xRange, yRange));
    }

}
