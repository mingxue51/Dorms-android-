package com.mc.youthhostels.fragments;

import com.mc.youthhostels.R;
import com.mc.youthhostels.dialog.Checkable;
import com.mc.youthhostels.entity.filters.Filterable;
import com.mc.youthhostels.presenters.SearchResultFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

public class TypeChooserFragment extends ChooserFragment {

    @Override
    boolean isScrollable() {
        return false;
    }

    @Override
    List<Checkable> getData() {
        List<Checkable> data = new ArrayList<>();

        for (Filterable filterable : SearchResultFragmentPresenter.filters.getTypeFilters()) {
            data.add(filterable);
        }

        return data;
    }

    @Override
    int getLayoutResource() {
        return R.layout.fragment_simple_chooser;
    }
}
