package com.mc.youthhostels.events;


import entity.Property.Property;

public class OrderBookNowClickedEvent {
    private Property property;

    public OrderBookNowClickedEvent(Property property) {
        this.property = property;
    }

    public Property getProperty() {
        return property;
    }
}
