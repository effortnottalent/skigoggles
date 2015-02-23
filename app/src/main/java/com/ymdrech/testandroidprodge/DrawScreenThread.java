package com.ymdrech.testandroidprodge;

import android.graphics.Color;
import android.graphics.Point;
import android.view.SurfaceHolder;

/**
 * Created by e4t on 2/15/2015.
 */
public class DrawScreenThread extends Thread {

    private static final int PAUSE_BETWEEN_DRAW_MILLISECONDS = 250;

    private WriteDataToScreen screen;
    private SurfaceHolder surfaceHolder;
    private boolean run = true;
    private ScreenData screenData = new ScreenData();
    private String scrolledTextPortion;
    private String scrolledText;
    private int scrolledTextStartCharIndex;

    public DrawScreenThread(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public void updateScreen(ScreenData screenData) {

        screen.clearScreen();

        DrawProperties dp = new DrawProperties();
        dp.setLineColour(Color.WHITE);
        dp.setFillColour(Color.TRANSPARENT);
        screen.drawRectangle(dp, new Point(0, 0), new Point(96 - 1, 64 - 1));

        if(screenData.getLocation() != null) {
            TextProperties speedTextProperties = new TextProperties();
            speedTextProperties.setForegroundColour(Color.WHITE);
            speedTextProperties.setOpacity(1F);
            speedTextProperties.setSize(48);
            String speed = String.format("%02.0f", screenData.getLocation().getSpeed());
            screen.writeTextAtPosition(speedTextProperties, new Point(2, 60), speed);
            speedTextProperties.setSize(10);
            screen.writeTextAtPosition(speedTextProperties, new Point(60, 60), "km/h");
        }

        TextProperties routeTextProperties = new TextProperties();
        routeTextProperties.setForegroundColour(Color.WHITE);
        routeTextProperties.setOpacity(1F);
        routeTextProperties.setSize(10);
        if(screenData.getRoute() != null) {
            Route route = screenData.getRoute();
            if(!routeTextToDisplay(route).equals(scrolledText)) {
                scrolledTextPortion = null;
                scrolledText = routeTextToDisplay(route);
            }
            updateScrolledTextPortion(96,12);
            screen.writeTextAtPosition(routeTextProperties, new Point(2, 12), scrolledTextPortion);
        }
        screen.updateScreen();
    }

    private String routeTextToDisplay(Route route) {
        return route.getName() + " - " + route.getDescription();
    }

    private void updateScrolledTextPortion(int fieldWidth, int fontSize) {
        // loop through text on each invocation
        int charsToShow = Math.round(fieldWidth / fontSize * 2);
        if(scrolledText.length() < charsToShow) {
            scrolledTextPortion = scrolledText;
        } else {
            String textToCutPortion = scrolledText + " - " + scrolledText;
            if (scrolledTextPortion == null || scrolledTextStartCharIndex > scrolledText.length()) {
                scrolledTextStartCharIndex = 0;
            } else {
                scrolledTextStartCharIndex++;
            }
            scrolledTextPortion = textToCutPortion.substring(scrolledTextStartCharIndex, scrolledTextStartCharIndex + charsToShow);
        }

    }

    private void initScreen() {
        if(screen == null) {
            screen = new OnScreenWriteDataToScreenImpl(surfaceHolder, 96, 64, 2);
        }
    }

    public void doStart() {
        initScreen();
        synchronized (surfaceHolder) {
            updateScreen(screenData);
        }
    }

    public void run() {
        initScreen();
        while(run) {
            updateScreen(screenData);
            try {
                Thread.sleep(PAUSE_BETWEEN_DRAW_MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ScreenData getScreenData() {
        return screenData;
    }

    public void setScreenData(ScreenData screenData) {
        this.screenData = screenData;
    }

}
