package com.yy.codex.coreanimation
import android.graphics.*
import com.yy.codex.uikit.*

/**
 * Created by adi on 17/1/17.
 */

internal object CALayerPainter {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    fun drawLayerTree(layer: CALayer, canvas: Canvas) {
        val srcBitmap = createBitmapWithLayerTree(layer)
        val maskBitmap = createBitmapWithMaskLayer(layer)
        val resultBitmap = createBitmapThatApplyMask(layer, srcBitmap, maskBitmap)
        paint.reset()
        canvas.drawBitmap(resultBitmap, 0f, 0f, paint)
    }

    fun drawCurrentLayer(layer: CALayer, canvas: Canvas) {
        if (layer.hidden) {
            return
        }
        if (layer.cornerRadius > 0) {
            drawRoundRect(canvas, layer)
            if (layer.bitmap != null) {
                drawRoundRectBitmap(canvas, layer)
            }
            if (layer.borderWidth > 0) {
                drawRoundRectBorder(canvas, layer)
            }
        } else {
            drawRect(canvas, layer)
            if (layer.bitmap != null) {
                drawRectBitmap(canvas, layer)
            }
            if (layer.borderWidth > 0) {
                drawRectBorder(canvas, layer)
            }
        }

        if (layer.mask != null) {
            // @TODO
        }
    }

    /* support method */

    private fun drawRoundRect(canvas: Canvas, layer: CALayer) {
        val frameRaw = layer.frame
        val scaledDensity = UIScreen.mainScreen.scale().toFloat()
        val origin = CALayer.calcOriginInSuperCoordinate(layer)
        val frame = CGRect(frameRaw.x * scaledDensity, frameRaw.y * scaledDensity, frameRaw.width * scaledDensity, frameRaw.height * scaledDensity)
        val borderWidth = layer.borderWidth.toFloat() * scaledDensity
        val cornerRadius = layer.cornerRadius.toFloat() * scaledDensity
        val halfBorderW = borderWidth / 2.0f

        paint.reset()
        paint.isAntiAlias = true
        paint.color = layer.backgroundColor.toInt()
        var rectFCopyed = frame.shrinkToRectF(halfBorderW, origin)
        if (layer.shadowRadius > 0) {
            val shadowRadius = layer.shadowRadius.toFloat() * scaledDensity
            val shadowX = layer.shadowX.toFloat() * scaledDensity
            val shadowY = layer.shadowY.toFloat() * scaledDensity
            paint.setShadowLayer(shadowRadius, shadowX, shadowY, layer.shadowColor.toInt())
            rectFCopyed = RectF(rectFCopyed.left, rectFCopyed.top, rectFCopyed.right - shadowX, rectFCopyed.bottom - shadowY)
        }
        canvas.drawRoundRect(rectFCopyed, cornerRadius, cornerRadius, paint)
    }

    private fun drawRoundRectBitmap(canvas: Canvas, layer: CALayer) {
        val frameRaw = layer.frame
        val bitmap = layer.bitmap
        val bitmapGravity = layer.bitmapGravity
        val scaledDensity = UIScreen.mainScreen.scale().toFloat()
        val origin = CALayer.calcOriginInSuperCoordinate(layer)
        val frame = CGRect(frameRaw.x * scaledDensity, frameRaw.y * scaledDensity, frameRaw.width * scaledDensity, frameRaw.height * scaledDensity)
        val cornerRadius = layer.cornerRadius.toFloat() * scaledDensity
        val bitmapColor = layer.bitmapColor

        val maskFrame = CGRect(origin.x, origin.y, frame.size.width, frame.size.height)
        val resultBitmap = createEmptyBitmap(maskFrame)
        val resultCanvas = Canvas(resultBitmap)
        val mixPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        resultCanvas.drawBitmap(createRadiusMask(maskFrame, cornerRadius.toDouble()), 0f, 0f, mixPaint)
        mixPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        if (bitmapColor != null) {
            mixPaint.colorFilter = PorterDuffColorFilter(bitmapColor.toInt(), PorterDuff.Mode.SRC_IN)
        }
        bitmap?.let {
            CALayerBitmapPainter.drawBitmap(resultCanvas, maskFrame, it, bitmapGravity, mixPaint)
        }
        paint.reset()
        if (bitmapColor != null) {
            paint.colorFilter = PorterDuffColorFilter(bitmapColor.toInt(), PorterDuff.Mode.SRC_IN)
        }
        canvas.drawBitmap(resultBitmap, 0f, 0f, paint)
    }

