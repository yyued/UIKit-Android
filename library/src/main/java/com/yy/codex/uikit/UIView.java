package com.yy.codex.uikit;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuiminghui on 2016/12/30.
 */

public class UIView extends UIResponder implements View.OnTouchListener {

    /* FrameLayout initialize methods */

    public UIView(Context context, View view) {
        super(context);
        setupProps();
        addView(view);
    }

    public UIView(Context context) {
        super(context);
        setupProps();
    }

    public UIView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupProps();
    }

    public UIView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupProps();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UIView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupProps();
    }

    private void setupProps(){
        this.layer.bindView(this);
        setWillNotDraw(false);
    }

    /* category UIView Layout */

    private CGRect frame = new CGRect(0, 0, 0, 0);
    private UIConstraint constraint = null;

    public CGRect getFrame() {
        return frame;
    }

    public void setFrame(CGRect frame) {
        if (this.getFrame().equals(frame)) {
            return;
        }
        CGRect oldValue = this.frame;
        this.frame = frame;
        layoutSubviews();
        float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        this.setX((float) frame.origin.getX() * scaledDensity);
        this.setY((float) frame.origin.getY() * scaledDensity);

        double mWidth = frame.size.getWidth() * scaledDensity;
        double mHeight = frame.size.getHeight() * scaledDensity;
        if (Math.ceil(mWidth) - mWidth < 0.1) {
            mWidth = Math.ceil(mWidth);
        }
        if (Math.ceil(mWidth) - mHeight < 0.1) {
            mHeight = Math.ceil(mHeight);
        }
        this.setMinimumWidth((int) mWidth);
        this.setMinimumHeight((int) mHeight);
        CALayer.scaledDensity = scaledDensity;
        this.layer.setFrame(new CGRect(0, 0, frame.size.getWidth(), frame.size.getHeight()));
        UIView.addAnimationState(this, "frame.origin.x", oldValue.origin.getX(), frame.origin.getX());
        UIView.addAnimationState(this, "frame.origin.y", oldValue.origin.getY(), frame.origin.getY());
        UIView.addAnimationState(this, "frame.size.width", oldValue.size.getWidth(), frame.size.getWidth());
        UIView.addAnimationState(this, "frame.size.height", oldValue.size.getHeight(), frame.size.getHeight());
    }

    public CGPoint getCenter() {
        return new CGPoint((frame.origin.getX() + frame.size.getWidth()) / 2.0, (frame.origin.getY() + frame.size.getHeight()) / 2.0);
    }

    public UIConstraint getConstraint() {
        return constraint;
    }

    public void setConstraint(UIConstraint constraint) {
        this.constraint = constraint;
        UIView superview = getSuperview();
        if (superview != null) {
            superview.layoutSubviews();
        }
    }

    public void layoutSubviews() {
        UIView previous = null;
        UIView[] subviews = getSubviews();
        for (int i = 0; i < subviews.length; i++) {
            UIView subview = subviews[i];
            if (subview.constraint != null && !subview.constraint.disabled) {
                subview.setFrame(subview.constraint.requestFrame(subview, this, previous));
            }
            previous = subview;
        }
    }

    private double maxWidth = 0.0;

    public double getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(double maxWidth) {
        this.maxWidth = maxWidth;
    }

    public CGSize intrinsicContentSize() {
        return new CGSize(0, 0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getSuperview() == null) {
            float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
            setFrame(new CGRect((float)left / scaledDensity, (float)top / scaledDensity, ((float)right - (float)left) / scaledDensity, ((float)bottom - (float)top) / scaledDensity));
        }
    }

    /* category UIView Rendering */

    @Override
    public void setAlpha(float alpha) {
        float oldValue = this.getAlpha();
        super.setAlpha(alpha);
        UIView.addAnimationState(this, "alpha", oldValue, alpha);
    }

    /* category UIView Hierarchy */

    public UIView getSuperview() {
        ViewParent parent = getParent();
        if (parent != null && UIView.class.isAssignableFrom(parent.getClass())) {
            return (UIView)parent;
        }
        return null;
    }

    public UIView[] getSubviews() {
        ArrayList<UIView> subviews = new ArrayList<>();
        for(int index = 0; index < getChildCount(); index++) {
            View nextChild = getChildAt(index);
            if (UIView.class.isAssignableFrom(nextChild.getClass())) {
                subviews.add((UIView)nextChild);
            }
        }
        UIView[] subviewsArr = new UIView[subviews.size()];
        subviews.toArray(subviewsArr);
        return subviewsArr;
    }

    public void removeFromSuperview() {
        if (getSuperview() != null) {
            getSuperview().removeView(this);
        }
    }

    public void insertSubview(UIView subview, int atIndex) {
        subview.removeFromSuperview();
        if (atIndex < 0 || atIndex > getChildCount()) {
            return;
        }
        addView(subview, atIndex, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void addSubview(UIView subview) {
        subview.removeFromSuperview();
        subview.setNextResponder(this);
        addView(subview, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void insertBelowSubview(UIView subview, UIView siblingSubview) {
        if (siblingSubview.getSuperview() == this) {
            int atIndex = indexOfChild(siblingSubview);
            insertSubview(subview, atIndex);
        }
    }

    public void insertAboveSubview(UIView subview, UIView siblingSubview) {
        if (siblingSubview.getSuperview() == this) {
            int atIndex = indexOfChild(siblingSubview);
            insertSubview(subview, atIndex + 1);
        }
    }

    public void bringSubviewToFront(UIView subview) {
        bringChildToFront(subview);
    }

    public void sendSubviewToBack(UIView subview) {
        if (subview.getSuperview() == this) {
            subview.removeFromSuperview();
            addSubview(subview);
        }
    }

    public void didAddSubview(UIView subview) {

    }

    public void willRemoveSubview(UIView subview) {

    }

    public void willMoveToSuperview(UIView newSuperview) {

    }

    public void didMoveToSuperview() {

    }

    public void willMoveToWindow() {

    }

    public void didMoveToWindow() {

    }

    @Override
    public void onViewAdded(View child) {
        if (UIView.class.isAssignableFrom(child.getClass())) {
            ((UIView) child).willMoveToSuperview(this);
        }
        super.onViewAdded(child);
        if (UIView.class.isAssignableFrom(child.getClass())) {
            didAddSubview((UIView) child);
            ((UIView) child).didMoveToSuperview();
        }
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        if (UIView.class.isAssignableFrom(child.getClass())) {
            willRemoveSubview((UIView) child);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        willMoveToWindow();
        super.onAttachedToWindow();
        didMoveToWindow();
    }

    /* category UIView Layer-Backed Service */

    private boolean wantsLayer = false;
    private CALayer layer = new CALayer();

    public boolean isWantsLayer() {
        return wantsLayer;
    }

    public void setWantsLayer(boolean wantsLayer) {
        this.wantsLayer = wantsLayer;
        invalidate();
    }

    public CALayer getLayer() {
        return layer;
    }

    public void setLayer(CALayer layer) {
        this.layer = layer;
    }

    public void drawRect(Canvas canvas, CGRect rect) {
        // TODO: 2017/1/3 adi
        if (wantsLayer){
            layer.drawRect(canvas, rect);
            setLayerType(LAYER_TYPE_SOFTWARE, null); // for PorterDuffXferMode
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRect(canvas, new CGRect(0, 0, canvas.getWidth(), canvas.getHeight()));
    }

    /* category: UIView touch events */

    private boolean userInteractionEnabled = false;
    private ArrayList<UIGestureRecognizer> gestureRecognizers = new ArrayList<UIGestureRecognizer>();
    private ArrayList<GestureDetector> gestureDetectors = new ArrayList<GestureDetector>();

    public boolean isUserInteractionEnabled() {
        return userInteractionEnabled;
    }

    public void setUserInteractionEnabled(boolean userInteractionEnabled) {
        this.userInteractionEnabled = userInteractionEnabled;
    }

    private GestureDetector gestureDetector;
    public void addGestureRecognizer(UIGestureRecognizer gestureRecognizer) {
        gestureRecognizer.view = this;
        this.setOnTouchListener(this);
        if (gestureRecognizer != null) {
            gestureDetector = new GestureDetector(getContext(), gestureRecognizer);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (gestureDetector != null && this.userInteractionEnabled) {
            gestureDetector.onTouchEvent(event);
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector != null) {
            gestureDetector.onTouchEvent(event);
        }
//        final int action = event.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                touchesBegan();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                touchesMoved();
//                break;
//            case MotionEvent.ACTION_UP:
//                touchesEnded();
//                break;
//        }

        return super.onTouchEvent(event);
    }

    /* UIView animation */

    public void animate(String aKey, float aValue) {
        if (aKey.equalsIgnoreCase("frame.origin.x")) {
            setFrame(this.frame.setX(aValue));
        }
        else if (aKey.equalsIgnoreCase("frame.origin.y")) {
            setFrame(this.frame.setY(aValue));
        }
        else if (aKey.equalsIgnoreCase("frame.size.width")) {
            setFrame(this.frame.setWidth(aValue));
        }
        else if (aKey.equalsIgnoreCase("frame.size.height")) {
            setFrame(this.frame.setHeight(aValue));
        }
        else if (aKey.equalsIgnoreCase("alpha")) {
            setAlpha(aValue);
        }
    }

    static private HashMap<UIView, HashMap<String, UIViewPropertiesLog>> animationState = null;

    static private void addAnimationState(UIView view, String aKey, double originValue, double finalValue) {
        if (animationState == null) {
            return;
        }
        if (originValue == finalValue) {
            return;
        }
        if (animationState.get(view) == null) {
            animationState.put(view, new HashMap<String, UIViewPropertiesLog>());
        }
        UIViewPropertiesLog<Number> log = new UIViewPropertiesLog<>();
        log.valueType = 1;
        log.originValue = originValue;
        log.finalValue = finalValue;
        animationState.get(view).put(aKey, log);
    }

    static private void resetAnimationState() {
        animationState = new HashMap<>();
    }

    static public void animate(double duration, Runnable animations, final Runnable completion) {
        resetAnimationState();
        animations.run();
        final int[] aniCount = {0};
        for (final Map.Entry<UIView, HashMap<String, UIViewPropertiesLog>> viewProps: animationState.entrySet()) {
            for (final Map.Entry<String, UIViewPropertiesLog> animateProp: viewProps.getValue().entrySet()) {
                UIViewPropertiesLog log = animateProp.getValue();
                if (log.valueType == 1) {
                    aniCount[0]++;
                    viewProps.getKey().animate(animateProp.getKey(), (float)((double)log.originValue));
                    ValueAnimator animator = ValueAnimator.ofFloat((float)((double)log.originValue), (float)((double)log.finalValue));
                    animator.setDuration((long) (duration * 1000));
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            float currentValue = (float)valueAnimator.getAnimatedValue();
                            viewProps.getKey().animate(animateProp.getKey(), currentValue);
                        }
                    });
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCount[0]--;
                            if (aniCount[0] <= 0 && completion != null) {
                                completion.run();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    animator.setTarget(viewProps.getKey());
                    animator.start();
                }
            }
        }
        animationState = null;
    }

    static public void animateWithSpring(Runnable animations, final Runnable completion) {
        resetAnimationState();
        animations.run();
        final int[] aniCount = {0};
        SpringSystem system = SpringSystem.create();
        for (final Map.Entry<UIView, HashMap<String, UIViewPropertiesLog>> viewProps: animationState.entrySet()) {
            for (final Map.Entry<String, UIViewPropertiesLog> animateProp: viewProps.getValue().entrySet()) {
                final UIViewPropertiesLog log = animateProp.getValue();
                if (log.valueType == 1) {
                    aniCount[0]++;
                    viewProps.getKey().animate(animateProp.getKey(), (float)((double)log.originValue));
                    Spring spring = system.createSpring();
                    spring.setCurrentValue((float)((double)log.originValue));
                    spring.addListener(new SpringListener() {
                        @Override
                        public void onSpringUpdate(Spring spring) {
                            float currentValue = (float)spring.getCurrentValue();
                            viewProps.getKey().animate(animateProp.getKey(), currentValue);
                        }

                        @Override
                        public void onSpringAtRest(Spring spring) {
                            float currentValue = (float)spring.getCurrentValue();
                            viewProps.getKey().animate(animateProp.getKey(), currentValue);
                            aniCount[0]--;
                            if (aniCount[0] <= 0 && completion != null) {
                                completion.run();
                            }
                        }
                        @Override
                        public void onSpringActivate(Spring spring) {
                        }

                        @Override
                        public void onSpringEndStateChange(Spring spring) {
                        }
                    });
                    spring.setEndValue((float)((double)log.finalValue));
                }
            }
        }
    }

    static public void animateWithSpring(double tension, double friction, double velocity, Runnable animations, final Runnable completion) {
        resetAnimationState();
        animations.run();
        final int[] aniCount = {0};
        SpringSystem system = SpringSystem.create();
        for (final Map.Entry<UIView, HashMap<String, UIViewPropertiesLog>> viewProps: animationState.entrySet()) {
            for (final Map.Entry<String, UIViewPropertiesLog> animateProp: viewProps.getValue().entrySet()) {
                UIViewPropertiesLog log = animateProp.getValue();
                if (log.valueType == 1) {
                    aniCount[0]++;
                    viewProps.getKey().animate(animateProp.getKey(), (float)((double)log.originValue));
                    Spring spring = system.createSpring();
                    spring.setCurrentValue((float)((double)log.originValue));
                    SpringConfig config = new SpringConfig(tension, friction);
                    spring.setSpringConfig(config);
                    spring.setVelocity(velocity);
                    spring.addListener(new SpringListener() {
                        @Override
                        public void onSpringUpdate(Spring spring) {
                            float currentValue = (float)spring.getCurrentValue();
                            viewProps.getKey().animate(animateProp.getKey(), currentValue);
                        }

                        @Override
                        public void onSpringAtRest(Spring spring) {
                            float currentValue = (float)spring.getCurrentValue();
                            viewProps.getKey().animate(animateProp.getKey(), currentValue);
                            aniCount[0]--;
                            if (aniCount[0] <= 0 && completion != null) {
                                completion.run();
                            }
                        }

                        @Override
                        public void onSpringActivate(Spring spring) {
                        }

                        @Override
                        public void onSpringEndStateChange(Spring spring) {
                        }
                    });
                    spring.setEndValue((float)((double)log.finalValue));
                }
            }
        }
    }

}

class UIViewPropertiesLog<T> {

    int valueType = 0;
    T originValue;
    T finalValue;

}