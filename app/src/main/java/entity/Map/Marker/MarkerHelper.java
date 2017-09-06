package entity.Map.Marker;

import com.mc.youthhostels.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import entity.Map.MapResourceProvider;

public  class MarkerHelper {

    public static MarkerOptions getCityCentreOptions(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(MapResourceProvider.getPropertyBitmap(R.drawable.icon_city_centre)));
        markerOptions.position(latLng);
        markerOptions.title("City Centre");
        markerOptions.anchor(0.5F, 0.5F);
        markerOptions.flat(true);

        return markerOptions;
    }

    public static MarkerOptions gePropertyMarkerOptions(LatLng location) {
        MarkerOptions options = new MarkerOptions();

        options.icon(BitmapDescriptorFactory.fromBitmap(MapResourceProvider.getPropertyBitmap(R.drawable.marker_dorms)));
        options.position(location);
        options.anchor(0.5F, 0.5F);
        options.flat(true);

        return options;
    }
}