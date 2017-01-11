package com.yy.codex.uikit.sample;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yy.codex.uikit.CALayer;
import com.yy.codex.uikit.CGPoint;
import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.UITouch;
import com.yy.codex.uikit.UIView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TestView(this));
    }

}

class TestView extends UIView {

    public TestView(Context context, View view) {
        super(context, view);
        init();
    }

    public TestView(Context context) {
        super(context);
        init();
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        final UIView view = new UIView(getContext());
        view.setFrame(new CGRect(44, 44, 44, 44));
//        view.setBackgroundColor(Color.BLACK);
        view.setWantsLayer(true);
        view.getLayer().setBackgroundColor(Color.BLACK);
        CALayer sublayer = new CALayer();
        sublayer.setFrame(new CGRect(20, 20, 20, 20));
        sublayer.setBackgroundColor(Color.YELLOW);
        CALayer subsubLayer = new CALayer();
        subsubLayer.setFrame(new CGRect(0,0,10,10));
        subsubLayer.setBackgroundColor(Color.GREEN);
        sublayer.addSubLayer(subsubLayer);
        view.getLayer().addSubLayer(sublayer);
        view.setUserInteractionEnabled(true);
        addSubview(view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println(event);
        return super.onTouchEvent(event);
    }

}
