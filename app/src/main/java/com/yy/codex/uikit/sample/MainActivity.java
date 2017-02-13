package com.yy.codex.uikit.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.BoringLayout;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.yy.codex.foundation.NSInvocation;
import com.yy.codex.foundation.NSLog;
import com.yy.codex.foundation.NSNotification;
import com.yy.codex.foundation.NSNotificationCenter;
import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.NSAttributedString;
import com.yy.codex.uikit.NSRange;
import com.yy.codex.uikit.UIBarButtonItem;
import com.yy.codex.uikit.UIButton;
import com.yy.codex.uikit.UIColor;
import com.yy.codex.uikit.UIControl;
import com.yy.codex.uikit.UIEdgeInsets;
import com.yy.codex.uikit.UIFont;
import com.yy.codex.uikit.UIImage;
import com.yy.codex.uikit.UIKeyboardManager;
import com.yy.codex.uikit.UIKeyboardType;
import com.yy.codex.uikit.UILabel;
import com.yy.codex.uikit.UIMenuController;
import com.yy.codex.uikit.UIMenuItem;
import com.yy.codex.uikit.UINavigationController;
import com.yy.codex.uikit.UINavigationController_ActivityBase;
import com.yy.codex.uikit.UIReturnKeyType;
import com.yy.codex.uikit.UIScreen;
import com.yy.codex.uikit.UITabBarActivity;
import com.yy.codex.uikit.UITabBarController;
import com.yy.codex.uikit.UITabBarItem;
import com.yy.codex.uikit.UITextField;
import com.yy.codex.uikit.UITextView;
import com.yy.codex.uikit.UIView;
import com.yy.codex.uikit.UIViewController;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends UITabBarActivity {

    @NotNull
    @Override
    public List<UIViewController> createViewControllers() {
        UIImage iconImage = new UIImage(this, R.drawable.ani_19);
        UINavigationController navigationControllerI = new UINavigationController_ActivityBase(this);
        navigationControllerI.setRootViewController(new TestViewController(this));
        navigationControllerI.setTabBarItem(new UITabBarItem());
        navigationControllerI.getTabBarItem().setTitle("Test");
        navigationControllerI.getTabBarItem().setImage(iconImage);
        UINavigationController navigationControllerII = new UINavigationController(this);
        navigationControllerII.setRootViewController(new NextViewController(this));
        navigationControllerII.setTabBarItem(new UITabBarItem());
        navigationControllerII.getTabBarItem().setTitle("Second");
        navigationControllerII.getTabBarItem().setImage(iconImage);
        ArrayList<UIViewController> list = new ArrayList<>();
        list.add(navigationControllerI);
        list.add(navigationControllerII);
        return list;
    }

    @NotNull
    @Override
    public UINavigationController createNavigationController() {
        return new UINavigationController_ActivityBase(this);
    }

//    @NotNull
//    @Override
//    public UIViewController rootViewController() {
//        return new TestViewController(this);
//    }

}

class TestTextView extends UIView {

    public TestTextView(@NotNull Context context, @NotNull View view) {
        super(context, view);
    }

    public TestTextView(@NotNull Context context) {
        super(context);
    }

    public TestTextView(@NotNull Context context, @NotNull AttributeSet attrs) {
        super(context, attrs);
    }

    public TestTextView(@NotNull Context context, @NotNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TestTextView(@NotNull Context context, @NotNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(@NotNull Canvas canvas) {
        super.onDraw(canvas);
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(30.0f);
        StaticLayout staticLayout = new StaticLayout("123123123123123123123123123", textPaint, 200, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        staticLayout.draw(canvas);
        int lineNumber = staticLayout.getLineForOffset(1);
        float ss = staticLayout.getPrimaryHorizontal(1);
        float sss =staticLayout.getSecondaryHorizontal(1);
        NSLog.INSTANCE.log(ss);
    }

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

//        TestTextView testTextView = new TestTextView(getContext());
//        testTextView.setFrame(new CGRect(0, 30, 300, 100));
//        testTextView.setBackgroundColor(new UIColor(0xe2/255.0, 0xe2/255.0, 0xe2/255.0, 1.0));
//        getView().addSubview(testTextView);

//        TextView textView = new TextView(getContext());
//        textView.setMaxWidth((int)(200 * UIScreen.Companion.getMainScreen().scale()));
//        textView.setMaxLines(999999);
//        textView.setText("hao123是汇集全网优质网址及资源的中文上网导航。及时收录影视、音乐、小说、游戏等分类的网址和内容,让您的网络生活更简单精彩。上网,从hao123开始。");
//
//        Layout layout = textView.getLayout();
//        layout.getPaint().setColor(0xffff0000);
//
//
//
//        UIView wrapper = new UIView(getContext(), textView);
//        wrapper.setBackgroundColor(new UIColor(0xe2/255.0, 0xe2/255.0, 0xe2/255.0, 1.0));
//        wrapper.setFrame(new CGRect(0, 0, 200, 200));
//        getView().addView(wrapper);

//        UITextField textField = (UITextField) getView().findViewById(R.id.roundView);
//        textField.setReturnKeyType(UIReturnKeyType.Next);
//        textField.setPlaceholder("请输入密码");
//        textField.setBorderStyle(UITextField.BorderStyle.Line);
//        textField.setClearButtonMode(UITextField.ViewMode.WhileEditing);
//        textField.setAlignment(Layout.Alignment.ALIGN_CENTER);
    }

    private void handleNextButtonTapped() {
        NextViewController nextViewController = new NextViewController(getContext());
        nextViewController.setHidesBottomBarWhenPushed(true);
        navigationController().pushViewController(nextViewController, true);
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
        getNavigationItem().setRightBarButtonItem(new UIBarButtonItem("Next", this, "handleNextButtonTapped"));
    }

    private void handleNextButtonTapped() {
        NextViewController nextViewController = new NextViewController(getContext());
        nextViewController.setHidesBottomBarWhenPushed(true);
        navigationController().pushViewController(nextViewController, true);
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
//        postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                UIViewAnimator.INSTANCE.spring(new Runnable() {
//                    @Override
//                    public void run() {
//                        testView.setFrame(new CGRect(200, 200, 88, 88));
//                        testView.getLayer().setCornerRadius(44.0);
//                    }
//                }, null);
//            }
//        }, 3000);
    }

}
