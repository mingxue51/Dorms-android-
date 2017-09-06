package com.mc.youthhostels.events;


import entity.Property.Property;

public class PropertyPreviewClicked {
    private Property property;

    public PropertyPreviewClicked(Property property) {
        this.property = property;
    }

    public Property getProperty() {
        return property;
    }
}
