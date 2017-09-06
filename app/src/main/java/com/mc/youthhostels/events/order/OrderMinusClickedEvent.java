package com.mc.youthhostels.events.order;

import com.mc.youthhostels.model.RoomOrderView;

public class OrderMinusClickedEvent {
    private RoomOrderView roomOrderView;

    public OrderMinusClickedEvent(RoomOrderView roomOrderView) {
        this.roomOrderView = roomOrderView;
    }

    public RoomOrderView getRoomOrderView() {
        return roomOrderView;
    }
}
