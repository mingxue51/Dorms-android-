package com.mc.youthhostels.views;

import java.util.List;

import com.mc.youthhostels.LoadingView;
import entity.Property.Property;
import entity.Property.Search.SearchProperty;

public interface SearchResultView extends LoadingView {
    void showResults(List<Property> properties);
    void refresh();
    void hideRefineSearch();
    boolean isRefineSearchOnScreen();
    void showFavoriteDialog(SearchProperty searchProperty, Property property);
}
