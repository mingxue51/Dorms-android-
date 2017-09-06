package com.mc.youthhostels.fragments;

import com.mc.youthhostels.R;
import com.mc.youthhostels.adapter.ChooserAdapter;
import com.mc.youthhostels.dialog.Checkable;
import com.mc.youthhostels.events.SortMethodSelectedEvent;
import com.mc.youthhostels.model.SortMethod;

import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;
import helper.H;

public class SortChooserFragment extends ChooserFragment {

    @Override
    boolean isScrollable() {
        return false;
    }

    @Override
    List<Checkable> getData() {
        return Arrays.asList(SortMethod.values());
    }

    @Override
    int getLayoutResource() {
        return R.layout.fragment_sort_choser;
    }
}
