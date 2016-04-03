package com.ymdrech.testandroidprodge;

import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

import com.ymdrech.testandroidprodge.model.Route;

/**
 * Created by e4t on 2/25/2015.
 */
public abstract class AbstractOnScreenDrawScreenThread implements DrawScreenThread {

    private static final int PAUSE_BETWEEN_DRAW_MILLISECONDS = 100;

    private WriteDataToScreen screen;
    private InfoScreenDTO infoScreenDTO = new InfoScreenDTO();
    private boolean run = true;
    private int areaWidth = 96;
    private int areaHeight = 64;
    private String scrolledTextPortion;
    private String scrolledText;
    private int scrolledTextStartCharIndex;
    private boolean shouldUpdateScreen = true;

    public void updateScreen() {
        synchronized (infoScreenDTO) {
            Log.d(getClass().getName(), "updating screen with infoScreenDTO " + infoScreenDTO);
            screen.clearScreen();
            if (infoScreenDTO.getRoute() != null) {
                drawElapsedDistance(infoScreenDTO.getRoute(), new Point(3, 14), new Point(areaWidth - 3, 17));
                writeRouteInfo(infoScreenDTO.getRoute());
            }
            doUpdateScreen();
            drawFrame(new Point(0, 0), new Point(areaWidth - 1, areaHeight - 1));
            screen.updateScreen();
            shouldUpdateScreen = false;
        }
    }

    protected abstract void doUpdateScreen();

    protected void drawFrame(Point topLeft, Point bottomRight) {
        DrawProperties dp = new DrawProperties();
        dp.setLineColour(Color.WHITE);
        dp.setFillColour(Color.TRANSPARENT);
        getScreen().drawRectangle(dp, topLeft, bottomRight);
    }

    private void drawElapsedDistance(Route route, Point topLeft, Point bottomRight) {
        DrawProperties elapsedDp = new DrawProperties();
        elapsedDp.setLineColour(Color.WHITE);
        elapsedDp.setFillColour(Color.TRANSPARENT);
        getScreen().drawRectangle(elapsedDp, topLeft, bottomRight);
        elapsedDp.setFillColour(Color.WHITE);
        int filledX = (int)Math.round((bottomRight.x - topLeft.x) * (1 - infoScreenDTO.getAmountComplete())) + topLeft.x;
        Point filledTopLeft = new Point(filledX, topLeft.y);
        getScreen().drawRectangle(elapsedDp, filledTopLeft, bottomRight);
    }

    private void writeRouteInfo(Route route) {
        TextProperties routeTextProperties = new TextProperties();
        if(route.getMetadata().contains("run")) {
            if(route.getMetadata().contains("red")) {
                routeTextProperties.setForegroundColour(Color.RED);
            } else if(route.getMetadata().contains("green")) {
                routeTextProperties.setForegroundColour(Color.GREEN);
            } else if(route.getMetadata().contains("black")) {
                routeTextProperties.setForegroundColour(Color.WHITE);
            } else if(route.getMetadata().contains("blue")) {
                routeTextProperties.setForegroundColour(Color.BLUE);
            }
        } else {
            routeTextProperties.setForegroundColour(Color.WHITE);
        }
        routeTextProperties.setOpacity(1F);
        routeTextProperties.setSize(8);
        if(infoScreenDTO.getRoute() != null) {
            if(!routeTextToDisplay(route).equals(scrolledText)) {
                scrolledTextPortion = null;
                scrolledText = routeTextToDisplay(route);
            }
            updateScrolledTextPortion(areaWidth,8);
            getScreen().writeTextAtPosition(routeTextProperties, new Point(2,9), scrolledTextPortion);
        }
    }

    private String routeTextToDisplay(Route route) {
        return route.getName() + " - " + route.getDescription();
    }

    private void updateScrolledTextPortion(int fieldWidth, int fontSize) {
        // loop through text on each invocation
        int charsToShow = (int)Math.round(fieldWidth / fontSize * 2.5);
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

    public void run() {
        Thread thread = new Thread() {
            public void run() {
                while(run) {
                    if (shouldUpdateScreen) {
                        updateScreen();
                    }
                    try {
                        Thread.sleep(PAUSE_BETWEEN_DRAW_MILLISECONDS);
                    } catch (InterruptedException e) {
                        Log.e(getClass().getName(), e.getMessage(), e);
                    }
                }
            }
        };
        thread.start();
    }

    public InfoScreenDTO getInfoScreenDTO() {
        return infoScreenDTO;
    }

    public void setInfoScreenDTO(InfoScreenDTO infoScreenDTO2) {
        shouldUpdateScreen = !infoScreenDTO2.equals(this.infoScreenDTO);
        this.infoScreenDTO = infoScreenDTO2;
    }

    public boolean isRunning() {
        return run;
    }

    public void setRunning(boolean run) {
        this.run = run;
    }

    public WriteDataToScreen getScreen() {
        return screen;
    }

    public void setScreen(WriteDataToScreen screen) {
        this.screen = screen;
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

}
