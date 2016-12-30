package com.yy.codex.uikit.sample;

import android.graphics.Color;
import android.support.v4.view.ViewGroupCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.yy.codex.uikit.UIView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIView view = new UIView(this);
        view.setBackgroundColor(Color.GRAY);
        setContentView(view);
    }
}
