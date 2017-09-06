package com.mc.youthhostels.views;

import com.mc.youthhostels.entity.Order;

import java.math.BigDecimal;
import java.util.Date;

import com.mc.youthhostels.LoadingView;

public interface PropertyOrderView extends LoadingView {
    void showOrder(Order order);
    void setDates(Date checkIn, Date checkOut);
    void updateFooter(BigDecimal totalPrice, BigDecimal percent, int nights, int guests);
    void showCalendar();
    void hideAll();
    void showAll();
}
