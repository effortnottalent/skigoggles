package com.ymdrech.testandroidprodge;

import android.graphics.Color;
import android.graphics.Point;

import java.util.Map;

/**
 * Created by e4t on 25/02/16.
 */
public class SimpleGridDrawScreenLayoutService extends AbstractDrawScreenLayoutService {

    public static final int GRID_UNIT = 16;

    @Override
    protected void doDrawScreen(Map<String, Object> screenLayoutData) {

        WriteDataToScreen writeDataToScreen = getWriteDataToScreen();
        DrawScreenHelper.blankScreen(writeDataToScreen, Color.BLACK);
        DrawScreenHelper.drawGrid(writeDataToScreen, GRID_UNIT);
        drawData(writeDataToScreen, screenLayoutData);
        writeDataToScreen.updateScreen();

    }

    private String getCompassFromBearing(Object bearing) {
        if(bearing instanceof Float) {
            String[] compassDirections = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
            int index = (int) (((float)bearing / 22.5) + .5);
            return compassDirections[index % 16];
        }
        return bearing.toString();
    }

    protected void drawData(WriteDataToScreen writeDataToScreen,
                            Map<String, Object> screenLayoutData) {

        int heightUnit = Math.round(writeDataToScreen.getAreaHeight() / GRID_UNIT);
        int widthUnit = Math.round(writeDataToScreen.getAreaWidth() / GRID_UNIT);

        DrawProperties whiteDp = new DrawProperties();
        whiteDp.setLineColour(Color.WHITE);
        whiteDp.setFillColour(Color.TRANSPARENT);

        writeDataToScreen.drawLine(whiteDp,
                new Point(1 * widthUnit, 8 * heightUnit),
                new Point(15 * widthUnit, 8 * heightUnit));

        writeDataToScreen.drawLine(whiteDp,
                new Point(8 * widthUnit, 2 * heightUnit),
                new Point(8 * widthUnit, 14 * heightUnit));

        TextProperties whiteTp = new TextProperties();
        whiteTp.setBackgroundColour(Color.TRANSPARENT);
        whiteTp.setForegroundColour(Color.WHITE);
        whiteTp.setOpacity(1F);
        whiteTp.setSize(heightUnit * 4);

        writeDataToScreen.writeTextAtPosition(whiteTp,
                new Point(1 * widthUnit, 6 * heightUnit),
                screenLayoutData.get("speed"));

        writeDataToScreen.writeTextAtPosition(whiteTp,
                new Point(1 * widthUnit, 13 * heightUnit),
                screenLayoutData.get("runs"));

        writeDataToScreen.writeTextAtPosition(whiteTp,
                new Point(9 * widthUnit, 6 * heightUnit),
                getCompassFromBearing(screenLayoutData.get("direction")));

        writeDataToScreen.writeTextAtPosition(whiteTp,
                new Point(9 * widthUnit, 13 * heightUnit),
                screenLayoutData.get("distance-down"));

        whiteTp.setSize(heightUnit);

        writeDataToScreen.writeTextAtPosition(whiteTp,
                new Point(1 * widthUnit, 3 * heightUnit),
                "speed");

        writeDataToScreen.writeTextAtPosition(whiteTp,
                new Point(1 * widthUnit, 10 * heightUnit),
                "runs");

        writeDataToScreen.writeTextAtPosition(whiteTp,
                new Point(9 * widthUnit, 3 * heightUnit),
                "direction");

        writeDataToScreen.writeTextAtPosition(whiteTp,
                new Point(9 * widthUnit, 10 * heightUnit),
                "distance");

    }
}
