package com.ymdrech.testandroidprodge.data;

import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e4t on 2/16/2015.
 */
@DatabaseTable
public class Route {

    @DatabaseField(generatedId = true) private int id;
    @DatabaseField private String name;
    @DatabaseField private String description;
    @DatabaseField private List<LatLng> route;
    @DatabaseField private List<Double> routeSegmentLengths = new ArrayList<Double>();
    @DatabaseField private List<String> metadata = new ArrayList<String>();
    @DatabaseField private double length;

    public List<Double> getRouteSegmentLengths() {
        return routeSegmentLengths;
    }

    public void setRouteSegmentLengths(List<Double> routeSegmentLengths) {
        this.routeSegmentLengths = routeSegmentLengths;
    }

    public void addRouteSegmentLength(Double routeSegmentLength) {
        routeSegmentLengths.add(routeSegmentLength);
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public List<String> getMetadata() {
        return metadata;
    }

    public void addMetadata(String item) {
        metadata.add(item);
    }

    public void setMetadata(List<String> metadata) {
        this.metadata = metadata;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route1 = (Route) o;

        if (Double.compare(route1.length, length) != 0) return false;
        if (description != null ? !description.equals(route1.description) : route1.description != null)
            return false;
        if (metadata != null ? !metadata.equals(route1.metadata) : route1.metadata != null)
            return false;
        if (name != null ? !name.equals(route1.name) : route1.name != null) return false;
        if (route != null ? !route.equals(route1.route) : route1.route != null) return false;
        if (routeSegmentLengths != null ? !routeSegmentLengths.equals(route1.routeSegmentLengths) : route1.routeSegmentLengths != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (route != null ? route.hashCode() : 0);
        result = 31 * result + (routeSegmentLengths != null ? routeSegmentLengths.hashCode() : 0);
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0);
        temp = Double.doubleToLongBits(length);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Route{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                //", route=" + route +
                //", routeSegmentLengths=" + routeSegmentLengths +
                ", metadata=" + metadata +
                ", length=" + length +
                '}';
    }
}