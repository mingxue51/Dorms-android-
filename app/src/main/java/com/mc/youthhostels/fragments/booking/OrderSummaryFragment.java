package com.mc.youthhostels.fragments.booking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mc.youthhostels.R;
import com.mc.youthhostels.entity.Order;
import com.mc.youthhostels.entity.Room;
import com.mc.youthhostels.events.booking.OrderSummarryNextClickedEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import entity.Generic.Price;
import entity.Property.Property;
import helper.App;
import helper.H;
import service.Localization;

public class OrderSummaryFragment extends Fragment {

    private Property property;

    @Bind(R.id.txt_property_type)
    TextView propertyType;
    @Bind(R.id.txt_property_name)
    TextView propertyName;
    @Bind(R.id.txt_property_street)
    TextView propertyStreet;
    @Bind(R.id.txt_check_in)
    TextView checkIn;
    @Bind(R.id.txt_check_out)
    TextView checkOut;
    @Bind(R.id.txt_number_of_nights)
    TextView numberOfNights;
    @Bind(R.id.lyt_dorms_rooms)
    LinearLayout roomsLayout;
    @Bind(R.id.txt_total)
    TextView total;
    @Bind(R.id.txt_pay_now_suggestion)
    TextView payNowSuggestion;
    @Bind(R.id.txt_deposit_pay_now)
    TextView depositPayNow;
    @Bind(R.id.txt_total_to_be_paid_now)
    TextView totalToBePaidNow;
    @Bind(R.id.txt_upon_arrival)
    TextView uponArrival;

    public static OrderSummaryFragment newInstance(Property property) {
        OrderSummaryFragment fragment = new OrderSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Property.BUNDLE, property);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_summary, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            property = (Property) getArguments().getSerializable(Property.BUNDLE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        H.logD("booking details on resume");
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.booking_details);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Order order = App.getInstance().getOrder();

        propertyType.setText(property.getPropertyType().toUpperCase());
        propertyName.setText(property.getPropertyName());

        if (property.getPropertyAddress() != null) {
            propertyStreet.setText(property.getFullAddressString());
        } else {
            propertyStreet.setVisibility(View.GONE);
        }

        for (Room room : order.getRooms()) {
            int guests = room.getSelected();
            if (guests <= 0) {
                continue;
            }

            View roomView = LayoutInflater.from(getActivity()).inflate(R.layout.item_room_summary, null);
            ViewHolder holder = new ViewHolder(roomView);

            holder.roomName.setText(room.getName());
            holder.roomTotalPrice.setText(Localization.getPriceLocalized(getActivity(), room.getTotalPricePerGuest()));
            holder.guestsCount.setText(getResources().getQuantityString(R.plurals.guests, guests, guests));

            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 57, getResources().getDisplayMetrics());

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    height);

            roomView.setLayoutParams(params);
            roomsLayout.addView(roomView);
        }

        BigDecimal totalPrice = order.getTotalPrice();
        BigDecimal percent = order.getDepositPercentage();
        BigDecimal deposit = totalPrice.multiply(percent.divide(BigDecimal.valueOf(100))).setScale(Price.DECIMAL_PLACES, RoundingMode.UP);
        depositPayNow.setText(Localization.getPriceLocalized(getActivity(), deposit));
        totalToBePaidNow.setText(Localization.getPriceLocalized(getActivity(), deposit));
        payNowSuggestion.setText(String.format(payNowSuggestion.getText().toString(), percent.intValueExact()));

        total.setText(Localization.getPriceLocalized(getActivity(), order.getTotalPrice()));

        int nights = order.getNightsCount();
        numberOfNights.setText(String.format(H.getString(R.string.number_of_nights_summary), nights));

        SimpleDateFormat simpleDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        checkIn.setText(String.format(checkIn.getText().toString(), simpleDate.format(order.getArrivalDate())));
        checkOut.setText(String.format(checkOut.getText().toString(), simpleDate.format(order.getDepartureDate())));
        uponArrival.setText(String.format(uponArrival.getText().toString(),
                                          Localization.getPriceLocalized(getActivity(), totalPrice.subtract(deposit))));
    }

    @OnClick(R.id.btn_modify)
    public void onModifyClicked() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.btn_next)
    public void onNextClicked() {
        EventBus.getDefault().post(new OrderSummarryNextClickedEvent());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    static class ViewHolder {
        @Bind(R.id.txt_room_name)
        TextView roomName;
        @Bind(R.id.txt_room_price)
        TextView roomTotalPrice;
        @Bind(R.id.txt_guests_count)
        TextView guestsCount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
