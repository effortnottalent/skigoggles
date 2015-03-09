package com.ymdrech.testandroidprodge;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.SurfaceHolder;

/**
 * Created by e4t on 2/11/2015.
 */
public class OnScreenWriteDataToScreenImpl implements WriteDataToScreen {

    private int areaWidth;
    private int areaHeight;
    private int zoomMultiplier = 1;
    private int offsetX = 0;
    private int offsetY = 0;
    private SurfaceHolder surfaceHolder;

    private Canvas lockedCanvas = null;

    public OnScreenWriteDataToScreenImpl(SurfaceHolder surfaceHolder, int areaWidth, int areaHeight, int zoomMultiplier, int offsetX, int offsetY) {
        this.surfaceHolder = surfaceHolder;
        this.areaWidth = areaWidth;
        this.areaHeight = areaHeight;
        this.zoomMultiplier = zoomMultiplier;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    private Rect calculateRect() {
        return new Rect(moveCoordX(0), moveCoordY(0), moveCoordX(areaWidth), moveCoordY(areaHeight));
    }

    public void clearScreen() {
        if(lockedCanvas == null) {
            lockedCanvas = surfaceHolder.lockCanvas(calculateRect());
        }
        final Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        lockedCanvas.drawRect(moveCoordX(0), moveCoordY(0), moveCoordX(areaWidth),
                moveCoordY(areaHeight), paint);
    }

    private int moveCoord(int coord) {
        return coord * zoomMultiplier + zoomMultiplier;
    }

    private int moveCoordX(int coord) {
        return moveCoord(coord) + offsetX;
    }

    private int moveCoordY(int coord) {
        return moveCoord(coord) + offsetY;
    }

    public void writeTextAtPosition(final TextProperties textProperties, final Point point, final String text) {
        final Paint paint = new Paint();
        // paint.setAlpha(Math.round(textProperties.getOpacity()*256));
        paint.setColor(textProperties.getForegroundColour());
        paint.setTextSize(textProperties.getSize() * zoomMultiplier);
        paint.setTypeface(Typeface.create(textProperties.getFont(), 0));
        if(lockedCanvas == null) {
            lockedCanvas = surfaceHolder.lockCanvas(calculateRect());
        }
        lockedCanvas.drawText(text, moveCoordX(point.x), moveCoordY(point.y), paint);
    }

    public void drawRectangle(final DrawProperties drawProperties, final Point topLeft, final Point bottomRight) {
        if(lockedCanvas == null) {
            lockedCanvas = surfaceHolder.lockCanvas(calculateRect());
        }
        // do fill first
        final Paint fillPaint = new Paint();
        fillPaint.setColor(drawProperties.getFillColour());
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setStrokeWidth(drawProperties.getLineWidth());
        lockedCanvas.drawRect(
                moveCoordX(topLeft.x),
                moveCoordY(topLeft.y),
                moveCoordX(bottomRight.x),
                moveCoordY(bottomRight.y),
                fillPaint);
        // then stroke (if needed)
        if(drawProperties.getLineColour() != drawProperties.getFillColour()) {
            final Paint strokePaint = new Paint();
            strokePaint.setColor(drawProperties.getLineColour());
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setStrokeWidth(
                    drawProperties.getLineWidth() == 0 ?
                            1 * zoomMultiplier :
                            drawProperties.getLineWidth() * zoomMultiplier);
            lockedCanvas.drawRect(
                    moveCoordX(topLeft.x),
                    moveCoordY(topLeft.y),
                    moveCoordX(bottomRight.x),
                    moveCoordY(bottomRight.y),
                    strokePaint);
        }
    }


    public void drawLine(final DrawProperties drawProperties, final Point topLeft, final Point bottomRight) {
        if (lockedCanvas == null) {
            lockedCanvas = surfaceHolder.lockCanvas(calculateRect());
        }
        final Paint paint = new Paint();
        paint.setColor(drawProperties.getLineColour());
        paint.setStrokeWidth(
                drawProperties.getLineWidth() == 0 ?
                        1 * zoomMultiplier :
                        drawProperties.getLineWidth() * zoomMultiplier);
        paint.setStyle(Paint.Style.STROKE);
        lockedCanvas.drawLine(
                moveCoordX(topLeft.x),
                moveCoordY(topLeft.y),
                moveCoordX(bottomRight.x),
                moveCoordY(bottomRight.y),
                paint);
    }

    public void drawArc(final DrawProperties drawProperties, final Point topLeft, final Point bottomRight, float startAngle, float sweepAngle) {
        if (lockedCanvas == null) {
            lockedCanvas = surfaceHolder.lockCanvas(calculateRect());
        }
        final Paint paint = new Paint();
        paint.setColor(drawProperties.getLineColour());
        paint.setStrokeWidth(
                drawProperties.getLineWidth() == 0 ?
                        1 * zoomMultiplier :
                        drawProperties.getLineWidth() * zoomMultiplier);
        paint.setStyle(Paint.Style.STROKE);
        lockedCanvas.drawArc(
                moveCoordX(topLeft.x),
                moveCoordY(topLeft.y),
                moveCoordX(bottomRight.x),
                moveCoordY(bottomRight.y),
                Math.round(startAngle * (180 / Math.PI)),
                Math.round(sweepAngle * (180 / Math.PI)),
                false,
                paint);
    }

    public void drawOval(final DrawProperties drawProperties, final Point topLeft, final Point bottomRight) {
        if(lockedCanvas == null) {
            lockedCanvas = surfaceHolder.lockCanvas(calculateRect());
        }
        // do fill first
        final Paint fillPaint = new Paint();
        fillPaint.setColor(drawProperties.getFillColour());
        fillPaint.setStrokeWidth(drawProperties.getLineWidth());
        fillPaint.setStyle(Paint.Style.FILL);
        lockedCanvas.drawOval(
                moveCoordX(topLeft.x),
                moveCoordY(topLeft.y),
                moveCoordX(bottomRight.x),
                moveCoordY(bottomRight.y),
                fillPaint);
        // then stroke (if needed)
        if(drawProperties.getLineColour() != drawProperties.getFillColour()) {
            final Paint strokePaint = new Paint();
            strokePaint.setColor(drawProperties.getLineColour());
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setStrokeWidth(
                    drawProperties.getLineWidth() == 0 ?
                            1 * zoomMultiplier :
                            drawProperties.getLineWidth() * zoomMultiplier);
            lockedCanvas.drawOval(
                    moveCoordX(topLeft.x),
                    moveCoordY(topLeft.y),
                    moveCoordX(bottomRight.x),
                    moveCoordY(bottomRight.y),
                    strokePaint);
        }
    }

    public void updateScreen() {
        if(lockedCanvas != null) {
            surfaceHolder.unlockCanvasAndPost(lockedCanvas);
            lockedCanvas = null;
        }
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
