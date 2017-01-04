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

        final UIView subview = new UIView(this);
        subview.setBackgroundColor(Color.YELLOW);
        subview.setFrame(new CGRect(0,0, 200, 200));
        view.addSubview(subview);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UIView.animateWithSpring(120.0, 8.0, 120.0, new Runnable() {
                            @Override
                            public void run() {
                                subview.setFrame(new CGRect(0, 0, 300, 300));
                            }
                        }, new Runnable() {
                            @Override
                            public void run() {
                                UIView.animate(0.3, new Runnable() {
                                    @Override
                                    public void run() {
                                        subview.setFrame(new CGRect(0,0,100,100));
                                    }
                                }, null);
                            }
                        });
                    }
                });
            }
        }, 2000);

        setContentView(view);
    }
}
