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
        int bodyColor = Color.WHITE;

        view.setBackgroundColor(bodyColor); // not onDraw if no set background

        UIView subview = new UIView(this);
        subview.setFrame(new CGRect(100,100, 200, 200));
        subview.setBackgroundColor(bodyColor);
        subview.setWantsLayer(true);
        subview.getLayer()
                .setShadowRadius(50).setShadowX(50).setShadowY(50)
                .setBorderWidth(10).setCornerRadius(50)
                .setBackgroundColor(Color.BLUE);
        view.addSubview(subview);

        UIView subsubview = new UIView(this);
        subsubview.setFrame(new CGRect(50, 50, 50, 50));
        subsubview.setBackgroundColor(bodyColor);
        subsubview.setWantsLayer(true);
        subsubview.getLayer()
                .setShadowRadius(50).setShadowX(10).setShadowY(10)
                .setBorderWidth(24).setBorderColor(0xffFF0000)
                .setBackgroundColor(Color.GREEN);
        subview.addSubview(subsubview);

        setContentView(view);
    }
}
