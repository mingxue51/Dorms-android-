package entity.Map;

import com.google.android.gms.maps.model.LatLng;

public class AnyBoundsFilter implements BoundsFilter {
    @Override
    public boolean filter(LatLng latLng) {
        return true;
    }

    @Override
    public String toString() {
        return "AnyBoundsFilter{}";
    }
}
