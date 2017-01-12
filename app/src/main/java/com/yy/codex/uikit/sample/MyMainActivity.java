package com.yy.codex.uikit.sample;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Scroller;

import com.yy.codex.uikit.CGPoint;
import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.NSLog;
import com.yy.codex.uikit.UIEvent;
import com.yy.codex.uikit.UIGestureRecognizer;
import com.yy.codex.uikit.UIScrollView;
import com.yy.codex.uikit.UITapGestureRecognizer;
import com.yy.codex.uikit.UITouch;
import com.yy.codex.uikit.UIView;

import java.util.Set;

/**
 * Created by it on 17/1/10.
 */

public class MyMainActivity extends AppCompatActivity {

    UIView view = null;
    Button button;
    Button moveButton;

    Scroller mScroller;

    TestLayout testLayout;

    UIScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new UIView(this);
        setContentView(R.layout.activity_main);
        view = (UIView)findViewById(R.id.testUIView);


        UIView testView = new MyTestView(this);
        testView.setBackgroundColor(Color.BLUE);
        testView.setFrame(new CGRect(50, 50, 100, 100));

        UIView testView2 = new MyTestView2(this);
        testView2.setBackgroundColor(Color.YELLOW);
        testView2.setFrame(new CGRect(50, 50, 50, 50));

        view.addSubview(testView);
        testView.addSubview(testView2);

//        scrollView = (UIScrollView)findViewById(R.id.testScrollView);
//        button = (Button)findViewById(R.id.testButton);
//        moveButton = (Button)findViewById(R.id.testMoveButton);
//
//        mScroller=new Scroller(this);
//
//        testLayout = (TestLayout)findViewById(R.id.testMyLayout);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                testLayout.beginScroll();
//            }
//        });
//
//        view.addGestureRecognizer(new UITapGestureRecognizer(this, "ttt:"));
//        view.setUserInteractionEnabled(true);
    }

    public void ttt(UIGestureRecognizer gestureRecognizer) {
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

    public MyTestView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void touchesBegan(Set<UITouch> touches, UIEvent event) {
        super.touchesBegan(touches, event);
        NSLog.log(touches);
    }

    @Override
    public void touchesEnded(Set<UITouch> touches, UIEvent event) {
        super.touchesEnded(touches, event);
        NSLog.log(touches);
    }
}

class MyTestView2 extends UIView {

    public MyTestView2(Context context, View view) {
        super(context, view);
    }

    public MyTestView2(Context context) {
        super(context);
    }

    public MyTestView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTestView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyTestView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

//    @Override
//    public void touchesBegan() {
//        super.touchesBegan();
//    }
//
//    @Override
//    public void touchesMoved() {
//        super.touchesMoved();
//    }
//
//    @Override
//    public void touchesEnded() {
//        super.touchesEnded();
//    }
}
