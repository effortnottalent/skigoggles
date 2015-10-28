package com.ymdrech.testandroidprodge.model;

import android.location.Location;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by e4t on 3/18/2015.
 */
@DatabaseTable
public class DataPoint {

    @DatabaseField(generatedId = true) private int id;
    @DatabaseField(dataType = DataType.SERIALIZABLE) private SerializableLocation location;
    @DatabaseField(foreign = true) private Route route;
    @DatabaseField(foreign = true) private Session session;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = new SerializableLocation(location);
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
