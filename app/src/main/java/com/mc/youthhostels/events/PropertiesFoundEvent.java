package com.mc.youthhostels.events;

import entity.Property.Properties;
import entity.Property.Search.SearchProperty;

public class PropertiesFoundEvent {
    private SearchProperty searchProperty;
    private Properties properties;

    public PropertiesFoundEvent(SearchProperty searchProperty, Properties properties) {
        this.searchProperty = searchProperty;
        this.properties = properties;
    }

    public SearchProperty getSearchProperty() {
        return searchProperty;
    }

    public Properties getProperties() {
        return properties;
    }
}
