package com.yy.codex.uikit.sample;

import android.graphics.Color;
import android.support.v4.view.ViewGroupCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.UIView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIView view = new UIView(this);
        view.setBackgroundColor(Color.GRAY);

        UIView subview = new UIView(this);
        subview.setBackgroundColor(Color.YELLOW);
        subview.setFrame(new CGRect(100,100, 200, 200));
        view.addSubview(subview);
        view.addSubview(subview);

        UIView subsubview = new UIView(this);
        subsubview.setBackgroundColor(Color.RED);
        subsubview.setFrame(new CGRect(50,50, 50, 50));
        subview.addSubview(subsubview);

        setContentView(view);
    }
}
