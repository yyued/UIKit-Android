package com.yy.codex.uikit.sample;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Scroller;

import com.yy.codex.uikit.UIGestureRecognizer;
import com.yy.codex.uikit.UIScrollView;
import com.yy.codex.uikit.UIView;

/**
 * Created by it on 17/1/10.
 */

public class MyMainActivity extends AppCompatActivity {

    UIView view = null;
    UIView testView2;
    UIView testView;
    Button button;
    Button moveButton;

    Scroller mScroller;

    TestLayout testLayout;

    UIScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        scrollView = (UIScrollView)findViewById(R.id.testScrollView);
//        button = (Button)findViewById(R.id.testButton);
//        moveButton = (Button)findViewById(R.id.testMoveButton);
//
//        mScroller=new Scroller(this);
//


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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyTestView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
