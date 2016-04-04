package com.ymdrech.testandroidprodge;

import android.location.Location;

import java.text.DecimalFormat;
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
        screenLayoutData.put("direction", "N/A");
        screenLayoutData.put("speed", "N/A");
        screenLayoutData.put("runs", "N/A");
        screenLayoutData.put("distance-down", "N/A");
        screenLayoutData.put("message-notification", false);

        if(instantLocation != null) {
            double speed = instantLocation.location.hasSpeed() ?
                    instantLocation.location.getSpeed() : instantLocation.calculatedSpeed;
            screenLayoutData.put("speed", new DecimalFormat("0.0").format(speed));
            if (instantLocation.location.hasBearing()) {
                screenLayoutData.put("direction", instantLocation.location.getBearing());
            }
        }
        if(currentSessionStats != null) {
            screenLayoutData.put("runs", currentSessionStats.numberRuns);
            screenLayoutData.put("distance-down", currentSessionStats.distanceDownMetres);
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
