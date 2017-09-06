/*

Copyright 2014 Marcin Polak

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

 */

package routedrawer;

import android.support.v4.util.Pair;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import routedrawer.model.TravelMode;
import rx.Observable;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class RouteRest implements RouteApi {

    private OkHttpClient client;

    public RouteRest(OkHttpClient client) {
        this.client = client;
    }

    public RouteRest() {
        this.client = new OkHttpClient();
    }

    @Override
    public Observable<Pair<String, TravelMode>> getJsonDirections(final LatLng start, final LatLng end, final TravelMode mode) {
        final Func0<Pair<String, TravelMode>> resultFunc = new Func0<Pair<String, TravelMode>>() {
            @Override
            public Pair<String, TravelMode> call() {
                try {
                    return new Pair<>(getJSONDirection(start, end, mode), mode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new Pair<>("", null);
            }
        };

        return Observable.defer(new Func0<Observable<Pair<String, TravelMode>>>() {
            @Override
            public Observable<Pair<String, TravelMode>> call() {
                return Observable.just(resultFunc.call());
            }
        }).subscribeOn(Schedulers.io());
    }

    private String getJSONDirection(LatLng start, LatLng end, TravelMode mode) throws IOException {
        String url = "http://maps.googleapis.com/maps/api/directions/json?"
                + "origin="
                + start.latitude + ","
                + start.longitude
                + "&destination="
                + end.latitude + ","
                + end.longitude
                + "&sensor=false&units=metric&mode="
                + mode.name().toLowerCase();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
