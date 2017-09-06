package com.mc.youthhostels.entity.filters;

import com.mc.youthhostels.R;

import entity.Property.Property;
import helper.H;

public class PrivateRoomsFilter extends FilterNew {
    public PrivateRoomsFilter() {
        super(H.getString(R.string.private_rooms));
        setActivated(true);
    }

    @Override
    public boolean isPass(Property property) {
        return property.isPrivateRooms();
    }
}
