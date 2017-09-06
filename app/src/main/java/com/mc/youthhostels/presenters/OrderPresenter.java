package com.mc.youthhostels.presenters;


import android.content.Intent;
import android.os.Bundle;

import com.mc.youthhostels.R;
import com.mc.youthhostels.activities.AppActivity;
import com.mc.youthhostels.entity.Order;
import com.mc.youthhostels.entity.Room;
import com.mc.youthhostels.events.DatesChosenEvent;
import com.mc.youthhostels.events.order.OrderMinusClickedEvent;
import com.mc.youthhostels.events.order.OrderPlusClickedEvent;
import com.mc.youthhostels.fragments.booking.OrderSummaryFragment;
import com.mc.youthhostels.model.RoomOrderView;
import com.mc.youthhostels.views.PropertyOrderView;

import java.math.BigDecimal;

import api.API;
import de.greenrobot.event.EventBus;
import entity.Property.Property;
import entity.Property.Search.SearchProperty;
import helper.App;
import helper.DT;
import helper.H;

public class OrderPresenter implements Presenter {

    PropertyOrderView view;
    Property property;
    SearchProperty searchProperty;
    private Order order;

    public OrderPresenter(PropertyOrderView view, Property property, SearchProperty searchProperty) {
        this.view = view;
        this.property = property;
        this.searchProperty = searchProperty;
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
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

    public void fetchOrder() {
        if (DT.isDateInThePast(searchProperty.getArrivalDate())) {
            if (view != null) {
                searchProperty.setDefaultDates();
                view.showCalendar();
                return;
            }
        }

        // check if we go back and show current order
        Order currentOrder = App.getInstance().getOrder();
        if (currentOrder != null && currentOrder.getTotalGuests() > 0) {
            view.showOrder(currentOrder);
            order = currentOrder;
            updateTotalOrder();
            return;
        }

        // start checking for availability
        App.getInstance().showLoading();
        view.hideAll();
        searchProperty.setPropertyId(property.getPropertyNumber());

        API.getInstance(App.getInstance()).getPropertyAvailability(searchProperty, new API.IGetRealTimeObject() {
            @Override
            public void getData(Object object) {
                App.getInstance().hideLoading();
                order = (Order) object;

                App.getInstance().setOrder(order);
                order.setArrivalDate(searchProperty.getArrivalDate());
                order.setDepartureDate(searchProperty.getDepartureDate());

                order.setPropertyNumber(property.getPropertyNumber());
                order.setPropertyName(property.getPropertyName());

                H.logD("order rooms count :" + order.getRooms().size());

                view.showOrder(order);
                view.updateFooter(new BigDecimal(0), order.getDepositPercentage(), searchProperty.getNumNightsP(), 0);
            }

            @Override
            public void onError(String message) {
                App.getInstance().hideLoading();
                H.logE(message);
                H.showDialog(H.getString(R.string.no_properties_found));
            }
        });
    }

    public void onEvent(OrderPlusClickedEvent event) {
        H.logD("on plus clicked event started");

        RoomOrderView orderView = event.getRoomOrderView();
        Room room = orderView.getRoom();

        if (room.getSelected() >= room.getBeds()) {
            //todo show limit message
        } else {
            int blockbeds = room.getBlockbeds();

            if (blockbeds == 0) {
                blockbeds = 1;
            }

            H.logD("blockbeds is " + blockbeds);

            room.setSelected(room.getSelected() + blockbeds);
            orderView.updateCount(room.getSelected());
            updateTotalOrder();
        }

        if (room.getSelected() > 0) {
            orderView.makeActive();
        } else {
            orderView.disable();
        }
    }

    public void onEvent(OrderMinusClickedEvent event) {
        RoomOrderView orderView = event.getRoomOrderView();
        Room room = orderView.getRoom();

        if (room.getSelected() <= 0) {
            //todo show limit message
        } else {
            int blockbeds = room.getBlockbeds();

            if (blockbeds == 0) {
                blockbeds = 1;
            }

            H.logD("blockbeds is " + blockbeds);

            room.setSelected(room.getSelected() - blockbeds);
            orderView.updateCount(room.getSelected());
            updateTotalOrder();
        }

        if (room.getSelected() > 0) {
            orderView.makeActive();
        } else {
            orderView.disable();
        }
    }

    private void updateTotalOrder() {
        H.logD("calculating total order");

        if (order == null) {
            H.logE("order is null in updateTotalOrder method");
            return;
        }

        view.updateFooter(order.getTotalPrice(),
                          order.getDepositPercentage(),
                          searchProperty.getNumNightsP(),
                          order.getTotalGuests());
    }

    public void onEvent(DatesChosenEvent event) {
        H.logD("dates chosen events in order presenter:" + event.getCheckIn() + " " + event.getCheckOut());
        view.setDates(event.getCheckIn(), event.getCheckOut());
        searchProperty.setArrivalDate(event.getCheckIn());
        searchProperty.setDepartureDate(event.getCheckOut());
        App.getInstance().resetOrder();
        fetchOrder();
    }

    public SearchProperty getSearchProperty() {
        return searchProperty;
    }
}
