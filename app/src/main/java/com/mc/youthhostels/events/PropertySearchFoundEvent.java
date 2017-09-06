package com.mc.youthhostels.events;

import entity.Property.Properties;
import entity.Property.Property;
import entity.Property.Search.SearchProperty;

public class PropertySearchFoundEvent {
    private SearchProperty searchProperty;
    private Property property;

    public PropertySearchFoundEvent(SearchProperty searchProperty, Property property) {
        this.searchProperty = searchProperty;
        this.property = property;
    }

    public SearchProperty getSearchProperty() {
        return searchProperty;
    }

    public Property getProperty() {
        return property;
    }
}
