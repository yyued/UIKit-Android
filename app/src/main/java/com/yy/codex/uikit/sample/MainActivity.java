package com.yy.codex.uikit.sample;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.yy.codex.uikit.CALayer;
import com.yy.codex.uikit.CGRect;
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
        final UIView roundView = new UIView(getContext());
        roundView.setFrame(new CGRect(44, 44, 44, 44));
        roundView.setWantsLayer(true);
        roundView.getLayer().setBackgroundColor(Color.BLACK);
        roundView.getLayer().setCornerRadius(22.0f);
        addSubview(roundView);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                UIView.animateWithSpring(new Runnable() {
                    @Override
                    public void run() {
                        roundView.setFrame(new CGRect(22, 22, 88, 88));
//                        roundView.getLayer().setCornerRadius(44.0f);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        roundView.getLayer().setCornerRadius(44.0f);
                        roundView.invalidate();
                    }
                });
            }
        }, 3000);
    }

}
