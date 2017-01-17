package com.yy.codex.uikit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.NonNull;

/**
 * Created by adi on 17/1/17.
 */

public class CALayerPainter {

    public static void draw(CALayer layer, Canvas canvas){

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
        float shadowRadius = (float) layer.getShadowRadius() * scaledDensity;
        float shadowX = (float) layer.getShadowX() * scaledDensity;
        float shadowY = (float) layer.getShadowY() * scaledDensity;

        // background
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(layer.getBackgroundColor().toInt());

        // background bitmap border
        if (layer.getCornerRadius() > 0){
            if (layer.getShadowRadius() > 0){
                paint.setShadowLayer(shadowRadius, shadowX, shadowY, layer.getShadowColor().toInt());
            }
            canvas.drawRoundRect(frame.shrinkToRectF(halfBorderW, origin), cornerRadius, cornerRadius, paint);

            if (bitmap != null){
                Paint mixPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                CGRect maskFrame = new CGRect(origin.x, origin.y, frame.size.width, frame.size.height);
                Bitmap maskBitmap = createRadiusMask(maskFrame, cornerRadius);
                canvas.drawBitmap(maskBitmap, 0, 0, mixPaint);
                mixPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                CALayerBitmapPainter.drawBitmap(canvas, maskFrame, bitmap, bitmapGravity, mixPaint);
            }

            if (borderWidth > 0){
                paint.reset();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(borderWidth);
                paint.setColor(layer.getBorderColor().toInt());
                canvas.drawRoundRect(frame.shrinkToRectF(halfBorderW, origin), cornerRadius, cornerRadius, paint);
            }
        }
        else {
            if (shadowRadius > 0){
                paint.setShadowLayer(shadowRadius, shadowX, shadowY, layer.getShadowColor().toInt());
            }
            canvas.drawRect(frame.toRectF(origin), paint);

            if (bitmap != null){
                paint.reset();
                CGRect bitmapFrame = new CGRect(origin.x, origin.y, frame.size.width, frame.size.height);
                if (layer.getBitmapColor() != null) {
                    float[] colorTransform = {
                            0, (float)layer.getBitmapColor().r, 0, 0, 0,
                            0, 0, (float)layer.getBitmapColor().g, 0, 0,
                            0, 0, 0, (float)layer.getBitmapColor().b, 0,
                            0, 0, 0, (float)layer.getBitmapColor().a, 0};
                    ColorMatrix colorMatrix = new ColorMatrix();
                    colorMatrix.set(colorTransform);
                    ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
                    paint.setColorFilter(colorFilter);
                }
                CALayerBitmapPainter.drawBitmap(canvas, bitmapFrame, bitmap, bitmapGravity, paint);
                paint.setColorFilter(null);
            }
            if (borderWidth > 0){
                paint.reset();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(borderWidth);
                paint.setColor(layer.getBitmapColor().toInt());
                canvas.drawRect(frame.shrinkToRectF(halfBorderW, origin), paint);
            }
        }

        if (layer.getMask() != null){
            // @TODO
        }
    }

    /* support method */

    private static Bitmap createRadiusMask(@NonNull CGRect rect, double radius){
        Bitmap maskBitmap = Bitmap.createBitmap((int)(rect.size.width+rect.origin.x), (int)(rect.size.height+rect.origin.y), Bitmap.Config.ARGB_8888);
        Canvas maskCanvas = new Canvas(maskBitmap);
        Paint maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskCanvas.drawRoundRect(rect.toRectF(), (float) radius, (float) radius, maskPaint);
        return maskBitmap;
    }


}
