package com.yy.codex.uikit.sample;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.yy.codex.uikit.CALayer;
import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.CGSize;
import com.yy.codex.uikit.UIBarButtonItem;
import com.yy.codex.uikit.UIColor;
import com.yy.codex.uikit.UIConstraint;
import com.yy.codex.uikit.UILabel;
import com.yy.codex.uikit.UINavigationActivity;
import com.yy.codex.uikit.UINavigationController;
import com.yy.codex.uikit.UINavigationController_ActivityBase;
import com.yy.codex.uikit.UIScrollView;
import com.yy.codex.uikit.UIView;
import com.yy.codex.uikit.UIViewAnimator;
import com.yy.codex.uikit.UIViewController;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends UINavigationActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNavigationController().getView().setMaterialDesign(true);
        CGSize size = new CGSize(100, 100);
        CGSize size2 = new CGSize(100, 100);
        if (size.equals(size2)) {
            System.gc();
        }
    }

    @NotNull
    @Override
    public UINavigationController createNavigationController() {
        return new UINavigationController_ActivityBase(this);
    }

    @NotNull
    @Override
    public UIViewController rootViewController() {
        return new TestViewController(this);
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
        getNavigationItem().setRightBarButtonItem(new UIBarButtonItem("Next", this, "handleNextButtonTapped"));
    }

    private void handleNextButtonTapped() {
        navigationController().pushViewController(new NextViewController(getContext()), true);
    }

}

class NextViewController extends UIViewController {

    public NextViewController(@NonNull Context context) {
        super(context);
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        setTitle("I'm Next");
        getView().setBackgroundColor(UIColor.Companion.getGrayColor());
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
    public void init() {
        super.init();
        final UIView testView = new UIView(getContext());
        testView.setFrame(new CGRect(44, 44, 44, 44));
        testView.setWantsLayer(true);
        testView.getLayer().setBackgroundColor(UIColor.Companion.getOrangeColor());
        testView.getLayer().setCornerRadius(22.0);
        testView.getLayer().setShadowColor(UIColor.Companion.getBlackColor().colorWithAlpha(0.5));
        testView.getLayer().setShadowRadius(8.0);
        testView.getLayer().setShadowX(2);
        testView.getLayer().setShadowY(2);
        testView.setBackgroundColor(UIColor.Companion.getOrangeColor());
        addSubview(testView);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                UIViewAnimator.INSTANCE.spring(new Runnable() {
                    @Override
                    public void run() {
                        testView.setFrame(new CGRect(200, 200, 88, 88));
                        testView.getLayer().setCornerRadius(44.0);
                    }
                }, null);
            }
        }, 3000);
    }

}
