package com.mc.youthhostels.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mc.youthhostels.CalendarFragment;
import com.mc.youthhostels.R;
import com.mc.youthhostels.activities.AppActivity;
import com.mc.youthhostels.entity.Order;
import com.mc.youthhostels.entity.Room;
import com.mc.youthhostels.events.OrderBookNowClickedEvent;
import com.mc.youthhostels.events.order.OrderMinusClickedEvent;
import com.mc.youthhostels.events.order.OrderPlusClickedEvent;
import com.mc.youthhostels.model.RoomOrderView;
import com.mc.youthhostels.presenters.OrderPresenter;
import com.mc.youthhostels.views.PropertyOrderView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import entity.Generic.Price;
import entity.Property.Property;
import entity.Property.Search.SearchProperty;
import helper.App;
import helper.DT;
import helper.H;
import service.Localization;

public class PropertyOrderFragment extends Fragment implements PropertyOrderView {
    public static final String TAG = PropertyOrderFragment.class.getSimpleName();

    @Bind(R.id.check_in_day)
    TextView checkInDay;
    @Bind(R.id.check_in_month)
    TextView checkInMonth;
    @Bind(R.id.check_in_week_day)
    TextView checkInWeekDay;
    @Bind(R.id.check_out_day)
    TextView checkOutDay;
    @Bind(R.id.check_out_month)
    TextView checkOutMonth;
    @Bind(R.id.check_out_week_day)
    TextView checkOutWeekDay;
    @Bind(R.id.txt_total_to_pay_now)
    TextView totalPayNow;
    @Bind(R.id.txt_nights)
    TextView txtNights;
    @Bind(R.id.txt_guests)
    TextView txtGuests;
    @Bind(R.id.txt_total)
    TextView txtTotal;
    @Bind(R.id.property_order_board)
    View board;

    private OrderPresenter presenter;
    private Property property;

