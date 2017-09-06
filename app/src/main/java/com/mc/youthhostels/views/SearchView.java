package com.mc.youthhostels.views;

import java.util.Date;

import com.mc.youthhostels.LoadingView;

public interface SearchView extends LoadingView {
    void setDates(Date checkIn, Date checkOut);
    void setLocationText(String location);
    void showOnlyDates();
    void showLocationInputDialog();
    void closeLocationInputDialog();
    void setAdditionBoardVisible(boolean visible);
    void hide();
}