    private fun drawRoundRectBorder(canvas: Canvas, layer: CALayer) {
        val frameRaw = layer.frame
        val scaledDensity = UIScreen.mainScreen.scale().toFloat()
        val origin = CALayer.calcOriginInSuperCoordinate(layer)
        val frame = CGRect(frameRaw.x * scaledDensity, frameRaw.y * scaledDensity, frameRaw.width * scaledDensity, frameRaw.height * scaledDensity)
        val borderWidth = layer.borderWidth.toFloat() * scaledDensity
        val cornerRadius = layer.cornerRadius.toFloat() * scaledDensity
        val halfBorderW = borderWidth / 2.0f

        paint.reset()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth
        paint.color = layer.borderColor.toInt()
        var rectFCopyed = frame.shrinkToRectF(halfBorderW, origin)
        if (layer.shadowRadius > 0) {
            val shadowX = layer.shadowX.toFloat() * scaledDensity
            val shadowY = layer.shadowY.toFloat() * scaledDensity
            rectFCopyed = RectF(rectFCopyed.left, rectFCopyed.top, rectFCopyed.right - shadowX, rectFCopyed.bottom - shadowY)
        }
        canvas.drawRoundRect(rectFCopyed, cornerRadius, cornerRadius, paint)
    }

    private fun drawRect(canvas: Canvas, layer: CALayer) {
        val frameRaw = layer.frame
        val scaledDensity = UIScreen.mainScreen.scale().toFloat()
        val origin = CALayer.calcOriginInSuperCoordinate(layer)
        val frame = CGRect(frameRaw.x * scaledDensity, frameRaw.y * scaledDensity, frameRaw.width * scaledDensity, frameRaw.height * scaledDensity)


        paint.reset()
        paint.color = layer.backgroundColor.toInt()
        var rectFCopyed = frame.toRectF(origin)
        if (layer.shadowRadius > 0) {
            val shadowRadius = layer.shadowRadius.toFloat() * scaledDensity
            val shadowX = layer.shadowX.toFloat() * scaledDensity
            val shadowY = layer.shadowY.toFloat() * scaledDensity
            paint.setShadowLayer(shadowRadius, shadowX, shadowY, layer.shadowColor.toInt())
            rectFCopyed = RectF(rectFCopyed.left, rectFCopyed.top, rectFCopyed.right - shadowX, rectFCopyed.bottom - shadowY)
        }
        canvas.drawRect(rectFCopyed, paint)
    }

    private fun drawRectBitmap(canvas: Canvas, layer: CALayer) {
        val frameRaw = layer.frame
        val bitmap = layer.bitmap
        val scaledDensity = UIScreen.mainScreen.scale().toFloat()
        val origin = CALayer.calcOriginInSuperCoordinate(layer)
        val frame = CGRect(frameRaw.x * scaledDensity, frameRaw.y * scaledDensity, frameRaw.width * scaledDensity, frameRaw.height * scaledDensity)
        val bitmapColor = layer.bitmapColor
        paint.reset()
        if (bitmapColor != null) {
            paint.colorFilter = PorterDuffColorFilter(bitmapColor.toInt(), PorterDuff.Mode.SRC_IN)
        }
        val bitmapFrame = CGRect(origin.x, origin.y, frame.size.width, frame.size.height)
        bitmap?.let {
            CALayerBitmapPainter.drawBitmap(canvas, bitmapFrame, it, layer.bitmapGravity, paint)
        }
        paint.colorFilter = null
    }

    private fun drawRectBorder(canvas: Canvas, layer: CALayer) {
        val frameRaw = layer.frame
        val scaledDensity = UIScreen.mainScreen.scale().toFloat()
        val origin = CALayer.calcOriginInSuperCoordinate(layer)
        val frame = CGRect(frameRaw.x * scaledDensity, frameRaw.y * scaledDensity, frameRaw.width * scaledDensity, frameRaw.height * scaledDensity)
        val borderWidth = layer.borderWidth.toFloat() * scaledDensity
        val halfBorderW = borderWidth / 2.0f

        paint.reset()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth
        paint.color = layer.borderColor.toInt()
        var rectFCopyed = frame.shrinkToRectF(halfBorderW, origin)
        if (layer.shadowRadius > 0) {
            val shadowX = layer.shadowX.toFloat() * scaledDensity
            val shadowY = layer.shadowY.toFloat() * scaledDensity
            rectFCopyed = RectF(rectFCopyed.left, rectFCopyed.top, rectFCopyed.right - shadowX, rectFCopyed.bottom - shadowY)
        }
        canvas.drawRect(rectFCopyed, paint)
    }

