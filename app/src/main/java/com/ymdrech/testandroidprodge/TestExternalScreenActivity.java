package com.ymdrech.testandroidprodge;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.ymdrech.testandroidprodge.model.Route;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by e4t on 28/02/16.
 */
public class TestExternalScreenActivity
        extends FragmentActivity
        implements SurfaceHolder.Callback {

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        final LocationService locationService = new DefaultLocationService();
        locationService.setRoutesService(new RoutesService() {
            @Override
            public List<Route> getRoutes() {
                return new ArrayList<Route>();
            }
        });
        locationService.setPersistenceService(new PersistenceService() {
            @Override
            public void persistLocationRecord(LocationRecord location) {

            }
        });

        LocationManager locationManager =
                (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                Date locationChangedDate = new Date();
                locationService.updateCurrentLocation(location, locationChangedDate);
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        final FriendService friendService = new StubFriendService();
        final AbstractUpdateScreenLayoutService updateScreenLayoutService =
                new SimpleUpdateScreenLayoutService();
        updateScreenLayoutService.setLocationService(locationService);
        updateScreenLayoutService.setFriendService(friendService);
        final WriteDataToScreen writeDataToScreen = new SurfaceHolderWriteDataToScreen();
        writeDataToScreen.setSurfaceHolder(holder);
        writeDataToScreen.setAreaHeight(64);
        writeDataToScreen.setAreaWidth(96);
        writeDataToScreen.setZoomMultiplier(4);

        final AbstractDrawScreenLayoutService drawScreenLayoutService =
                new SimpleGridDrawScreenLayoutService();
        drawScreenLayoutService.setUpdateScreenLayoutService(updateScreenLayoutService);
        drawScreenLayoutService.setWriteDataToScreen(writeDataToScreen);

        final AppSessionStore appSessionStore = new AppSessionStore();
        appSessionStore.setScreenLayouts(new ArrayList<AbstractDrawScreenLayoutService>() {{
            add(drawScreenLayoutService);
        }});

        final ScreenUpdateThread screenUpdateThread = new ScreenUpdateThread();
        screenUpdateThread.setAppSessionStore(appSessionStore);

        screenUpdateThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.eyescreen);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

    }

}
