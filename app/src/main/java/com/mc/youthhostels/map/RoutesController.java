package com.mc.youthhostels.map;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import helper.App;
import routedrawer.RouteRest;
import routedrawer.model.Routes;
import routedrawer.model.TravelMode;
import routedrawer.parser.RouteJsonParser;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RoutesController {

    @CheckResult
    public Observable<List<Routes>> getAvailableRoutes(final LatLng start, final LatLng finish) {
            final RouteRest routeRest = new RouteRest(App.getInstance().getOkHttpClient());
            List<Observable<Pair<String, TravelMode>>> travelObservables = new ArrayList<>();
            for (final TravelMode travelMode : TravelMode.values()) {
               travelObservables.add(routeRest.getJsonDirections(start, finish, travelMode));
            }
             return Observable.merge(travelObservables)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .map(s -> {
                        Routes routes = new RouteJsonParser<Routes>().parse(s.first, Routes.class);
                        if (routes != null) {
                            routes.setTravelMode(s.second);
                        }
                        return routes;
                    })
                    .filter(routes -> routes != null && routes.routes != null && routes.routes.size() > 0)
                    .doOnError(throwable -> {
                        throw new RuntimeException(throwable);
                    })
                    .toList();
    }

    @NonNull
    public Observable<Routes> getRoutesObservable(TravelMode travelMode, LatLng finish, LatLng start) {
        final RouteRest routeRest = new RouteRest(App.getInstance().getOkHttpClient());
        Observable<Pair<String, TravelMode>> routeObservable = routeRest.getJsonDirections(start, finish, travelMode);

        return routeObservable.observeOn(AndroidSchedulers.mainThread())
                .map(s -> new RouteJsonParser<Routes>().parse(s.first, Routes.class));
    }
}
