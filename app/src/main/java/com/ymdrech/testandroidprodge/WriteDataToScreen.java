package com.ymdrech.testandroidprodge;

import android.graphics.Point;

/**
 * Created by e4t on 2/11/2015.
 */
public interface WriteDataToScreen {

    public void writeTextAtPosition(TextProperties textProperties, Point point, String text);
    public void drawRectangle(DrawProperties drawProperties, Point topLeft, Point bottomRight);
    public void drawLine(DrawProperties drawProperties, Point topLeft, Point bottomRight);
    public void drawOval(DrawProperties drawProperties, Point topLeft, Point bottomRight);
    public void drawArc(DrawProperties drawProperties, Point topLeft, Point bottomRight, float startAngle, float sweepAngle);
    public void updateScreen();
    public void clearScreen();
}
