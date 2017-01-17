package com.yy.codex.uikit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * Created by adi on 17/1/17.
 */

public class CALayerPainter {

    private static Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // drawAllAsBitmap

    public static void drawLayerTree(CALayer layer, Canvas canvas){
        Bitmap srcBitmap = createBitmapWithLayerTree(layer);
        Bitmap maskBitmap = createBitmapWithMaskLayer(layer);

        sPaint.reset();
        if (layer.getTransforms() != null & layer.getTransforms().length > 0){
            Matrix matrix = createMatrix(layer);
            canvas.drawBitmap(srcBitmap, matrix, sPaint);
            if (layer.getClipToBounds()){
                sPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                canvas.drawBitmap(maskBitmap, matrix, sPaint);
                sPaint.setXfermode(null);
            }
        }
        else {
            canvas.drawBitmap(srcBitmap, 0, 0, sPaint);
            sPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(maskBitmap, 0, 0, sPaint);
            sPaint.setXfermode(null);
        }
    }

    public static void drawCurrentLayer(CALayer layer, Canvas canvas){
        if (layer.isHidden()){
            return;
        }
        CGRect frameRaw = layer.getFrame();
        Bitmap bitmap = layer.getBitmap();
        int bitmapGravity = layer.getBitmapGravity();
        float scaledDensity = (float) UIScreen.mainScreen.scale();
        CGPoint origin = CALayer.calcOriginInSuperCoordinate(layer);
        CGRect frame = new CGRect(frameRaw.getX() * scaledDensity, frameRaw.getY() * scaledDensity, frameRaw.getWidth() * scaledDensity, frameRaw.getHeight() * scaledDensity);
        float borderWidth = (float) layer.getBorderWidth() * scaledDensity;
        float cornerRadius = (float) layer.getCornerRadius() * scaledDensity;
        float halfBorderW = borderWidth / 2.0f;

        if (layer.getCornerRadius() > 0){
            drawRoundRect(canvas, frame.shrinkToRectF(halfBorderW, origin), layer.getBackgroundColor(), cornerRadius);
            if (bitmap != null){
                CGRect maskFrame = new CGRect(origin.x, origin.y, frame.size.width, frame.size.height);
                drawRoundRectBitmap(canvas, maskFrame, bitmap, bitmapGravity, layer.getBitmapColor(), cornerRadius);
            }
            if (borderWidth > 0){
                drawRoundRectBorder(canvas, frame.shrinkToRectF(halfBorderW, origin), borderWidth, layer.getBorderColor(), cornerRadius);
            }
        }
        else {
            drawRect(canvas, frame.toRectF(origin), layer.getBackgroundColor());
            if (bitmap != null){
                CGRect bitmapFrame = new CGRect(origin.x, origin.y, frame.size.width, frame.size.height);
                drawBitmap(canvas, bitmapFrame, bitmap, bitmapGravity, layer.getBitmapColor());
            }
            if (borderWidth > 0){
                drawBorder(canvas, frame.shrinkToRectF(halfBorderW, origin), borderWidth, layer.getBorderColor());
            }
        }

        if (layer.getMask() != null){
            // @TODO
        }
    }

    /* support method */

    private static void drawRect(Canvas canvas, RectF rectF, UIColor backgroundColor){
        sPaint.reset();
        sPaint.setColor(backgroundColor.toInt());
        canvas.drawRect(rectF, sPaint);
    }

