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

import com.yy.codex.uikit.CGPoint;
import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.NSLog;
import com.yy.codex.uikit.UIGestureRecognizerState;
import com.yy.codex.uikit.UILongPressGestureRecognizer;
import com.yy.codex.uikit.UIPanGestureRecognizer;
import com.yy.codex.uikit.UIScreen;
import com.yy.codex.uikit.UIScreenEdgePanGestureRecognizer;
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
        setUserInteractionEnabled(true);
        UIScreenEdgePanGestureRecognizer screenEdgePanGestureRecognizer = new UIScreenEdgePanGestureRecognizer(this, "onPan:");
        screenEdgePanGestureRecognizer.edge = UIScreenEdgePanGestureRecognizer.Edge.Bottom;
        addGestureRecognizer(screenEdgePanGestureRecognizer);
    }

    public void onPan(UIScreenEdgePanGestureRecognizer screenEdgePanGestureRecognizer) {
        if (screenEdgePanGestureRecognizer.getState() == UIGestureRecognizerState.Changed) {
            NSLog.log(screenEdgePanGestureRecognizer.translation());
        }
    }

}
