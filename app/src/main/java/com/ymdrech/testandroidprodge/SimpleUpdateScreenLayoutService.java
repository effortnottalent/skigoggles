package com.ymdrech.testandroidprodge;

import android.location.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by e4t on 25/02/16.
 */
public class SimpleUpdateScreenLayoutService extends AbstractUpdateScreenLayoutService {

    private LocationService locationService;
    private FriendService friendService;

    @Override
    public Map<String, Object> retrieveData() {

        LocationService.InstantLocation instantLocation = locationService.getInstantLocation();
        LocationService.CurrentSessionStats currentSessionStats =
                locationService.getCurrentSessionStats();

        Map<String, Object> screenLayoutData = new HashMap<>();

        if(instantLocation != null) {
            screenLayoutData.put("speed",
                    instantLocation.location.hasSpeed() ?
                            instantLocation.location.getSpeed() : instantLocation.calculatedSpeed);
            if (instantLocation.location.hasBearing()) {
                screenLayoutData.put("direction", instantLocation.location.getBearing());
            }
        } else {
            screenLayoutData.put("speed", "N/A");
            screenLayoutData.put("direction", "N/A");
        }
        if(currentSessionStats != null) {
            screenLayoutData.put("runs", currentSessionStats.numberRuns);
            screenLayoutData.put("distance-down", currentSessionStats.distanceDownMetres);
        } else {
            screenLayoutData.put("runs", "N/A");
            screenLayoutData.put("distance-down", "N/A");
        }
        screenLayoutData.put("message-notification", friendService.getMessages().size() != 0);
        return screenLayoutData;

    }

    @Override
    public void setLocationService(LocationService locationService) {
        this.locationService = locationService;
    }

    @Override
    public void setFriendService(FriendService friendService) {
        this.friendService = friendService;
    }

}
