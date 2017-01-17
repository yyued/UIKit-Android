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

    @Override
    protected void init() {
        super.init();
        setWantsLayer(true);
        setContentMode(UIViewContentMode.ScaleToFill);
    }

    /* UIView */

    @NonNull
    @Override
    public CGSize intrinsicContentSize() {
        if (mImage == null) {
            return new CGSize(0, 0);
        }
        return new CGSize(mImage.getSize().width, mImage.getSize().height);
    }

    /* UIImageView Props */

    /* Image Source */

    @Nullable private UIImage mImage;

    @Nullable
    public UIImage getImage() {
        return mImage;
    }

    public void setImage(@Nullable UIImage image) {
        if (this.mImage == image) {
            return;
        }
        this.mImage = image;
        this.getLayer().setBitmap(image.getBitmap());
        invalidate();
    }

    /* Content Mode */

    private UIViewContentMode mContentMode = UIViewContentMode.ScaleToFill;

    public UIViewContentMode getContentMode() {
        return mContentMode;
    }

    public void setContentMode(UIViewContentMode contentMode) {
        this.mContentMode = contentMode;
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

    @Nullable private ValueAnimator mAnimator = null;
    @Nullable private UIImage[] mAnimationImages = new UIImage[0];   // The array must contain UIImages. Setting hides the single mImage. default is nil
    private double mAnimationDuration = 0.0;     // for one cycle of images. default is number of images * 1/30th of a second (i.e. 30 fps)
    private int mAnimationRepeatCount = 0;       // 0 means infinite (default is 0)

    @Nullable
    public UIImage[] getAnimationImages() {
        return mAnimationImages;
    }

    public void setAnimationImages(@Nullable UIImage[] animationImages) {
        stopAnimating();
        this.mAnimationImages = animationImages;
        this.mAnimationDuration = this.mAnimationImages.length * (1.0 / 30.0);
    }

    public double getAnimationDuration() {
        return mAnimationDuration;
    }

    public void setAnimationDuration(double animationDuration) {
        this.mAnimationDuration = animationDuration;
    }

    public int getAnimationRepeatCount() {
        return mAnimationRepeatCount;
    }

    public void setAnimationRepeatCount(int animationRepeatCount) {
        this.mAnimationRepeatCount = animationRepeatCount;
    }

    public void startAnimating() {
        stopAnimating();
        if (mAnimationImages != null && mAnimationImages.length > 0) {
            ValueAnimator valueAnimator = new ValueAnimator().ofInt(0, mAnimationImages.length);
            valueAnimator.setDuration((long)(mAnimationDuration * 1000));
            if (mAnimationRepeatCount == 0) {
                valueAnimator.setRepeatCount(99999);
            }
            else {
                valueAnimator.setRepeatCount(mAnimationRepeatCount);
            }
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                    int currentIndex = (int)valueAnimator.getAnimatedValue();
                    if (currentIndex < mAnimationImages.length) {
                        setImage(mAnimationImages[currentIndex]);
                    }
                }
            });
            valueAnimator.start();
            this.mAnimator = valueAnimator;
        }
    }

    public void stopAnimating() {
        if (this.mAnimator != null){
            this.mAnimator.cancel();
        }
    }

    public boolean isAnimating() {
        return this.mAnimator != null && this.mAnimator.isRunning();
    }
}
