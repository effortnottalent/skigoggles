package com.ymdrech.testandroidprodge;

import android.graphics.Color;
import android.graphics.Point;

import com.ymdrech.testandroidprodge.model.Route;

/**
 * Created by e4t on 2/15/2015.
 */
public class SpeedTextOnScreenDrawScreenThread extends AbstractOnScreenDrawScreenThread {

    protected void doUpdateScreen() {

        if(getInfoScreenDTO().getRoute() != null) {
            writeSpeedInfo(getInfoScreenDTO().getRoute());
        }
    }

    private void writeSpeedInfo(Route route) {
        TextProperties textProperties = new TextProperties();
        textProperties.setForegroundColour(Color.WHITE);
        textProperties.setOpacity(1F);
        textProperties.setFont("monospace");
        if(getInfoScreenDTO().getLocation() != null) {
            float speed = getInfoScreenDTO().getLocation().getSpeed();
            String speedString = String.format("%3.0f", (speed == 0.0) ? getInfoScreenDTO().getCalculatedSpeed() : speed);
            textProperties.setSize(36);
            getScreen().writeTextAtPosition(textProperties, new Point(2,48), speedString);
            textProperties.setSize(8);
            getScreen().writeTextAtPosition(textProperties, new Point(68,50), "km/h");
        }

    }

}
