package com.ymdrech.testandroidprodge;

import android.location.Location;

import java.util.Date;
import java.util.List;

/**
 * Created by e4t on 25/02/16.
 */
public class AppSessionStore {

    private List<AbstractDrawScreenLayoutService> screenLayouts;

    public List<AbstractDrawScreenLayoutService> getScreenLayouts() {
        return screenLayouts;
    }

    public void setScreenLayouts(List<AbstractDrawScreenLayoutService> screenLayouts) {
        this.screenLayouts = screenLayouts;
    }
}
