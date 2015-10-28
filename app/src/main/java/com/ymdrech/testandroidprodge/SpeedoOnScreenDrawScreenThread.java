package com.ymdrech.testandroidprodge;

import android.graphics.Color;
import android.graphics.Point;

import com.ymdrech.testandroidprodge.model.Route;

/**
 * Created by e4t on 2/25/2015.
 */
public class SpeedoOnScreenDrawScreenThread extends AbstractOnScreenDrawScreenThread {

    int dialMaxSpeed = 150;
    float zeroSpeedAngle = new Float(Math.PI * 0.75);
    float maxSpeedAngle = new Float(Math.PI * 0.25);

    protected void doUpdateScreen() {
        if(getScreenData().getRoute() != null) {
            int diameter = getAreaHeight() - 20;
            drawDial(new Point(5, 20), diameter);
            drawPointer(new Point(5, 20), diameter, getScreenData().getCalculatedSpeed());
            writeSpeedInfo(getScreenData().getRoute());
        }
    }

    private void drawDial(Point topLeft, int diameter) {
        DrawProperties dp = new DrawProperties();
        dp.setLineColour(Color.WHITE);
        dp.setFillColour(Color.TRANSPARENT);
        dp.setLineWidth(2);
        getScreen().drawArc(dp,
                topLeft,
                new Point(topLeft.x + diameter, topLeft.y + diameter),
                zeroSpeedAngle,
                new Float(Math.PI * 2 - Math.abs(zeroSpeedAngle - maxSpeedAngle)));
    }

    private void drawPointer(Point topLeft, int diameter, double speed) {
        double angle = (zeroSpeedAngle
                + (Math.PI * 2 - Math.abs(zeroSpeedAngle - maxSpeedAngle)) * (speed / dialMaxSpeed));
        Point startPoint = new Point(
                topLeft.x + (int)Math.round(diameter / 2),
                topLeft.y + (int)Math.round(diameter / 2));
        Point endPoint = new Point(
                startPoint.x + (int)Math.round((diameter / 2) * Math.cos(angle)),
                startPoint.y + (int)Math.round((diameter / 2) * Math.sin(angle)));
        DrawProperties dp = new DrawProperties();
        dp.setLineColour(Color.WHITE);
        dp.setFillColour(Color.TRANSPARENT);
        getScreen().drawLine(dp, startPoint, endPoint);
    }

    private void writeSpeedInfo(Route route) {
        TextProperties textProperties = new TextProperties();
        textProperties.setForegroundColour(Color.WHITE);
        textProperties.setOpacity(1F);
        textProperties.setFont("monospace");
        if(getScreenData().getLocation() != null) {
            float speed = getScreenData().getLocation().getSpeed();
            String speedString = String.format("%3.0f", (speed == 0.0) ? getScreenData().getCalculatedSpeed() : speed);
            textProperties.setSize(8);
            getScreen().writeTextAtPosition(textProperties, new Point(60,48), speedString);
        }

    }
}
