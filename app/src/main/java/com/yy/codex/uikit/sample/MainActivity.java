package com.yy.codex.uikit.sample;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.UIColor;
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
    }

    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init() {
        super.init();
        final UIView aView = new UIView(getContext());
        aView.setFrame(new CGRect(0, 200, 44, 44));
        aView.setBackgroundColor(UIColor.redColor);
        postDelayed(new Runnable() {
            @Override
            public void run() {
//                UIView.animator.decay(aView, "frame.origin.x", 0, 0.6, null);
//                UIView.animator.decay(aView, "frame.origin.y", 0, 0.6, null);
//                UIView.animator.decayToValue(aView, "frame.origin.x", 0, 300, null);
            }
        }, 3000);
        addSubview(aView);
    }

}
