package com.yy.codex.uikit.sample;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.style.LeadingMarginSpan;
import android.util.AttributeSet;
import android.view.View;

import com.yy.codex.uikit.CALayer;
import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.NSAttributedString;
import com.yy.codex.uikit.NSLineBreakMode;
import com.yy.codex.uikit.UIConstraint;
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
        redView = new UIView(getContext());
        redView.setBackgroundColor(Color.RED);
        redView.setWantsLayer(true);
        redView.setFrame(new CGRect(0,0,200,200));
        CALayer mainLayer = redView.getLayer();
        mainLayer.setCornerRadius(5).setBorderWidth(10).setBorderColor(Color.BLACK)
                .setBackgroundColor(Color.GREEN);
        addSubview(redView);

        CALayer sub1 = new CALayer(new CGRect(10, 10, 150, 150));
        sub1.setCornerRadius(10).setBackgroundColor(Color.YELLOW);
        mainLayer.addSubLayer(sub1);

        CALayer sub2 = new CALayer(new CGRect(10, 10, 200, 200));
        sub2.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.avatar))
                .setBitmapGravity(CALayer.GRAVITY_BOTTOM)
                .setCornerRadius(10).setBackgroundColor(Color.BLUE);
        mainLayer.insertAboveSubLayer(sub2, sub1);

    }

}
