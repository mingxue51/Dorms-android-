package com.mc.youthhostels.model;

import com.mc.youthhostels.entity.Room;

public interface RoomOrderView {
    void makeActive();
    void disable();
    void updateCount(int count);
    Room getRoom();
    void updateSelected();
}
