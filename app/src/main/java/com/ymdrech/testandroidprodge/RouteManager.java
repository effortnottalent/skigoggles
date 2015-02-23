package com.ymdrech.testandroidprodge;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
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
    private static final double MAX_DIST_FROM_RUN = 50.0;

    private String kmlPath;
    private Context context;

    private List<Route> routes;

    private Route previousClosestRoute;

    public void refreshRoutes() {
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
    }

    private List<Route> generateRoutes(InputSource inputSource) {
        List<Route> routes = new ArrayList<Route>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputSource);
            XPathFactory xFactory = XPathFactory.newInstance();
            XPath xpath = xFactory.newXPath();
//            XPathExpression xPathExpression = xpath.compile(routeRootXpath);
//            NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
            NodeList nodeList = (NodeList) xpath.evaluate(routeRootXpath, document, XPathConstants.NODESET);
            for (int i=0; i<nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Route route = new Route();
                try {
                    route.setName(xpath.evaluate(routeNameXpathFragment, node));
                    route.setDescription(xpath.evaluate(routeDescriptionXpathFragment, node));
                    String rawLatLngString = xpath.evaluate(routeCoordsXpathFragment, node);
                    route.setRoute(buildLatLongFromCoordsString(rawLatLngString));
                    routes.add(route);
                } catch (BadRouteException e) {
                    Log.i(getClass().getName(), "Problem building route", e);
                }
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), "Problem getting routes from XML at InputSource " + inputSource, e);
        }
        return routes;
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
            if(PolyUtil.isLocationOnEdge(latLng, route.getRoute(), false, MAX_DIST_FROM_RUN)) {
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
