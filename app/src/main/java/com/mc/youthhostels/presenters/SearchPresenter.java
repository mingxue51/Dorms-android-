package com.mc.youthhostels.presenters;

import android.Manifest;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mc.youthhostels.R;
import com.mc.youthhostels.activities.AppActivity;
import com.mc.youthhostels.events.DatesChosenEvent;
import com.mc.youthhostels.events.HereAndTonightClickedEvent;
import com.mc.youthhostels.events.PropertiesFoundEvent;
import com.mc.youthhostels.events.PropertiesUpdatedEvent;
import com.mc.youthhostels.events.PropertySearchFoundEvent;
import com.mc.youthhostels.events.SuggestionCurrentLocationClickedEvent;
import com.mc.youthhostels.events.SuggestionSelectedEvent;
import com.mc.youthhostels.fragments.EditFavFragment;
import com.mc.youthhostels.views.SearchView;

import java.util.Date;
import java.util.List;

import api.API;
import de.greenrobot.event.EventBus;
import entity.Generic.GeoLocation;
import entity.Map.Location.CurrentLocations;
import entity.Map.Location.Distance.DistanceHelper;
import entity.Property.Properties;
import entity.Property.Property;
import entity.Property.Search.SearchProperty;
import entity.Property.Search.Suggestion;
import helper.App;
import helper.DT;
import helper.H;
import helper.IGetRequest;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static entity.Property.Search.SearchProperty.SearchSource.CITY;
import static entity.Property.Search.SearchProperty.SearchSource.GEOCODES;
import static entity.Property.Search.SearchProperty.SearchSource.PROPERTY;
import static entity.Property.Search.SearchProperty.SearchSource.UNKNOWN;

public class SearchPresenter implements Presenter {

    private SearchProperty searchProperty;
    private SearchView searchView;

    private Date selectedArrivalDate;
    private Date selectedDepartureDate;

    public SearchPresenter(SearchView searchView, SearchProperty searchProperty) {
        this.searchView = searchView;
        this.searchProperty = searchProperty;
        selectedArrivalDate = searchProperty.getArrivalDate();
        selectedDepartureDate = searchProperty.getDepartureDate();
    }

    protected void searchCurrentLocationByGeoCodes() {
        searchView.showLoading();
        API.getInstance(App.getInstance()).GetCurrentLocationByGeocodes(searchProperty.getGeoLocation().toLatLng(), new API.IGetRealTimeObject() {
            @Override
            public void getData(final Object object) {
                CurrentLocations currentLocations = (CurrentLocations) object;

                searchProperty.setCity(currentLocations.getCity());
                searchProperty.setCountry(currentLocations.getCountry());

                H.runOnUi(new Runnable() {
                    @Override
                    public void run() {
                        searchView.hideLoading();
                        searchView.setLocationText(searchProperty.getCityAndCountry());
                    }
                });
            }

            @Override
            public void onError(String message) {
                // todo
            }
        });
    }

