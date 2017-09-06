package com.mc.youthhostels.fragments.map;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mc.youthhostels.R;
import com.mc.youthhostels.fragments.PropertyOrderFragment;
import com.mc.youthhostels.helper.BoundsHelper;
import com.mc.youthhostels.map.RoutesController;
import com.mc.youthhostels.views.PropertyMapView;

import java.util.List;
import java.util.jar.Manifest;

import butterknife.Bind;
import butterknife.ButterKnife;
import entity.Generic.GeoLocation;
import entity.Map.AnyBoundsFilter;
import entity.Map.BoundsFilter;
import entity.Map.Location.Distance.DistanceHelper;
import entity.Map.Marker.MarkerHelper;
import entity.Property.Properties;
import entity.Property.Property;
import entity.Property.Search.SearchProperty;
import helper.H;
import routedrawer.model.Routes;
import routedrawer.model.TravelMode;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class MapFragment extends Fragment implements PropertyMapView {

    private static final String SHOW_ROUTE_BUNDLE = "show_route";
    private GoogleMap map;
    private Property property;
    private boolean showRoute;
    private Properties properties;
    private SearchProperty searchProperty;

    public static MapFragment newInstance(Property property, boolean showRoute) {
        MapFragment fragment = new MapFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(Property.BUNDLE, property);
        bundle.putBoolean(SHOW_ROUTE_BUNDLE, showRoute);

        fragment.setArguments(bundle);

        return fragment;
    }

    public static MapFragment newInstance(Properties properties, SearchProperty searchProperty) {
        MapFragment fragment = new MapFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(Properties.BUNDLE, properties);
        bundle.putSerializable(SearchProperty.BUNDLE, searchProperty);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            property = (Property) arguments.getSerializable(Property.BUNDLE);
            showRoute = arguments.getBoolean(SHOW_ROUTE_BUNDLE);
            properties = (Properties)arguments.getSerializable(Properties.BUNDLE);
            searchProperty = (SearchProperty)arguments.getSerializable(SearchProperty.BUNDLE);
        }
    }

    private void init() {
        initMap();
        
        if (properties != null) {
            multiple();
        } else {
            single();
        }
    }

    private void multiple() {
        map.getUiSettings().setMyLocationButtonEnabled(false);

        if ((map != null) && (properties != null) && (properties.getProperties().size() > 0)) {
            GeoLocation cityCenter = properties.getCityCenter();

            if (cityCenter != null) {
                map.addMarker(MarkerHelper.getCityCentreOptions(new LatLng(cityCenter.getLatitude(), cityCenter.getLontitude())));
            }

            BoundsHelper boundsHelper = new BoundsHelper(searchProperty.getSearchSource(), properties);
            BoundsFilter filter = boundsHelper.getFilter();
            H.logD("used bound filter :" + filter.toString());

            addMarkers(properties, filter);
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (marker == null || marker.getSnippet() == null) {
                        return false;
                    }

                    Integer index = Integer.valueOf(marker.getSnippet());
                    Property property = properties.getProperties().get(index);
                    getChildFragmentManager().beginTransaction()
                            .add(R.id.fragment_content, PropertyPreviewFragment.newInstance(property))
                            .addToBackStack(null)
                            .commit();

                    H.logD("market number clicked:" + marker.getSnippet());
                    return true;
                }
            });
        }
    }

    public static final Integer BOUNDS_PADDING = 15;
    public void addMarkers(Properties properties, BoundsFilter boundsFilter) {
        final LatLngBounds.Builder boundsBuilder = LatLngBounds.builder();

        List<Property> propertyList = properties.getProperties();
        for (int i = 0; i < propertyList.size(); i++) {
            Property property = propertyList.get(i);
            MarkerOptions options = MarkerHelper.gePropertyMarkerOptions(property.getLocation());
            options.snippet(String.valueOf(i));
            map.addMarker(options);
            LatLng point = property.getGeoLocation().toLatLng();

            if (boundsFilter == null || boundsFilter.filter(point)) {
                boundsBuilder.include(point);
            }
        }
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), BOUNDS_PADDING));
            }
        });
    }

    private void single() {
        map.addMarker(property.getMarkerOptions());

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(property.getLocation(), 15));
            return;
        }

        LatLng userLocation = DistanceHelper.getCurrentLocation();
        float distance = DistanceHelper.getDistance(property.getLocation(), userLocation);
        if (distance < DistanceHelper.MINIMUM_DISTANCE_IN_KILOMETERS) {
            H.logD("property is close");
            final LatLngBounds.Builder boundsBuilder = LatLngBounds.builder();
            boundsBuilder.include(userLocation);
            boundsBuilder.include(property.getLocation());
            map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 75));
                }
            });
        } else {
            H.logD("property not close to user location");
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(property.getLocation(), 15));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (showRoute) {
            RoutesController routesController = new RoutesController();
            Observable<List<Routes>> availableRoutes = routesController.getAvailableRoutes(DistanceHelper.getCurrentLocation(), property.getLocation());
            availableRoutes.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateInfoBoard);
        }
    }

    private void initMap() {
        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null);
        return view;
    }

    @Override
    public void updateInfoBoard(List<Routes> routes) {
        if (getView() == null) {
            return;
        }

        boolean hasAnyRoute = false;

        for (Routes route : routes) {
            if (route == null) {
                H.logE("routes is null");
                return;
            }

            if (route.routes == null) {
                H.logE("routes.routes is null");
                return;
            }

            if (route.routes.size() > 0) {
                String info = route.routes.get(0).legs.get(0).duration.text;

                ViewGroup layout = (ViewGroup) getView().findViewById(R.id.lyt_travel_modes);

                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_travel_mode, null);
                TravelModeViewHolder holder = new TravelModeViewHolder(view);

                TravelMode travelMode = route.getTravelMode();
                holder.icon.setImageDrawable(H.getDrawable(travelMode.getIconResource()));
                holder.time.setText(info);

                layout.addView(view);

                hasAnyRoute = true;
            }
        }

        if (hasAnyRoute) {
            getView().findViewById(R.id.lyt_travel_modes).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    static class TravelModeViewHolder {
        @Bind(R.id.img_travel_mode_icon)
        ImageView icon;
        @Bind(R.id.txt_travel_time)
        TextView time;

        TravelModeViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
