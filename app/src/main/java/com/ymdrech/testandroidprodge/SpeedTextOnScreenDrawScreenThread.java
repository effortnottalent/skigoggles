package com.ymdrech.testandroidprodge;

import android.graphics.Color;
import android.graphics.Point;

import com.ymdrech.testandroidprodge.data.Route;

/**
 * Created by e4t on 2/15/2015.
 */
public class SpeedTextOnScreenDrawScreenThread extends AbstractOnScreenDrawScreenThread {

    protected void doUpdateScreen() {

        if(getScreenData().getRoute() != null) {
            writeSpeedInfo(getScreenData().getRoute());
        }
    }

    private void writeSpeedInfo(Route route) {
        TextProperties textProperties = new TextProperties();
        textProperties.setForegroundColour(Color.WHITE);
        textProperties.setOpacity(1F);
        textProperties.setFont("monospace");
        if(getScreenData().getLocation() != null) {
            float speed = getScreenData().getLocation().getSpeed();
            String speedString = String.format("%3.0f", (speed == 0.0) ? getScreenData().getCalculatedSpeed() : speed);
            textProperties.setSize(36);
            getScreen().writeTextAtPosition(textProperties, new Point(2,48), speedString);
            textProperties.setSize(8);
            getScreen().writeTextAtPosition(textProperties, new Point(68,50), "km/h");
        }

    }

}
