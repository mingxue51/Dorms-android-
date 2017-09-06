package com.mc.youthhostels.entity.filters;


import entity.Property.Property;

public class TypeFilter extends FilterNew {

    public TypeFilter(String name) {
        super(name);
        setActivated(true);
    }

    @Override
    public boolean isPass(Property property) {
        return property.getPropertyType().equalsIgnoreCase(getName());
    }
}
