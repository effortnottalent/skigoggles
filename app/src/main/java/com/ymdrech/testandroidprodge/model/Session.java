package com.ymdrech.testandroidprodge.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by e4t on 3/6/2015.
 */
@DatabaseTable
public class Session {

    @DatabaseField(generatedId = true) private int id;
    @DatabaseField private String name;
    @DatabaseField private Date dateStarted;
    @DatabaseField private Date dateEnded;
    @ForeignCollectionField private Collection<DataPoint> dataPoints = new ArrayList<>();
    @DatabaseField private boolean isActive;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
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

    public Date getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(Date dateStarted) {
        this.dateStarted = dateStarted;
    }

    public Date getDateEnded() {
        return dateEnded;
    }

    public void setDateEnded(Date dateEnded) {
        this.dateEnded = dateEnded;
    }

    public List<DataPoint> getDataPoints() {
        return new ArrayList<DataPoint>(dataPoints);
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public void addDataPoint(DataPoint dataPoint) {
        this.dataPoints.add(dataPoint);
    }
}
