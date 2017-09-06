package entity.Map.Location;

import entity.Generic.GeoLocation;
import entity.Map.Location.Distance.Distance;
import service.PointInfo;

public interface LocationI {
    public Distance calculateDistance(GeoLocation start, GeoLocation end);
    public PointInfo getCurrentLocation();
}