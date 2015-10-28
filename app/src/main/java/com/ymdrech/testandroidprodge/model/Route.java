package com.ymdrech.testandroidprodge.model;

import com.j256.ormlite.field.DataType;
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
    @DatabaseField(dataType = DataType.SERIALIZABLE) private ArrayList<Coordinate> routeCoordinates = new ArrayList<>();
    @DatabaseField(dataType = DataType.SERIALIZABLE) private ArrayList<Double> routeSegmentLengths = new ArrayList<>();
    @DatabaseField(dataType = DataType.SERIALIZABLE) private ArrayList<String> metadata = new ArrayList<>();
    @DatabaseField private double length;
    @DatabaseField(foreign = true) private Session session;

    public Session getSession() {
        return session;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Coordinate> getRouteCoordinates() {
        return routeCoordinates;
    }

    public void addRoute(Coordinate gpsPos) {
        routeCoordinates.add(gpsPos);
    }

    public List<Double> getRouteSegmentLengths() {
        return routeSegmentLengths;
    }

    public void addRouteSegmentLength(Double routeSegmentLength) {
        routeSegmentLengths.add(routeSegmentLength);
    }

    public List<String> getMetadata() {
        return metadata;
    }

    public void addMetadata(String metadataItem) {
        metadata.add(metadataItem);
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", routeSegmentLengths=" + routeSegmentLengths.size() +
                ", metadata=" + metadata +
                ", length=" + length +
                ", routeCoordinates=" + routeCoordinates.size() +
                '}';
    }

    public void setRouteCoordinates(ArrayList<Coordinate> routeCoordinates) {
        this.routeCoordinates = routeCoordinates;
    }

    public void setRouteSegmentLengths(ArrayList<Double> routeSegmentLengths) {
        this.routeSegmentLengths = routeSegmentLengths;
    }

    public void setMetadata(ArrayList<String> metadata) {
        this.metadata = metadata;
    }
}