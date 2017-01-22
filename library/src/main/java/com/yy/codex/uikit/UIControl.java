package com.yy.codex.uikit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.yy.codex.foundation.NSInvocation;

import java.util.ArrayList;
import java.util.EnumSet;
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

    public enum State {
        Normal,
        Highlighted,
        Disabled,
        Selected,
    }

    public enum ContentVerticalAlignment {
        Center,
        Top,
        Bottom,
        Fill,
    }

    public enum ContentHorizontalAlignment {
        Center,
        Left,
        Right,
        Fill,
    }

    public UIControl(@NonNull Context context, @NonNull View view) {
        super(context, view);
    }

    public UIControl(@NonNull Context context) {
        super(context);
    }

    public UIControl(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public UIControl(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UIControl(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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

    @Override
    protected void init() {
        super.init();
        setUserInteractionEnabled(true);
        UILongPressGestureRecognizer longPressGestureRecognizer = new UILongPressGestureRecognizer(this, "onLongPressed:");
        longPressGestureRecognizer.mMinimumPressDuration = 0.05;
        addGestureRecognizer(longPressGestureRecognizer);
        UITapGestureRecognizer tapGestureRecognizer = new UITapGestureRecognizer(this, "onTapped:");
        addGestureRecognizer(tapGestureRecognizer);
    }

    private boolean mInside = true;

    protected void onLongPressed(UILongPressGestureRecognizer sender) {
        if (sender.mState == UIGestureRecognizerState.Began) {
            mInside = true;
            onEvent(Event.TouchDown);
            mTracking = true;
            mHighlighted = true;
            resetState();
        }
        else if (sender.mState == UIGestureRecognizerState.Changed) {
            if (isPointInside(sender.location(this))) {
                onEvent(Event.TouchDragInside);
                if (!mInside) {
                    onEvent(Event.TouchDragEnter);
                    mInside = true;
                    mHighlighted = true;
                    resetState();
                }
            }
            else {
                onEvent(Event.TouchDragOutside);
                if (mInside) {
                    onEvent(Event.TouchDragExit);
                    mInside = false;
                    mHighlighted = false;
                    resetState();
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
            mTracking = false;
            mHighlighted = false;
            resetState();
        }
    }

    protected void onTapped(UITapGestureRecognizer sender) {
        onEvent(Event.TouchUpInside);
    }

    protected void onEvent(Event event) {
        NSInvocation[] invocations = mInvocations.get(event);
        if (invocations != null) {
            for (int i = 0; i < invocations.length; i++) {
                invocations[i].invoke(new Object[]{this});
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
        double xRange = this.getFrame().getSize().getWidth() / 2.0;
        double yRange = this.getFrame().getSize().getHeight() / 2.0;
        return point.inRange(xRange + 22, yRange + 22, new CGPoint(xRange, yRange));
    }

    /* Enabled */

    private boolean mEnabled = true;

    @Override
    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
        setUserInteractionEnabled(enabled);
        resetState();
    }

    @Override
    public boolean isEnabled() {
        return mEnabled;
    }

    /* Selected */

    private boolean mSelected = false;

    @Override
    public void setSelected(boolean selected) {
        this.mSelected = selected;
        resetState();
    }

    @Override
    public boolean isSelected() {
        return mSelected;
    }

    /* Highlighted */

    private boolean mHighlighted = false;

    public boolean isHighlighted() {
        return mHighlighted;
    }

    /* State */

    private EnumSet<State> mState = EnumSet.of(State.Normal);

    protected void resetState() {
        EnumSet<State> state = EnumSet.of(State.Normal);
        if (isTracking() && isHighlighted()) {
            state.add(State.Highlighted);
        }
        if (isSelected()) {
            state.remove(State.Normal);
            state.add(State.Selected);
        }
        if (!isEnabled()) {
            state.add(State.Disabled);
        }
        mState = state;
    }

    public EnumSet<State> getState() {
        return mState;
    }

    /* Tracking */

    private boolean mTracking = false;

    public boolean isTracking() {
        return mTracking;
    }

    public boolean isTouchInside() {
        return mInside;
    }

    /* ContentAlignment */

    private ContentVerticalAlignment mContentVerticalAlignment = ContentVerticalAlignment.Center;

    public void setContentVerticalAlignment(ContentVerticalAlignment contentVerticalAlignment) {
        this.mContentVerticalAlignment = contentVerticalAlignment;
    }

    public ContentVerticalAlignment getContentVerticalAlignment() {
        return mContentVerticalAlignment;
    }

    private ContentHorizontalAlignment mContentHorizontalAlignment = ContentHorizontalAlignment.Center;

    public void setContentHorizontalAlignment(ContentHorizontalAlignment contentHorizontalAlignment) {
        this.mContentHorizontalAlignment = contentHorizontalAlignment;
    }

    public ContentHorizontalAlignment getContentHorizontalAlignment() {
        return mContentHorizontalAlignment;
    }

}
