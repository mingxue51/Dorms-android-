package com.mc.youthhostels.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import entity.Property.Property;

public class NameSortFunction implements SortFunction {
    @Override
    public void sort(List<Property> properties) {
        Collections.sort(properties, new Comparator<Property>() {
            public int compare(Property property1, Property property2) {
                String propertyName1 = property1.getPropertyName().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
                String propertyName2 = property2.getPropertyName().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
                return propertyName1.compareToIgnoreCase(propertyName2);
            }
        });
    }
}
