package com.yy.codex.uikit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

/**
 * Created by adi on 17/1/16.
 */

public class CALayerBitmapPainter {

    /* imageGravity const */

    public static final int GRAVITY_SCALE_ASPECT_FIT = 0x00;
    public static final int GRAVITY_SCALE_ASPECT_FILL = 0x01;
    public static final int GRAVITY_SCALE_TO_FILL = 0x02;
    public static final int GRAVITY_TOP_LEFT = 0x03;
    public static final int GRAVITY_TOP = 0x04;
    public static final int GRAVITY_TOP_RIGHT = 0x05;
    public static final int GRAVITY_LEFT = 0x06;
    public static final int GRAVITY_CENTER = 0x07;
    public static final int GRAVITY_RIGHT = 0x08;
    public static final int GRAVITY_BOTTOM_LEFT = 0x09;
    public static final int GRAVITY_BOTTOM = 0x0a;
    public static final int GRAVITY_BOTTOM_RIGHT = 0x0b;

    public static void drawBitmap(@NonNull Canvas canvas, @NonNull CGRect rect, @NonNull Bitmap bitmap, int bitmapGravity, Paint paint){
        switch (bitmapGravity){
            case GRAVITY_SCALE_TO_FILL:
                drawScaleToFill(canvas, rect, bitmap, paint);
                break;
            case GRAVITY_SCALE_ASPECT_FIT:
                drawScaleAsceptFit(canvas, rect, bitmap, paint);
                break;
            case GRAVITY_SCALE_ASPECT_FILL:
                drawScaleAsceptFill(canvas, rect, bitmap, paint);
                break;
            case GRAVITY_CENTER:
                drawCenter(canvas, rect, bitmap, paint);
                break;
            case GRAVITY_TOP:
                drawTop(canvas, rect, bitmap, paint);
                break;
            case GRAVITY_TOP_LEFT:
                drawTopLeft(canvas, rect, bitmap, paint);
                break;
            case GRAVITY_TOP_RIGHT:
                drawTopRight(canvas, rect, bitmap, paint);
                break;
            case GRAVITY_BOTTOM:
                drawBottom(canvas, rect, bitmap, paint);
                break;
            case GRAVITY_BOTTOM_LEFT:
                drawBottomLeft(canvas, rect, bitmap, paint);
                break;
            case GRAVITY_BOTTOM_RIGHT:
                drawBottomRight(canvas, rect, bitmap, paint);
                break;
            case GRAVITY_LEFT:
                drawLeft(canvas, rect, bitmap, paint);
                break;
            case GRAVITY_RIGHT:
                drawRight(canvas, rect, bitmap, paint);
                break;
        }
    }

    /* draw details */

