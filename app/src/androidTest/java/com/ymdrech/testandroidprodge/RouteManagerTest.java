package com.ymdrech.testandroidprodge;

import android.app.Activity;
import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;

import com.ymdrech.testandroidprodge.model.Route;

import junit.framework.Assert;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by e4t on 2/16/2015.
 */
public class RouteManagerTest extends InstrumentationTestCase {

    public void testThreeValleysKml() throws IOException {

        Activity activity = mock(Activity.class);
        AssetManager assetManager = mock(AssetManager.class);
        when(activity.getAssets()).thenReturn(assetManager);
        when(assetManager.open(anyString())).thenReturn(new FileInputStream("src/main/assets/3 Valleys Pistemap v1.0.kml"));

        RouteManager routeManager = new RouteManager(activity);
        List<Route> routes = routeManager.getRoutes("3 Valleys Pistemap v1.0.kml");
        Assert.assertEquals(routes.size(), 10);

    }

}
