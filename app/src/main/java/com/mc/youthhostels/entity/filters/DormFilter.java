package com.mc.youthhostels.entity.filters;

import com.mc.youthhostels.R;

import entity.Property.Property;
import helper.H;


public class DormFilter extends FilterNew {

    public DormFilter() {
        super(H.getString(R.string.dorms));
        setActivated(true);
    }

    @Override
    public boolean isPass(Property property) {
        return property.isDorms();
    }
}