    private static void drawRoundRect(Canvas canvas, RectF rectF, UIColor backgroundColor, float cornerRadius){
        sPaint.reset();
        sPaint.setColor(backgroundColor.toInt());
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, sPaint);
    }

    private static void drawBitmap(Canvas canvas, CGRect frame, Bitmap bitmap, int bitmapGravity, UIColor bitmapColor){
        sPaint.reset();
        if (bitmapColor != null) {
            float[] colorTransform = {
                    0, (float)bitmapColor.r, 0, 0, 0,
                    0, 0, (float)bitmapColor.g, 0, 0,
                    0, 0, 0, (float)bitmapColor.b, 0,
                    0, 0, 0, (float)bitmapColor.a, 0};
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.set(colorTransform);
            ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
            sPaint.setColorFilter(colorFilter);
        }
        CALayerBitmapPainter.drawBitmap(canvas, frame, bitmap, bitmapGravity, sPaint);
        sPaint.setColorFilter(null);
    }

    private static void drawRoundRectBitmap(Canvas canvas, CGRect maskFrame, Bitmap bitmap, int bitmapGravity, UIColor bitmapColor, float cornerRadius){
        // @TODO apply bitmapColor
        sPaint.reset();
        Paint mixPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Bitmap maskBitmap = createRadiusMask(maskFrame, cornerRadius);
        canvas.drawBitmap(maskBitmap, 0, 0, mixPaint);
        mixPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        CALayerBitmapPainter.drawBitmap(canvas, maskFrame, bitmap, bitmapGravity, mixPaint);
    }

    private static void drawBorder(Canvas canvas, RectF rectF, float borderWidth, UIColor borderColor){
        sPaint.reset();
        sPaint.setStyle(Paint.Style.STROKE);
        sPaint.setStrokeWidth(borderWidth);
        sPaint.setColor(borderColor.toInt());
        canvas.drawRect(rectF, sPaint);
    }

    private static void drawRoundRectBorder(Canvas canvas, RectF rectF, float borderWidth, UIColor borderColor, float cornerRadius){
        sPaint.reset();
        sPaint.setStyle(Paint.Style.STROKE);
        sPaint.setStrokeWidth(borderWidth);
        sPaint.setColor(borderColor.toInt());
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, sPaint);
    }

    private static Bitmap createBitmapWithLayerTree(CALayer layer){
        CGPoint origin = CALayer.calcOriginInSuperCoordinate(layer);
        float scaledDensity = (float) UIScreen.mainScreen.scale();
        int bitmapW = (int) (layer.getFrame().size.width * scaledDensity  + origin.x);
        int bitmapH = (int) (layer.getFrame().size.height * scaledDensity + origin.y);
        Bitmap bitmap = Bitmap.createBitmap(bitmapW, bitmapH, Bitmap.Config.ARGB_8888);
        layer.drawLayerTreeInCanvas(new Canvas(bitmap));
        return bitmap;
    }

    private static Bitmap createBitmapWithMaskLayer(CALayer layer){
        CGPoint origin = CALayer.calcOriginInSuperCoordinate(layer);
        float scaledDensity = (float) UIScreen.mainScreen.scale();
        float cornerRaidus = (float) layer.getCornerRadius() * scaledDensity;
        int bitmapW = (int) (layer.getFrame().size.width * scaledDensity  + origin.x);
        int bitmapH = (int) (layer.getFrame().size.height * scaledDensity + origin.y);
        Bitmap bitmap = Bitmap.createBitmap(bitmapW, bitmapH, Bitmap.Config.ARGB_8888);
        RectF rectF = new RectF((float)origin.x, (float) origin.y, bitmapW, bitmapH);
        new Canvas(bitmap).drawRoundRect(rectF, cornerRaidus, cornerRaidus, new Paint(Paint.ANTI_ALIAS_FLAG));
        return bitmap;
    }

    private static Bitmap createRadiusMask(@NonNull CGRect rect, double radius){
        Bitmap maskBitmap = Bitmap.createBitmap((int)(rect.size.width+rect.origin.x), (int)(rect.size.height+rect.origin.y), Bitmap.Config.ARGB_8888);
        Canvas maskCanvas = new Canvas(maskBitmap);
        Paint maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskCanvas.drawRoundRect(rect.toRectF(), (float) radius, (float) radius, maskPaint);
        return maskBitmap;
    }

    private static Matrix createMatrix(CALayer layer){
        float scaledDensity = (float) UIScreen.mainScreen.scale();
        CGTransform[] transforms = layer.getTransforms();
        Matrix matrix = new Matrix();
        if (transforms == null || transforms.length == 0){
            return matrix;
        }
        RectF rectF = layer.getFrame().toRectF();
        for (CGTransform transform : transforms){
            if (!transform.enable){
                continue;
            }
            if (transform instanceof CGTransformRotation){
                matrix.preRotate((float) ((CGTransformRotation) transform).angle, rectF.centerX() * (float) scaledDensity, rectF.centerY() * (float) scaledDensity);
            }
            else if (transform instanceof CGTransformTranslation){
                CGTransformTranslation translation = (CGTransformTranslation) transform;
                matrix.postTranslate((float) translation.tx, (float) translation.ty);
            }
            else if (transform instanceof CGTransformScale){
                CGTransformScale scale = (CGTransformScale)transform;
                matrix.postScale((float) scale.sx, (float) scale.sy, rectF.centerX() * (float) scaledDensity, rectF.centerY() * (float) scaledDensity);
            }
            else if (transform instanceof CGTransformMatrix){
                // @TODO
            }
        }
        return matrix;
    }

}
