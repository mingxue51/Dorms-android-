package com.mc.youthhostels.presenters;

import com.mc.youthhostels.activities.BookingActivity;
import com.mc.youthhostels.activities.PropertyDetailsActivity2;
import com.mc.youthhostels.events.AddToFavouriteClickedEvent;
import com.mc.youthhostels.events.BookFavouriteClickedEvent;
import com.mc.youthhostels.events.PropertiesUpdatedEvent;
import com.mc.youthhostels.events.PropertyPreviewClicked;
import com.mc.youthhostels.views.FavouriteView;

import java.lang.ref.WeakReference;
import java.util.List;

import api.API;
import de.greenrobot.event.EventBus;
import entity.Property.Property;
import entity.Property.Search.SearchProperty;
import helper.App;
import helper.H;
import helper.IGetRequest;

public class FavouritePresenter implements Presenter {

    private WeakReference<FavouriteView> view;

    public FavouritePresenter(FavouriteView view) {
        this.view = new WeakReference<>(view);
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);

        if (view.get() != null) {
            view.get().showLoading();
        }
        API.getInstance(App.getInstance()).getFavourites(new API.IGetRealTimeObject() {
            @Override
            public void getData(Object favourites) {
                FavouriteView view = FavouritePresenter.this.view.get();
                if (view != null) {
                    H.runOnUi(() -> {
                        view.showFavourites((List<Property>) favourites);
                        view.hideLoading();
                    });
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(AddToFavouriteClickedEvent event) {
        if (view.get() != null) {
            Property property = event.getProperty();
            SearchProperty searchProperty = new SearchProperty();
            searchProperty.setArrivalDate(property.getArrivalDate());
            searchProperty.setDepartureDate(property.getDepartureDate());
            view.get().showFavoriteDialog(searchProperty, property);
        }
    }

    public void onEvent(BookFavouriteClickedEvent event) {
        Property property = event.getProperty();

        SearchProperty searchProperty = new SearchProperty();
        searchProperty.setArrivalDate(property.getArrivalDate());
        searchProperty.setDepartureDate(property.getDepartureDate());
        searchProperty.setPropertyId(property.getPropertyNumber());

        BookingActivity.goTo(property, searchProperty);
    }

    public void onEvent(PropertyPreviewClicked event) {
        Property selectedProperty = event.getProperty();

        SearchProperty searchProperty = new SearchProperty();
        searchProperty.setArrivalDate(selectedProperty.getArrivalDate());
        searchProperty.setDepartureDate(selectedProperty.getDepartureDate());
        searchProperty.setPropertyId(selectedProperty.getPropertyNumber());

        searchProperty.setPropertyId(selectedProperty.getPropertyNumber());

        App.getInstance().showLoading();

        API.getInstance(App.getInstance()).getPropertyDetails(selectedProperty.getPropertyNumber(), new API.IGetRealTimeObject() {
            @Override
            public void getData(Object propertyDetails) {
                App.getInstance().hideLoading();
                Property property = (Property) propertyDetails;
                property.setPropertyType(selectedProperty.getPropertyType());
                property.setPropertyRatings(selectedProperty.getPropertyRatings());

                PropertyDetailsActivity2.goTo(property, searchProperty);
            }

            @Override
            public void onError(String message) {
                App.getInstance().hideLoading();
            }
        });
    }

    public void onEvent(PropertiesUpdatedEvent event) {
        if (view.get() != null) {
            view.get().refresh();
        } else {
            H.logE("search result view is null");
        }
    }
}
