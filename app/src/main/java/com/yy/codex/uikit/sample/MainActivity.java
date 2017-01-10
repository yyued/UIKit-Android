package com.yy.codex.uikit.sample;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.yy.codex.uikit.CALayer;
import com.yy.codex.uikit.CATextLayer;
import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.UILabel;
import com.yy.codex.uikit.UIView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final UIView view = new TestView(this);
        view.setBackgroundColor(Color.GRAY);
        setContentView(view);
    }
}

class TestView extends UIView {

    UILabel aLabel;

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

    public void init() {

        UIView redView = new UIView(getContext());
        redView.setWantsLayer(true);
        redView.setFrame(new CGRect(10, 10, 400, 400));
        CALayer mainLayer = redView.getLayer();
        mainLayer
                .setFrame(new CGRect(10, 10, 100, 100))
                .setCornerRadius(10).setBorderWidth(1).setBorderColor(Color.BLACK)
                .setBackgroundColor(Color.YELLOW);
        addSubview(redView);

        CALayer sub0 = new CALayer(new CGRect(10, 10, 100, 14));
        sub0.setBackgroundColor(Color.RED);
        mainLayer.addSubLayer(sub0);

        CATextLayer sub1 = new CATextLayer(new CGRect(10, 10, 100, 14));
        sub1.setString("xxJjxxoxx")
                .setFontSize(14)
                .setFontColor(Color.WHITE);
        mainLayer.addSubLayer(sub1);


    }

}
