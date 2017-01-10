package com.yy.codex.uikit.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.yy.codex.uikit.CALayer;
import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.UIImage;
import com.yy.codex.uikit.UIImageView;
import com.yy.codex.uikit.UITapGestureRecognizer;
import com.yy.codex.uikit.UIView;

import java.net.URL;

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
        final UIImageView imageView = new UIImageView(getContext());
        imageView.getLayer().setCornerRadius(45.0f);
        imageView.getLayer().setClipToBounds(true);
        imageView.setFrame(new CGRect(0, 0, 90, 90));
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap bitmap = BitmapFactory.decodeStream(new URL("http://img.hb.aicdn.com/a6b00bbdeeed21c79af74d043ba4b7505cbe11bf2225b-wsXC96_sq320").openConnection().getInputStream());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImage(new UIImage(bitmap));
                            imageView.invalidate();
                        }
                    });
                }
                catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        });
        addSubview(imageView);
        thread.start();
    }
}
