package com.yy.codex.coreanimation

import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import com.yy.codex.foundation.doubleEquals
import com.yy.codex.uikit.*
import java.util.*


/**
 * Created by cuiminghui on 2017/1/3.
 */

open class CALayer {

    enum class BitmapGravity {
        ScaleAspectFit,
        ScaleAspectFill,
        ScaleToFill,
        TopLeft,
        Top,
        TopRight,
        Left,
        Center,
        Right,
        BottomLeft,
        Bottom,
        BottomRight
    }

    /* layoutProps */

    var frame = com.yy.codex.uikit.CGRect(0.0, 0.0, 0.0, 0.0)
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    /* styleProps */

    var backgroundColor = com.yy.codex.uikit.UIColor.Companion.clearColor
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var cornerRadius = 0.0
        set(value) {
            if (!doubleEquals(field, value)) {
                val oldValue = field
                field = value
                this.setNeedDisplay(true)
                requestRootLayer().view?.let {
                    com.yy.codex.uikit.UIViewAnimator.addAnimationState(it, "layer.mCornerRadius", oldValue, value)
                }
            }
        }


    var borderWidth = 0.0
        set(value) {
            val oldValue = field
            if (!doubleEquals(field, value)) {
                field = value
                this.setNeedDisplay(true)
                requestRootLayer().view?.let {
                    com.yy.codex.uikit.UIViewAnimator.addAnimationState(it, "layer.mBorderWidth", oldValue, value)
                }
            }
        }

