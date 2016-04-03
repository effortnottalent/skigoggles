package com.ymdrech.testandroidprodge;

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
public class SurfaceHolderWriteDataToScreen implements WriteDataToScreen {

    private int areaWidth;
    private int areaHeight;
    private int zoomMultiplier = 1;
    private int offsetX = 0;
    private int offsetY = 0;
    private SurfaceHolder surfaceHolder;

    private Canvas canvas = null;

    private Rect calculateRect() {
        return new Rect(moveCoordX(0), moveCoordY(0), moveCoordX(areaWidth), moveCoordY(areaHeight));
    }

    public void clearScreen() {
        if(canvas == null) {
            canvas = surfaceHolder.lockCanvas(calculateRect());
        }
        final Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawRect(moveCoordX(0), moveCoordY(0), moveCoordX(areaWidth),
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
        paint.setAlpha(Math.round(textProperties.getOpacity() * 256));
        paint.setColor(textProperties.getForegroundColour());
        paint.setTextSize(textProperties.getSize() * zoomMultiplier);
        paint.setTypeface(Typeface.create(textProperties.getFont(), 0));

        if(canvas == null) {
            canvas = surfaceHolder.lockCanvas(calculateRect());
        }
        canvas.drawText(text, moveCoordX(point.x), moveCoordY(point.y), paint);
    }

    public void drawRectangle(final DrawProperties drawProperties, final Point topLeft, final Point bottomRight) {
        if(canvas == null) {
            canvas = surfaceHolder.lockCanvas(calculateRect());
        }
        // do fill first
        final Paint fillPaint = new Paint();
        fillPaint.setColor(drawProperties.getFillColour());
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setStrokeWidth(drawProperties.getLineWidth());
        canvas.drawRect(
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
            canvas.drawRect(
                    moveCoordX(topLeft.x),
                    moveCoordY(topLeft.y),
                    moveCoordX(bottomRight.x),
                    moveCoordY(bottomRight.y),
                    strokePaint);
        }
    }


    public void drawLine(final DrawProperties drawProperties, final Point topLeft, final Point bottomRight) {
        if (canvas == null) {
            canvas = surfaceHolder.lockCanvas(calculateRect());
        }
        final Paint paint = new Paint();
        paint.setColor(drawProperties.getLineColour());
        paint.setStrokeWidth(
                drawProperties.getLineWidth() == 0 ?
                        1 * zoomMultiplier :
                        drawProperties.getLineWidth() * zoomMultiplier);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(
                moveCoordX(topLeft.x),
                moveCoordY(topLeft.y),
                moveCoordX(bottomRight.x),
                moveCoordY(bottomRight.y),
                paint);
    }

    public void drawArc(final DrawProperties drawProperties, final Point topLeft, final Point bottomRight, float startAngle, float sweepAngle) {
        if (canvas == null) {
            canvas = surfaceHolder.lockCanvas(calculateRect());
        }
        final Paint paint = new Paint();
        paint.setColor(drawProperties.getLineColour());
        paint.setStrokeWidth(
                drawProperties.getLineWidth() == 0 ?
                        1 * zoomMultiplier :
                        drawProperties.getLineWidth() * zoomMultiplier);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(
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
        if(canvas == null) {
            canvas = surfaceHolder.lockCanvas(calculateRect());
        }
        // do fill first
        final Paint fillPaint = new Paint();
        fillPaint.setColor(drawProperties.getFillColour());
        fillPaint.setStrokeWidth(drawProperties.getLineWidth());
        fillPaint.setStyle(Paint.Style.FILL);
        canvas.drawOval(
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
            canvas.drawOval(
                    moveCoordX(topLeft.x),
                    moveCoordY(topLeft.y),
                    moveCoordX(bottomRight.x),
                    moveCoordY(bottomRight.y),
                    strokePaint);
        }
    }

    public void updateScreen() {
        if(canvas != null) {
            surfaceHolder.unlockCanvasAndPost(canvas);
            canvas = null;
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

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }
}
