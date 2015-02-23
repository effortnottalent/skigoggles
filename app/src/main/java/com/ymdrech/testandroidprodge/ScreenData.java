package com.ymdrech.testandroidprodge;

import android.location.Location;

/**
 * Created by e4t on 2/15/2015.
 */
public class ScreenData {

    private Location location;
    private Route route;

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
