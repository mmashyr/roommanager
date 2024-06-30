package com.mmashyr.roommanager.service.calculator;

import com.mmashyr.roommanager.model.RoomOccupancyInfo;
import com.mmashyr.roommanager.model.RoomsAvailability;
import com.mmashyr.roommanager.service.client.ClientService;
import com.mmashyr.roommanager.service.converter.RoomOccupancyInfoConverter;
import com.mmashyr.roommanager.service.factory.CalculationContextFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class OccupancyCalculatorService {

    private final List<RoomBooker> bookers;
    private final ClientService clientService;
    private final RoomOccupancyInfoConverter occupancyInfoConverter;
    private final CalculationContextFactory contextFactory;


    public OccupancyCalculatorService(@Qualifier("baseHotelChain") List<RoomBooker> bookers, ClientService clientService,
                                      RoomOccupancyInfoConverter occupancyInfoConverter, CalculationContextFactory contextFactory) {
        this.bookers = bookers;
        this.clientService = clientService;
        this.occupancyInfoConverter = occupancyInfoConverter;
        this.contextFactory = contextFactory;
    }

    public Collection<RoomOccupancyInfo> calculateOccupancy(Collection<RoomsAvailability> availabilityRequest) {
        Collection<Double> clientBudgets = clientService.getClientBudged();
        CalculationContext context = contextFactory.createCalculationContext(availabilityRequest, clientBudgets);
        bookers.forEach(booker -> booker.bookMatchedClients(context));

        return occupancyInfoConverter.toRoomOccupancyInfo(context.getClientBookingInfos());
    }
}