    private fun createEmptyBitmap(rect: CGRect): Bitmap {
        val bitmapW = (rect.size.width + rect.origin.x).toInt()
        val bitmapH = (rect.size.height + rect.origin.y).toInt()
        val bitmap = Bitmap.createBitmap(bitmapW, bitmapH, Bitmap.Config.ARGB_8888)
        return bitmap
    }

    private fun createEmptyBitmap(layer: CALayer): Bitmap {
        val origin = CALayer.calcOriginInSuperCoordinate(layer)
        val scaledDensity = UIScreen.mainScreen.scale().toFloat()
        val bitmapW = (layer.frame.size.width * scaledDensity + origin.x).toInt()
        val bitmapH = (layer.frame.size.height * scaledDensity + origin.y).toInt()
        val bitmap = Bitmap.createBitmap(bitmapW, bitmapH, Bitmap.Config.ARGB_8888)
        return bitmap
    }

    private fun createBitmapWithLayerTree(layer: CALayer): Bitmap {
        val bitmap = createEmptyBitmap(layer)
        layer.drawLayerTreeInCanvas(Canvas(bitmap))
        return bitmap
    }

    private fun createBitmapWithMaskLayer(layer: CALayer): Bitmap {
        val origin = CALayer.calcOriginInSuperCoordinate(layer)
        val scaledDensity = UIScreen.mainScreen.scale().toFloat()
        val cornerRaidus = layer.cornerRadius.toFloat() * scaledDensity
        val bitmapW = (layer.frame.size.width * scaledDensity + origin.x).toInt()
        val bitmapH = (layer.frame.size.height * scaledDensity + origin.y).toInt()
        val bitmap = Bitmap.createBitmap(bitmapW, bitmapH, Bitmap.Config.ARGB_8888)
        val rectF = RectF(origin.x.toFloat(), origin.y.toFloat(), bitmapW.toFloat(), bitmapH.toFloat())
        Canvas(bitmap).drawRoundRect(rectF, cornerRaidus, cornerRaidus, Paint(Paint.ANTI_ALIAS_FLAG))
        return bitmap
    }

    private fun createBitmapThatApplyMask(layer: CALayer, srcBitmap: Bitmap, maskBitmap: Bitmap): Bitmap {
        paint.reset()
        val bitmapMixed = createEmptyBitmap(layer)
        val canvasMixed = Canvas(bitmapMixed)
        if (layer.transforms.size > 0) {
            val matrix = createMatrix(layer)
            if (layer.clipToBounds) {
                canvasMixed.drawBitmap(maskBitmap, matrix, paint)
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            }
            canvasMixed.drawBitmap(srcBitmap, matrix, paint)
            paint.xfermode = null
        } else {
            canvasMixed.drawBitmap(maskBitmap, 0f, 0f, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvasMixed.drawBitmap(srcBitmap, 0f, 0f, paint)
            paint.xfermode = null
        }
        return bitmapMixed
    }

    private fun createRadiusMask(rect: CGRect, radius: Double): Bitmap {
        val maskBitmap = Bitmap.createBitmap((rect.size.width + rect.origin.x).toInt(), (rect.size.height + rect.origin.y).toInt(), Bitmap.Config.ARGB_8888)
        val maskCanvas = Canvas(maskBitmap)
        val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        maskCanvas.drawRoundRect(rect.toRectF(), radius.toFloat(), radius.toFloat(), maskPaint)
        return maskBitmap
    }

    private fun createMatrix(layer: CALayer): Matrix {
        val scaledDensity = UIScreen.mainScreen.scale().toFloat()
        val transforms = layer.transforms
        val matrix = Matrix()
        if (transforms == null || transforms.size == 0) {
            return matrix
        }
        val rectF = layer.frame.toRectF()
        transforms
                .filter { it.enable }
                .forEach {
                    if (it is CGTransformRotation) {
                        matrix.preRotate(it.angle.toFloat(), rectF.centerX() * scaledDensity, rectF.centerY() * scaledDensity)
                    } else if (it is CGTransformTranslation) {
                        matrix.postTranslate(it.tx.toFloat(), it.ty.toFloat())
                    } else if (it is CGTransformScale) {
                        matrix.postScale(it.sx.toFloat(), it.sy.toFloat(), rectF.centerX() * scaledDensity, rectF.centerY() * scaledDensity)
                    } else if (it is CGTransformMatrix) {
                        // @TODO
                    }
                }
        return matrix
    }

}
