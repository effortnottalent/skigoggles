package com.ymdrech.testandroidprodge;

import android.location.Location;

import com.ymdrech.testandroidprodge.model.Route;

/**
 * Created by e4t on 2/15/2015.
 */
public class InfoScreenDTO {

    private Location location;
    private Route route;
    private double calculatedSpeed;
    private double amountComplete;

    public double getAmountComplete() {
        return amountComplete;
    }

    public void setAmountComplete(double amountComplete) {
        this.amountComplete = amountComplete;
    }

    public double getCalculatedSpeed() {
        return calculatedSpeed;
    }

    public void setCalculatedSpeed(double calculatedSpeed) {
        this.calculatedSpeed = calculatedSpeed;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InfoScreenDTO that = (InfoScreenDTO) o;

        if (Double.compare(that.amountComplete, amountComplete) != 0) return false;
        if (Double.compare(that.calculatedSpeed, calculatedSpeed) != 0) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (Double.compare(that.location.getLatitude(), location.getLatitude()) != 0) return false;
        if (Double.compare(that.location.getLongitude(), location.getLongitude()) != 0) return false;
        if (Double.compare(that.location.getAltitude(), location.getAltitude()) != 0) return false;
        if (route != null ? !route.equals(that.route) : that.route != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = location != null ? location.hashCode() : 0;
        result = 31 * result + (route != null ? route.hashCode() : 0);
        temp = Double.doubleToLongBits(calculatedSpeed);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(amountComplete);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "InfoScreenDTO{" +
                "location=" + location +
                ", route=" + route +
                ", calculatedSpeed=" + calculatedSpeed +
                ", amountComplete=" + amountComplete +
                '}';
    }
}
