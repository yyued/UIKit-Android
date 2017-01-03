package com.yy.codex.uikit;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cuiminghui on 2016/12/30.
 */

public class UIView extends FrameLayout {

    /* FrameLayout initialize methods */

    public UIView(Context context, View view) {
        super(context);
        addView(view);
    }

    public UIView(Context context) {
        super(context);
    }

    public UIView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UIView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UIView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* category UIView Layout */

    private CGRect frame = new CGRect(0, 0, 0, 0);

    public CGRect getFrame() {
        return frame;
    }

    public void setFrame(CGRect frame) {
        CGRect oldValue = this.frame;
        this.frame = frame;
        this.setX((float) frame.origin.getX() * getContext().getResources().getDisplayMetrics().scaledDensity);
        this.setY((float) frame.origin.getY() * getContext().getResources().getDisplayMetrics().scaledDensity);
        this.setMinimumWidth((int) (frame.size.getWidth() * getContext().getResources().getDisplayMetrics().scaledDensity));
        this.setMinimumHeight((int) (frame.size.getHeight() * getContext().getResources().getDisplayMetrics().scaledDensity));
        UIView.addAnimationState(this, "frame.origin.x", oldValue.origin.getX(), frame.origin.getX());
        UIView.addAnimationState(this, "frame.origin.y", oldValue.origin.getY(), frame.origin.getY());
        UIView.addAnimationState(this, "frame.size.width", oldValue.size.getWidth(), frame.size.getWidth());
        UIView.addAnimationState(this, "frame.size.height", oldValue.size.getHeight(), frame.size.getHeight());
    }

    /* category UIView Hierarchy */

    private UIView superview;
    private UIView[] subviews = new UIView[0];

    public void removeFromSuperview() {
        if (superview != null) {
            superview.removeView(this);
            UIView[] cloneSubviews = new UIView[superview.subviews.length - 1];
            for (int i = 0, j = 0; i < superview.subviews.length; i++) {
                if (superview.subviews[i] != this) {
                    cloneSubviews[j] = subviews[i];
                }
                j++;
            }
            subviews = cloneSubviews;
        }
    }

    public void addSubview(UIView subview) {
        subview.removeFromSuperview();
        subview.superview = this;
        UIView[] cloneSubviews = new UIView[subviews.length + 1];
        for (int i = 0; i < subviews.length; i++) {
            cloneSubviews[i] = subviews[i];
        }
        cloneSubviews[cloneSubviews.length - 1] = subview;
        subviews = cloneSubviews;
        addView(subview, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRect(canvas, new CGRect(0, 0, canvas.getWidth(), canvas.getHeight()));
    }

    /* category: UIView touch events */

    private boolean userInteractionEnabled = false;


    public boolean isUserInteractionEnabled() {
        return userInteractionEnabled;
    }

    public void setUserInteractionEnabled(boolean userInteractionEnabled) {
        this.userInteractionEnabled = userInteractionEnabled;
    }

    public void addGestureRecognizer() {
        // todo: zhixuan
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
    }

    static private HashMap<UIView, HashMap<String, UIViewPropertiesLog>> animationState = new HashMap<>();

    static private void addAnimationState(UIView view, String aKey, double originValue, double finalValue) {
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
        animationState.clear();
    }

    static public void animate(double duration, Runnable animations, Runnable completion) {
        resetAnimationState();
        animations.run();
        for (final Map.Entry<UIView, HashMap<String, UIViewPropertiesLog>> viewProps: animationState.entrySet()) {
            for (final Map.Entry<String, UIViewPropertiesLog> animateProp: viewProps.getValue().entrySet()) {
                String aKey = animateProp.getKey();
                UIViewPropertiesLog log = animateProp.getValue();
                if (log.valueType == 1) {
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
                    animator.setTarget(viewProps.getKey());
                    animator.start();
                }
            }
        }
    }

    static public void animateWithSpring(double velocity, Runnable animations, Runnable completion) {
        resetAnimationState();
        animations.run();
        SpringSystem system = SpringSystem.create();
        for (final Map.Entry<UIView, HashMap<String, UIViewPropertiesLog>> viewProps: animationState.entrySet()) {
            for (final Map.Entry<String, UIViewPropertiesLog> animateProp: viewProps.getValue().entrySet()) {
                String aKey = animateProp.getKey();
                UIViewPropertiesLog log = animateProp.getValue();
                if (log.valueType == 1) {
                    viewProps.getKey().animate(animateProp.getKey(), (float)((double)log.originValue));
                    Spring spring = system.createSpring();
                    spring.setCurrentValue((float)((double)log.originValue));
                    spring.setVelocity(velocity);
                    spring.addListener(new SpringListener() {
                        @Override
                        public void onSpringUpdate(Spring spring) {
                            float currentValue = (float)spring.getCurrentValue();
                            viewProps.getKey().animate(animateProp.getKey(), currentValue);
                        }

                        @Override
                        public void onSpringAtRest(Spring spring) {

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

    static public void animateWithSpring(double tension, double friction, double velocity, Runnable animations, Runnable completion) {
        resetAnimationState();
        animations.run();
        SpringSystem system = SpringSystem.create();
        for (final Map.Entry<UIView, HashMap<String, UIViewPropertiesLog>> viewProps: animationState.entrySet()) {
            for (final Map.Entry<String, UIViewPropertiesLog> animateProp: viewProps.getValue().entrySet()) {
                String aKey = animateProp.getKey();
                UIViewPropertiesLog log = animateProp.getValue();
                if (log.valueType == 1) {
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