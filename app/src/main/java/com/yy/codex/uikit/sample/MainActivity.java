package com.yy.codex.uikit.sample;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewGroupCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.style.LeadingMarginSpan;
import android.util.AttributeSet;
import android.view.View;

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
        aLabel = new UILabel(getContext());
        aLabel.setBackgroundColor(Color.GREEN);
        aLabel.setConstraint(new UIConstraint());
        aLabel.getConstraint().width = "50%";
        aLabel.getConstraint().centerHorizontally = true;
        aLabel.getConstraint().centerVertically = true;
        NSAttributedString attributedString = new NSAttributedString("Test");
        attributedString.setFont(null, 30);
        attributedString.setTextColor(Color.RED);
        attributedString.setAlignment(Layout.Alignment.ALIGN_CENTER);
        attributedString.setDeleteline(true);
        aLabel.setAttributedText(attributedString);
        addSubview(aLabel);
    }

}
