package entity.Map;

import com.google.android.gms.maps.model.LatLng;

public interface BoundsFilter {
    boolean filter(LatLng latLng);
}
