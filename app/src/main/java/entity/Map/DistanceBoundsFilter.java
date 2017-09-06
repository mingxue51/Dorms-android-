package entity.Map;

import com.google.android.gms.maps.model.LatLng;

import entity.Map.Location.Distance.DistanceHelper;

public class DistanceBoundsFilter implements BoundsFilter {
    private static final double MAX_DISTANCE = 3000d; // 3km

    private final LatLng point;

    public DistanceBoundsFilter(LatLng start) {
        this.point = start;
    }

    @Override
    public boolean filter(LatLng latLng) {
        return DistanceHelper.distanceBetween(point, latLng) <= MAX_DISTANCE;
    }

    @Override
    public String toString() {
        return "DistanceBoundsFilter{" +
                "point=" + point +
                '}';
    }
}
