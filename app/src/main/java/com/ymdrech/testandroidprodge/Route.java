package com.ymdrech.testandroidprodge;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by e4t on 2/16/2015.
 */
public class Route {
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<LatLng> getRoute() {
        return route;
    }

    public void setRoute(List<LatLng> route) {
        this.route = route;
    }

    private List<LatLng> route;
}