    public void onCurrentLocationClicked() {
        Runnable action = new Runnable() {
            @Override
            public void run() {
                GeoLocation geoLocation = DistanceHelper.getCurrentLocation(App.getInstance().getBaseContext());
                if (geoLocation != null) {
                    searchProperty.setGeoLocation(geoLocation);
                    if (searchProperty.isValid()) {
                        searchProperty.setSearchSource(GEOCODES);
                        searchCurrentLocationByGeoCodes();
                    }
                } else {
                    H.showDialog(H.getString(R.string.unavailable_location));
                }
            }
        };

        AppActivity currentActivity = App.getInstance().getCurrentActivity();
        if (currentActivity != null) {
            currentActivity.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                                            AppActivity.PERMISSION_REQUEST_CODE,
                                            action,
                                            H.getString(R.string.location_permission_explanation));
        }
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
    }

    // handling event from calendar fragment
    public void onEvent(DatesChosenEvent event) {
        Date checkIn = event.getCheckIn();
        Date checkOut = event.getCheckOut();
        H.logD("dates choosen events:" + checkIn + " " + checkOut);
        searchView.setDates(checkIn, checkOut);

        H.logD("selected arrival : " + DT.getDateForAPI(checkIn));
        H.logD("selected departure : " + DT.getDateForAPI(checkOut));

        selectedArrivalDate = checkIn;
        selectedDepartureDate = checkOut;
    }

    public void onEvent(SuggestionCurrentLocationClickedEvent event) {
        // close dialog
        onCurrentLocationClicked();
    }

    public void onEvent(HereAndTonightClickedEvent event) {
        H.logD("here and tonight in search presenter started");
        Runnable action = new Runnable() {
            @Override
            public void run() {
                GeoLocation geoLocation = DistanceHelper.getCurrentLocation(App.getInstance().getBaseContext());
                if (geoLocation != null) {
                    searchProperty.setGeoLocation(geoLocation);
                    if (searchProperty.isValid()) {
                        searchProperty.setTonightDates();
                        searchByGeoCodes(searchProperty);
                    }
                } else {
                    H.showDialog(H.getString(R.string.unavailable_location));
                }
            }
        };

        H.logD("here and tonight start request permission");
        AppActivity currentActivity = App.getInstance().getCurrentActivity();
        if (currentActivity != null) {
            currentActivity.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                    AppActivity.PERMISSION_REQUEST_CODE,
                    action,
                    H.getString(R.string.location_permission_explanation));
        }
    }

    protected void searchByGeoCodes(SearchProperty searchProperty) {
        searchView.showLoading();

        Observable<Properties> propertiesObservable = API.getInstance().searchByGeocodes(searchProperty);
        Observable<List<Property>> favouritesObservable = API.getInstance().getFavourites();

        Observable.combineLatest(propertiesObservable, favouritesObservable, Pair::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    searchView.hideLoading();
                    H.showDialog(H.getString(R.string.something_went_wrong));
                })
                .subscribe(pair -> applyCitySearchResults(pair.first, pair.second));
    }

    public void onEvent(SuggestionSelectedEvent suggestionSelectedEvent) {
        searchView.closeLocationInputDialog();

        searchProperty.setSearchSource(UNKNOWN);
        Suggestion suggestion = suggestionSelectedEvent.getSuggestion();
        searchProperty.setCity(suggestion.getHb_city());
        //searchProperty.setCountryId(suggestion.getCountry_id());
        searchProperty.setCountry(suggestion.getHb_country());
        searchProperty.setPropertyId(suggestion.getProperty_number());

        if (!TextUtils.isEmpty(searchProperty.getPropertyId())) {
            searchView.setLocationText(suggestion.getProperty_name());
        } else {
            searchView.setLocationText(searchProperty.getCityAndCountry());
        }

        searchView.setAdditionBoardVisible(true);
    }

    public void onAddFavouriteClicked(Property property) {
        property.setArrivalDate(selectedArrivalDate);
        property.setDepartureDate(selectedDepartureDate);

        App.getInstance().showLoading();
        API.getInstance(App.getInstance()).addToFavourite(property, new API.IGetRealTimeObject() {
            @Override
            public void getData(Object object) {
                if (property != null) {
                    if (property.getFavouriteId() != null) {
                        App.getInstance().showToast(H.getString(R.string.favorite_updated_message));
                    } else {
                        App.getInstance().showToast(H.getString(R.string.favorite_added_message));
                    }

                    H.runOnUi(new Runnable() {
                        @Override
                        public void run() {
                            EventBus.getDefault().post(new PropertiesUpdatedEvent());
                        }
                    });

                    property.setFavourite(true);
                    property.setFavouriteId(((String) object));
                    H.logD("property added to favourite");
                    App.getInstance().hideLoading();

                    H.runOnUi(new Runnable() {
                        @Override
                        public void run() {
                            closeView();
                        }
                    });
                }
            }

            @Override
            public void onError(String message) {
                App.getInstance().hideLoading();
                H.logE(message);
            }
        });
    }

    public void onDeleteFavouriteClicked(Property property) {
        App.getInstance().showLoading();
        API.getInstance(App.getInstance()).deleteFavorite(property, new IGetRequest() {
            @Override
            public void onSuccess(String message) {
                if (property != null) {
                    property.setFavourite(false);
                    property.setFavouriteId(null);
                    property.setFavouriteNote(null);
                    H.logD("property added to favourite");
                    App.getInstance().hideLoading();

                    H.runOnUi(new Runnable() {
                        @Override
                        public void run() {
                            closeView();
                            App.getInstance().showToast(H.getString(R.string.favourite_deleted_message));
                            EventBus.getDefault().post(new PropertiesUpdatedEvent());
                        }
                    });
                }
            }

            @Override
            public void onError(String message) {
                App.getInstance().hideLoading();
            }
        });
    }

    private void closeView() {
        App.getInstance().getCurrentActivity().hideKeyboard();
        if (searchView != null) {
            searchView.hide();
        }
    }

    public void onLocationInputClicked() {
        searchView.showLocationInputDialog();
    }

    public Date getArrivalDate() {
        return searchProperty.getArrivalDate();
    }

    public Date getDepartureDate() {
        return searchProperty.getDepartureDate();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
    }

    public void onSearchButtonClicked() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(App.getInstance());
        if (status != ConnectionResult.SUCCESS) {
            H.showDialog("Sorry, there is no 'google play services' installed on your device");
            return;
        }

        if (searchProperty.getCityAndCountry() == null &&
            TextUtils.isEmpty(searchProperty.getPropertyId())) {
            onLocationInputClicked();
            return;
        }

        searchProperty.setArrivalDate(selectedArrivalDate);
        searchProperty.setDepartureDate(selectedDepartureDate);

        searchView.showLoading();

        if (TextUtils.isEmpty(searchProperty.getPropertyId())) {
            searchByCity();
        } else {
            searchByProperty();
        }
    }

    private void searchByCity() {
        Observable<Properties> propertiesObservable = API.getInstance().searchByLocation(searchProperty);
        Observable<List<Property>> favouritesObservable = API.getInstance().getFavourites();

        Observable.combineLatest(propertiesObservable, favouritesObservable, Pair::new)
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .doOnError(throwable -> {
                      searchView.hideLoading();
                      H.showDialog(H.getString(R.string.something_went_wrong));
                  })
                  .subscribe(pair -> applyCitySearchResults(pair.first, pair.second));
    }

    private void searchByProperty() {
        Observable<Property> propertyObservable = API.getInstance().searchByProperty(searchProperty.getPropertyId());
        Observable<List<Property>> favouritesObservable = API.getInstance().getFavourites();

        Observable.combineLatest(propertyObservable, favouritesObservable, Pair::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    searchView.hideLoading();
                    H.showDialog(H.getString(R.string.something_went_wrong));
                })
                .subscribe(pair -> applyPropertySearchResults(pair.first, pair.second));
    }

    private void applyPropertySearchResults(Property property, List<Property> favourites) {
        searchView.hideLoading();
        searchProperty.setSearchSource(PROPERTY);
        searchProperty.setArrivalDate(selectedArrivalDate);
        searchProperty.setDepartureDate(selectedDepartureDate);


        for (Property favourite : favourites) {
            if (property.getPropertyNumber().equalsIgnoreCase(favourite.getPropertyNumber())) {
                property.setFavourite(true);
                property.setFavouriteId(favourite.getFavouriteId());
                property.setFavouriteNote(favourite.getFavouriteNote());
                break;
            }
        }

        EventBus.getDefault().post(new PropertySearchFoundEvent(searchProperty, property));
    }

    private void applyCitySearchResults(Properties properties, List<Property> favourites) {
        searchView.hideLoading();
        searchProperty.setSearchSource(CITY);
        searchProperty.setArrivalDate(selectedArrivalDate);
        searchProperty.setDepartureDate(selectedDepartureDate);

        if (properties.isEmptyResults()) {
                H.showDialog(H.getString(R.string.no_properties_found));
                return;
            }

            if (properties.getProperties().size() > 0) {
                searchProperty.setCountry(properties.getCityInfo().getCountry());
                searchProperty.setCity(properties.getCityInfo().getCity());
            }

            H.logD("favourites found when search count:" + favourites.size());

            for (Property favourite : favourites) {
                for (Property property : properties.getProperties()) {
                    if (property.getPropertyNumber().equalsIgnoreCase(favourite.getPropertyNumber())) {
                        property.setFavourite(true);
                        property.setFavouriteId(favourite.getFavouriteId());
                        property.setFavouriteNote(favourite.getFavouriteNote());
                    }
                }
            }

            EventBus.getDefault().post(new PropertiesFoundEvent(searchProperty, properties));
    }

    public void refresh() {
        boolean isDatesAbsent = DT.isDateInThePast(searchProperty.getArrivalDate()) ||
                                DT.isDateInThePast(searchProperty.getDepartureDate());
        if (isDatesAbsent) {
            searchProperty.setDefaultDates();
        }
        searchView.setDates(searchProperty.getArrivalDate(), searchProperty.getDepartureDate());
        searchView.setLocationText(searchProperty.getCityAndCountry());
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
    }
}
