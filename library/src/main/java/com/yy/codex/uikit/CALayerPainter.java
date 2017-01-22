package com.yy.codex.uikit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * Created by adi on 17/1/17.
 */

public class CALayerPainter {

    private static Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static void drawLayerTree(CALayer layer, Canvas canvas){
        Bitmap srcBitmap = createBitmapWithLayerTree(layer);
        Bitmap maskBitmap = createBitmapWithMaskLayer(layer);
        Bitmap resultBitmap = createBitmapThatApplyMask(layer, srcBitmap, maskBitmap);
        sPaint.reset();
        canvas.drawBitmap(resultBitmap, 0, 0, sPaint);
    }

    public static void drawCurrentLayer(CALayer layer, Canvas canvas){
        if (layer.isHidden()){
            return;
        }
        if (layer.getCornerRadius() > 0){
            drawRoundRect(canvas, layer);
            if (layer.getBitmap() != null){
                drawRoundRectBitmap(canvas, layer);
            }
            if (layer.getBorderWidth() > 0){
                drawRoundRectBorder(canvas, layer);
            }
        }
        else {
            drawRect(canvas, layer);
            if (layer.getBitmap() != null){
                drawRectBitmap(canvas, layer);
            }
            if (layer.getBorderWidth() > 0){
                drawRectBorder(canvas, layer);
            }
        }

        if (layer.getMask() != null){
            // @TODO
        }
    }

    /* support method */

    private static void drawRoundRect(Canvas canvas, CALayer layer){
        CGRect frameRaw = layer.getFrame();
        float scaledDensity = (float) UIScreen.mainScreen.scale();
        CGPoint origin = CALayer.calcOriginInSuperCoordinate(layer);
        CGRect frame = new CGRect(frameRaw.getX() * scaledDensity, frameRaw.getY() * scaledDensity, frameRaw.getWidth() * scaledDensity, frameRaw.getHeight() * scaledDensity);
        float borderWidth = (float) layer.getBorderWidth() * scaledDensity;
        float cornerRadius = (float) layer.getCornerRadius() * scaledDensity;
        float halfBorderW = borderWidth / 2.0f;

        sPaint.reset();
        sPaint.setAntiAlias(true);
        sPaint.setColor(layer.getBackgroundColor().toInt());
        RectF rectFCopyed = frame.shrinkToRectF(halfBorderW, origin);
        if (layer.getShadowRadius() > 0) {
            float shadowRadius = (float) layer.getShadowRadius() * scaledDensity;
            float shadowX = (float) layer.getShadowX() * scaledDensity;
            float shadowY = (float) layer.getShadowY() * scaledDensity;
            sPaint.setShadowLayer(shadowRadius, shadowX, shadowY, layer.getShadowColor().toInt());
            rectFCopyed = new RectF(rectFCopyed.left, rectFCopyed.top, rectFCopyed.right - shadowX, rectFCopyed.bottom - shadowY);
        }
        canvas.drawRoundRect(rectFCopyed, cornerRadius, cornerRadius, sPaint);
    }

    private static void drawRoundRectBitmap(Canvas canvas, CALayer layer){
        CGRect frameRaw = layer.getFrame();
        Bitmap bitmap = layer.getBitmap();
        CALayer.BitmapGravity bitmapGravity = layer.getBitmapGravity();
        float scaledDensity = (float) UIScreen.mainScreen.scale();
        CGPoint origin = CALayer.calcOriginInSuperCoordinate(layer);
        CGRect frame = new CGRect(frameRaw.getX() * scaledDensity, frameRaw.getY() * scaledDensity, frameRaw.getWidth() * scaledDensity, frameRaw.getHeight() * scaledDensity);
        float cornerRadius = (float) layer.getCornerRadius() * scaledDensity;
        UIColor bitmapColor = layer.getBitmapColor();

        CGRect maskFrame = new CGRect(origin.x, origin.y, frame.size.width, frame.size.height);
        Bitmap resultBitmap = createEmptyBitmap(maskFrame);
        Canvas resultCanvas = new Canvas(resultBitmap);
        Paint mixPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        resultCanvas.drawBitmap(createRadiusMask(maskFrame, cornerRadius), 0, 0, mixPaint);
        mixPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        if (bitmapColor != null) {
            mixPaint.setColorFilter(new PorterDuffColorFilter(bitmapColor.toInt(), PorterDuff.Mode.SRC_IN));
        }
        CALayerBitmapPainter.drawBitmap(resultCanvas, maskFrame, bitmap, bitmapGravity, mixPaint);
        sPaint.reset();
        if (bitmapColor != null) {
            sPaint.setColorFilter(new PorterDuffColorFilter(bitmapColor.toInt(), PorterDuff.Mode.SRC_IN));
        }
        canvas.drawBitmap(resultBitmap, 0, 0, sPaint);
    }

