package com.yy.codex.uikit.sample;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.UIActivity;
import com.yy.codex.uikit.UIBarButtonItem;
import com.yy.codex.uikit.UIColor;
import com.yy.codex.uikit.UINavigationActivity;
import com.yy.codex.uikit.UINavigationController;
import com.yy.codex.uikit.UITabBar;
import com.yy.codex.uikit.UITabBarController;
import com.yy.codex.uikit.UIView;
import com.yy.codex.uikit.UIViewAnimator;
import com.yy.codex.uikit.UIViewController;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends UINavigationActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UITabBarController tabBarController = new UITabBarController(this);
        UINavigationController navigationControllerI = new UINavigationController(this);
        navigationControllerI.setRootViewController(new TestViewController(this));
        UIViewController[] viewControllers = new UIViewController[]{
                navigationControllerI
        };
        tabBarController.setViewControllers(viewControllers);
        setViewController(tabBarController);
//        getNavigationController().getView().setMaterialDesign(true);
    }

//    @NotNull
//    @Override
//    public UINavigationController createNavigationController() {
//        return new UINavigationController_ActivityBase(this);
//    }

//    @NotNull
//    @Override
//    public UIViewController rootViewController() {
//        return new TestViewController(this);
//    }

}

class TestViewController extends UIViewController {

    public TestViewController(Context context) {
        super(context);
    }

    @Override
    public void loadView() {
        setView(loadViewFromXML(R.layout.test_view));
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        setTitle("Test");
        getNavigationItem().setRightBarButtonItem(new UIBarButtonItem("Next", this, "handleNextButtonTapped"));
        postDelay(new Runnable() {
            @Override
            public void run() {
                UIViewAnimator.INSTANCE.linear(new Runnable() {
                    @Override
                    public void run() {
                        getView().findViewById(R.id.roundView).setAlpha(0.0f);
                    }
                });
            }
        },3000);
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
