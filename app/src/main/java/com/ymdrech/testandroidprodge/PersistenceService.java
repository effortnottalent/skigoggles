package com.ymdrech.testandroidprodge;

import android.location.Location;

import com.ymdrech.testandroidprodge.model.Route;

import java.util.Date;

/**
 * Created by e4t on 28/02/16.
 */
public interface PersistenceService {

    class LocationRecord {
        public final Route route;
        public final Location location;
        public final Date date;
        public LocationRecord(Route route, Location location, Date date) {
            this.route = route;
            this.location = location;
            this.date = date;
        }
    }

    public void persistLocationRecord(LocationRecord location);
}
