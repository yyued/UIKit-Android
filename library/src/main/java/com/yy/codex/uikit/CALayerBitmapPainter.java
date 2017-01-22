package com.yy.codex.uikit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

/**
 * Created by adi on 17/1/16.
 */

public class CALayerBitmapPainter {

    public static void drawBitmap(@NonNull Canvas canvas, @NonNull CGRect rect, @NonNull Bitmap bitmap, CALayer.BitmapGravity bitmapGravity, Paint paint){
        switch (bitmapGravity){
            case ScaleToFill:
                drawScaleToFill(canvas, rect, bitmap, paint);
                break;
            case ScaleAspectFit:
                drawScaleAsceptFit(canvas, rect, bitmap, paint);
                break;
            case ScaleAspectFill:
                drawScaleAsceptFill(canvas, rect, bitmap, paint);
                break;
            case Center:
                drawCenter(canvas, rect, bitmap, paint);
                break;
            case Top:
                drawTop(canvas, rect, bitmap, paint);
                break;
            case TopLeft:
                drawTopLeft(canvas, rect, bitmap, paint);
                break;
            case TopRight:
                drawTopRight(canvas, rect, bitmap, paint);
                break;
            case Bottom:
                drawBottom(canvas, rect, bitmap, paint);
                break;
            case BottomLeft:
                drawBottomLeft(canvas, rect, bitmap, paint);
                break;
            case BottomRight:
                drawBottomRight(canvas, rect, bitmap, paint);
                break;
            case Left:
                drawLeft(canvas, rect, bitmap, paint);
                break;
            case Right:
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
        double frameW = rect.getSize().getWidth();
        double frameH = rect.getSize().getHeight();
        double frameRatio = frameW / frameH;
        double frameX = rect.getOrigin().getX();
        double frameY = rect.getOrigin().getY();
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
        double frameW = rect.getSize().getWidth();
        double frameH = rect.getSize().getHeight();
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
        double frameW = rect.getSize().getWidth();
        double frameH = rect.getSize().getHeight();
        double frameX = rect.getOrigin().getX();
        double frameY = rect.getOrigin().getY();
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
        double frameW = rect.getSize().getWidth();
        double frameH = rect.getSize().getHeight();
        double frameX = rect.getOrigin().getX();
        double frameY = rect.getOrigin().getY();
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
        double frameW = rect.getSize().getWidth();
        double frameH = rect.getSize().getHeight();
        double frameX = rect.getOrigin().getX();
        double frameY = rect.getOrigin().getY();
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
        double frameW = rect.getSize().getWidth();
        double frameH = rect.getSize().getHeight();
        double frameX = rect.getOrigin().getX();
        double frameY = rect.getOrigin().getY();
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
        double frameW = rect.getSize().getWidth();
        double frameH = rect.getSize().getHeight();
        double frameX = rect.getOrigin().getX();
        double frameY = rect.getOrigin().getY();
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
        double frameW = rect.getSize().getWidth();
        double frameH = rect.getSize().getHeight();
        double frameX = rect.getOrigin().getX();
        double frameY = rect.getOrigin().getY();
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
        double frameW = rect.getSize().getWidth();
        double frameH = rect.getSize().getHeight();
        double frameX = rect.getOrigin().getX();
        double frameY = rect.getOrigin().getY();
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
        double frameW = rect.getSize().getWidth();
        double frameH = rect.getSize().getHeight();
        double frameX = rect.getOrigin().getX();
        double frameY = rect.getOrigin().getY();
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
        double frameW = rect.getSize().getWidth();
        double frameH = rect.getSize().getHeight();
        double frameX = rect.getOrigin().getX();
        double frameY = rect.getOrigin().getY();
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
