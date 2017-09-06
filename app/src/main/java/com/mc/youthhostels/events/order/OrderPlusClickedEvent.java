package com.mc.youthhostels.events.order;


import com.mc.youthhostels.model.RoomOrderView;

public class OrderPlusClickedEvent {
    private RoomOrderView roomOrderView;

    public OrderPlusClickedEvent(RoomOrderView roomOrderView) {
        this.roomOrderView = roomOrderView;
    }

    public RoomOrderView getRoomOrderView() {
        return roomOrderView;
    }
}
