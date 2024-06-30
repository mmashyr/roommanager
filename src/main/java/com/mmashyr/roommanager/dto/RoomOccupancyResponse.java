package com.mmashyr.roommanager.dto;

import com.mmashyr.roommanager.model.RoomOccupancyInfo;

import java.util.Collection;

public record RoomOccupancyResponse(Collection<RoomOccupancyInfo> infos) {
}
