package com.ymdrech.testandroidprodge;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.ymdrech.testandroidprodge.model.Route;

import java.util.Date;
import java.util.List;

/**
 * Created by e4t on 25/02/16.
 */
public interface LocationService {

    class InstantLocation {
        public final double calculatedSpeed;
        public final Location location;
        public final Date date;
        InstantLocation(double calculatedSpeed, Location location, Date date){
            this.calculatedSpeed = calculatedSpeed;
            this.location = location;
            this.date = date;
        }
    }

    class CurrentSessionStats {
        public final int numberRuns;
        public final int distanceDownMetres;
        public final int distanceUpMetres;
        public final int averageSpeedDown;
        public final int timeSeconds;

        public CurrentSessionStats(int numberRuns, int distanceDownMetres, int distanceUpMetres, int averageSpeedDown, int timeSeconds) {
            this.numberRuns = numberRuns;
            this.distanceDownMetres = distanceDownMetres;
            this.distanceUpMetres = distanceUpMetres;
            this.averageSpeedDown = averageSpeedDown;
            this.timeSeconds = timeSeconds;
        }
    }

    public CurrentSessionStats getCurrentSessionStats();
    public List<Route> getNearbyRoutes();
    public Route getClosestRoute();
    public InstantLocation getInstantLocation();
    public void updateCurrentLocation(Location location, Date date);
}
