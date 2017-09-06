package com.mc.youthhostels.events;

import java.util.Date;

public class DatesChosenEvent {
    private Date checkIn;
    private Date checkOut;

    public DatesChosenEvent(Date checkIn, Date checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }
}
