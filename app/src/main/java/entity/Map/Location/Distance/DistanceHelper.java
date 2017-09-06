package entity.Map.Location.Distance;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

import java.math.BigDecimal;

import entity.Generic.GeoLocation;
import entity.Map.Location.Distance.Distance.Unit;
import entity.Map.Location.LocationI;
import entity.Map.MapFabric;
import entity.Map.MapProvider;
import helper.App;
import helper.Session;
import service.PointInfo;

public class DistanceHelper {
    public static boolean fakeLocation = false;

    private static final float KMS_TO_MILES_FACTOR = 0.621371192f;

    public static final String FAKE_COORDS_PREFIX = "FAKE_COORDS";
    public static final int METERS_IN_KILOMETERS = 1000;
    public static final int MINIMUM_DISTANCE_IN_KILOMETERS = 50;

    public static final double DEBUG_LONDON_LATITUDE = 51.500152;
    public static final double DEBUG_LONDON_LONGIITUDE = -0.126236;

    public static Distance getDistance(GeoLocation start, GeoLocation end) {
        final String distanceType = Distance.getDistanceType(App.getInstance().getBaseContext());
        final Unit settingsUnit = Unit.getByType(distanceType);
        final Distance distanceInKm = getDistanceInKm(start, end);

        if (distanceInKm.getUnit().equals(settingsUnit)) {
            return distanceInKm;
        }

        final float miles = distanceInKm.getValue().floatValue() * KMS_TO_MILES_FACTOR;
        return new Distance(BigDecimal.valueOf(miles), Unit.MILES);
    }

    private static Distance getDistanceInKm(GeoLocation start, GeoLocation end) {
        Distance result = null;
        try {
            if (start != null && end != null) {
                Location from = new Location("mc");
                Location to = new Location("mc");

                to.setLongitude(end.getLontitude());
                to.setLatitude(end.getLatitude());

                from.setLongitude(start.getLontitude());
                from.setLatitude(start.getLatitude());

                float distance = from.distanceTo(to) / METERS_IN_KILOMETERS;
                if (distance < 0) {
                    distance = distance * (-1);
                }
                result = new Distance(new BigDecimal(distance), Unit.KM);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static float getDistance(LatLng start, LatLng finish) {
        Location from = new Location("mc");
        Location to = new Location("mc");

        to.setLongitude(finish.longitude);
        to.setLatitude(finish.latitude);

        from.setLongitude(start.longitude);
        from.setLatitude(start.latitude);

        float distance = from.distanceTo(to) / METERS_IN_KILOMETERS;
        if (distance < 0) {
            distance = distance * (-1);
        }
        return distance;
    }

    public static GeoLocation debugLocation() {
        return new GeoLocation(DistanceHelper.DEBUG_LONDON_LONGIITUDE, DistanceHelper.DEBUG_LONDON_LATITUDE);
    }

    public static GeoLocation convertPointInfoToGeoLocation(PointInfo pi) {
        GeoLocation location = new GeoLocation();
        try {
            if (pi.getLocation() != null) {
                location.setLatitude(pi.getLocation().getLatitude());
                location.setLontitude(pi.getLocation().getLongitude());
            }
        } catch (Exception e) {
        }
        return location;
    }

    public static GeoLocation getCurrentLocation(Context context) {
        GeoLocation currentLocation;
        if (fakeLocation) {

            String lat = Session.getStringValue(App.getInstance().getBaseContext(), FAKE_COORDS_PREFIX + "lat");
            String lon = Session.getStringValue(App.getInstance().getBaseContext(), FAKE_COORDS_PREFIX + "lon");

            if (lat == null || lon == null || lat.isEmpty() || lon.isEmpty()) {
                currentLocation = DistanceHelper.debugLocation();
            } else {
                currentLocation = new GeoLocation(Double.parseDouble(lon), Double.parseDouble(lat));
            }

        } else {
            LocationI mLocationManager = new MapFabric(context).getLocation(MapProvider.GOOGLE.PROVIDER);
            currentLocation = DistanceHelper.convertPointInfoToGeoLocation(mLocationManager.getCurrentLocation());
        }
        return currentLocation;
    }

    public static LatLng getCurrentLocation() {
        return getCurrentLocation(App.getInstance()).toLatLng();
    }

    public static float distanceBetween(LatLng latLng1, LatLng latLng2) {
        Location loc1 = new Location(LocationManager.GPS_PROVIDER);
        Location loc2 = new Location(LocationManager.GPS_PROVIDER);

        loc1.setLatitude(latLng1.latitude);
        loc1.setLongitude(latLng1.longitude);

        loc2.setLatitude(latLng2.latitude);
        loc2.setLongitude(latLng2.longitude);

        return loc1.distanceTo(loc2);
    }
}
