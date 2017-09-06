package com.mc.youthhostels.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import entity.Property.Property;

public class RatingSortFunction implements SortFunction {
    @Override
    public void sort(List<Property> properties) {
        Collections.sort(properties, new Comparator<Property>() {
            public int compare(Property p1, Property p2) {
                Integer r1 = p1.getPropertyRatings().getOverall();
                Integer r2 = p2.getPropertyRatings().getOverall();
                return r2.compareTo(r1);
            }
        });
    }
}
