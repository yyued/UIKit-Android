package com.yy.codex.uikit.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Scroller;

import com.yy.codex.uikit.CGPoint;
import com.yy.codex.uikit.UIGestureRecognizer;
import com.yy.codex.uikit.UIView;

/**
 * Created by it on 17/1/10.
 */

public class MyMainActivity extends AppCompatActivity {

    UIView view = null;
    Button button;
    Button moveButton;

    Scroller mScroller;

    TestLayout testLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new UIView(this);
        setContentView(R.layout.activity_main);
        view = (UIView)findViewById(R.id.testUIView);

    }

}
