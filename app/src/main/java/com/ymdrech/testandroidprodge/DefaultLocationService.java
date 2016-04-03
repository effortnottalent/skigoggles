package com.ymdrech.testandroidprodge;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.ymdrech.testandroidprodge.model.Coordinate;
import com.ymdrech.testandroidprodge.model.Route;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by e4t on 28/02/16.
 */
public class DefaultLocationService implements LocationService {

    private static final double MAX_DIST_FROM_RUN = 10.0;
    private static final double MAX_DIST_FROM_RUNS_NEARBY = 500.0;

    private RoutesService routesService;
    private PersistenceService persistenceService;

    private InstantLocation previousInstantLocation;

    private Route previousClosestRoute;

    @Override
    public CurrentSessionStats getCurrentSessionStats() {
        return null;
    }

    @Override
    public List<Route> getNearbyRoutes() {
        return getNearbyRoutes(MAX_DIST_FROM_RUNS_NEARBY);
    }

    private List<Route> getNearbyRoutes(double withinDistance) {

        LatLng latLng = new LatLng(previousInstantLocation.location.getLatitude(),
                previousInstantLocation.location.getLongitude());
        List<Route> routesWithinRange = new ArrayList<Route>();
        for(Route route : routesService.getRoutes()) {
            List<LatLng> latLngs = new ArrayList<LatLng>();
            for(Coordinate gpsPos : route.getRouteCoordinates()) {
                latLngs.add(gpsPos.getLatLng());
            }
            if(PolyUtil.isLocationOnEdge(latLng, latLngs, false, withinDistance)) {
                routesWithinRange.add(route);
            }
        }
        return routesWithinRange;
    }

    @Override
    public InstantLocation getInstantLocation() {
        return previousInstantLocation;
    }

    @Override
    public void updateCurrentLocation(Location location, Date date) {
        double calculatedSpeed = getInstantaneousSpeed(previousInstantLocation.location, location);
        previousInstantLocation = new InstantLocation(calculatedSpeed, location, date);
        persistenceService.persistLocationRecord(
                new PersistenceService.LocationRecord(getClosestRoute(), location, date));
    }

    public Route getClosestRoute() {
        List<Route> nearbyRoutes = getNearbyRoutes(MAX_DIST_FROM_RUN);
        for(Route route : nearbyRoutes) {
            if(previousClosestRoute == null) {
                previousClosestRoute = route;
                return route;
            } else {
                if(route == previousClosestRoute) {
                    return route;
                }
            }
        }
        if(nearbyRoutes.size() != 0) {
            return nearbyRoutes.get(0);
        } else {
            return null;
        }

    }

    private double getInstantaneousSpeed(Location beforeLocation, Location afterLocation) {
        double distance = SphericalUtil.computeDistanceBetween(
                new LatLng(beforeLocation.getLatitude(), beforeLocation.getLongitude()),
                new LatLng(afterLocation.getLatitude(), afterLocation.getLongitude()));
        double time = afterLocation.getElapsedRealtimeNanos() - beforeLocation.getElapsedRealtimeNanos();
        return distance / time * 1e9;
    }


}
