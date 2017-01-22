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
import com.yy.codex.uikit.CGTransform;
import com.yy.codex.uikit.CGTransformRotation;
import com.yy.codex.uikit.CGTransformTranslation;
import com.yy.codex.uikit.UIColor;
import com.yy.codex.uikit.UIScreen;
import com.yy.codex.uikit.UIView;

/**
 * Created by adi on 17/1/17.
 */

public class TestLayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        NSLog.warn(0x01 == 1);
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
//        testHierarchy();
    }


    /* test style */

    private void testBorder(){
        UIView view = createAndAddSubview(null, UIColor.grayColor);
        CALayer mainLayer = view.getLayer();

        // #1
        CALayer a1 = new CALayer(new CGRect(10, 10, 100, 100));
        a1.setBackgroundColor(UIColor.yellowColor);
        mainLayer.addSubLayer(a1);

        CALayer a2 = new CALayer(new CGRect(120, 10, 100, 100));
        a2.setBackgroundColor(UIColor.yellowColor)
                .setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img180x180))
                .setBitmapGravity(CALayer.BitmapGravity.ScaleAspectFill);
        mainLayer.addSubLayer(a2);

        CALayer a3 = new CALayer(new CGRect(230, 10, 100, 100));
        a3.setBackgroundColor(UIColor.yellowColor)
                .setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img180x180))
                .setBitmapGravity(CALayer.BitmapGravity.ScaleAspectFill)
                .setBitmapColor(UIColor.redColor);
        mainLayer.addSubLayer(a3);

        // #2
        CALayer a4 = new CALayer(new CGRect(10, 120, 100, 100));
        a4.setCornerRadius(10);
        a4.setBackgroundColor(UIColor.yellowColor);
        mainLayer.addSubLayer(a4);

        CALayer a5 = new CALayer(new CGRect(120, 120, 100, 100));
        a5.setCornerRadius(10);
        a5.setBackgroundColor(UIColor.yellowColor)
                .setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img180x180))
                .setBitmapGravity(CALayer.BitmapGravity.ScaleAspectFill);
        mainLayer.addSubLayer(a5);

        CALayer a6 = new CALayer(new CGRect(230, 120, 100, 100));
        a6.setCornerRadius(10);
        a6.setBackgroundColor(UIColor.yellowColor)
                .setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img180x180))
                .setBitmapGravity(CALayer.BitmapGravity.ScaleAspectFill)
                .setBitmapColor(UIColor.redColor);
        mainLayer.addSubLayer(a6);

        // #3
        CALayer a7 = new CALayer(new CGRect(10, 230, 100, 100));
        a7.setBorderWidth(2).setBorderColor(UIColor.whiteColor).setCornerRadius(10);
        a7.setBackgroundColor(UIColor.yellowColor);
        mainLayer.addSubLayer(a7);

        CALayer a8 = new CALayer(new CGRect(120, 230, 100, 100));
        a8.setBorderWidth(2).setBorderColor(UIColor.whiteColor).setCornerRadius(10);
        a8.setBackgroundColor(UIColor.yellowColor)
                .setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img180x180))
                .setBitmapGravity(CALayer.BitmapGravity.ScaleAspectFill);
        mainLayer.addSubLayer(a8);

        CALayer a9 = new CALayer(new CGRect(230, 230, 100, 100));
        a9.setBorderWidth(2).setBorderColor(UIColor.whiteColor).setCornerRadius(10);
        a9.setBackgroundColor(UIColor.yellowColor)
                .setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img180x180))
                .setBitmapGravity(CALayer.BitmapGravity.ScaleAspectFill)
                .setBitmapColor(UIColor.redColor);
        mainLayer.addSubLayer(a9);
    }

    /* test bitmapGravity */

    private void testBitmapGravity(){

        UIView view = createAndAddSubview(null, UIColor.orangeColor);
        CALayer mainLayer = view.getLayer();

        CGRect[] rects = {new CGRect(10, 10, 100, 100), new CGRect(120, 10, 100, 100), new CGRect(230, 10, 100, 100),
                new CGRect(10, 120, 100, 100), new CGRect(120, 120, 100, 100), new CGRect(230, 120, 100, 100),
                new CGRect(10, 230, 100, 100), new CGRect(120, 230, 100, 100), new CGRect(230, 230, 100, 100),
                new CGRect(10, 340, 100, 100), new CGRect(120, 340, 100, 100), new CGRect(230, 340, 100, 100)};

        int idx = 0;
        for (CGRect rect : rects){
            CALayer layer = new CALayer(rect);
            layer.setClipToBounds(true);
            layer.setBorderWidth(2).setBorderColor(UIColor.greenColor).setCornerRadius(10);
            CALayer.BitmapGravity bitmapGravity = CALayer.BitmapGravity.ScaleAspectFit;
            switch (idx){
                case 0: bitmapGravity = CALayer.BitmapGravity.ScaleAspectFit; break;
                case 1: bitmapGravity = CALayer.BitmapGravity.ScaleAspectFill; break;
                case 2: bitmapGravity = CALayer.BitmapGravity.ScaleToFill; break;
                case 3: bitmapGravity = CALayer.BitmapGravity.TopLeft; break;
                case 4: bitmapGravity = CALayer.BitmapGravity.Top; break;
                case 5: bitmapGravity = CALayer.BitmapGravity.TopRight; break;
                case 6: bitmapGravity = CALayer.BitmapGravity.Left; break;
                case 7: bitmapGravity = CALayer.BitmapGravity.Center; break;
                case 8: bitmapGravity = CALayer.BitmapGravity.Right; break;
                case 9: bitmapGravity = CALayer.BitmapGravity.BottomLeft; break;
                case 10: bitmapGravity = CALayer.BitmapGravity.Bottom; break;
                case 11: bitmapGravity = CALayer.BitmapGravity.BottomRight; break;
            }
            layer.setBitmapGravity(bitmapGravity).setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.jpg180x180));
            mainLayer.addSubLayer(layer);
            idx++;
        }
    }

    /* test hierarchy */

    private void testHierarchy(){
        // 测试层级坐标 clipToBound transform 情况

        UIView view = createAndAddSubview(null, UIColor.grayColor);
        CALayer mainLayer = view.getLayer();

        // 测试坐标, clipToBound = NO
        CALayer a1 = createSubLayer(mainLayer, new CGRect(10, 10, 100, 100), UIColor.whiteColor);
        CALayer b1 = createSubLayer(mainLayer, new CGRect(120, 10, 100, 100), UIColor.blackColor);
        CALayer a11 = createSubLayer(a1, new CGRect(10, -10, 80, 80), UIColor.orangeColor);
        CALayer a111 = createSubLayer(a11, new CGRect(10, -20, 50, 50), UIColor.yellowColor);

        // 测试坐标, clipToBound = YES
        CALayer c1 = createSubLayer(mainLayer, new CGRect(10, 120, 100, 100), UIColor.whiteColor);
        CALayer d2 = createSubLayer(mainLayer, new CGRect(120, 120, 100, 100), UIColor.blackColor);
        CALayer c11 = createSubLayer(c1, new CGRect(10, -10, 80, 80), UIColor.orangeColor);
        CALayer c111 = createSubLayer(c11, new CGRect(10, -20, 50, 50), UIColor.yellowColor);
        c1.setClipToBounds(true); // @Bug 导致子层 Transform 无效
        CGTransform[] tfs = {new CGTransformRotation(30), new CGTransformTranslation(10, 10)};
        c11.setTransforms(tfs);
    }


    /* test transform */


    /* utils */

    private UIView createAndAddSubview(@Nullable CGRect rect, @Nullable UIColor bgColor){
        UIView view = new UIView(getContext());
        view.setWantsLayer(true);
        view.setFrame(rect == null ? UIScreen.mainScreen.bounds() : rect);
        view.setBackgroundColor(bgColor.toInt());
        addSubview(view);
        return view;
    }

    private CALayer createSubLayer(@NonNull CALayer superLayer, @Nullable CGRect rect, @Nullable UIColor bgColor){
        CALayer layer = new CALayer(rect);
        layer.setBackgroundColor(bgColor);
        superLayer.addSubLayer(layer);
        return layer;
    }
}
