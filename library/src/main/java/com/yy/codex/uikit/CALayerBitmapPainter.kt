package com.yy.codex.uikit

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

/**
 * Created by adi on 17/1/16.
 */

internal object CALayerBitmapPainter {

    fun drawBitmap(canvas: Canvas, rect: CGRect, bitmap: Bitmap, bitmapGravity: CALayer.BitmapGravity, paint: Paint) {
        when (bitmapGravity) {
            CALayer.BitmapGravity.ScaleToFill -> drawScaleToFill(canvas, rect, bitmap, paint)
            CALayer.BitmapGravity.ScaleAspectFit -> drawScaleAspectFit(canvas, rect, bitmap, paint)
            CALayer.BitmapGravity.ScaleAspectFill -> drawScaleAspectFill(canvas, rect, bitmap, paint)
            CALayer.BitmapGravity.Center -> drawCenter(canvas, rect, bitmap, paint)
            CALayer.BitmapGravity.Top -> drawTop(canvas, rect, bitmap, paint)
            CALayer.BitmapGravity.TopLeft -> drawTopLeft(canvas, rect, bitmap, paint)
            CALayer.BitmapGravity.TopRight -> drawTopRight(canvas, rect, bitmap, paint)
            CALayer.BitmapGravity.Bottom -> drawBottom(canvas, rect, bitmap, paint)
            CALayer.BitmapGravity.BottomLeft -> drawBottomLeft(canvas, rect, bitmap, paint)
            CALayer.BitmapGravity.BottomRight -> drawBottomRight(canvas, rect, bitmap, paint)
            CALayer.BitmapGravity.Left -> drawLeft(canvas, rect, bitmap, paint)
            CALayer.BitmapGravity.Right -> drawRight(canvas, rect, bitmap, paint)
        }
    }

    /* draw details */

