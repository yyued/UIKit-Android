package com.yy.codex.uikit

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

/**
 * Created by cuiminghui on 2017/1/10.
 */

class UIImageView : UIView {

    /* Constructor */

    constructor(context: Context, view: View) : super(context, view) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    override fun init() {
        super.init()
        wantsLayer = true
        contentMode = UIViewContentMode.ScaleToFill
    }

    /* UIView */

    override fun intrinsicContentSize(): CGSize {
        val image = image ?: return CGSize(.0, .0)
        return CGSize(image.size.width, image.size.height)
    }

    /* UIImageView Props */

    /* Image Source */

    var image: UIImage? = null
        set(image) {
            if (field === image) {
                return
            }
            field = image
            this.layer.bitmap = field?.bitmap ?: null
            invalidate()
        }

    /* Content Mode */

    var contentMode = UIViewContentMode.ScaleToFill
        set(contentMode) {
            field = contentMode
            if (contentMode == UIViewContentMode.ScaleToFill) {
                this.layer.bitmapGravity = CALayer.BitmapGravity.ScaleToFill
            } else if (contentMode == UIViewContentMode.ScaleAspectFit) {
                this.layer.bitmapGravity = CALayer.BitmapGravity.ScaleAspectFit
            } else if (contentMode == UIViewContentMode.ScaleAspectFill) {
                this.layer.bitmapGravity = CALayer.BitmapGravity.ScaleAspectFill
            } else if (contentMode == UIViewContentMode.TopLeft) {
                this.layer.bitmapGravity = CALayer.BitmapGravity.TopLeft
            } else if (contentMode == UIViewContentMode.Top) {
                this.layer.bitmapGravity = CALayer.BitmapGravity.Top
            } else if (contentMode == UIViewContentMode.TopRight) {
                this.layer.bitmapGravity = CALayer.BitmapGravity.TopRight
            } else if (contentMode == UIViewContentMode.Left) {
                this.layer.bitmapGravity = CALayer.BitmapGravity.Left
            } else if (contentMode == UIViewContentMode.Right) {
                this.layer.bitmapGravity = CALayer.BitmapGravity.Right
            } else if (contentMode == UIViewContentMode.Center) {
                this.layer.bitmapGravity = CALayer.BitmapGravity.Center
            } else if (contentMode == UIViewContentMode.BottomLeft) {
                this.layer.bitmapGravity = CALayer.BitmapGravity.BottomLeft
            } else if (contentMode == UIViewContentMode.Bottom) {
                this.layer.bitmapGravity = CALayer.BitmapGravity.Bottom
            } else if (contentMode == UIViewContentMode.BottomRight) {
                this.layer.bitmapGravity = CALayer.BitmapGravity.BottomRight
            }
        }

    /* Animated Image */

    private var animator: ValueAnimator? = null

    var animationImages: List<UIImage> = listOf()
        set(animationImages) {
            stopAnimating()
            field = animationImages
            this.animationDuration = animationImages.size * (1.0 / 30.0)
        }   // The array must contain UIImages. Setting hides the single mImage. default is nil

    var animationDuration = 0.0     // for one cycle of images. default is number of images * 1/30th of a second (i.e. 30 fps)
    var animationRepeatCount = 0       // 0 means infinite (default is 0)

    fun startAnimating() {
        stopAnimating()
        if (animationImages.size > 0) {
            val valueAnimator = ValueAnimator.ofInt(0, animationImages.size)
            valueAnimator.duration = (animationDuration * 1000).toLong()
            if (animationRepeatCount == 0) {
                valueAnimator.repeatCount = 99999
            } else {
                valueAnimator.repeatCount = animationRepeatCount
            }
            valueAnimator.addUpdateListener({ valueAnimator ->
                val currentIndex = valueAnimator.animatedValue as Int
                if (currentIndex < animationImages.size) {
                    image = animationImages[currentIndex]
                }
            })
            valueAnimator.start()
            this.animator = valueAnimator
        }
    }

    fun stopAnimating() {
        animator?.let { it.cancel() }
    }

    val isAnimating: Boolean
        get() = animator?.isRunning ?: false
}
