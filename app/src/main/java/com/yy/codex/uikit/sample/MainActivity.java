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
import com.yy.codex.uikit.UILabel;
import com.yy.codex.uikit.UINavigationBar;
import com.yy.codex.uikit.UINavigationController;
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

        UINavigationController navigationController = new UINavigationController(this);
        navigationController.setRootViewController(new TestViewController(this));
        navigationController.getView().setMaterialDesign(true);

        setContentView(navigationController.getView());
    }

}

class TestViewController extends UIViewController {

    public TestViewController(Context context) {
        super(context);
    }

    @Override
    public void loadView() {
        setView(new TestView(getContext()));
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        setTitle("Test");
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
//        setAutomaticallyAdjustsSpace(true);
        UIScrollView scrollView = new UIScrollView(getContext());
        UIConstraint constraint = new UIConstraint();
        constraint.centerHorizontally = true;
        constraint.centerVertically = true;
        constraint.width = "100%";
        constraint.height = "100%";
        scrollView.setConstraint(constraint);
        scrollView.setBackgroundColor(UIColor.whiteColor);
        for (int i = 0; i < 20; i++) {
            UILabel label = new UILabel(getContext());
            label.setFrame(new CGRect(0,100 * i,100,22));
//            label.setBackgroundColor(new UIColor(1, 0, 0, 1.0 - (i / 100.0)));
            label.setText("i = " + i);
            scrollView.addSubview(label);
        }
        scrollView.setBounces(true);
        scrollView.setAlwaysBounceVertical(true);
        scrollView.setContentSize(new CGSize(0, 2000));
        addSubview(scrollView);
    }

}
