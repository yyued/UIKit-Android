package com.yy.codex.uikit.sample;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.yy.codex.uikit.CALayer;
import com.yy.codex.uikit.CATextLayer;
import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.CGTransform;
import com.yy.codex.uikit.CGTransformRotation;
import com.yy.codex.uikit.CGTransformScale;
import com.yy.codex.uikit.CGTransformTranslation;
import com.yy.codex.uikit.NSLog;
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
//        final UIView redView = new UIView(getContext());
//        redView.setWantsLayer(true);
//        redView.setFrame(new CGRect(88, 88, 88, 88));
//        redView.getLayer().setBackgroundColor(Color.BLACK);
//        redView.getLayer().setCornerRadius(44.0);
//        redView.setUserInteractionEnabled(true);
//
//        UITapGestureRecognizer tapGestureRecognizer = new UITapGestureRecognizer(this, "onTap:");
////        redView.addGestureRecognizer(tapGestureRecognizer);
//
////        UILongPressGestureRecognizer longPressGestureRecognizer = new UILongPressGestureRecognizer(this, "onLongPressed:");
////        redView.addGestureRecognizer(longPressGestureRecognizer);
//
//        UIPanGestureRecognizer panGestureRecognizer = new UIPanGestureRecognizer(this, "onPan:");
////        redView.addGestureRecognizer(panGestureRecognizer);
//
//
//        final UIView blueView = new UIView(getContext());
//        blueView.setFrame(new CGRect(0,0,44,44));
//        blueView.setBackgroundColor(Color.BLUE);
//        blueView.setUserInteractionEnabled(true);
//        blueView.addGestureRecognizer(new UITapGestureRecognizer(new Runnable() {
//            @Override
//            public void run() {
//                blueView.setBackgroundColor(Color.LTGRAY);
//            }
//        }));
//        redView.addSubview(blueView);
//
//
//        addSubview(redView);
        testLayer();
    }

    private void testLayer(){
        UIView view = new UIView(getContext());
        view.setWantsLayer(true);
        view.setFrame(new CGRect(10, 10, 300, 300));
        view.setBackgroundColor(Color.GRAY);
        addSubview(view);

        CALayer layer = view.getLayer();
        layer.setFrame(new CGRect(10, 10, 100, 100));
        CGTransform[] tfs = {new CGTransformTranslation(-14, -14), new CGTransformRotation(45), new CGTransformScale(1.5, 1.5)};
//        layer.setTransforms(tfs);
        layer.setBackgroundColor(Color.BLACK);

        CATextLayer textLayer = new CATextLayer(new CGRect(10, 10, 60, 60));
        textLayer.setString("xoxJj");
        textLayer.setFontColor(Color.RED);
        layer.addSubLayer(textLayer);

//        Path path = new Path();
//        path.addCircle(150, 100, 25, Path.Direction.CW);
//        path.moveTo(150, 125);
//        path.lineTo(150, 175);
//        path.lineTo(125, 225);
//        path.moveTo(100, 150);
//        path.lineTo(200, 150);
//
//        CAShapeLayer shapeLayer = new CAShapeLayer();
//        shapeLayer.setLineWidth(5).setPath(path);
////        layer.addSubLayer(shapeLayer);
//
//        CALayer contentLayer = new CALayer();
//        contentLayer.setFrame(new CGRect(10, 10, 200, 200));
//        contentLayer.setBackgroundColor(Color.GREEN);
//        contentLayer.setMask(shapeLayer);
//        layer.addSubLayer(contentLayer);

//        CAGradientLayer gradientLayer = new CAGradientLayer();
//        layer.addSubLayer(gradientLayer);


        CGTransform transform = new CGTransform();
        NSLog.log(transform);


    }

//    public void onTap(UITapGestureRecognizer tapGestureRecognizer) {
//        final UIView redView = tapGestureRecognizer.getView();
//        if (redView != null) {
//                UIView.animateWithSpring(new Runnable() {
//                    @Override
//                    public void run() {
//                        redView.setFrame(new CGRect(44, 44, 88 * 2, 88 * 2));
//                        redView.getLayer().setCornerRadius(88.0);
//                    }
//                }, new Runnable() {
//                    @Override
//                    public void run() {
//                        NSLog.log(redView);
//                    }
//                });
//        }
//    }
//
//    public void onLongPressed(UILongPressGestureRecognizer longPressGestureRecognizer) {
//        final UIView redView = longPressGestureRecognizer.getView();
//        if (redView != null) {
//            if (longPressGestureRecognizer.getState() == UIGestureRecognizerState.Began) {
//                redView.getLayer().setBackgroundColor(Color.GRAY);
//            }
//            else if (longPressGestureRecognizer.getState() == UIGestureRecognizerState.Changed) {
//                redView.getLayer().setBackgroundColor(Color.GREEN);
//                NSLog.log(longPressGestureRecognizer.location());
//            }
//            else if (longPressGestureRecognizer.getState() == UIGestureRecognizerState.Ended) {
//                redView.getLayer().setBackgroundColor(Color.YELLOW);
//            }
//        }
//    }
//
//    public void onPan(UIPanGestureRecognizer panGestureRecognizer) {
//        final UIView redView = panGestureRecognizer.getView();
//        if (redView != null && panGestureRecognizer instanceof UIPanGestureRecognizer) {
//            if (panGestureRecognizer.getState() == UIGestureRecognizerState.Began) {
//                redView.getLayer().setBackgroundColor(Color.GRAY);
//            }
//            else if (panGestureRecognizer.getState() == UIGestureRecognizerState.Changed) {
//                redView.getLayer().setBackgroundColor(Color.GREEN);
//                CGPoint translation = panGestureRecognizer.translation();
//                redView.setFrame(new CGRect(88 + translation.getX(), 88 + translation.getY(), 88.0, 88.0));
//            }
//            else if (panGestureRecognizer.getState() == UIGestureRecognizerState.Ended) {
//                redView.getLayer().setBackgroundColor(Color.YELLOW);
//                NSLog.log((panGestureRecognizer.velocity()));
//            }
//        }
//    }

}
