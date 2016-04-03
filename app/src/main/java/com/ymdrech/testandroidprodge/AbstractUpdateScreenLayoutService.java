package com.ymdrech.testandroidprodge;

import java.util.Map;

/**
 * Created by e4t on 25/02/16.
 */
public abstract class AbstractUpdateScreenLayoutService {

    private LocationService locationService;
    private FriendService friendService;

    public LocationService getLocationService() {
        return locationService;
    }

    public void setLocationService(LocationService locationService) {
        this.locationService = locationService;
    }

    public FriendService getFriendService() {
        return friendService;
    }

    public void setFriendService(FriendService friendService) {
        this.friendService = friendService;
    }

    public abstract Map<String, Object> retrieveData();
}
