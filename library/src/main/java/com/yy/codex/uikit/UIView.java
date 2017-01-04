package com.yy.codex.uikit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by cuiminghui on 2016/12/30.
 */

public class UIView extends FrameLayout {

    /* FrameLayout initialize methods */

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
        this.frame = frame;
        this.setX((float) frame.origin.getX());
        this.setY((float) frame.origin.getY());
        this.setMinimumWidth((int) frame.size.getWidth());
        this.setMinimumHeight((int) frame.size.getHeight());
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
        if (wantsLayer){
            layer.drawRect(canvas, rect);
            if (layer.getShadowRadius() > 0 && getLayerType() != LAYER_TYPE_SOFTWARE){
                setLayerType(LAYER_TYPE_SOFTWARE, null);
            }
        }
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

}