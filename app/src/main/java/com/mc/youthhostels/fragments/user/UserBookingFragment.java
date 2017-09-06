package com.mc.youthhostels.fragments.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mc.youthhostels.R;
import com.mc.youthhostels.entity.Booking;
import com.mc.youthhostels.events.booking.UserBookingsFetchedEvent;
import com.mc.youthhostels.fragments.booking.NewUserReviewFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import entity.Generic.GeoLocation;
import entity.Property.Property;
import helper.App;
import helper.DT;
import helper.H;
import service.Localization;

public class UserBookingFragment extends Fragment {
    private static final String IS_UPCOMING_TAG = "is_upcoming";
    private boolean isUpcoming;
    private BookingAdapter adapter;

    public static UserBookingFragment newInstance(boolean isUpcoming) {
        UserBookingFragment fragment = new UserBookingFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_UPCOMING_TAG, isUpcoming);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            isUpcoming = arguments.getBoolean(IS_UPCOMING_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_some_fragment, container, false);

        RecyclerView list = (RecyclerView) view.findViewById(R.id.list_bookings);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);
        adapter = new BookingAdapter(isUpcoming);
        list.setAdapter(adapter);

        H.logD("booking fragment view created");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(UserBookingsFetchedEvent event) {
        H.logD("user bookings fetched: in on UserBookingsFetchedEvent");
        List<Booking> bookings = event.getBookings();

        List<Booking> filteredBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            if(DT.isDateInThePast(DT.getDateFromString(booking.getArrivalDate()))) {
                if (!isUpcoming) {
                    filteredBookings.add(booking);
                }
            }else {
                if (isUpcoming) {
                    filteredBookings.add(booking);
                }
            }
        }

        adapter.setBookings(filteredBookings);
        adapter.notifyDataSetChanged();
    }


    public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
        private List<Booking> bookings = new ArrayList<>();
        private boolean isUpcoming;

        public BookingAdapter(boolean isUpcoming) {
            this.isUpcoming = isUpcoming;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(App.getInstance().getCurrentActivity()).inflate(R.layout.item_user_booking, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BookingAdapter.ViewHolder holder, int position) {
            Booking booking = bookings.get(position);
            Property property = booking.getProperty();
            Glide.with(App.getInstance()).load(property.getBigPreview())
                                         .skipMemoryCache(true)
                                         .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                         .into(holder.propertyPreview);

            holder.propertyName.setText(property.getPropertyName());
            holder.propertyType.setText(property.getPropertyType().toUpperCase());

            String finalRefNum = String.format(H.getString(R.string.reference_number),
                                               booking.getReferenceNumber());

            holder.referenceNumber.setText(finalRefNum);

            String currencyCode = booking.getCurrencyCode();
            holder.alreadyPaid.setText(Localization.getFormattedPrice(booking.getChargedPrice(), currencyCode));
            holder.amount.setText(Localization.getFormattedPrice(booking.getOnArrivalPrice(), currencyCode));
            holder.total.setText(Localization.getFormattedPrice(booking.getTotalPrice(), currencyCode));

            holder.checkInOn.setText(String.format(H.getString(R.string.check_in_on), booking.getArrivalDate()));

            String actionButtonText = isUpcoming ? H.getString(R.string.directions) : H.getString(R.string.leave_review);
            holder.action.setText(actionButtonText);
            holder.txtNights.setText(getResources().getQuantityString(R.plurals.nights,
                                                                      booking.getNumNights(),
                                                                      booking.getNumNights()));
            holder.txtGuests.setText(getResources().getQuantityString(R.plurals.guests,
                                                                      booking.getNumGuests(),
                                                                      booking.getNumGuests()));
            holder.action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isUpcoming) {
                        GeoLocation geoLocation = property.getGeoLocation();
                        String cords = String.valueOf(geoLocation.getLatitude()) + "," + String.valueOf(geoLocation.getLontitude());
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + cords);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        App.getInstance().getCurrentActivity().startActivity(mapIntent);
                    } else {
                        NewUserReviewFragment fragment = NewUserReviewFragment.newInstance(booking);
                        App.getInstance().getCurrentActivity().showFragment(fragment);
                    }
                }
            });

            holder.action.setVisibility(View.VISIBLE);
            if (!isUpcoming && booking.hasReview()) {
                holder.action.setVisibility(View.GONE);
            }

            String phoneNumber = property.getPropertyTelephone();
            if (phoneNumber != null && phoneNumber.length() > 0) {
                holder.phoneNumber.setText(String.format(H.getString(R.string.property_phone_number), phoneNumber));
            } else {
                holder.phoneNumber.setVisibility(View.GONE);
            }

            holder.phoneNumber.setTextIsSelectable(true);

            String email = property.getPropertyEmail();
            if (email != null && email.length() > 0) {
                holder.mailButton.setVisibility(View.VISIBLE);
                holder.mailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                                Uri.fromParts("mailto",
                                              email,
                                              null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                        App.getInstance().getCurrentActivity().startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    }
                });
            } else {
                holder.mailButton.setVisibility(View.GONE);
            }

            if (!isUpcoming) {
                holder.mailButton.setVisibility(View.GONE);
            }
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
            @Bind(R.id.txt_reference_number)
            TextView referenceNumber;
            @Bind(R.id.txt_check_in_on)
            TextView checkInOn;
            @Bind(R.id.txt_already_paid)
            TextView alreadyPaid;
            @Bind(R.id.txt_amount)
            TextView amount;
            @Bind(R.id.txt_total)
            TextView total;
            @Bind(R.id.txt_phone_number)
            TextView phoneNumber;
            @Bind(R.id.btn_action)
            Button action;
            @Bind(R.id.btn_mail)
            ImageButton mailButton;
            @Bind(R.id.txt_nights)
            TextView txtNights;
            @Bind(R.id.txt_guests)
            TextView txtGuests;

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