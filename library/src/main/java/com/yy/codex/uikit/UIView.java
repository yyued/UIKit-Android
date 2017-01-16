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
        UIScreen.mainScreen.setContext(getContext());
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
        this.setX((float) (frame.origin.getX() * UIScreen.mainScreen.scale()));
        this.setY((float) (frame.origin.getY() * UIScreen.mainScreen.scale()));

        double mWidth = frame.size.getWidth() * UIScreen.mainScreen.scale();
        double mHeight = frame.size.getHeight() * UIScreen.mainScreen.scale();
        if (Math.ceil(mWidth) - mWidth < 0.1) {
            mWidth = Math.ceil(mWidth);
        }
        if (Math.ceil(mWidth) - mHeight < 0.1) {
            mHeight = Math.ceil(mHeight);
        }
        this.setMinimumWidth((int) mWidth);
        this.setMinimumHeight((int) mHeight);
        CALayer.scaledDensity = (float) UIScreen.mainScreen.scale();
        this.layer.setFrame(new CGRect(0, 0, frame.size.getWidth(), frame.size.getHeight()));
        UIView.animator.addAnimationState(this, "frame.origin.x", oldValue.origin.getX(), frame.origin.getX());
        UIView.animator.addAnimationState(this, "frame.origin.y", oldValue.origin.getY(), frame.origin.getY());
        UIView.animator.addAnimationState(this, "frame.size.width", oldValue.size.getWidth(), frame.size.getWidth());
        UIView.animator.addAnimationState(this, "frame.size.height", oldValue.size.getHeight(), frame.size.getHeight());
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

    @NonNull
    public CGSize intrinsicContentSize() {
        return new CGSize(0, 0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getSuperview() == null) {
            setFrame(new CGRect((float)left / UIScreen.mainScreen.scale(), (float)top / UIScreen.mainScreen.scale(), ((float)right - (float)left) / UIScreen.mainScreen.scale(), ((float)bottom - (float)top) / UIScreen.mainScreen.scale()));
        }
    }

    /* category UIView Rendering */

    public void setBackgroundColor(UIColor color) {
        setBackgroundColor(color.toInt());
    }

    @Override
    public void setAlpha(float alpha) {
        float oldValue = this.getAlpha();
        super.setAlpha(alpha);
        UIView.animator.addAnimationState(this, "alpha", oldValue, alpha);
    }

    private UIColor mTintColor = null;

    public void setTintColor(UIColor tintColor) {
        this.mTintColor = tintColor;
    }

    public UIColor getTintColor() {
        UIColor tintColor = mTintColor;
        UIView superview = getSuperview();
        while (tintColor == null && superview != null) {
            tintColor = superview.mTintColor;
            superview = superview.getSuperview();
        }
        if (tintColor == null) {
            tintColor = new UIColor(0x00/255.0, 0x7a/255.0, 0xff/255.0, 1.0);
        }
        return tintColor;
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
    public void onViewAdded(@NonNull View child) {
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
    public void onViewRemoved(@NonNull View child) {
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

    public void addGestureRecognizer(@NonNull UIGestureRecognizer gestureRecognizer) {
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


    protected UIViewTouchHandler mTouchHandler;

    @Nullable
    private UIView hitTestView;

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (mTouchHandler == null) {
            mTouchHandler = new UIViewTouchHandler(this);
        }
        mTouchHandler.onTouchEvent(event);
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
                    return hitTestView;
                }
            }
            return this;
        }
        return null;
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
        UIView toViewSuperView = toView;
        UIView superView = this;

        // toView is a subview in 'this'
        do {
            convertPoint = convertPointToSubView(point, toViewSuperView);
            toViewSuperView = toViewSuperView.getSuperview();
        }while (toViewSuperView != this && toViewSuperView != null);

        // 'this' is a subview in toView
        if (toViewSuperView == null) {
            do {
                convertPoint = convertPointToSubView(point, superView);
                superView = superView.getSuperview();
            }while (superView != toViewSuperView && superView != null);
        }

        if (superView == null) {
            toViewSuperView = toView;
            superView = this;
            // 'this' and toView not in a same tree
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
        return convertPoint;
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

    @Nullable static private UIGestureRecognizerLooper sGestureRecognizerLooper = null;

    @Override
    public void touchesBegan(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesBegan(touches, event);
        if (UIGestureRecognizerLooper.isHitTestedView(touches, this)) {
            if (sGestureRecognizerLooper == null || sGestureRecognizerLooper.isFinished() || sGestureRecognizerLooper.mGestureRecognizers.size() == 0) {
                sGestureRecognizerLooper = new UIGestureRecognizerLooper(this);
            }
            sGestureRecognizerLooper.onTouchesBegan(touches, event);
        }
    }

    @Override
    public void touchesMoved(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesMoved(touches, event);
        if (UIGestureRecognizerLooper.isHitTestedView(touches, this)) {
            if (sGestureRecognizerLooper == null || sGestureRecognizerLooper.isFinished()) {
                sGestureRecognizerLooper = new UIGestureRecognizerLooper(this);
            }
            sGestureRecognizerLooper.onTouchesMoved(touches, event);
        }
    }

    @Override
    public void touchesEnded(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesEnded(touches, event);
        if (UIGestureRecognizerLooper.isHitTestedView(touches, this)) {
            if (sGestureRecognizerLooper == null || sGestureRecognizerLooper.isFinished()) {
                sGestureRecognizerLooper = new UIGestureRecognizerLooper(this);
            }
            sGestureRecognizerLooper.onTouchesEnded(touches, event);
        }
    }

    /* UIView animation */

    static UIViewAnimator animator = new UIViewAnimator();

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

}