    var borderColor = com.yy.codex.uikit.UIColor.Companion.blackColor
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var shadowX = 2.0
        set(value) {
            if (!doubleEquals(field, value)) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var shadowY = 2.0
        set(value) {
            if (!doubleEquals(field, value)) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var shadowRadius = 0.0
        set(value) {
            if (!doubleEquals(field, value)) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var shadowColor = com.yy.codex.uikit.UIColor.Companion.blackColor
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var bitmap: android.graphics.Bitmap? = null
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var bitmapGravity = com.yy.codex.coreanimation.CALayer.BitmapGravity.ScaleAspectFit
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var bitmapColor: com.yy.codex.uikit.UIColor? = null
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var clipToBounds = false
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var hidden = false
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    /* renderProps */

    private var needDisplay = false

    fun setNeedDisplay(mNeedDisplay: Boolean) {
        this.needDisplay = mNeedDisplay
        if (mNeedDisplay) {
            requestRootLayer().view?.let(com.yy.codex.uikit.UIView::invalidate)
        }
    }

    /*
        以下情况，在新画布绘制。
        1. 有 transform 属性时
        2. 有子节点 且 mClipToBounds 时
    */
    var isNewCanvasContext = false
        get() {
            val result = this.transforms != null && this.transforms!!.size > 0 || this.sublayers.size > 0 && this.clipToBounds
            return result
        }

    var mask: CALayer? = null // not support
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    /* transformProp */

    var transforms: List<CGTransform> = listOf()

    fun setTransform(a: CGTransform) {
        this.transforms = listOf(a)
    }

    /* hierarchyProps */

    protected var view: UIView? = null

    fun bindView(view: UIView) {
        this.view = view
    }

    var superLayer: CALayer? = null
        private set

    var sublayers: MutableList<CALayer> = mutableListOf()
        private set

    /* category CALayer Constructor */

    constructor() {}

    constructor(mFrame: CGRect) {
        val x = mFrame.origin.x.toFloat()
        val y = mFrame.origin.y.toFloat()
        val w = mFrame.size.width.toFloat()
        val h = mFrame.size.height.toFloat()
        this.frame = CGRect(x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble())
    }

    /* category CALayer Hierarchy */

    fun resetBelongings(isRoot: Boolean) {
        if (isRoot) {
            superLayer = null
        }
        for (sublayer in sublayers) {
            sublayer.superLayer = this
            sublayer.resetBelongings(false)
        }
    }

    fun removeFromSuperLayer() {
        superLayer?.let {
            it.sublayers.remove(this)
        }
    }

    fun addSubLayer(layer: CALayer) {
        layer.removeFromSuperLayer()
        layer.superLayer = this
        this.sublayers.add(layer)
    }

    fun insertSubLayerAtIndex(subLayer: CALayer, index: Int) {
        subLayer.removeFromSuperLayer()
        if (index < 1) {
            this.sublayers.add(0, subLayer)
        } else if (index > this.sublayers.size - 1) {
            this.sublayers.add(subLayer)
        } else {
            this.sublayers.add(index, subLayer)
        }
    }

    fun insertBelowSubLayer(subLayer: CALayer, siblingSubview: CALayer) {
        val idx = this.sublayers.indexOf(siblingSubview)
        if (idx > -1) {
            subLayer.removeFromSuperLayer()
            this.sublayers.add(idx, subLayer)
        }
    }

    fun insertAboveSubLayer(subLayer: CALayer, siblingSubview: CALayer) {
        val idx = this.sublayers.indexOf(siblingSubview)
        if (idx > -1) {
            subLayer.removeFromSuperLayer()
            this.sublayers.add(idx + 1, subLayer)
        }
    }

    fun replaceSubLayer(subLayer: CALayer, newLayer: CALayer) {
        val idx = this.sublayers.indexOf(subLayer)
        if (idx > -1) {
            subLayer.removeFromSuperLayer()
            insertSubLayerAtIndex(newLayer, idx)
        }
    }

    /* category CALayer Appearance */

    fun drawRect(canvas: Canvas, rect: CGRect) {
        if (this.askIfNeedDisplay()) {
            this.resetNeedDisplayToFalse()
            drawAllLayers(canvas, rect)
        }
    }

    private fun drawAllLayers(canvas: Canvas, rect: CGRect) {
        if (hidden) {
            return
        }
        if (this.isNewCanvasContext) {
            CALayerPainter.drawLayerTree(this, canvas)
        } else {
            this.drawInCanvas(canvas)
            for (item in this.sublayers) {
                item.drawAllLayers(canvas, rect)
            }
        }
    }

    protected open fun drawInCanvas(canvas: Canvas) {
        CALayerPainter.drawCurrentLayer(this, canvas)
    }

    fun drawLayerTreeInCanvas(canvas: Canvas) {
        this.drawInCanvas(canvas)
        for (item in this.sublayers) {
            item.drawLayerTreeInCanvas(canvas)
        }
    }

    private fun askIfNeedDisplay(): Boolean {
        return true
    }

    private fun resetNeedDisplayToFalse() {
        this.needDisplay = false
        for (item in this.sublayers) {
            item.resetNeedDisplayToFalse()
        }
    }

    private fun requestRootLayer(): CALayer {
        var root: CALayer = this
        while (root.superLayer != null) {
            root = root.superLayer as CALayer
        }
        return root
    }

    /* Enlarger Layer */

    open fun wantsEnlargerLayer(): Boolean {
        if (superLayer == null) {
            if (shadowColor != null) {
                return true
            }
        }
        return false
    }

    open fun enlargerInsets(): UIEdgeInsets {
        val top = -(-shadowRadius + shadowY)
        val left = -(-shadowRadius + shadowX)
        val bottom = shadowRadius + shadowY
        val right = shadowRadius + shadowX
        return UIEdgeInsets(top, left, bottom, right)
    }

    /* Animation */

    fun animate(aKey: String, aValue: Float) {
        if (aKey.equals("layer.mCornerRadius", ignoreCase = true)) {
            cornerRadius = aValue.toDouble()
        } else if (aKey.equals("layer.mBorderWidth", ignoreCase = true)) {
            borderWidth = aValue.toDouble()
        }
    }

    /* Copy */

    open fun copyProps(toLayer: CALayer) {
        toLayer.frame = frame
        toLayer.backgroundColor = backgroundColor
        toLayer.cornerRadius = cornerRadius
        toLayer.borderWidth = borderWidth
        toLayer.borderColor = borderColor
        toLayer.shadowX = shadowX
        toLayer.shadowY = shadowY
        toLayer.shadowRadius = shadowRadius
        toLayer.shadowColor = shadowColor
        toLayer.bitmap = bitmap
        toLayer.bitmapGravity = bitmapGravity
        toLayer.bitmapColor = bitmapColor
        toLayer.clipToBounds = clipToBounds
        toLayer.hidden = hidden
        toLayer.mask = mask
        toLayer.sublayers = sublayers
    }

    open fun resetProps(attributes: TypedArray) {
        if (attributes.getColor(R.styleable.UIView_layer_backgroundColor, -1) != -1) {
            backgroundColor = UIColor(attributes.getColor(R.styleable.UIView_layer_backgroundColor, -1))
        }
        cornerRadius = attributes.getFloat(R.styleable.UIView_layer_cornerRadius, 0.0f).toDouble()
        borderWidth = attributes.getFloat(R.styleable.UIView_layer_borderWidth, 0.0f).toDouble()
        if (attributes.getColor(R.styleable.UIView_layer_borderColor, -1) != -1) {
            borderColor = UIColor(attributes.getColor(R.styleable.UIView_layer_borderColor, -1))
        }
        shadowX = attributes.getFloat(R.styleable.UIView_layer_shadowX, 0.0f).toDouble()
        shadowY = attributes.getFloat(R.styleable.UIView_layer_shadowY, 0.0f).toDouble()
        shadowRadius = attributes.getFloat(R.styleable.UIView_layer_shadowRadius, 0.0f).toDouble()
        if (attributes.getColor(R.styleable.UIView_layer_shadowColor, -1) != -1) {
            shadowColor = UIColor(attributes.getColor(R.styleable.UIView_layer_shadowColor, -1))
        }
        clipToBounds = attributes.getBoolean(R.styleable.UIView_layer_clipToBounds, false)
    }

    companion object {

        /* category CALayer support method */

        fun calculateOriginInSuperCoordinate(layer: CALayer): CGPoint {
            val scaledDensity = UIScreen.mainScreen.scale().toFloat()
            var oriX = layer.frame.origin.x
            var oriY = layer.frame.origin.y
            var p: CALayer? = layer.superLayer
            while (p != null) {
                oriX += p.frame.origin.x
                oriY += p.frame.origin.y
                p = p.superLayer
            }
            return CGPoint(oriX * scaledDensity, oriY * scaledDensity)
        }
    }


}