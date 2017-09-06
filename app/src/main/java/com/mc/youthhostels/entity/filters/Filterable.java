package com.mc.youthhostels.entity.filters;

import com.mc.youthhostels.dialog.Checkable;

import entity.Property.Property;

public interface  Filterable extends Checkable {
    boolean isActivated();
    void sync();
    void apply();
    boolean isPass(Property property);
}
