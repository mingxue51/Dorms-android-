package com.mc.youthhostels.presenters;

import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.mc.youthhostels.map.Location;
import com.mc.youthhostels.map.LocationType;
import com.mc.youthhostels.map.RoutesController;
import com.mc.youthhostels.views.DirectionsView;

import java.util.ArrayList;
import java.util.List;

import api.API;
import entity.Generic.Districts.District;
import entity.Map.Location.Distance.DistanceHelper;
import entity.Property.Property;
import helper.App;
import helper.H;
import helper.IGetRequest;
import routedrawer.model.Routes;
import routedrawer.model.TravelMode;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DirectionsPresenter implements Presenter {

    DirectionsView view;
    Property currentProperty;
    RoutesController routesController;

    public DirectionsPresenter(DirectionsView view) {
        this.view = view;
    }

    @Override
    public void onResume() {
        routesController = new RoutesController();
        getUserBookings();
    }

    @Override
    public void onCreate() {
        view.init();
    }

    private void getUserBookings() {
    }

    public void showLoading() {
        view.showLoading();
    }

    public void hideLoading() {
        view.hideLoading();
    }

    public void onDirectionClick(final Property property) {
        showLoading();


//        LatLng start = DistanceHelper.debugLocation().toLatLng(); //DistanceHelper.getCurrentLocation(getActivity()).toLatLng();
//        LatLng finish = property.getGeoLocation().toLatLng();
//
//        Observable<List<Routes>> availableRoutes = routesController.getAvailableRoutes(start, finish);
//        availableRoutes.observeOn(AndroidSchedulers.mainThread());
//
        Observable<Property> propertyObservable = Observable.create(new Observable.OnSubscribe<Property>() {
            @Override
            public void call(final Subscriber<? super Property> subscriber) {
                API.getInstance(App.getInstance()).getPropertyDetails(property.getPropertyNumber(), new API.IGetRealTimeObject() {
                    @Override
                    public void getData(Object object) {
                        subscriber.onNext((Property) object);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(String message) {
                        subscriber.onError(new RuntimeException(message));
                    }
                });
            }
        });

        propertyObservable.observeOn(AndroidSchedulers.mainThread())
                          .subscribeOn(Schedulers.io())
                          .subscribe(property1 -> {
                                view.updateDirectionChooser(property1);
                                view.showMap(property1);
                                currentProperty = property1;
                                hideLoading();
        });
//
//        Observable.combineLatest(propertyObservable, availableRoutes, (property1, routes) -> new Pair<>(routes, property1))
//                  .observeOn(AndroidSchedulers.mainThread())
//                  .subscribe(pair -> {
//                      List<Routes> routes = pair.first;
//                      for (Routes route : routes) {
//                          H.logD("test " + route.getTravelMode());
//                      }
//                      view.updateTransportChooser(routes);
//                      Property targetProperty = pair.second;
//                      view.updateDirectionChooser(targetProperty);
//                      view.showMap(targetProperty);
//
//                      currentProperty = targetProperty;
//
//                      hideLoading();
//                  });

        view.hideProperties();
    }

    public void onDestinationChanged(String where) {
        Location location = getLocationByName(currentProperty, where);

        if (location != null) {
            if (location.getLocationType().equals(LocationType.DISTRICT)) {
                getDistrictPolygons(location)
                        .subscribe(coords -> {
                            District district = (District) location;
                            district.setCords(coords);

                            routesController.getAvailableRoutes(DistanceHelper.getCurrentLocation(App.getInstance()).toLatLng(), location.getLocation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(view::updateTransportChooser);
                        });
            } else {
                routesController.getAvailableRoutes(DistanceHelper.getCurrentLocation(App.getInstance()).toLatLng(), location.getLocation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(view::updateTransportChooser);
            }
        }
    }

    public void onByChanged(String by, String where) {
        TravelMode travelMode = TravelMode.getBy(App.getInstance(), by);
        Location location = getLocationByName(currentProperty, where);

//        if (location != null) {
//            if (location.getLocationType().equals(LocationType.DISTRICT)) {
//                // need to fetch district polygon coordinates first
//                // and show polygon in this case
//                getDistrictPolygons(location)
//                        .subscribe(coords -> {
//                            District district = (District) location;
//                            district.setCords(coords);
//
//                            showRoute(location, travelMode);
//                        });
//            } else {
                showRoute(location, travelMode);
//            }
//        }
    }

    private Observable<List<LatLng>> getDistrictPolygons(final Location location) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {

                API.getInstance(App.getInstance()).getDistrictCoords(location.getLocationId(), new IGetRequest() {
                    @Override
                    public void onSuccess(String message) {
                        subscriber.onNext(message);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(String message) {
                        subscriber.onError(new RuntimeException(message));
                    }
                });
            }
        }).map(this::getDistrictCordsFromResponse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * input - string in this format [[lat1,lng1],[lat2,lng2],...]
     */
    private List<LatLng> getDistrictCordsFromResponse(String responseCords) {
        String input = responseCords.replace("[","").replace("]", "");
        boolean isLat = true;
        double lat = 0;
        double lng = 0;

        List<LatLng> result = new ArrayList<>();
        String[] split = input.split(",");
        for (String cord : split) {
            double value = Double.parseDouble(cord);
            if (isLat) {
                lng = value;
            } else {
                lat = value;
                result.add(new LatLng(lat, lng));
            }
            isLat = !isLat;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (LatLng latLng : result) {
            stringBuilder.append(latLng.latitude).append(" ").append(latLng.longitude).append(" ");
        }
        H.logD("district's cords :" + stringBuilder.toString());

        return result;
    }

    private void showRoute(Location location, TravelMode travelMode) {
        LatLng finish = location.getLocation();
        if (finish != null) {
            LatLng start = DistanceHelper.getCurrentLocation(App.getInstance()).toLatLng();

            Observable<Routes> routesObservable = routesController.getRoutesObservable(travelMode, location.getLocation(), start);

            routesObservable.subscribe(routes -> {
                view.showRoute(start, location, routes);
            });
        }
    }

    @Nullable
    private Location getLocationByName(Property property, String where) {
        List<Location> locations = property.getLocations();

        for (Location location : locations) {
            if (location.getLocationName().equals(where)) {
                return location;
            }
        }

        return null;
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onPause() {

    }
}
