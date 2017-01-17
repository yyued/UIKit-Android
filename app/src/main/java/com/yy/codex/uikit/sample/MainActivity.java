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
import com.yy.codex.uikit.CGSize;
import com.yy.codex.uikit.UIColor;
import com.yy.codex.uikit.UIConstraint;
import com.yy.codex.uikit.UIScrollView;
import com.yy.codex.uikit.UISwitch;
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

        UIScrollView scrollView = new UIScrollView(getContext());
        UIConstraint constraint = new UIConstraint();
        constraint.centerHorizontally = true;
        constraint.centerVertically = true;
        constraint.width = "100%";
        constraint.height = "100%";
        scrollView.setConstraint(constraint);

        scrollView.setBackgroundColor(UIColor.blackColor.colorWithAlpha(0.1));

        for (int i = 0; i < 20; i++) {
            UIView redView = new UIView(getContext());
            redView.setFrame(new CGRect(0,100 * i,20,20));
            redView.setBackgroundColor(new UIColor(1, 0, 0, 1.0 - (i / 100.0)));
            if (i == 0) {
                redView.setBackgroundColor(UIColor.blueColor);
            }
            scrollView.addSubview(redView);
        }

        scrollView.setContentSize(new CGSize(0, 100 * 20));

        addSubview(scrollView);
    }

}
