package com.yy.codex.uikit;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by cuiminghui on 2016/12/30.
 */

public class UIView extends FrameLayout {

    private CGRect frame = new CGRect(0, 0, 0, 0);
    private boolean wantsLayer = false;
    private CALayer layer = new CALayer();
    private boolean userInteractionEnabled = false;

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

    public CGRect getFrame() {
        return frame;
    }

    public void setFrame(CGRect frame) {
        this.frame = frame;
    }

    public boolean isUserInteractionEnabled() {
        return userInteractionEnabled;
    }

    public void setUserInteractionEnabled(boolean userInteractionEnabled) {
        this.userInteractionEnabled = userInteractionEnabled;
    }

    public void addGestureRecognizer() {
        // todo: zhixuan
    }

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

}
