package com.mc.youthhostels.entity.filters;

import entity.Property.Amenity;
import entity.Property.Property;

public class AmenityFilter extends FilterNew {

    public AmenityFilter(String name) {
        super(name);
    }

    @Override
    public boolean isPass(Property property) {
        for (Amenity amenity : property.getAmenitiesForProperty()) {
            if (amenity.getName().equals(getName())) {
                return true;
            }
        }
        return false;
    }
}
