package com.yy.codex.uikit.sample;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.yy.codex.uikit.CALayer;
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
//        UIView posView = new UIView(getContext());
//        posView.setFrame(new CGRect(10, 10, 10, 10));
//        posView.setBackgroundColor(Color.WHITE);
//        addSubview(posView);

        UIView redView = new UIView(getContext());
        redView.setWantsLayer(true);
        redView.setFrame(new CGRect(10, 10, 400, 400));
        CALayer mainLayer = redView.getLayer();
        mainLayer
                .setFrame(new CGRect(10, 10, 100, 100))
//                .setShadowRadius(8).setShadowX(-2).setShadowY(-2).setShadowColor(Color.RED)
                .setCornerRadius(10).setBorderWidth(1).setBorderColor(Color.BLACK)
//                .setClipToBounds(true) // @UIView bug, disable auto clipChildren
                .setBackgroundColor(Color.YELLOW);
        addSubview(redView);

//        UIView redSubView = new UIView(getContext());
//        redSubView.setBackgroundColor(Color.WHITE);
//        redSubView.setFrame(new CGRect(100, 100, 200, 200));
//        redView.addSubview(redSubView);

        CALayer sub1 = new CALayer(new CGRect(10, 10, 200, 160));
        sub1.setCornerRadius(10)
                .setBackgroundColor(Color.RED);
        mainLayer.addSubLayer(sub1);
//
//        CALayer sub12 = new CALayer(new CGRect(20, 20, 150, 150));
//        sub12.setCornerRadius(10)
//                .setBackgroundColor(Color.GREEN);
//        sub1.addSubLayer(sub12);
//
//        CALayer sub13 = new CALayer(new CGRect(30, 30, 150, 150));
//        sub13.setCornerRadius(10)
//                .setBackgroundColor(Color.BLUE);
//        sub1.addSubLayer(sub13);


        // test insert:atIndex
//        sub1.insertSubLayerAtIndex(sub13, 0);
//        sub1.insertSubLayerAtIndex(sub12, 1);
//        sub1.insertBelowSubLayer(sub13, sub12);

        // test remove
//        sub1.removeFromSuperLayer();

        // test imageGravity
        CALayer sub2 = new CALayer(new CGRect(10, 10, 200, 160));
        sub2.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img120x180))
                .setBitmapGravity(CALayer.GRAVITY_TOP_RIGHT)
                .setCornerRadius(10).setBackgroundColor(Color.BLUE);
        mainLayer.insertAboveSubLayer(sub2, sub1);

    }

}