    private static void drawRoundRectBorder(Canvas canvas, CALayer layer){
        CGRect frameRaw = layer.getFrame();
        float scaledDensity = (float) UIScreen.mainScreen.scale();
        CGPoint origin = CALayer.calcOriginInSuperCoordinate(layer);
        CGRect frame = new CGRect(frameRaw.getX() * scaledDensity, frameRaw.getY() * scaledDensity, frameRaw.getWidth() * scaledDensity, frameRaw.getHeight() * scaledDensity);
        float borderWidth = (float) layer.getBorderWidth() * scaledDensity;
        float cornerRadius = (float) layer.getCornerRadius() * scaledDensity;
        float halfBorderW = borderWidth / 2.0f;

        sPaint.reset();
        sPaint.setAntiAlias(true);
        sPaint.setStyle(Paint.Style.STROKE);
        sPaint.setStrokeWidth(borderWidth);
        sPaint.setColor(layer.getBorderColor().toInt());
        RectF rectFCopyed = frame.shrinkToRectF(halfBorderW, origin);
        if (layer.getShadowRadius() > 0){
            float shadowX = (float) layer.getShadowX() * scaledDensity;
            float shadowY = (float) layer.getShadowY() * scaledDensity;
            rectFCopyed = new RectF(rectFCopyed.left, rectFCopyed.top, rectFCopyed.right - shadowX, rectFCopyed.bottom - shadowY);
        }
        canvas.drawRoundRect(rectFCopyed, cornerRadius, cornerRadius, sPaint);
    }

    private static void drawRect(Canvas canvas, CALayer layer){
        CGRect frameRaw = layer.getFrame();
        float scaledDensity = (float) UIScreen.mainScreen.scale();
        CGPoint origin = CALayer.calcOriginInSuperCoordinate(layer);
        CGRect frame = new CGRect(frameRaw.getX() * scaledDensity, frameRaw.getY() * scaledDensity, frameRaw.getWidth() * scaledDensity, frameRaw.getHeight() * scaledDensity);


        sPaint.reset();
        sPaint.setColor(layer.getBackgroundColor().toInt());
        RectF rectFCopyed = frame.toRectF(origin);
        if (layer.getShadowRadius() > 0){
            float shadowRadius = (float) layer.getShadowRadius() * scaledDensity;
            float shadowX = (float) layer.getShadowX() * scaledDensity;
            float shadowY = (float) layer.getShadowY() * scaledDensity;
            sPaint.setShadowLayer(shadowRadius, shadowX, shadowY, layer.getShadowColor().toInt());
            rectFCopyed = new RectF(rectFCopyed.left, rectFCopyed.top, rectFCopyed.right - shadowX, rectFCopyed.bottom - shadowY);
        }
        canvas.drawRect(rectFCopyed, sPaint);
    }

