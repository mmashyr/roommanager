package com.mmashyr.roommanager.service.converter;

import com.mmashyr.roommanager.model.ClientBookingInfo;
import com.mmashyr.roommanager.model.RoomOccupancyInfo;
import com.mmashyr.roommanager.model.RoomType;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static java.util.stream.Collectors.*;

@Service
public class RoomOccupancyInfoConverter {

    public Collection<RoomOccupancyInfo> toRoomOccupancyInfo(Collection<ClientBookingInfo> clientBookingInfos) {
        return clientBookingInfos.stream()
                .filter(ClientBookingInfo::isBooked)
                .collect(groupingBy(
                        ClientBookingInfo::getBookedRoom,
                        collectingAndThen(
                                toList(),
                                bookings -> {
                                    RoomType roomType = bookings.get(0).getBookedRoom();
                                    int totalGuests = bookings.size();
                                    double totalProfit = bookings.stream().mapToDouble(ClientBookingInfo::getBudget).sum();
                                    return new RoomOccupancyInfo(roomType, totalGuests, totalProfit);
                                }
                        )
                )).values();
    }
}
