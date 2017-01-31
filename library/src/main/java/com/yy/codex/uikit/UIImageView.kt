package com.yy.codex.uikit

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import com.yy.codex.coreanimation.CALayer

/**
 * Created by cuiminghui on 2017/1/10.
 */

class UIImageView : UIView {

    /* Constructor */

    constructor(context: Context, view: View) : super(context, view) {
    }

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun init() {
        super.init()
        wantsLayer = true
        contentMode = UIViewContentMode.ScaleToFill
    }

    /* XML */

    override fun prepareProps(attrs: AttributeSet) {
        super.prepareProps(attrs)
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.UIImageView, 0, 0)
        typedArray.getResourceId(R.styleable.UIImageView_image, -1)?.let {
            if (it != -1) {
                initializeAttributes.put("UIImageView.image", UIImage(context, it))
            }
        }
        typedArray.getInt(R.styleable.UIImageView_contentMode, -1)?.let {
            if (it != -1) {
                initializeAttributes.put("UIImageView.contentMode", it)
            }
        }
        initializeAttributes.put("UIView.wantsLayer", true)
        typedArray.recycle()
    }

    override fun resetProps() {
        super.resetProps()
        (initializeAttributes["UIImageView.image"] as? UIImage)?.let {
            image = it
        }
        (initializeAttributes["UIImageView.contentMode"] as? Int)?.let {
            when (it) {
                0 -> contentMode = UIViewContentMode.ScaleToFill
                1 -> contentMode = UIViewContentMode.ScaleAspectFit
                2 -> contentMode = UIViewContentMode.ScaleAspectFill
                3 -> contentMode = UIViewContentMode.Redraw
                4 -> contentMode = UIViewContentMode.Center
                5 -> contentMode = UIViewContentMode.Top
                6 -> contentMode = UIViewContentMode.Bottom
                7 -> contentMode = UIViewContentMode.Left
                8 -> contentMode = UIViewContentMode.Right
                9 -> contentMode = UIViewContentMode.TopLeft
                10 -> contentMode = UIViewContentMode.TopRight
                11 -> contentMode = UIViewContentMode.BottomLeft
                12 -> contentMode = UIViewContentMode.BottomRight
            }
        }
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
            if (constraint != null) {
                superview?.let(UIView::layoutSubviews)
            }
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