    private fun drawScaleToFill(canvas: Canvas, rect: CGRect, bitmap: Bitmap, paint: Paint) {
        val imageRect = CGRect(0.0, 0.0, bitmap.width.toDouble(), bitmap.height.toDouble())
        val frameRect = rect
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint)
    }

    private fun drawScaleAspectFit(canvas: Canvas, rect: CGRect, bitmap: Bitmap, paint: Paint) {
        val imageW = bitmap.width.toDouble()
        val imageH = bitmap.height.toDouble()
        val imageRatio = imageW / imageH
        val frameW = rect.size.width
        val frameH = rect.size.height
        val frameRatio = frameW / frameH
        val frameX = rect.origin.x
        val frameY = rect.origin.y
        val imageRect = CGRect(0.0, 0.0, imageW, imageH)
        val frameRect: CGRect
        if (frameRatio > imageRatio) {
            val scaledFrameW = frameH * imageRatio
            frameRect = CGRect(frameX + (frameW - scaledFrameW) / 2, frameY, scaledFrameW, frameH)
        } else {
            val scaledH = frameW / imageRatio
            frameRect = CGRect(frameX, frameY + (frameH - scaledH) / 2, frameW, scaledH)
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint)
    }

    private fun drawScaleAspectFill(canvas: Canvas, rect: CGRect, bitmap: Bitmap, paint: Paint) {
        val imageW = bitmap.width.toDouble()
        val imageH = bitmap.height.toDouble()
        val imageRatio = imageW / imageH
        val frameW = rect.size.width
        val frameH = rect.size.height
        val frameRatio = frameW / frameH
        val imageRect: CGRect
        val frameRect = rect
        if (frameRatio > imageRatio) {
            val clipedImageH = imageW / frameRatio
            imageRect = CGRect(0.0, (imageH - clipedImageH) / 2, imageW, clipedImageH)
        } else {
            val clipedImageW = imageH * frameRatio
            imageRect = CGRect((imageW - clipedImageW) / 2, 0.0, clipedImageW, imageH)
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint)
    }

    private fun drawCenter(canvas: Canvas, rect: CGRect, bitmap: Bitmap, paint: Paint) {
        val imageW = bitmap.width.toDouble()
        val imageH = bitmap.height.toDouble()
        val frameW = rect.size.width
        val frameH = rect.size.height
        val frameX = rect.origin.x
        val frameY = rect.origin.y
        var imageRect = CGRect(0.0, 0.0, imageW, imageH)
        var frameRect = rect
        if (frameW >= imageW && frameH >= imageH) {
            frameRect = CGRect(frameX + (frameW - imageW) / 2, frameY + (frameH - imageH) / 2, imageW, imageH)
        } else if (frameW < imageW && frameH >= imageH) {
            imageRect = CGRect((imageW - frameW) / 2, 0.0, frameW, imageH)
            frameRect = CGRect(frameX, frameY + (frameH - imageH) / 2, frameW, imageH)
        } else if (frameH < imageH && frameW >= imageW) {
            imageRect = CGRect(0.0, (imageH - frameH) / 2, imageW, frameH)
            frameRect = CGRect(frameX + (frameW - imageW) / 2, frameY, imageW, frameH)
        } else {
            imageRect = CGRect((imageW - frameW) / 2, (imageH - frameH) / 2, frameW, frameH)
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint)
    }

    private fun drawTop(canvas: Canvas, rect: CGRect, bitmap: Bitmap, paint: Paint) {
        val imageW = bitmap.width.toDouble()
        val imageH = bitmap.height.toDouble()
        val frameW = rect.size.width
        val frameH = rect.size.height
        val frameX = rect.origin.x
        val frameY = rect.origin.y
        var imageRect = CGRect(0.0, 0.0, imageW, imageH)
        var frameRect = rect
        if (frameW >= imageW && frameH >= imageH) {
            frameRect = CGRect(frameX + (frameW - imageW) / 2, frameY, imageW, imageH)
        } else if (frameW < imageW && frameH >= imageH) {
            imageRect = CGRect((imageW - frameW) / 2, 0.0, frameW, imageH)
            frameRect = CGRect(frameX, frameY, frameW, imageH)
        } else if (frameH < imageH && frameW >= imageW) {
            imageRect = CGRect(0.0, 0.0, imageW, frameH)
            frameRect = CGRect(frameX + (imageW - frameW) / 2, frameY, imageW, frameH)
        } else {
            imageRect = CGRect((imageW - frameW) / 2, 0.0, frameW, frameH)
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint)
    }

    private fun drawTopLeft(canvas: Canvas, rect: CGRect, bitmap: Bitmap, paint: Paint) {
        val imageW = bitmap.width.toDouble()
        val imageH = bitmap.height.toDouble()
        val frameW = rect.size.width
        val frameH = rect.size.height
        val frameX = rect.origin.x
        val frameY = rect.origin.y
        var imageRect = CGRect(0.0, 0.0, imageW, imageH)
        var frameRect = rect
        if (frameW >= imageW && frameH >= imageH) {
            frameRect = CGRect(frameX, frameY, imageW, imageH)
        } else if (frameW < imageW && frameH >= imageH) {
            imageRect = CGRect(0.0, 0.0, frameW, imageH)
            frameRect = CGRect(frameX, frameY, frameW, imageH)
        } else if (frameH < imageH && frameW >= imageW) {
            imageRect = CGRect(0.0, 0.0, imageW, frameH)
            frameRect = CGRect(frameX, frameY, imageW, frameH)
        } else {
            imageRect = CGRect(0.0, 0.0, frameW, frameH)
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint)
    }

    private fun drawTopRight(canvas: Canvas, rect: CGRect, bitmap: Bitmap, paint: Paint) {
        val imageW = bitmap.width.toDouble()
        val imageH = bitmap.height.toDouble()
        val frameW = rect.size.width
        val frameH = rect.size.height
        val frameX = rect.origin.x
        val frameY = rect.origin.y
        var imageRect = CGRect(0.0, 0.0, imageW, imageH)
        var frameRect = rect
        if (frameW >= imageW && frameH >= imageH) {
            frameRect = CGRect(frameX + (frameW - imageW), frameY, imageW, imageH)
        } else if (frameW < imageW && frameH >= imageH) {
            imageRect = CGRect(imageW - frameW, 0.0, frameW, imageH)
            frameRect = CGRect(frameX, frameY, frameW, imageH)
        } else if (frameH < imageH && frameW >= imageW) {
            imageRect = CGRect(0.0, 0.0, imageW, frameH)
            frameRect = CGRect(frameX + (frameW - imageW), frameY, imageW, frameH)
        } else {
            imageRect = CGRect(imageW - frameW, 0.0, frameW, frameH)
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint)
    }

    private fun drawBottom(canvas: Canvas, rect: CGRect, bitmap: Bitmap, paint: Paint) {
        val imageW = bitmap.width.toDouble()
        val imageH = bitmap.height.toDouble()
        val frameW = rect.size.width
        val frameH = rect.size.height
        val frameX = rect.origin.x
        val frameY = rect.origin.y
        var imageRect = CGRect(0.0, 0.0, imageW, imageH)
        var frameRect = rect
        if (frameW >= imageW && frameH >= imageH) {
            frameRect = CGRect(frameX + (frameW - imageW) / 2, frameY + (frameH - imageH), imageW, imageH)
        } else if (frameW < imageW && frameH >= imageH) {
            imageRect = CGRect((imageW - frameW) / 2, 0.0, frameW, imageH)
            frameRect = CGRect(frameX, frameY + (frameH - imageH), frameW, imageH)
        } else if (frameH < imageH && frameW >= imageW) {
            imageRect = CGRect(0.0, imageH - frameH, imageW, frameH)
            frameRect = CGRect(frameX + (frameW - imageW) / 2, frameY, imageW, frameH)
        } else {
            imageRect = CGRect((imageW - frameW) / 2, imageH - frameH, frameW, frameH)
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint)
    }

    private fun drawBottomLeft(canvas: Canvas, rect: CGRect, bitmap: Bitmap, paint: Paint) {
        val imageW = bitmap.width.toDouble()
        val imageH = bitmap.height.toDouble()
        val frameW = rect.size.width
        val frameH = rect.size.height
        val frameX = rect.origin.x
        val frameY = rect.origin.y
        var imageRect = CGRect(0.0, 0.0, imageW, imageH)
        var frameRect = rect
        if (frameW >= imageW && frameH >= imageH) {
            frameRect = CGRect(frameX, frameY + (frameH - imageH), imageW, imageH)
        } else if (frameW < imageW && frameH >= imageH) {
            imageRect = CGRect(0.0, 0.0, frameW, imageH)
            frameRect = CGRect(frameX, frameY + (frameH - imageH), frameW, imageH)
        } else if (frameH < imageH && frameW >= imageW) {
            imageRect = CGRect(0.0, imageH - frameH, imageW, frameH)
            frameRect = CGRect(frameX, frameY, imageW, frameH)
        } else {
            imageRect = CGRect(0.0, imageH - frameH, frameW, frameH)
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint)
    }

    private fun drawBottomRight(canvas: Canvas, rect: CGRect, bitmap: Bitmap, paint: Paint) {
        val imageW = bitmap.width.toDouble()
        val imageH = bitmap.height.toDouble()
        val frameW = rect.size.width
        val frameH = rect.size.height
        val frameX = rect.origin.x
        val frameY = rect.origin.y
        var imageRect = CGRect(0.0, 0.0, imageW, imageH)
        var frameRect = rect
        if (frameW >= imageW && frameH >= imageH) {
            frameRect = CGRect(frameX + (frameW - imageW), frameY + (frameH - imageH), imageW, imageH)
        } else if (frameW < imageW && frameH >= imageH) {
            imageRect = CGRect(imageW - frameW, 0.0, frameW, imageH)
            frameRect = CGRect(frameX, frameY + (frameH - imageH), frameW, imageH)
        } else if (frameH < imageH && frameW >= imageW) {
            imageRect = CGRect(0.0, imageH - frameH, imageW, frameH)
            frameRect = CGRect(frameX + (frameW - imageW), frameY, imageW, frameH)
        } else {
            imageRect = CGRect(imageW - frameW, imageH - frameH, frameW, frameH)
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint)
    }

    private fun drawLeft(canvas: Canvas, rect: CGRect, bitmap: Bitmap, paint: Paint) {
        val imageW = bitmap.width.toDouble()
        val imageH = bitmap.height.toDouble()
        val frameW = rect.size.width
        val frameH = rect.size.height
        val frameX = rect.origin.x
        val frameY = rect.origin.y
        var imageRect = CGRect(0.0, 0.0, imageW, imageH)
        var frameRect = rect
        if (frameW >= imageW && frameH >= imageH) {
            frameRect = CGRect(frameX, frameY + (frameH - imageH) / 2, imageW, imageH)
        } else if (frameW < imageW && frameH >= imageH) {
            imageRect = CGRect(0.0, 0.0, frameW, imageH)
            frameRect = CGRect(frameX, frameY + (frameH - imageH) / 2, frameW, imageH)
        } else if (frameH < imageH && frameW >= imageW) {
            imageRect = CGRect(0.0, (imageH - frameH) / 2, imageW, frameH)
            frameRect = CGRect(frameX, frameY, imageW, frameH)
        } else {
            imageRect = CGRect(0.0, (imageH - frameH) / 2, frameW, frameH)
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint)
    }

    private fun drawRight(canvas: Canvas, rect: CGRect, bitmap: Bitmap, paint: Paint) {
        val imageW = bitmap.width.toDouble()
        val imageH = bitmap.height.toDouble()
        val frameW = rect.size.width
        val frameH = rect.size.height
        val frameX = rect.origin.x
        val frameY = rect.origin.y
        var imageRect = CGRect(0.0, 0.0, imageW, imageH)
        var frameRect = rect
        if (frameW >= imageW && frameH >= imageH) {
            frameRect = CGRect(frameX + (frameW - imageW), frameY + (frameH - imageH) / 2, imageW, imageH)
        } else if (frameW < imageW && frameH >= imageH) {
            imageRect = CGRect(imageW - frameW, 0.0, frameW, imageH)
            frameRect = CGRect(frameX, frameY + (frameH - imageH) / 2, frameW, imageH)
        } else if (frameH < imageH && frameW >= imageW) {
            imageRect = CGRect(0.0, (imageH - frameH) / 2, imageW, frameH)
            frameRect = CGRect(frameX + (frameW - imageW), frameY, imageW, frameH)
        } else {
            imageRect = CGRect(imageW - frameW, (imageH - frameH) / 2, frameW, frameH)
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint)
    }

}
