package com.yy.codex.uikit.sample;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.yy.codex.uikit.CALayer;
import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.UIView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TestView(this));
    }

}

class TestView extends UIView {

    public TestView(Context context, View view) {
        super(context, view);
        init();
    }

    public TestView(Context context) {
        super(context);
        init();
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        UIView redView = new UIView(getContext());
        redView.setWantsLayer(true);
        redView.setFrame(new CGRect(0, 0, 400, 400));
//        redView.setBackgroundColor(Color.BLACK);
        CALayer mainLayer = redView.getLayer();
        mainLayer
                .setFrame(new CGRect(10, 10, 120, 120))
                .setCornerRadius(3)
                .setBorderWidth(10).setBorderColor(Color.GRAY)
                .setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img212x645))
                .setBitmapGravity(CALayer.GRAVITY_LEFT)
                .setClipToBounds(true)
                .setBackgroundColor(Color.GREEN)
        ;
        addSubview(redView);

        CALayer subLayer = new CALayer();
        subLayer.setFrame(new CGRect(-10, 10, 60, 60))
                .setCornerRadius(3)
                .setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img180x180))
                .setBitmapGravity(CALayer.GRAVITY_SCALE_ASCEPT_FIT)
                .setClipToBounds(true)
                .setBackgroundColor(Color.YELLOW);
        mainLayer.addSubLayer(subLayer);

        CALayer subsubLayer = new CALayer();
        subsubLayer.setFrame(new CGRect(10, 10, 30, 30))
                .setCornerRadius(3)
                .setBackgroundColor(Color.RED);
        subLayer.addSubLayer(subsubLayer);

    }

}