    private static void drawRectBitmap(Canvas canvas, CALayer layer){
        CGRect frameRaw = layer.getFrame();
        Bitmap bitmap = layer.getBitmap();
        float scaledDensity = (float) UIScreen.mainScreen.scale();
        CGPoint origin = CALayer.calcOriginInSuperCoordinate(layer);
        CGRect frame = new CGRect(frameRaw.getX() * scaledDensity, frameRaw.getY() * scaledDensity, frameRaw.getWidth() * scaledDensity, frameRaw.getHeight() * scaledDensity);
        UIColor bitmapColor = layer.getBitmapColor();

        sPaint.reset();
        if (bitmapColor != null) {
            sPaint.setColorFilter(new PorterDuffColorFilter(bitmapColor.toInt(), PorterDuff.Mode.SRC_IN));
        }
        CGRect bitmapFrame = new CGRect(origin.x, origin.y, frame.size.width, frame.size.height);
        CALayerBitmapPainter.drawBitmap(canvas, bitmapFrame, bitmap, layer.getBitmapGravity(), sPaint);
        sPaint.setColorFilter(null);
    }

    private static void drawRectBorder(Canvas canvas, CALayer layer){
        CGRect frameRaw = layer.getFrame();
        float scaledDensity = (float) UIScreen.mainScreen.scale();
        CGPoint origin = CALayer.calcOriginInSuperCoordinate(layer);
        CGRect frame = new CGRect(frameRaw.getX() * scaledDensity, frameRaw.getY() * scaledDensity, frameRaw.getWidth() * scaledDensity, frameRaw.getHeight() * scaledDensity);
        float borderWidth = (float) layer.getBorderWidth() * scaledDensity;
        float halfBorderW = borderWidth / 2.0f;

        sPaint.reset();
        sPaint.setStyle(Paint.Style.STROKE);
        sPaint.setStrokeWidth(borderWidth);
        sPaint.setColor(layer.getBorderColor().toInt());
        RectF rectFCopyed = frame.shrinkToRectF(halfBorderW, origin);
        if (layer.getShadowRadius() > 0){
            float shadowX = (float) layer.getShadowX() * scaledDensity;
            float shadowY = (float) layer.getShadowY() * scaledDensity;
            rectFCopyed = new RectF(rectFCopyed.left, rectFCopyed.top, rectFCopyed.right - shadowX, rectFCopyed.bottom - shadowY);
        }
        canvas.drawRect(rectFCopyed, sPaint);
    }

    private static Bitmap createEmptyBitmap(CGRect rect){
        int bitmapW = (int) (rect.size.width + rect.origin.x);
        int bitmapH = (int) (rect.size.height + rect.origin.y);
        Bitmap bitmap = Bitmap.createBitmap(bitmapW, bitmapH, Bitmap.Config.ARGB_8888);
        return bitmap;
    }

    private static Bitmap createEmptyBitmap(CALayer layer){
        CGPoint origin = CALayer.calcOriginInSuperCoordinate(layer);
        float scaledDensity = (float) UIScreen.mainScreen.scale();
        int bitmapW = (int) (layer.getFrame().size.width * scaledDensity  + origin.x);
        int bitmapH = (int) (layer.getFrame().size.height * scaledDensity + origin.y);
        Bitmap bitmap = Bitmap.createBitmap(bitmapW, bitmapH, Bitmap.Config.ARGB_8888);
        return bitmap;
    }

    private static Bitmap createBitmapWithLayerTree(CALayer layer){
        Bitmap bitmap = createEmptyBitmap(layer);
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

    private static Bitmap createBitmapThatApplyMask(CALayer layer, Bitmap srcBitmap, Bitmap maskBitmap){
        sPaint.reset();
        Bitmap bitmapMixed = createEmptyBitmap(layer);
        Canvas canvasMixed = new Canvas(bitmapMixed);
        if (layer.getTransforms() != null && layer.getTransforms().length > 0){
            Matrix matrix = createMatrix(layer);
            if (layer.getClipToBounds()){
                canvasMixed.drawBitmap(maskBitmap, matrix, sPaint);
                sPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            }
            canvasMixed.drawBitmap(srcBitmap, matrix, sPaint);
            sPaint.setXfermode(null);
        }
        else {
            canvasMixed.drawBitmap(maskBitmap, 0, 0, sPaint);
            sPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvasMixed.drawBitmap(srcBitmap, 0, 0, sPaint);
            sPaint.setXfermode(null);
        }
        return bitmapMixed;
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
