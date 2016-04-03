package com.ymdrech.testandroidprodge;

import android.graphics.Color;
import android.graphics.Point;

/**
 * Created by e4t on 03/04/16.
 */
public class DrawScreenHelper {

    public static void drawGrid(WriteDataToScreen writeDataToScreen, int gridUnit) {

        int widthUnit = Math.round(writeDataToScreen.getAreaWidth() / gridUnit);
        int heightUnit = Math.round(writeDataToScreen.getAreaHeight() / gridUnit);

        DrawProperties greyDp = new DrawProperties();
        int gridColour = Color.argb(255, 50, 50, 50);
        greyDp.setLineColour(gridColour);
        greyDp.setFillColour(gridColour);

        for(int x = 0; x < writeDataToScreen.getAreaWidth(); x += widthUnit * 2) {
            for(int y = 0; y < writeDataToScreen.getAreaHeight(); y += heightUnit * 2) {
                writeDataToScreen.drawRectangle(greyDp,
                        new Point(x, y),
                        new Point(x + widthUnit, y + heightUnit));
                writeDataToScreen.drawRectangle(greyDp,
                        new Point(x + widthUnit, y + heightUnit),
                        new Point(x + widthUnit * 2, y + heightUnit * 2));
            }
        }

        greyDp.setFillColour(Color.TRANSPARENT);
        writeDataToScreen.drawRectangle(greyDp,
                new Point(0, 0),
                new Point(writeDataToScreen.getAreaWidth(), writeDataToScreen.getAreaHeight()));

    }

}
