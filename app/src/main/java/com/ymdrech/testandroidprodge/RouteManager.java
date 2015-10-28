package com.ymdrech.testandroidprodge;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.j256.ormlite.dao.Dao;
import com.ymdrech.testandroidprodge.model.Coordinate;
import com.ymdrech.testandroidprodge.model.DatabaseHelper;
import com.ymdrech.testandroidprodge.model.Route;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Created by e4t on 2/16/2015.
 */
public class RouteManager {

//    private String routeRootXpath = "//kml:Folder/kml:Placemark";
    private String routeRootXpath = "//Folder/Placemark";
    private String routeNameXpathFragment = "name";
    private String routeDescriptionXpathFragment = "description";
    private String routeCoordsXpathFragment = "LineString/coordinates";
    private static final String DELIM_LATLONG_GROUP = " ";
    private static final String DELIM_LATLONG_ELEMENT = ",";
    private String fileExtension = "kml";
    private static final double MAX_DIST_FROM_RUN = 10.0;
    private String kmlPath;
    private Context context;
    private List<Route> routes = new ArrayList<>();
    private Route previousClosestRoute;
    private DatabaseHelper databaseHelper;

    public void refreshRoutes() {
        loadRoutesFromDb();
        try {
            routes = getRoutes(kmlPath);
        } catch (IOException e) {
            Log.e(getClass().getName(), "Problem refreshing routes", e);
        }
    }

    public void setKmlRoot(String kmlPath) {
        this.kmlPath = kmlPath;
    }

    public List<Route> getRoutes(String kmlPath) throws IOException {
        List<Route> routes = new ArrayList<Route>();
        AssetManager assetManager = context.getAssets();
        String[] items = assetManager.list(kmlPath);
        for(String item : items) {
            if(item.endsWith(fileExtension)) {
                InputSource inputSource = new InputSource(assetManager.open(item));
                List<Route> generatedRoutes = generateRoutes(inputSource);
                if(generatedRoutes != null) {
                    routes.addAll(generatedRoutes);
                }
            }
        }
        return routes;
    }

    public RouteManager(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    private void loadRoutesFromDb() {
        try {
            Dao routeDao = databaseHelper.getDao(Route.class);
            routes = routeDao.queryForAll();
            Log.i(getClass().getName(), "loaded " + routes.size() + " routes from DB");
        } catch (SQLException e) {
            Log.e(getClass().getName(), "Problems retrieving routes from db", e);
        }
    }

    private List<Route> generateRoutes(InputSource inputSource) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputSource);
            XPathFactory xFactory = XPathFactory.newInstance();
            XPath xpath = xFactory.newXPath();
            NodeList nodeList = (NodeList) xpath.evaluate(routeRootXpath, document, XPathConstants.NODESET);
            for (int i=0; i<nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Route route = new Route();
                String routeName = xpath.evaluate(routeNameXpathFragment, node);
                boolean alreadyLoadedRoute = false;
                for(Route loadedRoute : routes) {
                    if (loadedRoute.getName().equals(routeName)) {
                        alreadyLoadedRoute = true;
                        Log.i(getClass().getName(), "Found " + routeName + " previously loaded from DB");
                        break;
                    }
                }
                if(!alreadyLoadedRoute) {
                    Log.i(getClass().getName(), "Loading route " + routeName);
                    try {
                        route.setName(routeName);
                        route.setDescription(xpath.evaluate(routeDescriptionXpathFragment, node));
                        String rawLatLngString = xpath.evaluate(routeCoordsXpathFragment, node);
                        for (LatLng latLng : buildLatLongFromCoordsString(rawLatLngString)) {
                            Coordinate gpsPos = new Coordinate();
                            gpsPos.setLatLng(latLng);
                            route.addRoute(gpsPos);
                        }
                        calculateRouteAndPortionLengths(route);
                        addMetadata(route);
                        routes.add(route);
                    } catch (BadRouteException e) {
                        Log.i(getClass().getName(), "Problem building route", e);
                    }
                    databaseHelper.getDao(Route.class).create(route);
                }
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), "Problem getting routes from XML at InputSource " + inputSource +
                    " and/or saving", e);
        }
        return routes;
    }

    private void calculateRouteAndPortionLengths(Route route) {
        double routeLength = 0.0;
        if(route.getRouteCoordinates().size() != 0) {
            Coordinate previousGpsPos = route.getRouteCoordinates().iterator().next();
            for (Coordinate gpsPos : route.getRouteCoordinates()) {
                double routeSegmentLength = SphericalUtil.computeDistanceBetween(
                        previousGpsPos.getLatLng(), gpsPos.getLatLng());
                route.addRouteSegmentLength(routeSegmentLength);
                routeLength += routeSegmentLength;
                previousGpsPos = gpsPos;
            }
        }
        route.setLength(routeLength);
    }

    private void addMetadata(Route route) {
        addRouteGradeMetadata(route);
        addLiftTypeMetadata(route);
    }

    private void addRouteGradeMetadata(Route route) {
        List<String> grades = new ArrayList<String>() {{
            add("red");
            add("green");
            add("blue");
            add("black");
        }};
        for(String grade : grades) {
            if(route.getDescription().equalsIgnoreCase(grade)) {
                route.addMetadata(grade);
                route.addMetadata("run");
            }

        }
    }

    private void addLiftTypeMetadata(Route route) {
        List<String> liftTypes = new ArrayList<String>() {{
            add("gondola");
            add("drag");
            add("chair");
            add("button");
            add("bucket");
        }};
        for(String liftType : liftTypes) {
            if(route.getDescription().equalsIgnoreCase(liftType)) {
                route.addMetadata(liftType);
                route.addMetadata("lift");
            }

        }
    }

    private List<LatLng> buildLatLongFromCoordsString(String rawString) {
        List<LatLng> latLngs = new ArrayList<LatLng>();
        for(String singleLatLongGroup : rawString.split(DELIM_LATLONG_GROUP)) {
            String[] singleLatLng = singleLatLongGroup.split(DELIM_LATLONG_ELEMENT);
            if(singleLatLng.length != 3) {
                continue;
            }
            LatLng latLng = new LatLng(Double.parseDouble(singleLatLng[1]), Double.parseDouble(singleLatLng[0]));
            latLngs.add(latLng);
        }
        return latLngs;
    }

    public Route closestRoute(LatLng latLng) {
        List<Route> routesWithinRange = new ArrayList<Route>();
        // first get all routes within range
        for(Route route : routes) {
            List<LatLng> latLngs = new ArrayList<LatLng>();
            for(Coordinate gpsPos : route.getRouteCoordinates()) {
                latLngs.add(gpsPos.getLatLng());
            }
            if(PolyUtil.isLocationOnEdge(latLng, latLngs, false, MAX_DIST_FROM_RUN)) {
                routesWithinRange.add(route);
            }
        }
        // then pick most valid route, given previous closest
        // TODO: do some clever stuff with altitude changes
        for(Route route : routesWithinRange) {
            if(previousClosestRoute == null) {
                previousClosestRoute = route;
                return route;
            } else {
                if(route == previousClosestRoute) {
                    return route;
                }
            }
        }
        if(routesWithinRange.size() != 0) {
            return routesWithinRange.get(0);
        } else {
            return null;
        }
    }

}
