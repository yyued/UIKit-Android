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
import com.yy.codex.uikit.UIViewContentMode;

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
        testAnimation();
//        final UIImageView imageView = new UIImageView(getContext());
//        imageView.setBackgroundColor(Color.GRAY);
//        imageView.getLayer().setCornerRadius(45.0f);
//        imageView.getLayer().setClipToBounds(true);
//        imageView.setFrame(new CGRect(44, 44, 90, 90));
//        final Handler handler = new Handler();
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    final Bitmap bitmap = BitmapFactory.decodeStream(new URL("http://img.hb.aicdn.com/a6b00bbdeeed21c79af74d043ba4b7505cbe11bf2225b-wsXC96_sq320").openConnection().getInputStream());
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            imageView.setImage(new UIImage(bitmap));
//                            imageView.invalidate();
//                        }
//                    });
//                }
//                catch (Exception e) {
//                    System.out.println(e.toString());
//                }
//            }
//        });
//        addSubview(imageView);
//        thread.start();
    }

    void testAnimation() {
        final UIImageView imageView = new UIImageView(getContext());
        imageView.setFrame(new CGRect(0, 0, 15, 15));
        imageView.setAnimationImages(new UIImage[]{
                new UIImage(getContext(), R.drawable.ani_0),
                new UIImage(getContext(), R.drawable.ani_1),
                new UIImage(getContext(), R.drawable.ani_2),
                new UIImage(getContext(), R.drawable.ani_3),
                new UIImage(getContext(), R.drawable.ani_4),
                new UIImage(getContext(), R.drawable.ani_5),
                new UIImage(getContext(), R.drawable.ani_6),
                new UIImage(getContext(), R.drawable.ani_7),
                new UIImage(getContext(), R.drawable.ani_8),
                new UIImage(getContext(), R.drawable.ani_9),
                new UIImage(getContext(), R.drawable.ani_10),
                new UIImage(getContext(), R.drawable.ani_11),
                new UIImage(getContext(), R.drawable.ani_12),
                new UIImage(getContext(), R.drawable.ani_13),
                new UIImage(getContext(), R.drawable.ani_14),
                new UIImage(getContext(), R.drawable.ani_15),
                new UIImage(getContext(), R.drawable.ani_16),
                new UIImage(getContext(), R.drawable.ani_17),
                new UIImage(getContext(), R.drawable.ani_18),
                new UIImage(getContext(), R.drawable.ani_19),
        });
        addSubview(imageView);
        imageView.startAnimating();
    }

}
