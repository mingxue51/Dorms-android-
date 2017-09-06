package com.mc.youthhostels.events;

import java.util.List;

import entity.Property.Property;

public class FavouritesLoadedEvent {
    private List<Property> favourites;

    public FavouritesLoadedEvent(List<Property> favourites) {
        this.favourites = favourites;
    }

    public List<Property> getFavourites() {
        return favourites;
    }
}
