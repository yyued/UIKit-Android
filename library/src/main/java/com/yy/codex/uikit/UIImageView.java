package com.yy.codex.uikit;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cuiminghui on 2017/1/10.
 */

public class UIImageView extends UIView {

    /* Constructor */

    public UIImageView(@NonNull Context context, @NonNull View view) {
        super(context, view);
        init();
    }

    public UIImageView(@NonNull Context context) {
        super(context);
        init();
    }

    public UIImageView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UIImageView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UIImageView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setWantsLayer(true);
        setContentMode(UIViewContentMode.ScaleToFill);
    }

    /* UIView */

    @NonNull
    @Override
    public CGSize intrinsicContentSize() {
        if (image == null) {
            return new CGSize(0, 0);
        }
        return new CGSize(image.getSize().getWidth(), image.getSize().getHeight());
    }

    /* UIImageView Props */

    /* Image Source */

    @Nullable private UIImage image;

    @Nullable
    public UIImage getImage() {
        return image;
    }

    public void setImage(@Nullable UIImage image) {
        if (this.image == image) {
            return;
        }
        this.image = image;
        this.getLayer().setBitmap(image.getBitmap());
        invalidate();
    }

    /* Content Mode */

    private UIViewContentMode contentMode = UIViewContentMode.ScaleToFill;

    public UIViewContentMode getContentMode() {
        return contentMode;
    }

    public void setContentMode(UIViewContentMode contentMode) {
        this.contentMode = contentMode;
        if (contentMode == UIViewContentMode.ScaleToFill) {
            this.getLayer().setBitmapGravity(CALayerBitmapPainter.GRAVITY_SCALE_TO_FILL);
        }
        else if (contentMode == UIViewContentMode.ScaleAspectFit) {
            this.getLayer().setBitmapGravity(CALayerBitmapPainter.GRAVITY_SCALE_ASCEPT_FIT);
        }
        else if (contentMode == UIViewContentMode.ScaleAspectFill) {
            this.getLayer().setBitmapGravity(CALayerBitmapPainter.GRAVITY_SCALE_ASCEPT_FILL);
        }
        else if (contentMode == UIViewContentMode.TopLeft) {
            this.getLayer().setBitmapGravity(CALayerBitmapPainter.GRAVITY_TOP_LEFT);
        }
        else if (contentMode == UIViewContentMode.Top) {
            this.getLayer().setBitmapGravity(CALayerBitmapPainter.GRAVITY_TOP);
        }
        else if (contentMode == UIViewContentMode.TopRight) {
            this.getLayer().setBitmapGravity(CALayerBitmapPainter.GRAVITY_TOP_RIGHT);
        }
        else if (contentMode == UIViewContentMode.Left) {
            this.getLayer().setBitmapGravity(CALayerBitmapPainter.GRAVITY_LEFT);
        }
        else if (contentMode == UIViewContentMode.Right) {
            this.getLayer().setBitmapGravity(CALayerBitmapPainter.GRAVITY_RIGHT);
        }
        else if (contentMode == UIViewContentMode.Center) {
            this.getLayer().setBitmapGravity(CALayerBitmapPainter.GRAVITY_CENRER);
        }
        else if (contentMode == UIViewContentMode.BottomLeft) {
            this.getLayer().setBitmapGravity(CALayerBitmapPainter.GRAVITY_BOTTOM_LEFT);
        }
        else if (contentMode == UIViewContentMode.Bottom) {
            this.getLayer().setBitmapGravity(CALayerBitmapPainter.GRAVITY_BOTTOM);
        }
        else if (contentMode == UIViewContentMode.BottomRight) {
            this.getLayer().setBitmapGravity(CALayerBitmapPainter.GRAVITY_BOTTOM_RIGHT);
        }
    }

    /* Animated Image */

    @Nullable private ValueAnimator animator = null;
    @Nullable private UIImage[] animationImages = new UIImage[0];   // The array must contain UIImages. Setting hides the single image. default is nil
    private double animationDuration = 0.0;     // for one cycle of images. default is number of images * 1/30th of a second (i.e. 30 fps)
    private int animationRepeatCount = 0;       // 0 means infinite (default is 0)

    @Nullable
    public UIImage[] getAnimationImages() {
        return animationImages;
    }

    public void setAnimationImages(@Nullable UIImage[] animationImages) {
        stopAnimating();
        this.animationImages = animationImages;
        this.animationDuration = this.animationImages.length * (1.0 / 30.0);
    }

    public double getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(double animationDuration) {
        this.animationDuration = animationDuration;
    }

    public int getAnimationRepeatCount() {
        return animationRepeatCount;
    }

    public void setAnimationRepeatCount(int animationRepeatCount) {
        this.animationRepeatCount = animationRepeatCount;
    }

    public void startAnimating() {
        stopAnimating();
        if (animationImages != null && animationImages.length > 0) {
            ValueAnimator valueAnimator = new ValueAnimator().ofInt(0, animationImages.length);
            valueAnimator.setDuration((long)(animationDuration * 1000));
            if (animationRepeatCount == 0) {
                valueAnimator.setRepeatCount(99999);
            }
            else {
                valueAnimator.setRepeatCount(animationRepeatCount);
            }
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                    int currentIndex = (int)valueAnimator.getAnimatedValue();
                    if (currentIndex < animationImages.length) {
                        setImage(animationImages[currentIndex]);
                    }
                }
            });
            valueAnimator.start();
            this.animator = valueAnimator;
        }
    }

    public void stopAnimating() {
        if (this.animator != null){
            this.animator.cancel();
        }
    }

    public boolean isAnimating() {
        return this.animator != null && this.animator.isRunning();
    }
}
