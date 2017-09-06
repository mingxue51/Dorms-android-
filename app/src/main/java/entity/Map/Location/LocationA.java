package entity.Map.Location;

import android.content.Context;
import android.location.LocationManager;

import java.math.BigDecimal;

import entity.Generic.GeoLocation;
import entity.Map.Location.Distance.Distance;
import helper.Root;
import service.ApplicationService;
import service.PointInfo;

public abstract class LocationA extends Root {
    private static final long serialVersionUID = 1L;
    private ApplicationService mAppService;
    public LocationA(Context context) {
        super(context);
    }

    public Distance calculateDistance(GeoLocation start, GeoLocation end) {
        Distance lDistance = new Distance();
        double fromLat = start.getLatitude();
        double fromLong = start.getLontitude();
        double toLat = end.getLatitude();
        double toLong= end.getLontitude();
        double d2r = Math.PI / 180;
        double dLong = (toLong - fromLong) * d2r;
        double dLat = (toLat - fromLat) * d2r;
        double a = Math.pow(Math.sin(dLat / 2.0), 2) + Math.cos(fromLat * d2r)
                * Math.cos(toLat * d2r) * Math.pow(Math.sin(dLong / 2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6367000 * c;
        lDistance.setValue(new BigDecimal(Math.round(d))); //return Math.round(d);
        return lDistance;
    }

    protected PointInfo getCurrentLocation() {
        PointInfo mPoint = null;


        mAppService = new ApplicationService(getContext());
        boolean network = mAppService.isAbleToLocate(LocationManager.NETWORK_PROVIDER);
        boolean gps = mAppService.isAbleToLocate(LocationManager.GPS_PROVIDER);

        if (network) {
            mPoint = mAppService.getLocation(LocationManager.NETWORK_PROVIDER);
        } else if (gps) {
            mPoint = mAppService.getLocation(LocationManager.GPS_PROVIDER);
        }

        return mPoint;
    }


}
