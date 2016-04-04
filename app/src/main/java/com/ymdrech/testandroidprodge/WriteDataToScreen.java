package com.ymdrech.testandroidprodge;

import android.graphics.Point;
import android.view.SurfaceHolder;

/**
 * Created by e4t on 2/11/2015.
 */
public interface WriteDataToScreen {

    public void writeTextAtPosition(TextProperties textProperties, Point point, Object text);
    public void drawRectangle(DrawProperties drawProperties, Point topLeft, Point bottomRight);
    public void drawLine(DrawProperties drawProperties, Point topLeft, Point bottomRight);
    public void drawOval(DrawProperties drawProperties, Point topLeft, Point bottomRight);
    public void drawArc(DrawProperties drawProperties, Point topLeft, Point bottomRight, float startAngle, float sweepAngle);
    public void updateScreen();
    public void clearScreen();
    public int getAreaHeight();
    public int getAreaWidth();
    public void setAreaHeight(int areaHeight);
    public void setAreaWidth(int areaWidth);
    public void setSurfaceHolder(SurfaceHolder surfaceHolder);
    public void setZoomMultiplier(int zoomMultiplier);
    public int getZoomMultiplier();
}
