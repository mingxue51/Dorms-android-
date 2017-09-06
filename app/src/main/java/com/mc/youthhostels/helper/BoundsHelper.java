package com.mc.youthhostels.helper;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import entity.Generic.GeoLocation;
import entity.Map.AnyBoundsFilter;
import entity.Map.BoundsFilter;
import entity.Map.DistanceBoundsFilter;
import entity.Map.Location.Distance.DistanceHelper;
import entity.Property.Properties;
import entity.Property.Property;
import helper.App;
import helper.H;

import static entity.Property.Search.SearchProperty.*;
import static entity.Property.Search.SearchProperty.SearchSource.*;

public class BoundsHelper {
    private static final BoundsFilter ANY_BOUNDS_FILTER = new AnyBoundsFilter();

    private SearchSource searchSource;
    private Properties properties;

    public BoundsHelper(SearchSource searchSource, Properties properties) {
        this.searchSource = searchSource;
        this.properties = properties;
    }

    @NonNull
    public BoundsFilter getFilter() {
        if (searchSource.equals(CITY)) {
            return getFilterByCity();
        } else if (searchSource.equals(GEOCODES)) {
            GeoLocation currentLocation = DistanceHelper.getCurrentLocation(App.getInstance().getApplicationContext());

            DistanceBoundsFilter filter = new DistanceBoundsFilter(currentLocation.toLatLng());
            int count = getPropertiesCount(filter);

            if (count > 0) {
                return filter;
            } else {
                return getFilterByCity();
            }
        } else {
            throw new RuntimeException("no right SearchSource");
        }

        //return ANY_BOUNDS_FILTER;
    }

    private BoundsFilter getFilterByCity() {
        GeoLocation cityCenter = properties.getCityCenter();

        if (cityCenter != null) {
            DistanceBoundsFilter filter = new DistanceBoundsFilter(cityCenter.toLatLng());
            int count = getPropertiesCount(filter);
            if (count > 0) {
                H.logD("use city center for filter");
                return filter;
            }
        }
        return ANY_BOUNDS_FILTER;
    }

    private int getPropertiesCount(BoundsFilter filter) {
        if (properties == null) {
            return 0;
        }

        if (filter == null) {
            return  0;
        }

        int count = 0;
        for (Property property : properties.getProperties()) {
            GeoLocation geoLocation = property.getGeoLocation();
            LatLng latLng = geoLocation.toLatLng();
            if (filter.filter(latLng)) {
                count++;
            }
        }

        return count;
    }
}
