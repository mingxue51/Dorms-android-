package com.mc.youthhostels.presenters;

import android.widget.Toast;

import com.mc.youthhostels.R;
import com.mc.youthhostels.activities.BookingActivity;
import com.mc.youthhostels.activities.PropertyDetailsActivity2;
import com.mc.youthhostels.entity.filters.Filters;
import com.mc.youthhostels.events.AddToFavouriteClickedEvent;
import com.mc.youthhostels.events.PropertiesFilteredEvent;
import com.mc.youthhostels.events.PropertiesFoundEvent;
import com.mc.youthhostels.events.PropertiesUpdatedEvent;
import com.mc.youthhostels.events.PropertyPreviewClicked;
import com.mc.youthhostels.events.PropertySearchFoundEvent;
import com.mc.youthhostels.events.SortMethodSelectedEvent;
import com.mc.youthhostels.events.booking.CheckAvailabilityClickedEvent;
import com.mc.youthhostels.model.SortMethod;
import com.mc.youthhostels.views.SearchResultView;

import java.util.List;

import api.API;
import de.greenrobot.event.EventBus;
import entity.Property.Property;
import entity.Property.Search.SearchProperty;
import entity.User.User;
import helper.App;
import helper.H;
import helper.IGetRequest;

public class SearchResultFragmentPresenter implements Presenter {

    private SearchResultView searchResultView;
    private SearchProperty searchProperty;
    private List<Property> properties;

    public static Filters filters;

    public SearchResultFragmentPresenter(SearchResultView searchResultView, SearchProperty searchProperty, List<Property> properties) {
        this.searchResultView = searchResultView;
        this.searchProperty = searchProperty;
        this.properties = properties;

        filters = new Filters();
        filters.init(properties);

        SortMethod.reset();
        SortMethod.getCheckedMethod().getSortFunction().sort(properties);
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(PropertyPreviewClicked propertyPreviewClicked) {
        if (searchResultView.isRefineSearchOnScreen()) {
            searchResultView.hideRefineSearch();
            return;
        }

        Property selectedProperty = propertyPreviewClicked.getProperty();
        searchProperty.setPropertyId(selectedProperty.getPropertyNumber());
        searchResultView.showLoading();
        API.getInstance(App.getInstance()).getPropertyDetails(selectedProperty.getPropertyNumber(), new API.IGetRealTimeObject() {
            @Override
            public void getData(Object propertyDetails) {
                searchResultView.hideLoading();
                Property property = (Property) propertyDetails;
                property.setPropertyType(selectedProperty.getPropertyType());
                property.setPropertyRatings(selectedProperty.getPropertyRatings());
                property.setFavourite(selectedProperty.isFavourite());
                property.setFavouriteId(selectedProperty.getFavouriteId());
                PropertyDetailsActivity2.goTo(property, searchProperty);
            }

            @Override
            public void onError(String message) {
                searchResultView.hideLoading();
            }
        });
    }

    public void onEvent(CheckAvailabilityClickedEvent event) {
        BookingActivity.goTo(event.getProperty(), searchProperty);
    }

    public void onEvent(PropertiesFoundEvent event) {
        H.logD("search result presenter: properties found event");
        searchResultView.hideRefineSearch();

        searchProperty = event.getSearchProperty();
        properties = event.getProperties().getProperties();

        filters.init(properties);
        SortMethod.reset();
        SortMethod.getCheckedMethod().getSortFunction().sort(properties);
        searchResultView.showResults(properties);
    }

    public void onEvent(PropertySearchFoundEvent event) {
        H.logD("main presenter: move to property details");
        PropertyDetailsActivity2.goTo(event.getProperty(), event.getSearchProperty());
    }

    public void onEvent(PropertiesFilteredEvent event) {
        filters.apply();
        List<Property> filtered = filters.filterAll(this.properties);
        SortMethod.getCheckedMethod().getSortFunction().sort(properties);
        searchResultView.showResults(filtered);
    }

    public void onEvent(SortMethodSelectedEvent event) {
        if (event.getSortMethod() != null) {
            event.getSortMethod().getSortFunction().sort(properties);
            searchResultView.showResults(properties);
        }
    }

    public void onEvent(AddToFavouriteClickedEvent event) {
        Property property = event.getProperty();
        if (!User.isLogged()) {
            Toast.makeText(App.getInstance(), H.getString(R.string.please_sign_in), Toast.LENGTH_LONG).show();
            return;
        }
        searchResultView.showFavoriteDialog(searchProperty, property);
    }

    public void onEvent(PropertiesUpdatedEvent event) {
        if (searchResultView != null) {
            searchResultView.refresh();
        } else {
            H.logE("search result view is null");
        }
    }

    @Override
    public void onPause() {
    }
}
