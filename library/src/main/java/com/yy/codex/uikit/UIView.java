package com.yy.codex.uikit;

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

import java.util.ArrayList;

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
        this.mLayer.bindView(this);
        setWillNotDraw(false);
    }

    /* category UIView Layout */

    @NonNull  private CGRect mFrame = new CGRect(0, 0, 0, 0);

    @NonNull
    public CGRect getFrame() {
        return mFrame;
    }

    public void setFrame(@NonNull CGRect frame) {
        if (this.getFrame().equals(frame)) {
            return;
        }
        CGRect oldValue = this.mFrame;
        this.mFrame = frame;
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
        this.mLayer.setFrame(new CGRect(0, 0, frame.size.getWidth(), frame.size.getHeight()));
        UIView.sAnimator.addAnimationState(this, "mFrame.origin.x", oldValue.origin.getX(), frame.origin.getX());
        UIView.sAnimator.addAnimationState(this, "mFrame.origin.y", oldValue.origin.getY(), frame.origin.getY());
        UIView.sAnimator.addAnimationState(this, "mFrame.size.width", oldValue.size.getWidth(), frame.size.getWidth());
        UIView.sAnimator.addAnimationState(this, "mFrame.size.height", oldValue.size.getHeight(), frame.size.getHeight());
    }

    @NonNull
    public CGPoint getCenter() {
        return new CGPoint((mFrame.origin.getX() + mFrame.size.getWidth()) / 2.0, (mFrame.origin.getY() + mFrame.size.getHeight()) / 2.0);
    }

    @Nullable private UIConstraint mConstraint = null;

    @Nullable
    public UIConstraint getConstraint() {
        return mConstraint;
    }

    public void setConstraint(@Nullable UIConstraint constraint) {
        this.mConstraint = constraint;
        UIView superview = getSuperview();
        if (superview != null) {
            superview.layoutSubviews();
        }
    }

    private double mMaxWidth = 0.0;

    public double getMaxWidth() {
        return mMaxWidth;
    }

    public void setMaxWidth(double maxWidth) {
        this.mMaxWidth = maxWidth;
    }

    public void layoutSubviews() {
        UIView previous = null;
        UIView[] subviews = getSubviews();
        for (int i = 0; i < subviews.length; i++) {
            UIView subview = subviews[i];
            if (subview.mConstraint != null && !subview.mConstraint.disabled) {
                subview.setFrame(subview.mConstraint.requestFrame(subview, this, previous));
            }
            previous = subview;
        }
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
        UIView.sAnimator.addAnimationState(this, "alpha", oldValue, alpha);
    }

    private UIColor mTintColor = null;

    public void setTintColor(UIColor tintColor) {
        this.mTintColor = tintColor;
        tintColorDidChanged();
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

    public void tintColorDidChanged() {
        UIView[] subviews = getSubviews();
        for (int i = 0; i < subviews.length; i++) {
            subviews[i].tintColorDidChanged();
        }
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

    public void didAddSubview(@NonNull UIView subview) {}

    public void willRemoveSubview(@NonNull UIView subview) {}

    public void willMoveToSuperview(@Nullable UIView newSuperview) {
        tintColorDidChanged();
    }

    public void didMoveToSuperview() {}

    public void willMoveToWindow() {}

    public void didMoveToWindow() {}

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

    private boolean mWantsLayer = false;
    @NonNull private CALayer mLayer = new CALayer();

    public boolean isWantsLayer() {
        return mWantsLayer;
    }

    public void setWantsLayer(boolean wantsLayer) {
        if (this.mWantsLayer != wantsLayer) {
            this.mWantsLayer = wantsLayer;
            if (wantsLayer) {
                setLayerType(LAYER_TYPE_SOFTWARE, null);
            }
            invalidate();
        }
    }

    @NonNull
    public CALayer getLayer() {
        return mLayer;
    }

    public void drawRect(@NonNull Canvas canvas, @NonNull CGRect rect) {
        // TODO: 2017/1/3 adi
        if (mWantsLayer){
            mLayer.drawRect(canvas, rect);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        drawRect(canvas, new CGRect(0, 0, canvas.getWidth(), canvas.getHeight()));
    }

    /* category: UIView touch events */

    private boolean mUserInteractionEnabled = true;

    @NonNull private ArrayList<UIGestureRecognizer> mGestureRecognizers = new ArrayList<>();

    public boolean isUserInteractionEnabled() {
        return mUserInteractionEnabled;
    }

    public void setUserInteractionEnabled(boolean userInteractionEnabled) {
        this.mUserInteractionEnabled = userInteractionEnabled;
    }

    public void addGestureRecognizer(@NonNull UIGestureRecognizer gestureRecognizer) {
        mGestureRecognizers.add(gestureRecognizer);
        gestureRecognizer.didAddToView(this);
    }

    @NonNull public ArrayList<UIGestureRecognizer> getGestureRecognizers() {
        return mGestureRecognizers;
    }

    @Nullable protected UIViewTouchHandler mTouchHandler;

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
        return UIViewHelpers.hitTest(this, point, event);
    }

    @NonNull
    public CGPoint convertPoint(@NonNull CGPoint point, @NonNull UIView toView) {
        return UIViewHelpers.convertPoint(this, point, toView);
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

    @NonNull static public UIViewAnimator sAnimator = new UIViewAnimator();

    public void animate(@NonNull String aKey, float aValue) {
        if (aKey.equalsIgnoreCase("mFrame.origin.x")) {
            setFrame(this.mFrame.setX(aValue));
        }
        else if (aKey.equalsIgnoreCase("mFrame.origin.y")) {
            setFrame(this.mFrame.setY(aValue));
        }
        else if (aKey.equalsIgnoreCase("mFrame.size.width")) {
            setFrame(this.mFrame.setWidth(aValue));
        }
        else if (aKey.equalsIgnoreCase("mFrame.size.height")) {
            setFrame(this.mFrame.setHeight(aValue));
        }
        else if (aKey.equalsIgnoreCase("alpha")) {
            setAlpha(aValue);
        }
        else if (aKey.startsWith("mLayer.")) {
            getLayer().animate(aKey, aValue);
        }
    }

}