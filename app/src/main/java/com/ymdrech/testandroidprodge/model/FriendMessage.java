package com.ymdrech.testandroidprodge.model;

import android.location.Location;

import java.util.Date;

/**
 * Created by e4t on 25/02/16.
 */
public class FriendMessage {

    private String message;
    private Date dateSent;
    private Date dateRead;
    private Location location;
    private boolean read;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public Date getDateRead() {
        return dateRead;
    }

    public void setDateRead(Date dateRead) {
        this.dateRead = dateRead;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
