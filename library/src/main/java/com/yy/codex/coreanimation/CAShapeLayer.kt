package com.yy.codex.coreanimation

import android.graphics.*
import com.yy.codex.uikit.UIColor
import com.yy.codex.uikit.UIScreen

/**
 * Created by adi on 17/1/10.
 */

class CAShapeLayer : CALayer() {

    var path: Path? = null
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var fillColor: UIColor? = UIColor.blackColor

    var strokeColor: UIColor? = null
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var lineWidth = 1.0
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    /* category CAShapeLayer Constructor */

    override fun drawInCanvas(canvas: Canvas) {
        super.drawInCanvas(canvas)
        val paint = Paint()
        path?.let {
            val path = Path(it)
            val matrix = Matrix()
            matrix.setScale(UIScreen.mainScreen.scale().toFloat(), UIScreen.mainScreen.scale().toFloat())
            path.transform(matrix)
            fillColor?.let {
                paint.reset()
                paint.isAntiAlias = true
                paint.style = Paint.Style.FILL
                paint.color = it.toInt()
                canvas.drawPath(path, paint)
            }
            strokeColor?.let {
                paint.reset()
                paint.isAntiAlias = true
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = (lineWidth * UIScreen.mainScreen.scale()).toFloat()
                paint.color = it.toInt()
                canvas.drawPath(path, paint)
            }
        }
    }

}
