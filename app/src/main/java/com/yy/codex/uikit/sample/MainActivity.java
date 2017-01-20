package com.yy.codex.uikit.sample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.CGSize;
import com.yy.codex.uikit.NSAttributedString;
import com.yy.codex.uikit.UIBarButtonItem;
import com.yy.codex.uikit.UIColor;
import com.yy.codex.uikit.UIConstraint;
import com.yy.codex.uikit.UIFont;
import com.yy.codex.uikit.UINavigationBar;
import com.yy.codex.uikit.UINavigationItem;
import com.yy.codex.uikit.UIScrollView;
import com.yy.codex.uikit.UISwitch;
import com.yy.codex.uikit.UIView;
import com.yy.codex.uikit.UIViewController;

import java.io.File;
import java.util.HashMap;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIViewController viewController = new UIViewController(this);
        viewController.setView(new TestView(this));
        setContentView(viewController.getView());
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

        setBackgroundColor(UIColor.whiteColor);

        UIScrollView scrollView = new UIScrollView(getContext());
        UIConstraint constraint = new UIConstraint();
        constraint.centerHorizontally = true;
        constraint.centerVertically = true;
        constraint.width = "100%";
        constraint.height = "100% - 44";
        constraint.top = "22";
        scrollView.setConstraint(constraint);

        scrollView.setBackgroundColor(UIColor.whiteColor);

        for (int i = 0; i < 20; i++) {
            UIView redView = new UIView(getContext());
            redView.setFrame(new CGRect(0,100 * i,20,20));
            redView.setBackgroundColor(new UIColor(1, 0, 0, 1.0 - (i / 100.0)));
            if (i == 0) {
                redView.setBackgroundColor(UIColor.blueColor);
            }
            scrollView.addSubview(redView);
        }

        scrollView.setAlwaysBounceVertical(true);
        scrollView.setContentSize(new CGSize(0, 2000));

        addSubview(scrollView);


        setMaterialDesign(true);

        final UINavigationBar navigationBar = new UINavigationBar(getContext());

        addSubview(navigationBar);

        final UINavigationItem navigationItem = new UINavigationItem(getContext());
        navigationItem.setTitle("测试");
        UIBarButtonItem barButtonItem = new UIBarButtonItem();
        barButtonItem.setTitle("Test");
        navigationItem.setRightBarButtonItem(barButtonItem);

        navigationBar.setItems(new UINavigationItem[]{navigationItem}, false);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                final UINavigationItem navigationItem = new UINavigationItem(getContext());
                navigationItem.setTitle("修改用户名");
                UIBarButtonItem barButtonItem = new UIBarButtonItem();
//                barButtonItem.setTitle("Next");
                navigationItem.setRightBarButtonItem(barButtonItem);
                navigationBar.pushNavigationItem(navigationItem, true);
            }
        }, 3000);


    }

}
