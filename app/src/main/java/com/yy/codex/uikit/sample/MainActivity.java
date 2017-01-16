package com.yy.codex.uikit.sample;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.NSLog;
import com.yy.codex.uikit.UIButton;
import com.yy.codex.uikit.UIConstraint;
import com.yy.codex.uikit.UIControl;
import com.yy.codex.uikit.UIFont;
import com.yy.codex.uikit.UILongPressGestureRecognizer;
import com.yy.codex.uikit.UIPinchGestureRecognizer;
import com.yy.codex.uikit.UITapGestureRecognizer;
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
        UIButton button = new UIButton(getContext());
        UIConstraint constraint = new UIConstraint();
        constraint.centerVertically = true;
        constraint.centerHorizontally = true;
        constraint.width = "200";
        constraint.height = "44";
        button.setConstraint(constraint);
        button.setFont(new UIFont(34));
        button.setTitle("Hello", UIControl.State.Normal);


        addSubview(button);
    }

    public void onTouchDragExit(UIControl control) {
        control.setBackgroundColor(Color.GRAY);
    }

    public void onTouchDragEnter(UIControl control) {
        control.setBackgroundColor(Color.GREEN);
    }

    public void onTouchUpInside(UIControl control) {
        control.setBackgroundColor(Color.RED);
    }

    public void onTouchUpOutside(UIControl control) {
        control.setBackgroundColor(Color.YELLOW);
    }

}
