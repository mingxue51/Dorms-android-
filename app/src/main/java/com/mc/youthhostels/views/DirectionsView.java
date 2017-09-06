package com.mc.youthhostels.views;


import com.google.android.gms.maps.model.LatLng;
import com.mc.youthhostels.map.Location;

import java.util.List;

import com.mc.youthhostels.LoadingView;
import entity.Property.Property;
import routedrawer.model.Routes;

public interface DirectionsView extends LoadingView {
    void init();
    void showMap(Property property);
    void apply();
    void hideProperties();
    void updateTransportChooser(List<Routes> routes);
    void updateDirectionChooser(Property property);
    void showRoute(LatLng start, Location finish, Routes routes);
}
