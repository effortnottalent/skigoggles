package com.ymdrech.testandroidprodge;

import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.maps.android.SphericalUtil;
import com.j256.ormlite.dao.Dao;
import com.ymdrech.testandroidprodge.data.DatabaseHelper;
import com.ymdrech.testandroidprodge.data.Route;
import com.ymdrech.testandroidprodge.data.Session;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements SurfaceHolder.Callback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private List<DrawScreenThread> drawThreadList = new ArrayList<DrawScreenThread>();
    private Marker marker;
    private float zoomLevel = 13;
    private RouteManager routeManager;
    private String kmlRoot = "";
    private static final int LOCATION_REQUEST_INTERVAL = 333;
    private Location previousLocation;
    private DatabaseHelper databaseHelper;
    private Session currentSession;

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
        initialiseDatabase();

    }

    private void initialiseDatabase() {
        databaseHelper = new DatabaseHelper(this);
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
        locationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_REQUEST_INTERVAL);
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
        DrawScreenThread drawThread1 = new SpeedTextOnScreenDrawScreenThread();
        drawThread1.setScreen(new OnScreenWriteDataToScreenImpl(surfaceHolder, 96, 64, 4, 0, 0));
        drawThread1.setRunning(true);
        drawThread1.run();
        drawThreadList.add(drawThread1);
        DrawScreenThread drawThread2 = new SpeedoOnScreenDrawScreenThread();
        drawThread2.setScreen(new OnScreenWriteDataToScreenImpl(surfaceHolder, 96, 64, 4, (96+4)*4, 0));
        drawThread2.setRunning(true);
        drawThread2.run();
        drawThreadList.add(drawThread2);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(drawThreadList != null) {
            for(DrawScreenThread drawScreenThread : drawThreadList) {
                drawScreenThread.setRunning(false);
            }
        }
    }

    protected void startLocationUpdates() {
        Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
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
        addLocationToSession(location);
        ScreenData screenData = new ScreenData();
        screenData.setLocation(location);
        if(previousLocation != null) {
            screenData.setCalculatedSpeed(getInstantaneousSpeed(previousLocation, location));
        }
        if(marker == null) {
            setUpMap();
        }
        if(location != null) {
            LatLng markerLatLong = new LatLng(location.getLatitude(), location.getLongitude());
            marker.setPosition(markerLatLong);
            screenData.setRoute(routeManager.closestRoute(markerLatLong));
            if(screenData.getRoute() != null) {
                screenData.setAmountComplete(getPercentageRouteComplete(location, screenData.getRoute()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLong, getZoomLevel()));
            }
            if(drawThreadList != null) {
                for(DrawScreenThread drawScreenThread : drawThreadList) {
                    drawScreenThread.setScreenData(screenData);
                }
            }
            previousLocation = location;
            Log.d(getClass().getName(), "updating screenData to " + screenData);
        }
    }

    private Session createNewSession() {
        Session session = new Session();
        session.setDateStarted(new Date());
        return session;
    }

    private void assignCurrentSession() throws SQLException {
        if(currentSession == null) {
            Dao sessionDao = databaseHelper.getDao(Session.class);
            List<Session> sessions = sessionDao.queryForEq("isActive", true);
            if(sessions.size() == 0) {
                currentSession = createNewSession();
            } else {
                currentSession = sessions.get(sessions.size() - 1);
                if (sessions.size() != 1) {
                    Log.w(getClass().getName(), "Got more than one active session, so simply picking the " +
                            "first one as active and setting the others to inactive. This shouldn't " +
                            "happen very often...");
                    for (int i = sessions.size() - 1; i > 1; i--) {
                        Session session = sessions.get(i);
                        session.setActive(false);
                        sessionDao.update(session);
                    }
                }
            }
        }
    }

    private void addLocationToSession(Location location) {
        try {
            assignCurrentSession();
            updateCurrentSession(location);
        } catch (SQLException e) {
            Log.e(getClass().getName(), "Problems adding location to session", e);
        }
    }

    private void updateCurrentSession(Location location) throws SQLException {
        currentSession.addLocation(location);
        Dao sessionDao = databaseHelper.getDao(Session.class);
        sessionDao.update(currentSession);
    }

    private double getInstantaneousSpeed(Location beforeLocation, Location afterLocation) {
        double distance = SphericalUtil.computeDistanceBetween(
                new LatLng(beforeLocation.getLatitude(), beforeLocation.getLongitude()),
                new LatLng(afterLocation.getLatitude(), afterLocation.getLongitude()));
        double time = afterLocation.getElapsedRealtimeNanos() - beforeLocation.getElapsedRealtimeNanos();
        return distance / time * 1e9;
    }

    private double getPercentageRouteComplete(Location location, Route route) {
        int routeLatLngIndex = -1;
        double distanceToLatLngAtIndex = Double.POSITIVE_INFINITY;
        for(int i=0; i < route.getRoute().size(); i++) {
            double distance = SphericalUtil.computeDistanceBetween(
                    new LatLng(location.getLatitude(), location.getLongitude()),
                    route.getRoute().get(i));
            if(distance < distanceToLatLngAtIndex) {
                routeLatLngIndex = i;
                distanceToLatLngAtIndex = distance;
            }
        }
        double distance = 0.0;
        for(int i=routeLatLngIndex; i < route.getRouteSegmentLengths().size(); i++) {
            distance += route.getRouteSegmentLengths().get(i);
        }
        return distance / route.getLength();
    }

}
