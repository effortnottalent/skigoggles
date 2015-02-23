package com.ymdrech.testandroidprodge;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements SurfaceHolder.Callback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private DrawScreenThread drawThread;
    private ScreenData screenData = new ScreenData();
    private Marker marker;
    private float zoomLevel = 13;
    private RouteManager routeManager;
    private String kmlRoot = "";

    public float getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(float zoomLevel) {
        this.zoomLevel = zoomLevel;
        // https://code.google.com/p/android/issues/detail?id=82997
        if(zoomLevel == 14F) {
            zoomLevel = 13.9F;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        setUpMapIfNeeded();
        surfaceView = (SurfaceView) findViewById(R.id.eyescreen);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        buildGoogleApiClient();
        createLocationRequest();
        initialiseRouteManager();
    }

    private void initialiseRouteManager() {

        routeManager = new RouteManager(this);
        routeManager.setKmlRoot(kmlRoot);
        routeManager.refreshRoutes();

    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(250);
        locationRequest.setFastestInterval(250);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #googleMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #googleMap} is not null.
     */
    private void setUpMap() {
        //LatLng startLatLng = new LatLng(51.5072, -3.5784);
        LatLng startLatLng = new LatLng(45.430774, 6.59979);
        marker = googleMap.addMarker(new MarkerOptions().position(startLatLng).title("Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, getZoomLevel()));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawScreenThread(surfaceHolder);
        drawThread.setScreenData(screenData);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean noRetry = false;
        while(!noRetry) {
            try {
                drawThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            noRetry = true;
        }
    }

    protected void startLocationUpdates() {
        Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        screenData.setLocation(lastKnownLocation);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, this);
        onLocationChanged(lastKnownLocation);
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        screenData.setLocation(location);
        if(marker == null) {
            setUpMap();
        }
        if(location != null) {
            LatLng markerLatLong = new LatLng(location.getLatitude(), location.getLongitude());
            marker.setPosition(markerLatLong);
            screenData.setRoute(routeManager.closestRoute(markerLatLong));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLong, getZoomLevel()));
        }
    }

}
