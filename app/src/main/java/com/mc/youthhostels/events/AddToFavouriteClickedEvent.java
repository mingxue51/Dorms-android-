package com.mc.youthhostels.events;


import entity.Property.Property;

public class AddToFavouriteClickedEvent {
    private Property property;

    public AddToFavouriteClickedEvent(Property property) {
        this.property = property;
    }

    public Property getProperty() {
        return property;
    }
}
