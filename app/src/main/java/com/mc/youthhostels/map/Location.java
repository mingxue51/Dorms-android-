package com.mc.youthhostels.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public interface Location {
    LatLng getLocation();
    String getLocationId();
    LocationType getLocationType();
    String getLocationName();
    MarkerOptions getMarkerOptions();
}
