package com.mc.youthhostels.events.booking;

import com.mc.youthhostels.entity.Booking;

import java.util.List;

public class UserBookingsFetchedEvent {
    private List<Booking> bookings;

    public UserBookingsFetchedEvent(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public List<Booking> getBookings() {
        return bookings;
    }
}
