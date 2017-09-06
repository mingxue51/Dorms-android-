package com.mc.youthhostels.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.mc.youthhostels.R;
import com.mc.youthhostels.activities.AppActivity;
import com.mc.youthhostels.entity.Booking;
import com.mc.youthhostels.events.booking.UserBookingsFetchedEvent;
import com.mc.youthhostels.presenters.UserBookingPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import entity.Generic.GeoLocation;
import entity.Property.Property;
import helper.App;
import helper.H;

public class DirectionsFragment extends Fragment {
    private static final String PANORAMA_FRAGMENT_TAG = "PANORAMA_TAG";

    private UserBookingPresenter presenter;
    private DirectionsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new UserBookingPresenter();
        presenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_directions, container, false);

        RecyclerView list = (RecyclerView) view.findViewById(R.id.list_bookings);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);
        adapter = new DirectionsAdapter();
        list.setAdapter(adapter);

        H.logD("booking fragment view created");

//        getChildFragmentManager().beginTransaction()
//                .replace(R.id.streetviewpanorama,
//                        new SupportStreetViewPanoramaFragment(),
//                        PANORAMA_FRAGMENT_TAG)
//                .commit();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //hideStreetViewFragment();
    }

    private void hideStreetViewFragment() {
        SupportStreetViewPanoramaFragment streetViewPanoramaFragment = getSupportStreetViewPanoramaFragment();

        if (streetViewPanoramaFragment == null) {
            H.logE("street view fragment is null");
            return;
        }

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .hide(streetViewPanoramaFragment)
                .commit();
    }

    private void showStreetViewFragment() {
        SupportStreetViewPanoramaFragment streetViewPanoramaFragment = getSupportStreetViewPanoramaFragment();

        if (streetViewPanoramaFragment == null) {
            H.logE("street view fragment is null");
            return;
        }

        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction()
                .show(streetViewPanoramaFragment)
                .addToBackStack(null)
                .commit();
    }

    private SupportStreetViewPanoramaFragment getSupportStreetViewPanoramaFragment() {
        return (SupportStreetViewPanoramaFragment) getChildFragmentManager()
                .findFragmentByTag(PANORAMA_FRAGMENT_TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        EventBus.getDefault().register(this);

        AppActivity activity = (AppActivity) getActivity();
        activity.setDefaultActionBar();
        activity.setActionBarTitle(H.getString(R.string.directions));
    }

    @Override
    public void onPause() {
        presenter.onPause();
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    public void onEvent(UserBookingsFetchedEvent event) {
        H.logD("user bookings fetched: in on UserBookingsFetchedEvent");
        List<Booking> bookings = event.getBookings();
        adapter.setBookings(bookings);
        adapter.notifyDataSetChanged();
    }

    public class DirectionsAdapter extends RecyclerView.Adapter<DirectionsAdapter.ViewHolder> {
        private List<Booking> bookings = new ArrayList<>();

        public DirectionsAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(App.getInstance().getCurrentActivity()).inflate(R.layout.item_directions, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DirectionsAdapter.ViewHolder holder, int position) {
            Booking booking = bookings.get(position);
            Property property = booking.getProperty();

            initPreview(holder, property);
            initHeader(holder, property);
            initAddress(holder, booking);
            initDirectionButton(holder, property);
            initStreetViewButton(holder, property);
        }

        private void initStreetViewButton(ViewHolder holder, final Property property) {
            holder.streeViewButton.setVisibility(View.GONE);

            holder.streeViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStreetViewFragment();
                    SupportStreetViewPanoramaFragment panoramaFragment = getSupportStreetViewPanoramaFragment();
                    panoramaFragment.getStreetViewPanorama().setOnStreetViewPanoramaChangeListener(new StreetViewPanorama.OnStreetViewPanoramaChangeListener() {
                        @Override
                        public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {
                            if (streetViewPanoramaLocation != null && streetViewPanoramaLocation.links != null) {
                            } else {
                                // location not available
                                Toast.makeText(getActivity(), R.string.street_view_not_available, Toast.LENGTH_LONG).show();
                                hideStreetViewFragment();
                            }
                        }
                    });

                    panoramaFragment.getStreetViewPanorama().setPosition(property.getLocation());
                }
            });
        }

        private void initDirectionButton(ViewHolder holder, final Property property) {
            holder.directionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        GeoLocation geoLocation = property.getGeoLocation();
                        String cords = String.valueOf(geoLocation.getLatitude()) + "," + String.valueOf(geoLocation.getLontitude());
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + cords);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        App.getInstance().getCurrentActivity().startActivity(mapIntent);
                }
            });
        }

        private void initAddress(ViewHolder holder, Booking booking) {
            if (booking.getFullAddress() != null) {
                holder.propertyAddress.setVisibility(View.VISIBLE);
                holder.propertyAddress.setText(booking.getFullAddress());
            } else {
                holder.propertyAddress.setVisibility(View.GONE);
            }
        }

        private void initHeader(ViewHolder holder, Property property) {
            holder.propertyName.setText(property.getPropertyName());
            holder.propertyType.setText(property.getPropertyType().toUpperCase());
        }

        private void initPreview(ViewHolder holder, Property property) {
            Glide.with(App.getInstance()).load(property.getBigPreview())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.propertyPreview);
        }

        @Override
        public int getItemCount() {
            return bookings.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.img_property_preview)
            ImageView propertyPreview;
            @Bind(R.id.txt_property_type)
            TextView propertyType;
            @Bind(R.id.txt_property_name)
            TextView propertyName;
            @Bind(R.id.txt_address)
            TextView propertyAddress;
            @Bind(R.id.btn_direction)
            Button directionButton;
            @Bind(R.id.btn_street_view)
            ImageButton streeViewButton;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

        public void setBookings(List<Booking> bookings) {
            this.bookings = bookings;
        }
    }
}
