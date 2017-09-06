package entity.Map;

import android.content.Context;

import entity.Map.Location.LocationGoogle;
import entity.Map.Location.LocationI;

// todo remove this class
public class MapFabric extends MapAF {
    public MapFabric(Context context) {
        super(context);
    }

    @Override
    public LocationI getLocation(String provider) {
        if (provider == null) {
            return null;
        }

        if (provider.equalsIgnoreCase(MapProvider.GOOGLE.PROVIDER)) {
            return new LocationGoogle(getContext()) {
            };

        }

        return null;
    }

}