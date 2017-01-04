package com.yy.codex.uikit.sample;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewGroupCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.UIView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final UIView view = new TestView(this);
        view.setBackgroundColor(Color.GRAY);
        setContentView(view);
    }
}

class TestView extends UIView {

    UIView redView;

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

    public void init() {
        redView = new UIView(getContext());
        redView.setBackgroundColor(Color.RED);
        redView.setFrame(new CGRect(0,0,20,20));
        addSubview(redView);
    }

    @Override
    public void layoutSubviews() {
        super.layoutSubviews();
        redView.setFrame(new CGRect(getFrame().size.getWidth() * 0.25, getFrame().size.getHeight() * 0.375 ,getFrame().size.getWidth() * 0.5, getFrame().size.getHeight() * 0.25));
    }

}
