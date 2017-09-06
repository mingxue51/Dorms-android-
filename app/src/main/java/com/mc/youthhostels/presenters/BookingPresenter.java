package com.mc.youthhostels.presenters;

import android.content.Intent;
import android.support.v4.content.IntentCompat;

import com.mc.youthhostels.MainActivity2;
import com.mc.youthhostels.R;
import com.mc.youthhostels.events.OrderBookNowClickedEvent;
import com.mc.youthhostels.events.booking.ConfirmPaymentClickedEvent;
import com.mc.youthhostels.events.booking.DoneClickedEvent;
import com.mc.youthhostels.events.booking.FacebookLoginCompletedEvent;
import com.mc.youthhostels.events.booking.NextOnPersonDetailsClickedEvent;
import com.mc.youthhostels.events.booking.OrderSummarryNextClickedEvent;
import com.mc.youthhostels.events.booking.SkipStepClickedEvent;
import com.mc.youthhostels.fragments.booking.FastLoginFragment;
import com.mc.youthhostels.fragments.booking.OrderSummaryFragment;
import com.mc.youthhostels.views.BookingView;

import api.API;
import de.greenrobot.event.EventBus;
import entity.User.User;
import helper.App;
import helper.H;

public class BookingPresenter implements Presenter {

    private BookingView view;

    public BookingPresenter(BookingView view) {
        this.view = view;
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate() {
        App.getInstance().resetOrder();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(ConfirmPaymentClickedEvent event) {
        view.showLoading();
        API.getInstance(App.getInstance()).bookProperty(App.getInstance().getOrder(), new API.IGetRealTimeObject() {
            @Override
            public void getData(Object object) {
                view.hideLoading();
                view.showSuccessfulPayment();

                // start fetching user bookings for offline mode
                API.getInstance(App.getInstance().getBaseContext()).getUserBookings(new API.IGetRealTimeObject() {
                    @Override
                    public void getData(final Object object) {
                    }
                    @Override
                    public void onError(final String message) {
                        H.logE("bookings for offline mode not saved");
                    }
                });
            }

            @Override
            public void onError(String message) {
                view.hideLoading();
                H.logD("error in payment: " + message);
            }
        });
    }

    public void onEvent(OrderSummarryNextClickedEvent event) {
        if (User.isLogged()) {
            view.showPersonDetails();
        } else {
            view.showFastLogin();
        }
    }

    public void onEvent(FacebookLoginCompletedEvent event) {
        App.getInstance().getCurrentActivity().hideFragment(FastLoginFragment.class);
        view.showPersonDetails();
    }

    public void onEvent(SkipStepClickedEvent event) {
        view.showPersonDetails();
    }

    public void onEvent(NextOnPersonDetailsClickedEvent event) {
        view.showPayment();
    }

    public void onEvent(OrderBookNowClickedEvent event) {
        if (App.getInstance().getOrder() == null ||
            App.getInstance().getOrder().getTotalGuests() == 0) {

            H.showDialog(H.getString(R.string.message_no_rooms_added));
            return;
        }

        App.getInstance().getCurrentActivity().showFragment(OrderSummaryFragment.newInstance(event.getProperty()));
    }
}
