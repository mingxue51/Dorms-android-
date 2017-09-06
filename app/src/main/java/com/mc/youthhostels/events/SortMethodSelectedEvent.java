package com.mc.youthhostels.events;

import com.mc.youthhostels.model.SortMethod;

public class SortMethodSelectedEvent {
    private SortMethod sortMethod;

    public SortMethodSelectedEvent(SortMethod sortMethod) {
        this.sortMethod = sortMethod;
    }

    public SortMethod getSortMethod() {
        return sortMethod;
    }
}
