package com.mc.youthhostels.views;

import com.mc.youthhostels.LoadingView;

public interface BookingView extends LoadingView {
    void showSuccessfulPayment();
    void showPayment();
    void showPersonDetails();
    void showFastLogin();
}
