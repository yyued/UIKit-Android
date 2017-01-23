package com.yy.codex.uikit.sample;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.CGSize;
import com.yy.codex.uikit.UIColor;
import com.yy.codex.uikit.UIConstraint;
import com.yy.codex.uikit.UIScrollView;
import com.yy.codex.uikit.UIView;

/**
 * Created by it on 17/1/10.
 */

public class MyMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyTestView(this));
    }

}

class MyTestView extends UIView {

    public MyTestView(Context context, View view) {
        super(context, view);
    }

    public MyTestView(Context context) {
        super(context);
    }

    public MyTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyTestView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void init() {
        super.init();
        UIScrollView scrollView = new UIScrollView(getContext());
        UIConstraint constraint = new UIConstraint();
        constraint.setCenterHorizontally(true);
        constraint.setCenterVertically(true);
        constraint.setWidth("100%");
        constraint.setHeight("100%");
        scrollView.setConstraint(constraint);
        scrollView.setBounces(true);
        scrollView.setPagingEnabled(true);
        scrollView.setBackgroundColor(UIColor.Companion.getBlackColor().colorWithAlpha(0.1));
        for (int i = 0; i < 100; i++) {
            UIView redView = new UIView(getContext());
            redView.setFrame(new CGRect(100 * i, 0,20,20));
            redView.setBackgroundColor(new UIColor(1, 0, 0, 1.0 - (i / 100.0)));
            if (i == 0) {
                redView.setBackgroundColor(UIColor.Companion.getBlueColor());
            }
            scrollView.addSubview(redView);
        }
        scrollView.setContentSize(new CGSize(100 * 100, 0));
        addSubview(scrollView);


    }
}
