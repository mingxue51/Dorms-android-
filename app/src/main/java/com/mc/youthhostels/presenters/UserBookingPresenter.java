package com.mc.youthhostels.presenters;


import android.os.Handler;

import com.mc.youthhostels.R;
import com.mc.youthhostels.entity.Booking;
import com.mc.youthhostels.events.booking.UserBookingsFetchedEvent;

import java.util.List;

import api.API;
import de.greenrobot.event.EventBus;
import entity.User.User;
import helper.App;
import helper.H;

public class UserBookingPresenter implements Presenter {

    @Override
    public void onResume() {
        getUserBookings();
    }

    private void getUserBookings() {
        H.logD("start fetching user bookings");
        App.getInstance().showLoading();
        if (!App.getInstance().isNetworkAvailable()) {
            User user = User.getUserFromSession(App.getInstance());
            List<Booking> bookings = user.getSavedBookings();
            if (bookings != null) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        H.logD("bookings fetched from session");
                        App.getInstance().hideLoading();
                        EventBus.getDefault().post(new UserBookingsFetchedEvent(bookings));
                    }
                }, 800);
            } else {
                H.showDialog(H.getString(R.string.check_internet_connection));
            }
            return;
        }

        API.getInstance(App.getInstance().getBaseContext()).getUserBookings(new API.IGetRealTimeObject() {
            @Override
            public void getData(final Object object) {
                App.getInstance().hideLoading();
                App.getInstance().getCurrentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Booking> bookings = (List<Booking>)object;
                        EventBus.getDefault().post(new UserBookingsFetchedEvent(bookings));
                    }
                });
            }

            @Override
            public void onError(final String message) {
                App.getInstance().hideLoading();
                H.logE(message);
            }
        });
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onPause() {
    }
}
