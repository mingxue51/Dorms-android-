package com.mc.youthhostels.events.booking;

import entity.Property.Property;
import entity.Property.Search.SearchProperty;

public class CheckAvailabilityClickedEvent {
    private Property property;

    public CheckAvailabilityClickedEvent(Property property) {
        this.property = property;
    }

    public Property getProperty() {
        return property;
    }
}
