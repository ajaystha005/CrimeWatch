package com.mvp.crimewatch.utils;

import com.google.android.gms.maps.model.LatLng;
import com.mvp.crimewatch.coordinate.proj.Projection;
import com.mvp.crimewatch.coordinate.proj.ProjectionFactory;
import com.mvp.crimewatch.helper.Point2D;

/**
 * Created by ajayshrestha on 3/19/17.
 */

public class CoordinateUtils {

    private static final double FEET_TO_METER = .3048;
    private static final String ILLINOIS_STATE_PLANE = "nad83:1201";

    /**
     * Get the Project - nad83
     *
     * @return
     */
    private static Projection getProjection(String projection) {
        return ProjectionFactory.getNamedPROJ4CoordinateSystem(projection);
    }

    /**
     * Get LAT LNG based on state plane Coordinate
     *
     * @param xCoordinate
     * @param yCoordinate
     * @return
     */
    public static LatLng getLatLng(double xCoordinate, double yCoordinate) {
        xCoordinate *= FEET_TO_METER;
        yCoordinate *= FEET_TO_METER;

        Point2D statPlanePoint = new Point2D(xCoordinate, yCoordinate);
        Point2D origin = new Point2D(0, 0);
        getProjection(ILLINOIS_STATE_PLANE).inverseTransform(statPlanePoint, origin);
        return new LatLng(origin.getY(), origin.getX());
    }
}
