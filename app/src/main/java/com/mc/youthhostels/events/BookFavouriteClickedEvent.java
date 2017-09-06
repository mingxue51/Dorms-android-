package com.mc.youthhostels.events;

import entity.Property.Property;

public class BookFavouriteClickedEvent {
    private Property property;

    public BookFavouriteClickedEvent(Property property) {
        this.property = property;
    }

    public Property getProperty() {
        return property;
    }
}
