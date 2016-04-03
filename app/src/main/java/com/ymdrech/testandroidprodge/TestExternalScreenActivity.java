package com.ymdrech.testandroidprodge;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by e4t on 28/02/16.
 */
public class TestExternalScreenActivity
        extends FragmentActivity
        implements SurfaceHolder.Callback,
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
            LocationListener {

    private LocationService locationService;

    private ScreenUpdateThread screenUpdateThread;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        final LocationService locationService = new DefaultLocationService();
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

        screenUpdateThread = new ScreenUpdateThread();
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
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Date locationChangedDate = new Date();
        locationService.updateCurrentLocation(location, locationChangedDate);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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
