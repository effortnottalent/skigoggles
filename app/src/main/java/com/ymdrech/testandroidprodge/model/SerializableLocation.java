package com.ymdrech.testandroidprodge.model;

import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Printer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by e4t on 3/10/2015.
 */
public class SerializableLocation extends Location implements Serializable {
    @Override
    public void set(Location l) {
        super.set(l);
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public float distanceTo(Location dest) {
        return super.distanceTo(dest);
    }

    @Override
    public float bearingTo(Location dest) {
        return super.bearingTo(dest);
    }

    @Override
    public String getProvider() {
        return super.getProvider();
    }

    @Override
    public void setProvider(String provider) {
        super.setProvider(provider);
    }

    @Override
    public long getTime() {
        return super.getTime();
    }

    @Override
    public void setTime(long time) {
        super.setTime(time);
    }

    @Override
    public long getElapsedRealtimeNanos() {
        return super.getElapsedRealtimeNanos();
    }

    @Override
    public void setElapsedRealtimeNanos(long time) {
        super.setElapsedRealtimeNanos(time);
    }

    @Override
    public double getLatitude() {
        return super.getLatitude();
    }

    @Override
    public void setLatitude(double latitude) {
        super.setLatitude(latitude);
    }

    @Override
    public double getLongitude() {
        return super.getLongitude();
    }

    @Override
    public void setLongitude(double longitude) {
        super.setLongitude(longitude);
    }

    @Override
    public boolean hasAltitude() {
        return super.hasAltitude();
    }

    @Override
    public double getAltitude() {
        return super.getAltitude();
    }

    @Override
    public void setAltitude(double altitude) {
        super.setAltitude(altitude);
    }

    @Override
    public void removeAltitude() {
        super.removeAltitude();
    }

    @Override
    public boolean hasSpeed() {
        return super.hasSpeed();
    }

    @Override
    public float getSpeed() {
        return super.getSpeed();
    }

    @Override
    public void setSpeed(float speed) {
        super.setSpeed(speed);
    }

    @Override
    public void removeSpeed() {
        super.removeSpeed();
    }

    @Override
    public boolean hasBearing() {
        return super.hasBearing();
    }

    @Override
    public float getBearing() {
        return super.getBearing();
    }

    @Override
    public void setBearing(float bearing) {
        super.setBearing(bearing);
    }

    @Override
    public void removeBearing() {
        super.removeBearing();
    }

    @Override
    public boolean hasAccuracy() {
        return super.hasAccuracy();
    }

    @Override
    public float getAccuracy() {
        return super.getAccuracy();
    }

    @Override
    public void setAccuracy(float accuracy) {
        super.setAccuracy(accuracy);
    }

    @Override
    public void removeAccuracy() {
        super.removeAccuracy();
    }

    @Override
    public Bundle getExtras() {
        return super.getExtras();
    }

    @Override
    public void setExtras(Bundle extras) {
        super.setExtras(extras);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void dump(Printer pw, String prefix) {
        super.dump(pw, prefix);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
    }

    @Override
    public boolean isFromMockProvider() {
        return super.isFromMockProvider();
    }

    public SerializableLocation(String provider) {
        super(provider);
    }

    public SerializableLocation(Location l) {
        super(l);
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeDouble(getLatitude());
        out.writeDouble(getLongitude());
        out.writeFloat(getAccuracy());
        out.writeDouble(getAltitude());
        out.writeFloat(getBearing());
        out.writeLong(getElapsedRealtimeNanos());
        out.writeFloat(getSpeed());
        out.writeLong(getTime());
        out.writeObject(getProvider());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if(in.available() != 0) {
            setLatitude(in.readDouble());
            setLongitude(in.readDouble());
            setAccuracy(in.readFloat());
            setAltitude(in.readDouble());
            setBearing(in.readFloat());
            setElapsedRealtimeNanos(in.readLong());
            setSpeed(in.readFloat());
            setTime(in.readLong());
            setProvider(in.readObject().toString());
        }
    }

}