    private static void drawScaleToFill(Canvas canvas, CGRect rect, Bitmap bitmap, Paint paint){
        CGRect imageRect = new CGRect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        CGRect frameRect = rect;
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint);
    }

    private static void drawScaleAsceptFit(Canvas canvas, CGRect rect, Bitmap bitmap, Paint paint){
        double imageW = bitmap.getWidth();
        double imageH = bitmap.getHeight();
        double imageRatio = imageW / imageH;
        double frameW = rect.size.width;
        double frameH = rect.size.height;
        double frameRatio = frameW / frameH;
        double frameX = rect.origin.x;
        double frameY = rect.origin.y;
        CGRect imageRect = new CGRect(0, 0, imageW, imageH), frameRect;
        if (frameRatio > imageRatio){
            double scaledFrameW = frameH * imageRatio;
            frameRect = new CGRect(frameX + (frameW - scaledFrameW) / 2, frameY, scaledFrameW, frameH);
        }
        else {
            double scaledH = frameW / imageRatio;
            frameRect = new CGRect(frameX, frameY + (frameH - scaledH)/2, frameW, scaledH);
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint);
    }

    private static void drawScaleAsceptFill(Canvas canvas, CGRect rect, Bitmap bitmap, Paint paint){
        double imageW = bitmap.getWidth();
        double imageH = bitmap.getHeight();
        double imageRatio = imageW / imageH;
        double frameW = rect.size.width;
        double frameH = rect.size.height;
        double frameRatio = frameW / frameH;
        CGRect imageRect, frameRect = rect;
        if (frameRatio > imageRatio){
            double clipedImageH = imageW / frameRatio;
            imageRect = new CGRect(0, (imageH - clipedImageH)/2, imageW, clipedImageH);
        }
        else {
            double clipedImageW = imageH * frameRatio;
            imageRect = new CGRect((imageW - clipedImageW) / 2, 0, clipedImageW, imageH);
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint);
    }

    private static void drawCenter(Canvas canvas, CGRect rect, Bitmap bitmap, Paint paint){
        double imageW = bitmap.getWidth();
        double imageH = bitmap.getHeight();
        double frameW = rect.size.width;
        double frameH = rect.size.height;
        double frameX = rect.origin.x;
        double frameY = rect.origin.y;
        CGRect imageRect = new CGRect(0, 0, imageW, imageH);
        CGRect frameRect = rect;
        if (frameW >= imageW && frameH >= imageH){
            frameRect = new CGRect(frameX + (frameW - imageW) / 2, frameY + (frameH - imageH) / 2, imageW, imageH);
        }
        else if (frameW < imageW && frameH >= imageH ){
            imageRect = new CGRect((imageW - frameW)/2, 0, frameW, imageH);
            frameRect = new CGRect(frameX, frameY+(frameH - imageH)/2, frameW, imageH);
        }
        else if (frameH < imageH && frameW >= imageW) {
            imageRect = new CGRect(0, (imageH - frameH)/2, imageW, frameH);
            frameRect = new CGRect(frameX+(frameW - imageW)/2, frameY, imageW, frameH);
        }
        else {
            imageRect = new CGRect((imageW - frameW)/2, (imageH - frameH)/2, frameW, frameH);
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint);
    }

    private static void drawTop(Canvas canvas, CGRect rect, Bitmap bitmap, Paint paint){
        double imageW = bitmap.getWidth();
        double imageH = bitmap.getHeight();
        double frameW = rect.size.width;
        double frameH = rect.size.height;
        double frameX = rect.origin.x;
        double frameY = rect.origin.y;
        CGRect imageRect = new CGRect(0, 0, imageW, imageH);
        CGRect frameRect = rect;
        if (frameW >= imageW && frameH >= imageH){
            frameRect = new CGRect(frameX+(frameW-imageW)/2, frameY, imageW, imageH);
        }
        else if (frameW < imageW && frameH >= imageH ){
            imageRect = new CGRect((imageW-frameW)/2, 0, frameW, imageH);
            frameRect = new CGRect(frameX, frameY, frameW, imageH);
        }
        else if (frameH < imageH && frameW >= imageW) {
            imageRect = new CGRect(0, 0, imageW, frameH);
            frameRect = new CGRect(frameX+(imageW-frameW)/2, frameY, imageW, frameH);
        }
        else {
            imageRect = new CGRect((imageW-frameW)/2, 0, frameW, frameH);
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint);
    }

    private static void drawTopLeft(Canvas canvas, CGRect rect, Bitmap bitmap, Paint paint){
        double imageW = bitmap.getWidth();
        double imageH = bitmap.getHeight();
        double frameW = rect.size.width;
        double frameH = rect.size.height;
        double frameX = rect.origin.x;
        double frameY = rect.origin.y;
        CGRect imageRect = new CGRect(0, 0, imageW, imageH);
        CGRect frameRect = rect;
        if (frameW >= imageW && frameH >= imageH){
            frameRect = new CGRect(frameX, frameY, imageW, imageH);
        }
        else if (frameW < imageW && frameH >= imageH ){
            imageRect = new CGRect(0, 0, frameW, imageH);
            frameRect = new CGRect(frameX, frameY, frameW, imageH);
        }
        else if (frameH < imageH && frameW >= imageW) {
            imageRect = new CGRect(0, 0, imageW, frameH);
            frameRect = new CGRect(frameX, frameY, imageW, frameH);
        }
        else {
            imageRect = new CGRect(0, 0, frameW, frameH);
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint);
    }

    private static void drawTopRight(Canvas canvas, CGRect rect, Bitmap bitmap, Paint paint){
        double imageW = bitmap.getWidth();
        double imageH = bitmap.getHeight();
        double frameW = rect.size.width;
        double frameH = rect.size.height;
        double frameX = rect.origin.x;
        double frameY = rect.origin.y;
        CGRect imageRect = new CGRect(0, 0, imageW, imageH);
        CGRect frameRect = rect;
        if (frameW >= imageW && frameH >= imageH){
            frameRect = new CGRect(frameX+(frameW-imageW), frameY, imageW, imageH);
        }
        else if (frameW < imageW && frameH >= imageH ){
            imageRect = new CGRect((imageW-frameW), 0, frameW, imageH);
            frameRect = new CGRect(frameX, frameY, frameW, imageH);
        }
        else if (frameH < imageH && frameW >= imageW) {
            imageRect = new CGRect(0, 0, imageW, frameH);
            frameRect = new CGRect(frameX+(frameW-imageW), frameY, imageW, frameH);
        }
        else {
            imageRect = new CGRect((imageW-frameW), 0, frameW, frameH);
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint);
    }

    private static void drawBottom(Canvas canvas, CGRect rect, Bitmap bitmap, Paint paint){
        double imageW = bitmap.getWidth();
        double imageH = bitmap.getHeight();
        double frameW = rect.size.width;
        double frameH = rect.size.height;
        double frameX = rect.origin.x;
        double frameY = rect.origin.y;
        CGRect imageRect = new CGRect(0, 0, imageW, imageH);
        CGRect frameRect = rect;
        if (frameW >= imageW && frameH >= imageH){
            frameRect = new CGRect(frameX+(frameW-imageW)/2, frameY+(frameH-imageH), imageW, imageH);
        }
        else if (frameW < imageW && frameH >= imageH ){
            imageRect = new CGRect((imageW-frameW)/2, 0, frameW, imageH);
            frameRect = new CGRect(frameX, frameY+(frameH-imageH), frameW, imageH);
        }
        else if (frameH < imageH && frameW >= imageW) {
            imageRect = new CGRect(0, (imageH-frameH), imageW, frameH);
            frameRect = new CGRect(frameX+(frameW-imageW)/2, frameY, imageW, frameH);
        }
        else {
            imageRect = new CGRect((imageW-frameW)/2, (imageH-frameH), frameW, frameH);
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint);
    }

    private static void drawBottomLeft(Canvas canvas, CGRect rect, Bitmap bitmap, Paint paint){
        double imageW = bitmap.getWidth();
        double imageH = bitmap.getHeight();
        double frameW = rect.size.width;
        double frameH = rect.size.height;
        double frameX = rect.origin.x;
        double frameY = rect.origin.y;
        CGRect imageRect = new CGRect(0, 0, imageW, imageH);
        CGRect frameRect = rect;
        if (frameW >= imageW && frameH >= imageH){
            frameRect = new CGRect(frameX, frameY+(frameH-imageH), imageW, imageH);
        }
        else if (frameW < imageW && frameH >= imageH ){
            imageRect = new CGRect(0, 0, frameW, imageH);
            frameRect = new CGRect(frameX, frameY+(frameH-imageH), frameW, imageH);
        }
        else if (frameH < imageH && frameW >= imageW) {
            imageRect = new CGRect(0, (imageH-frameH), imageW, frameH);
            frameRect = new CGRect(frameX, frameY, imageW, frameH);
        }
        else {
            imageRect = new CGRect(0, (imageH-frameH), frameW, frameH);
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint);
    }

    private static void drawBottomRight(Canvas canvas, CGRect rect, Bitmap bitmap, Paint paint){
        double imageW = bitmap.getWidth();
        double imageH = bitmap.getHeight();
        double frameW = rect.size.width;
        double frameH = rect.size.height;
        double frameX = rect.origin.x;
        double frameY = rect.origin.y;
        CGRect imageRect = new CGRect(0, 0, imageW, imageH);
        CGRect frameRect = rect;
        if (frameW >= imageW && frameH >= imageH){
            frameRect = new CGRect(frameX+(frameW-imageW), frameY+(frameH-imageH), imageW, imageH);
        }
        else if (frameW < imageW && frameH >= imageH ){
            imageRect = new CGRect((imageW-frameW), 0, frameW, imageH);
            frameRect = new CGRect(frameX, frameY+(frameH-imageH), frameW, imageH);
        }
        else if (frameH < imageH && frameW >= imageW) {
            imageRect = new CGRect(0, (imageH-frameH), imageW, frameH);
            frameRect = new CGRect(frameX+(frameW-imageW), frameY, imageW, frameH);
        }
        else {
            imageRect = new CGRect((imageW-frameW), (imageH-frameH), frameW, frameH);
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint);
    }

    private static void drawLeft(Canvas canvas, CGRect rect, Bitmap bitmap, Paint paint){
        double imageW = bitmap.getWidth();
        double imageH = bitmap.getHeight();
        double frameW = rect.size.width;
        double frameH = rect.size.height;
        double frameX = rect.origin.x;
        double frameY = rect.origin.y;
        CGRect imageRect = new CGRect(0, 0, imageW, imageH);
        CGRect frameRect = rect;
        if (frameW >= imageW && frameH >= imageH){
            frameRect = new CGRect(frameX, frameY+(frameH-imageH)/2, imageW, imageH);
        }
        else if (frameW < imageW && frameH >= imageH ){
            imageRect = new CGRect(0, 0, frameW, imageH);
            frameRect = new CGRect(frameX, frameY+(frameH-imageH)/2, frameW, imageH);
        }
        else if (frameH < imageH && frameW >= imageW) {
            imageRect = new CGRect(0, (imageH-frameH)/2, imageW, frameH);
            frameRect = new CGRect(frameX, frameY, imageW, frameH);
        }
        else {
            imageRect = new CGRect(0, (imageH-frameH)/2, frameW, frameH);
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint);
    }

    private static void drawRight(Canvas canvas, CGRect rect, Bitmap bitmap, Paint paint){
        double imageW = bitmap.getWidth();
        double imageH = bitmap.getHeight();
        double frameW = rect.size.width;
        double frameH = rect.size.height;
        double frameX = rect.origin.x;
        double frameY = rect.origin.y;
        CGRect imageRect = new CGRect(0, 0, imageW, imageH);
        CGRect frameRect = rect;
        if (frameW >= imageW && frameH >= imageH){
            frameRect = new CGRect(frameX+(frameW-imageW), frameY+(frameH-imageH)/2, imageW, imageH);
        }
        else if (frameW < imageW && frameH >= imageH ){
            imageRect = new CGRect((imageW-frameW), 0, frameW, imageH);
            frameRect = new CGRect(frameX, frameY+(frameH-imageH)/2, frameW, imageH);
        }
        else if (frameH < imageH && frameW >= imageW) {
            imageRect = new CGRect(0, (imageH-frameH)/2, imageW, frameH);
            frameRect = new CGRect(frameX+(frameW-imageW), frameY, imageW, frameH);
        }
        else {
            imageRect = new CGRect((imageW-frameW), (imageH-frameH)/2, frameW, frameH);
        }
        canvas.drawBitmap(bitmap, imageRect.toRect(), frameRect.toRect(), paint);
    }

}
