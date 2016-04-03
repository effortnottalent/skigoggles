package com.ymdrech.testandroidprodge;

/**
 * Created by e4t on 2/25/2015.
 */
public interface DrawScreenThread {
    public void setInfoScreenDTO(InfoScreenDTO infoScreenDTO);
    public void updateScreen();
    public void run();
    public boolean isRunning();
    public void setRunning(boolean run);
    public void setScreen(WriteDataToScreen screen);
}
