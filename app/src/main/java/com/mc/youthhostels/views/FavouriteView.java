package com.mc.youthhostels.views;

import android.support.v4.app.FragmentManager;

import java.util.List;

import com.mc.youthhostels.LoadingView;
import com.mc.youthhostels.fragments.EditFavFragment;

import entity.Property.Property;
import entity.Property.Search.SearchProperty;

public interface FavouriteView extends LoadingView {
    void showFavourites(List<Property> properties);
    void refresh();
    void showFavoriteDialog(SearchProperty searchProperty, Property property);
}