    public static PropertyOrderFragment newInstance(Property property, SearchProperty searchProperty) {
        PropertyOrderFragment propertyOrderFragment = new PropertyOrderFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(Property.BUNDLE, property);
        bundle.putSerializable(SearchProperty.BUNDLE, searchProperty);

        propertyOrderFragment.setArguments(bundle);

        return propertyOrderFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        presenter.fetchOrder();
        updateFooter(new BigDecimal(0), new BigDecimal(0), 0, 0);

        AppActivity activity = (AppActivity) getActivity();
        activity.setDefaultActionBar();
        activity.setActionBarTitle(property.getPropertyName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        property = (Property) getArguments().getSerializable(Property.BUNDLE);
        SearchProperty searchProperty = (SearchProperty) getArguments().getSerializable(SearchProperty.BUNDLE);

        presenter = new OrderPresenter(this, property, searchProperty);

        H.logD("order fragment: on create method called");
    }

    @OnClick(R.id.lyt_dates)
    public void onDatesClick(View v) {
        showCalendar();
    }

    @Override
    public void showCalendar() {
        CalendarFragment.newInstance(presenter.getSearchProperty().getArrivalDate(),
                presenter.getSearchProperty().getDepartureDate())
                .show(getChildFragmentManager(), CalendarFragment.TAG);
    }

    private void setCheckInDate(Date checkIn) {
        checkInDay.setText(DT.formatDay(checkIn));
        checkInMonth.setText(DT.formateDate(checkIn).toUpperCase());
        checkInWeekDay.setText(DT.formateWeekDate(checkIn));
    }

    private void setCheckOutDate(Date checkOut) {
        checkOutDay.setText(DT.formatDay(checkOut));
        checkOutMonth.setText(DT.formateDate(checkOut).toUpperCase());
        checkOutWeekDay.setText(DT.formateWeekDate(checkOut));
    }

    @Override
    public void setDates(Date checkIn, Date checkOut) {
        setCheckInDate(checkIn);
        setCheckOutDate(checkOut);
    }

    @Override
    public void hideAll() {
        H.runOnUi(new Runnable() {
            @Override
            public void run() {
                if (board != null) {
                    board.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void showAll() {
        H.runOnUi(new Runnable() {
            @Override
            public void run() {
                if (board != null) {
                    board.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_order, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setDates(presenter.getSearchProperty().getArrivalDate(), presenter.getSearchProperty().getDepartureDate());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void hideLoading() {
        App.getInstance().hideLoading();
    }

    @Override
    public void showLoading() {
        App.getInstance().showLoading();
    }

    @Override
    public void showOrder(Order order) {
        showAll();
        initPriceSuggestion(order);
        initRoomsLayout(R.id.lyt_dorms, R.id.lyt_dorms_suggestion, order.getDorms());
        initRoomsLayout(R.id.lyt_private_rooms, R.id.lyt_private_rooms_suggestion, order.getPrivateRooms());
    }

    private void initPriceSuggestion(Order order) {
        if (order.getDorms().isEmpty()) {
            if (getView() != null) {
                getView().findViewById(R.id.txt_dorms_price_description).setVisibility(View.GONE);
            }
        }
    }

    private void initRoomsLayout(int layoutId, int suggestionId,  List<Room> rooms) {
        ViewGroup layout = (ViewGroup) getView().findViewById(layoutId);
        layout.removeAllViews();

        getView().findViewById(suggestionId).setVisibility(View.VISIBLE);
        layout.setVisibility(View.VISIBLE);

        if (rooms.isEmpty()) {
            getView().findViewById(suggestionId).setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
            return;
        }

        for (Room room : rooms) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_room, null);
            ViewHolder holder = new ViewHolder(view, room);

            holder.roomName.setText(room.getName());
            holder.roomPrice.setText(Localization.getPriceLocalized(getActivity(), room.getAveragePrice()));

            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 57, getResources().getDisplayMetrics());

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    height);

            if (room.getSelected() > 0) {
                holder.makeActive();
                holder.updateCount(room.getSelected());
            }

            view.setLayoutParams(params);
            layout.addView(view);
        }
    }

    static class ViewHolder implements RoomOrderView {
        @Bind(R.id.txt_room_name)
        TextView roomName;
        @Bind(R.id.txt_room_price)
        TextView roomPrice;
        @Bind(R.id.lyt_order_control)
        LinearLayout orderControl;
        @Bind(R.id.btn_minus)
        ImageButton minus;
        @Bind(R.id.btn_plus)
        ImageButton plus;
        @Bind(R.id.txt_selected)
        TextView selected;

        private Room room;

        ViewHolder(View view, Room room) {
            ButterKnife.bind(this, view);
            this.room = room;
        }

        @Override
        public void makeActive() {
            orderControl.setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.green_action));
            minus.setImageDrawable(H.getDrawable(R.drawable.icon_minus_white));
            plus.setImageDrawable(H.getDrawable(R.drawable.icon_plus_white));
            selected.setTextColor(H.getColor(R.color.white));
        }

        @Override
        public void disable() {
            orderControl.setBackground(ContextCompat.getDrawable(App.getInstance(), R.drawable.back));
            minus.setImageDrawable(H.getDrawable(R.drawable.icon_minus_grey));
            plus.setImageDrawable(H.getDrawable(R.drawable.icon_plus_green));
            selected.setTextColor(H.getColor(R.color.grey_checkable));
        }

        @Override
        public void updateCount(int count) {
            selected.setText(String.valueOf(count));
        }

        @OnClick(R.id.btn_plus)
        public void onPlusClicked() {
            H.logD("on plus button clicked");
            EventBus.getDefault().post(new OrderPlusClickedEvent(this));
        }

        @OnClick(R.id.btn_minus)
        public void onMinusCllicked() {
            EventBus.getDefault().post(new OrderMinusClickedEvent(this));
        }

        @Override
        public void updateSelected() {
            selected.setText(String.valueOf(room.getSelected()));
        }

        @Override
        public Room getRoom() {
            return room;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void updateFooter(BigDecimal totalPrice, BigDecimal percent, int nights, int guests) {
        String payNowString = getActivity().getString(R.string.total_to_be_paid_now);
        BigDecimal deposit = totalPrice.multiply(percent.divide(BigDecimal.valueOf(100))).setScale(Price.DECIMAL_PLACES, RoundingMode.UP);
        String totalDeposit = getActivity().getString(R.string.total_to_be_paid);
        totalDeposit += Localization.getPriceLocalized(getActivity(), totalPrice);
        payNowString += Localization.getPriceLocalized(getActivity(), deposit);
        totalPayNow.setText(payNowString);
        txtTotal.setText(totalDeposit);
        txtNights.setText(getResources().getQuantityString(R.plurals.nights, nights, nights));
        txtGuests.setText(getResources().getQuantityString(R.plurals.guests, guests, guests));

        H.logD("footer updated");
    }

    @OnClick(R.id.btn_book_now)
    public void onBookNowClicked() {
        EventBus.getDefault().post(new OrderBookNowClickedEvent(property));
    }
}
