package com.ymdrech.testandroidprodge;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceHolder;

/**
 * Created by e4t on 2/11/2015.
 */
public class OnScreenWriteDataToScreenImpl implements WriteDataToScreen {

    private int areaWidth;
    private int areaHeight;
    private int saveNum;
    private int zoomMultiplier = 1;
    private SurfaceHolder surfaceHolder;

    private Canvas lockedCanvas = null;

    public OnScreenWriteDataToScreenImpl(SurfaceHolder surfaceHolder, int areaWidth, int areaHeight, int zoomMultiplier) {
        this.areaWidth = areaWidth;
        this.areaHeight = areaHeight;
        this.zoomMultiplier = zoomMultiplier;
        this.surfaceHolder = surfaceHolder;
    }

    public void clearScreen() {
        if(lockedCanvas == null) {
            lockedCanvas = surfaceHolder.lockCanvas();
        }
        final Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        lockedCanvas.drawRect(0, 0, lockedCanvas.getWidth(), lockedCanvas.getHeight(), paint);
    }

    public void writeTextAtPosition(final TextProperties textProperties, final Point point, final String text) {
        final Paint paint = new Paint();
        // paint.setAlpha(Math.round(textProperties.getOpacity()*256));
        paint.setColor(textProperties.getForegroundColour());
        paint.setTextSize(textProperties.getSize());
        if(lockedCanvas == null) {
            lockedCanvas = surfaceHolder.lockCanvas();
        }
        lockedCanvas.drawText(text, point.x, point.y, paint);
    }

    public void drawRectangle(final DrawProperties drawProperties, final Point topLeft, final Point bottomRight) {
        if(lockedCanvas == null) {
            lockedCanvas = surfaceHolder.lockCanvas();
        }
        // do fill first
        final Paint fillPaint = new Paint();
        fillPaint.setColor(drawProperties.getFillColour());
        fillPaint.setStyle(Paint.Style.FILL);
        lockedCanvas.drawRect(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y, fillPaint);
        // then stroke (if needed)
        if(drawProperties.getLineColour() != drawProperties.getFillColour()) {
            final Paint strokePaint = new Paint();
            strokePaint.setColor(drawProperties.getLineColour());
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setStrokeWidth(drawProperties.getLineWidth() == 0 ? 1 : drawProperties.getLineWidth());
            lockedCanvas.drawRect(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y, strokePaint);
        }
    }

    public void drawOval(final DrawProperties drawProperties, final Point topLeft, final Point bottomRight) {
        final Paint paint = new Paint();
        if(lockedCanvas == null) {
            lockedCanvas = surfaceHolder.lockCanvas();
        }
        lockedCanvas.drawOval(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y, paint);
    }

    public void updateScreen() {
        if(lockedCanvas == null) {
            lockedCanvas = surfaceHolder.lockCanvas();
        }
        surfaceHolder.unlockCanvasAndPost(lockedCanvas);
        lockedCanvas = null;
    }

    public int getAreaWidth() {
        return areaWidth;
    }

    public void setAreaWidth(int areaWidth) {
        this.areaWidth = areaWidth;
    }

    public int getAreaHeight() {
        return areaHeight;
    }

    public void setAreaHeight(int areaHeight) {
        this.areaHeight = areaHeight;
    }

    public int getZoomMultiplier() {
        return zoomMultiplier;
    }

    public void setZoomMultiplier(int zoomMultiplier) {
        this.zoomMultiplier = zoomMultiplier;
    }

}
