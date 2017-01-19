package com.yy.codex.uikit.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.yy.codex.foundation.NSLog;
import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.UIColor;
import com.yy.codex.uikit.UIControl;
import com.yy.codex.uikit.UISlider;
import com.yy.codex.uikit.UISliderCallback;
import com.yy.codex.uikit.UISwitch;
import com.yy.codex.uikit.UIView;

/**
 * Created by adi on 17/1/18.
 */

public class TestUIComponent extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TestComponent(this));
    }
}

class TestComponent extends UIView {
    public TestComponent(@NonNull Context context, @NonNull View view) {
        super(context, view);
    }

    public TestComponent(@NonNull Context context) {
        super(context);
    }

    public TestComponent(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public TestComponent(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestComponent(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void init() {
        super.init();

        UISwitch uiSwitch = new UISwitch(getContext());
        uiSwitch.setFrame(new CGRect(10, 10, 52, 44));
//        uiSwitch.setOn(true);
        addSubview(uiSwitch);

        UISlider uiSlider = new UISlider(getContext());
        uiSlider.setFrame(new CGRect(10, 100, 200, 32));
        uiSlider.onSlide(new UISliderCallback() {
            @Override
            public void handle(double slideValue) {
                NSLog.warn(slideValue);
            }
        });
        addSubview(uiSlider);


        TestXView view1 = new TestXView(getContext());
        view1.setFrame(new CGRect(10, 200, 100, 100));
//        view1.setBackgroundColor(UIColor.orangeColor);
        addSubview(view1);
    }
}

class TestXView extends UIView{
    public TestXView(@NonNull Context context, @NonNull View view) {
        super(context, view);
    }

    public TestXView(@NonNull Context context) {
        super(context);
    }

    public TestXView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public TestXView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestXView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(UIColor.yellowColor.toInt());
        paint.setShadowLayer(1, 50, 50, UIColor.greenColor.toInt());
        canvas.drawRoundRect(new RectF(30, 30, 130, 130), 50, 50, paint);
    }

    @Override
    protected void init() {
        super.init();
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }
}
