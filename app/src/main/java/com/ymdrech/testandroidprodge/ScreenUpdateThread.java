package com.ymdrech.testandroidprodge;

import android.util.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by e4t on 25/02/16.
 */
public class ScreenUpdateThread extends Thread {

    private static int DEFAULT_REFRESH_PERIOD_MS = 500;

    private int refreshPeriod = DEFAULT_REFRESH_PERIOD_MS;
    private AppSessionStore appSessionStore;
    private boolean stopThread;

    public boolean isStopThread() {
        return stopThread;
    }

    @Override
    public void run() {
        while(!stopThread) {
            for(AbstractDrawScreenLayoutService screenLayout : appSessionStore.getScreenLayouts()) {
                screenLayout.drawScreen();
            }
            try {
                Thread.sleep(refreshPeriod);
            } catch (InterruptedException e) {
                Log.i(getClass().getName(), "thread sleep interrupted", e);
            }
        }
    }

    public void stopThread() {
        stopThread = true;
    }

    public void setAppSessionStore(AppSessionStore appSessionStore) {
        this.appSessionStore = appSessionStore;
    }
}
