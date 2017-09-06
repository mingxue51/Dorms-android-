package com.mc.youthhostels.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import entity.Property.Property;

public class PriceSortFunction implements SortFunction {
    @Override
    public void sort(List<Property> properties) {
        Collections.sort(properties, new Comparator<Property>() {
            public int compare(Property p1, Property p2) {
                return p1.getComparedPrice().compareTo(p2.getComparedPrice());
            }
        });
    }
}
