package com.yy.codex.uikit;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by cuiminghui on 2016/12/30.
 */

public class UIView extends UIResponder {

    /* FrameLayout initialize methods */

    public UIView(@NonNull Context context, @NonNull View view) {
        super(context);
        setupProps();
        addView(view);
    }

    public UIView(@NonNull Context context) {
        super(context);
        setupProps();
    }

    public UIView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        setupProps();
    }

    public UIView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupProps();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UIView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupProps();
    }

    private void setupProps(){
        this.layer.bindView(this);
        setWillNotDraw(false);
    }

    /* category UIView Layout */

    @NonNull  private CGRect frame = new CGRect(0, 0, 0, 0);
    @Nullable private UIConstraint constraint = null;

    @NonNull
    public CGRect getFrame() {
        return frame;
    }

    public void setFrame(@NonNull CGRect frame) {
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

    @NonNull
    public CGPoint getCenter() {
        return new CGPoint((frame.origin.getX() + frame.size.getWidth()) / 2.0, (frame.origin.getY() + frame.size.getHeight()) / 2.0);
    }

    @Nullable
    public UIConstraint getConstraint() {
        return constraint;
    }

    public void setConstraint(@Nullable UIConstraint constraint) {
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

    @Nullable
    public UIView getSuperview() {
        ViewParent parent = getParent();
        if (parent != null && UIView.class.isAssignableFrom(parent.getClass())) {
            return (UIView)parent;
        }
        return null;
    }

    @NonNull
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

    public void insertSubview(@NonNull UIView subview, int atIndex) {
        subview.removeFromSuperview();
        if (atIndex < 0 || atIndex > getChildCount()) {
            return;
        }
        addView(subview, atIndex, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void addSubview(@NonNull UIView subview) {
        subview.removeFromSuperview();
        subview.setNextResponder(this);
        addView(subview, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void insertBelowSubview(@NonNull UIView subview, @NonNull UIView siblingSubview) {
        if (siblingSubview.getSuperview() == this) {
            int atIndex = indexOfChild(siblingSubview);
            insertSubview(subview, atIndex);
        }
    }

    public void insertAboveSubview(@NonNull UIView subview, @NonNull UIView siblingSubview) {
        if (siblingSubview.getSuperview() == this) {
            int atIndex = indexOfChild(siblingSubview);
            insertSubview(subview, atIndex + 1);
        }
    }

    public void bringSubviewToFront(@NonNull UIView subview) {
        bringChildToFront(subview);
    }

    public void sendSubviewToBack(@NonNull UIView subview) {
        if (subview.getSuperview() == this) {
            subview.removeFromSuperview();
            addSubview(subview);
        }
    }

    public void didAddSubview(@NonNull UIView subview) {

    }

    public void willRemoveSubview(@NonNull UIView subview) {

    }

    public void willMoveToSuperview(@Nullable UIView newSuperview) {

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
    @NonNull private CALayer layer = new CALayer();

    public boolean isWantsLayer() {
        return wantsLayer;
    }

    public void setWantsLayer(boolean wantsLayer) {
        if (this.wantsLayer != wantsLayer) {
            this.wantsLayer = wantsLayer;
            if (wantsLayer) {
                setLayerType(LAYER_TYPE_SOFTWARE, null);
            }
            invalidate();
        }
    }

    @NonNull
    public CALayer getLayer() {
        return layer;
    }

    public void drawRect(@NonNull Canvas canvas, @NonNull CGRect rect) {
        // TODO: 2017/1/3 adi
        if (wantsLayer){
            layer.drawRect(canvas, rect);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        drawRect(canvas, new CGRect(0, 0, canvas.getWidth(), canvas.getHeight()));
    }

    /* category: UIView touch events */

    private boolean userInteractionEnabled = true;
    @NonNull private ArrayList<UIGestureRecognizer> gestureRecognizers = new ArrayList<>();

    public boolean isUserInteractionEnabled() {
        return userInteractionEnabled;
    }

    public void setUserInteractionEnabled(boolean userInteractionEnabled) {
        this.userInteractionEnabled = userInteractionEnabled;
    }

    public void addGestureRecognizer(UIGestureRecognizer gestureRecognizer) {
        gestureRecognizers.add(gestureRecognizer);
        gestureRecognizer.didAddToView(this);
    }

    @NonNull public ArrayList<UIGestureRecognizer> getGestureRecognizers() {
        return gestureRecognizers;
    }

    private boolean multipleTouchEnabled = false;
    public boolean isMultipleTouchEnabled() {
        return multipleTouchEnabled;
    }

    public void setMultipleTouchEnabled(boolean multipleTouchEnabled) {
        this.multipleTouchEnabled = multipleTouchEnabled;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        CGPoint touchPoint = new CGPoint(event.getX() / scaledDensity, event.getY() / scaledDensity);
        UIView hitTestView = hitTest(touchPoint, event);
        sendEvent(event, hitTestView);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull MotionEvent ev) {
        return true;
    }

    @Nullable
    public UIView hitTest(@NonNull CGPoint point, @NonNull MotionEvent event) {
        UIView[] views = getSubviews();
        if (!isUserInteractionEnabled() && !(getAlpha() > 0)) {
            return null;
        }

        if (pointInside(point)) {
            for (UIView subview: views) {
                CGPoint convertedPoint = convertPoint(point, subview);
                UIView hitTestView = subview.hitTest(convertedPoint, event);
                if (hitTestView != null) {
                    prepareTouch(convertedPoint, hitTestView, event);
                    return hitTestView;
                }
            }
            prepareTouch(point, this, event);
            return this;
        }
        return null;
    }

    private int mTouchCount = 0;
    @Nullable private Set<UITouch> touches = new HashSet<UITouch>();

    private void prepareTouch(@NonNull CGPoint touchPoint, @NonNull UIView hitTestView, @NonNull MotionEvent event) {
        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                mTouchCount++;
                touches.clear();
                UITouch touch = new UITouch(hitTestView, touchPoint);
                touch.resetTapCount();
                touches.add(touch);
            }
                break;
            case MotionEvent.ACTION_MOVE: {
                touches.clear();
                for (int i = 0; i < event.getPointerCount(); i++) {
                    double x = event.getX(i);
                    double y = event.getY(i);

                    UITouch touch = new UITouch(hitTestView, touchPoint);
                    touches.add(touch);
                }
            }
                break;
            case MotionEvent.ACTION_UP:{
                mTouchCount--;
                touches.clear();
                UITouch touch = new UITouch(hitTestView, touchPoint);
                touch.resetTapCount();
                touches.add(touch);
            }
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                mTouchCount++;
                UITouch touch = new UITouch(hitTestView, touchPoint);
                touch.resetTapCount();
                touches.add(touch);
            }
                break;
            case MotionEvent.ACTION_POINTER_UP: {
                mTouchCount--;
            }
                break;
            default:
                break;
        }
    }

    private void sendEvent(@NonNull MotionEvent event, @NonNull UIView hitTestView) {
        final int action = event.getAction();
        UIEvent ev = new UIEvent();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                hitTestView.touchesBegan(touches, ev);
                break;
            case MotionEvent.ACTION_MOVE:
                hitTestView.touchesMoved(touches, ev);
                break;
            case MotionEvent.ACTION_UP:
                hitTestView.touchesEnded(touches, ev);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                hitTestView.touchesBegan(touches, ev);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                hitTestView.touchesEnded(touches, ev);
                break;
            default:
                break;
        }
    }

    public boolean pointInside(@NonNull CGPoint point) {
        double h = getFrame().size.getHeight();
        double w = getFrame().size.getWidth();

        double touchX = point.getX();
        double touchY = point.getY();

        if (touchY <= h && touchX <= w && touchY >= 0 && touchX >= 0) {
            return true;
        }

        return false;
    }

    @NonNull
    public CGPoint convertPoint(@NonNull CGPoint point, @NonNull UIView toView) {
        if (this == toView) {
            return point;
        }

        CGPoint convertPoint = point;
        List<UIView> listSubViews = Arrays.asList(this.getSubviews());
        List<UIView> listToViewViews = Arrays.asList(toView.getSubviews());
        UIView toViewSuperView = toView;
        UIView superView = this;

        if (listSubViews.contains(toView)) {
            // toView is a subview in 'this'
            do {
                convertPoint = convertPointToSubView(point, toViewSuperView);
                toViewSuperView = toViewSuperView.getSuperview();
            }while (toViewSuperView != this && toViewSuperView != null);

            return convertPoint;
        }
        else if (listToViewViews.contains(this)){
            // 'this' is a subview in toView
            do {
                convertPoint = convertPointToSuperView(convertPoint, superView);
                superView = superView.getSuperview();
            }while (toViewSuperView != this);

            return convertPoint;
        }
        else {
            do {
                UIView innerToViewSuperView = toViewSuperView.getSuperview();
                UIView innerSuperView = superView.getSuperview();
                if (innerToViewSuperView == superView) {
                    break;
                }

                convertPoint = convertPointToSuperView(convertPoint, superView);

                if (innerToViewSuperView != null) {
                    toViewSuperView = innerToViewSuperView;
                }

                if (innerSuperView != null) {
                    superView = innerSuperView;
                }

            }while (toViewSuperView != superView);

            if (toViewSuperView != null && superView != null) {

                double toX = toView.frame.origin.getX();
                double toY = toView.frame.origin.getY();

                return new CGPoint(convertPoint.getX() - toX, convertPoint.getY() - toY);
            }
        }

        return new CGPoint(0, 0);
    }

    @NonNull
    private CGPoint convertPointToSuperView(@NonNull CGPoint point, @NonNull UIView superView) {
        double x = superView.frame.origin.getX();
        double y = superView.frame.origin.getY();
        return new CGPoint(point.getX() + x, point.getY() + y);
    }

    @NonNull
    private CGPoint convertPointToSubView(@NonNull CGPoint point, @NonNull UIView subView) {
        double x = subView.frame.origin.getX();
        double y = subView.frame.origin.getY();
        return new CGPoint(point.getX() - x, point.getY() - y);
    }

    /* UIView animation */

    public void animate(@NonNull String aKey, float aValue) {
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
        else if (aKey.startsWith("layer.")) {
            getLayer().animate(aKey, aValue);
        }
    }

    @Nullable static private HashMap<UIView, HashMap<String, UIViewPropertiesLog>> animationState = null;

    static void addAnimationState(@NonNull UIView view, @NonNull String aKey, double originValue, double finalValue) {
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

    static public void animate(double duration, @NonNull Runnable animations, @Nullable final Runnable completion) {
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
        if (aniCount[0] <= 0) {
            if (completion != null) {
                completion.run();
            }
        }
        animationState = null;
    }

    static public void animateWithSpring(@NonNull Runnable animations, @Nullable final Runnable completion) {
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
        if (aniCount[0] <= 0) {
            if (completion != null) {
                completion.run();
            }
        }
        animationState = null;
    }

    static public void animateWithSpring(double tension, double friction, double velocity, @NonNull Runnable animations, @Nullable final Runnable completion) {
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
        if (aniCount[0] <= 0) {
            if (completion != null) {
                completion.run();
            }
        }
        animationState = null;
    }

}

class UIViewPropertiesLog<T> {

    int valueType = 0;
    T originValue;
    T finalValue;

}