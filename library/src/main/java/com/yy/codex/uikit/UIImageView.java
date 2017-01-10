package com.yy.codex.uikit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cuiminghui on 2017/1/10.
 */

public class UIImageView extends UIView {

    /* Constructor */

    public UIImageView(Context context, View view) {
        super(context, view);
        init();
    }

    public UIImageView(Context context) {
        super(context);
        init();
    }

    public UIImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UIImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UIImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setWantsLayer(true);
        setContentMode(UIViewContentMode.ScaleToFill);
    }

    /* UIImageView Props */

    private UIImage image;

    public UIImage getImage() {
        return image;
    }

    public void setImage(UIImage image) {
        this.image = image;
        this.getLayer().setBitmap(image.getBitmap());
    }

    private UIViewContentMode contentMode = UIViewContentMode.ScaleToFill;

    public UIViewContentMode getContentMode() {
        return contentMode;
    }

    public void setContentMode(UIViewContentMode contentMode) {
        this.contentMode = contentMode;
        if (contentMode == UIViewContentMode.ScaleToFill) {
            this.getLayer().setBitmapGravity(CALayer.GRAVITY_SCALE_TO_FILL);
        }
        else if (contentMode == UIViewContentMode.ScaleAspectFit) {
            this.getLayer().setBitmapGravity(CALayer.GRAVITY_SCALE_ASCEPT_FIT);
        }
        else if (contentMode == UIViewContentMode.ScaleAspectFill) {
            this.getLayer().setBitmapGravity(CALayer.GRAVITY_SCALE_ASCEPT_FILL);
        }
        else if (contentMode == UIViewContentMode.TopLeft) {
            this.getLayer().setBitmapGravity(CALayer.GRAVITY_TOP_LEFT);
        }
        else if (contentMode == UIViewContentMode.Top) {
            this.getLayer().setBitmapGravity(CALayer.GRAVITY_TOP);
        }
        else if (contentMode == UIViewContentMode.TopRight) {
            this.getLayer().setBitmapGravity(CALayer.GRAVITY_TOP_RIGHT);
        }
        else if (contentMode == UIViewContentMode.Left) {
            this.getLayer().setBitmapGravity(CALayer.GRAVITY_LEFT);
        }
        else if (contentMode == UIViewContentMode.Right) {
            this.getLayer().setBitmapGravity(CALayer.GRAVITY_RIGHT);
        }
        else if (contentMode == UIViewContentMode.Center) {
            this.getLayer().setBitmapGravity(CALayer.GRAVITY_CENRER);
        }
        else if (contentMode == UIViewContentMode.BottomLeft) {
            this.getLayer().setBitmapGravity(CALayer.GRAVITY_BOTTOM_LEFT);
        }
        else if (contentMode == UIViewContentMode.Bottom) {
            this.getLayer().setBitmapGravity(CALayer.GRAVITY_BOTTOM);
        }
        else if (contentMode == UIViewContentMode.BottomRight) {
            this.getLayer().setBitmapGravity(CALayer.GRAVITY_BOTTOM_RIGHT);
        }
    }

}
