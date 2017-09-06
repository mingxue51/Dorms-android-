package com.mc.youthhostels.model;

import android.support.annotation.NonNull;

import com.mc.youthhostels.R;
import com.mc.youthhostels.dialog.Checkable;
import com.mc.youthhostels.events.SortMethodSelectedEvent;

import de.greenrobot.event.EventBus;
import helper.H;

public enum SortMethod implements Checkable {
    PRICE(R.string.sort_method_price, new PriceSortFunction()),
    RATING(R.string.sort_method_rating, new RatingSortFunction()),
    LOCATION(R.string.sort_method_location, new LocationSortFunction()),
    //SAFE(R.string.sort_method_safe, new SafetySortFunction()), // this will be used in feature
    NAME(R.string.sort_method_name, new NameSortFunction());

    private static final SortMethod DEFAULT = PRICE;

    private int nameResource;
    private boolean checked;
    private SortFunction sortFunction;


    SortMethod(int nameResource, SortFunction sortFunction) {
        this.nameResource = nameResource;
        this.sortFunction = sortFunction;
    }

    @Override
    public String getName() {
        return H.getString(nameResource);
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public void actionOnSetChecked() {
        for (SortMethod sortMethod : values()) {
            sortMethod.setChecked(false);
        }

        checked = true;
        EventBus.getDefault().post(new SortMethodSelectedEvent(this));
        H.logD(getName() + " sort method selected");
    }

    public SortFunction getSortFunction() {
        return sortFunction;
    }

    public static void reset() {
        for (SortMethod sortMethod : values()) {
            sortMethod.setChecked(false);
        }

        PRICE.setChecked(true);
    }

    @NonNull
    public static SortMethod getCheckedMethod() {
        for (SortMethod sortMethod : values()) {
            if (sortMethod.isChecked()) {
                return sortMethod;
            }
        }
        return DEFAULT;
    }
}
