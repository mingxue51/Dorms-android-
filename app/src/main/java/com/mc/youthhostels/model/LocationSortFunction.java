package com.mc.youthhostels.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import entity.Property.Property;

public class LocationSortFunction implements SortFunction {
    @Override
    public void sort(List<Property> properties) {
        Collections.sort(properties, Collections.reverseOrder(new Comparator<Property>() {
            public int compare(Property p1, Property p2) {
                Integer safety1 = p1.getPropertyRatings().getLocation();
                Integer safety2 = p2.getPropertyRatings().getLocation();
                return safety1.compareTo(safety2);
            }
        }));
    }
}
