package entity.Map.Location;

import android.content.Context;

import entity.Generic.GeoLocation;
import entity.Map.Location.Distance.Distance;
import service.PointInfo;

public  class LocationGoogle extends LocationA implements LocationI{

    public LocationGoogle(Context context)
    {
        super(context);
    }

    @Override
    public Distance calculateDistance(GeoLocation start, GeoLocation end) {
      return   super.calculateDistance(start, end);
    }

    @Override
    public PointInfo getCurrentLocation() {
        return super.getCurrentLocation();
    }
}