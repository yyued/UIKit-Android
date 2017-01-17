package com.yy.codex.uikit.sample;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.yy.codex.foundation.NSLog;
import com.yy.codex.uikit.CALayer;
import com.yy.codex.uikit.CALayerBitmapPainter;
import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.UIColor;
import com.yy.codex.uikit.UIScreen;
import com.yy.codex.uikit.UIView;

/**
 * Created by adi on 17/1/17.
 */

public class TestLayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TestView1(this));
    }
}


class TestView1 extends UIView {

    private CALayer layer;

    public TestView1(@NonNull Context context, @NonNull View view) {
        super(context, view);
    }

    public TestView1(@NonNull Context context) {
        super(context);
    }

    public TestView1(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView1(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestView1(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void init() {
        super.init();

//        testBorder();

        testBitmapGravity();
    }


    /* test style */
    private void testBorder(){
        UIView view = createAndAddSubview(null, UIColor.orangeColor);
        CALayer mainLayer = view.getLayer();

        CALayer a1 = new CALayer(new CGRect(10, 10, 100, 100));
        a1.setBorderWidth(1);
        mainLayer.addSubLayer(a1);

        CALayer a2 = new CALayer(new CGRect(120, 10, 100, 100));
        a2.setBorderWidth(2).setBorderColor(UIColor.whiteColor).setCornerRadius(10);
        mainLayer.addSubLayer(a2);

        CALayer a3 = new CALayer(new CGRect(230, 10, 100, 100));
        a3.setBorderWidth(2).setBorderColor(UIColor.whiteColor).setCornerRadius(10);
        a3.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img212x645))
                .setBitmapGravity(CALayerBitmapPainter.GRAVITY_SCALE_ASPECT_FILL);
        mainLayer.addSubLayer(a3);
    }

    /* test bitmapGravity */
    private void testBitmapGravity(){
        UIView view = createAndAddSubview(null, UIColor.orangeColor);
        CALayer mainLayer = view.getLayer();

        CGRect[] rects = {new CGRect(10, 10, 100, 100), new CGRect(120, 10, 100, 100), new CGRect(230, 10, 100, 100),
                new CGRect(10, 120, 100, 100), new CGRect(120, 120, 100, 100), new CGRect(230, 120, 100, 100),
                new CGRect(10, 230, 100, 100), new CGRect(120, 230, 100, 100), new CGRect(230, 230, 100, 100)};

        for (CGRect rect : rects){
            CALayer layer = new CALayer(rect);
            layer.setBorderWidth(2).setBorderColor(UIColor.whiteColor).setCornerRadius(10);
            mainLayer.addSubLayer(layer);
        }
    }

    /* test hierarchy */


    /* test transform */



    /* utils */

    private UIView createAndAddSubview(@Nullable CGRect rect, @Nullable UIColor bgColor ){
        UIView view = new UIView(getContext());
        view.setWantsLayer(true);
        view.setFrame(rect == null ? UIScreen.mainScreen.bounds() : rect);
        view.setBackgroundColor(bgColor.toInt());
        addSubview(view);
        return view;
    }
}
