package com.mmashyr.roommanager.service.calculator;

import com.mmashyr.roommanager.model.ClientBookingInfo;
import com.mmashyr.roommanager.model.RoomType;
import lombok.Data;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;

@Data
public class GenericRoomBooker implements RoomBooker {

    private final RoomType matchedRoomType;
    private final Predicate<ClientBookingInfo> matchingUserPredicate;
    private final Comparator<ClientBookingInfo> sortingOrder;

    @Override
    public void bookMatchedClients(CalculationContext calculationContext) {
        Long neededTypeRooms = calculationContext.getNumberOfRoomsOfType(matchedRoomType);
        if (neededTypeRooms > 0) {
            Collection<ClientBookingInfo> potentialClients = calculationContext.getMatchingClients(matchingUserPredicate, sortingOrder);
            for (ClientBookingInfo clientInfo : potentialClients) {
                if (neededTypeRooms > 0) {
                    calculationContext.bookRoom(clientInfo, matchedRoomType);
                    neededTypeRooms--;
                } else {
                    break;
                }
            }
        }
    }
}
