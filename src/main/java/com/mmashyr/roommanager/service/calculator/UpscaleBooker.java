package com.mmashyr.roommanager.service.calculator;

import com.mmashyr.roommanager.model.ClientBookingInfo;
import com.mmashyr.roommanager.model.RoomType;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.IntStream.iterate;

@Data
public class UpscaleBooker implements RoomBooker {

    private final RoomType matchedRoomType;
    private final RoomType upgradeTo;
    private final Predicate<ClientBookingInfo> matchingUserPredicate;
    private final Comparator<ClientBookingInfo> sortingOrder;

    @Override
    public void bookMatchedClients(CalculationContext calculationContext) {
        Long baseTypeRooms = calculationContext.getNumberOfRoomsOfType(matchedRoomType);
        Long upgradedTypeRooms = calculationContext.getNumberOfRoomsOfType(upgradeTo);

        List<ClientBookingInfo> potentialClients = calculationContext.getMatchingClients(matchingUserPredicate, sortingOrder);
        long numberOfClientsWithMissingBaseRooms = potentialClients.size() - baseTypeRooms;
        long numberOfClientsToUpgrade = Math.min(numberOfClientsWithMissingBaseRooms, upgradedTypeRooms);

        iterate(0,
                i -> i < numberOfClientsToUpgrade,
                i -> i + 1)
                .mapToObj(potentialClients::get)
                .forEach(clientInfo -> calculationContext.bookRoom(clientInfo, upgradeTo));
    }
}
