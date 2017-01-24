package com.yy.codex.coreanimation

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.yy.codex.uikit.CGRect
import com.yy.codex.uikit.UIScreen

/**
 * Created by adi on 17/1/10.
 */

class CATextLayer(frame: CGRect) : CALayer(frame) {

    var string: String? = null
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var fontSize = 14f
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var fontColor = Color.BLACK
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var alignMode = ALIGN_LEFT
        set(value) {
            field = value
            when (field) {
                ALIGN_LEFT -> paintAlignMode = Paint.Align.LEFT
                ALIGN_RIGHT -> paintAlignMode = Paint.Align.RIGHT
                ALIGN_CENTER -> paintAlignMode = Paint.Align.CENTER
                else -> paintAlignMode = Paint.Align.LEFT
            }
            this.setNeedDisplay(true)
        }

    private var paintAlignMode: Paint.Align = Paint.Align.LEFT

    var truncateMode: Int = 0 // not support
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var wrapped: Boolean = false // not support
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    override fun drawInCanvas(canvas: Canvas) {
        super.drawInCanvas(canvas)
        val rect = this.frame.toRect()
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = fontColor
        paint.textSize = fontSize * UIScreen.mainScreen.scale().toFloat()
        paint.textAlign = paintAlignMode
        val fontMetrics = paint.fontMetricsInt
        val baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2
        var drawX = rect.left
        when (alignMode) {
            ALIGN_CENTER -> drawX = rect.centerX()
            ALIGN_RIGHT -> drawX = rect.right
            ALIGN_LEFT -> drawX = rect.left
        }
        string?.let {
            canvas.drawText(it, drawX.toFloat(), baseline.toFloat(), paint)
        }
    }

    companion object {
        val ALIGN_LEFT = 0x01
        val ALIGN_RIGHT = 0x02
        val ALIGN_CENTER = 0x03
        val ALIGN_JUSTIFY = 0x04 // not support
    }

}
