package com.mmashyr.roommanager.model;

import lombok.Data;

@Data
public class ClientBookingInfo {

    private final double budget;
    private RoomType bookedRoom;

    public boolean isBooked() {
        return bookedRoom != null;
    }

}
