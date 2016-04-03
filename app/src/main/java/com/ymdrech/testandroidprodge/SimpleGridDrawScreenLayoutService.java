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
        DrawScreenHelper.drawGrid(writeDataToScreen, GRID_UNIT);
        drawData(writeDataToScreen, screenLayoutData);
        writeDataToScreen.updateScreen();

    }

    protected void drawData(WriteDataToScreen writeDataToScreen,
                            Map<String, Object> screenLayoutData) {

        int heightUnit = Math.round(writeDataToScreen.getAreaHeight() / GRID_UNIT);
        int widthUnit = Math.round(writeDataToScreen.getAreaWidth() / GRID_UNIT);

        DrawProperties whiteDp = new DrawProperties();
        whiteDp.setLineColour(Color.WHITE);
        whiteDp.setFillColour(Color.TRANSPARENT);

        writeDataToScreen.drawLine(whiteDp,
                new Point(2 * widthUnit, 8 * heightUnit),
                new Point(14 * widthUnit, 8 * heightUnit));

        writeDataToScreen.drawLine(whiteDp,
                new Point(8 * widthUnit, 1 * heightUnit),
                new Point(8 * widthUnit, 15 * heightUnit));

        TextProperties whiteTp = new TextProperties();
        whiteTp.setBackgroundColour(Color.TRANSPARENT);
        whiteTp.setForegroundColour(Color.WHITE);
        whiteTp.setOpacity(1F);
        whiteTp.setSize(heightUnit * 4);

        writeDataToScreen.writeTextAtPosition(whiteTp,
                new Point(1 * widthUnit, 6 * heightUnit),
                (String) screenLayoutData.get("speed"));

        writeDataToScreen.writeTextAtPosition(whiteTp,
                new Point(1 * widthUnit, 13 * heightUnit),
                (String) screenLayoutData.get("runs"));

        writeDataToScreen.writeTextAtPosition(whiteTp,
                new Point(10 * widthUnit, 6 * heightUnit),
                (String) screenLayoutData.get("direction"));

        writeDataToScreen.writeTextAtPosition(whiteTp,
                new Point(10 * widthUnit, 13 * heightUnit),
                (String) screenLayoutData.get("distance-down"));

        whiteTp.setSize(heightUnit);

        writeDataToScreen.writeTextAtPosition(whiteTp,
                new Point(2 * widthUnit, 3 * heightUnit),
                "speed");

        writeDataToScreen.writeTextAtPosition(whiteTp,
                new Point(2 * widthUnit, 10 * heightUnit),
                "runs");

        writeDataToScreen.writeTextAtPosition(whiteTp,
                new Point(9 * widthUnit, 3 * heightUnit),
                "direction");

        writeDataToScreen.writeTextAtPosition(whiteTp,
                new Point(9 * widthUnit, 10 * heightUnit),
                "distance");

    }
}
