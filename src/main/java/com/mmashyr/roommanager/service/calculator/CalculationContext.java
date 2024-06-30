package com.mmashyr.roommanager.service.calculator;

import com.mmashyr.roommanager.model.ClientBookingInfo;
import com.mmashyr.roommanager.model.RoomType;
import com.mmashyr.roommanager.model.RoomsAvailability;
import lombok.Data;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toMap;

@Data
public class CalculationContext {
    private final Map<RoomType, Long> availability;
    private final Collection<ClientBookingInfo> clientBookingInfos;

    public CalculationContext(Collection<RoomsAvailability> availabilities, Collection<Double> clientBudgets) {
        this.availability = availabilities.stream()
                .collect(toMap(
                        RoomsAvailability::roomType,
                        RoomsAvailability::numberOfRooms));
        this.clientBookingInfos = clientBudgets.stream()
                .map(ClientBookingInfo::new)
                .toList();
    }

    public Long getNumberOfRoomsOfType(RoomType roomType) {
        Long numberOfRooms = availability.get(roomType);
        return numberOfRooms == null ? 0 : numberOfRooms;
    }

    public void bookRoom(ClientBookingInfo clientInfo, RoomType roomType) {
        Long roomsLeft = availability.get(roomType);
        if (roomsLeft != null && roomsLeft > 0) {
            clientInfo.setBookedRoom(roomType);
            availability.put(roomType, roomsLeft - 1);
        }
    }

    public List<ClientBookingInfo> getMatchingClients(Predicate<ClientBookingInfo> matchingUserPredicate,
                                                      Comparator<ClientBookingInfo> sortingOrder) {
        return clientBookingInfos
                .stream()
                .filter(matchingUserPredicate)
                .sorted(sortingOrder)
                .toList();
    }
}
