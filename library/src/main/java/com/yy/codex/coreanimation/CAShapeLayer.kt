package com.yy.codex.coreanimation

import android.graphics.*

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

    var strokeColor = Color.BLACK
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
        path?.let {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = lineWidth.toFloat()
            paint.color = strokeColor
            canvas.drawPath(it, paint)
        }
    }

}
