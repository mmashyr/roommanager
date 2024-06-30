package com.mmashyr.roommanager.service.calculator;

import com.mmashyr.roommanager.model.ClientBookingInfo;
import com.mmashyr.roommanager.model.RoomOccupancyInfo;
import com.mmashyr.roommanager.model.RoomsAvailability;
import com.mmashyr.roommanager.service.client.ClientService;
import com.mmashyr.roommanager.service.converter.RoomOccupancyInfoConverter;
import com.mmashyr.roommanager.service.factory.CalculationContextFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class OccupancyCalculatorServiceTest {

    @Mock
    private RoomBooker firstBooker;

    @Mock
    private RoomBooker secondBooker;

    @Mock
    private ClientService clientService;

    @Mock
    private RoomOccupancyInfoConverter occupancyInfoConverter;

    @Mock
    private CalculationContextFactory contextFactory;

    @Mock
    private RoomOccupancyInfo roomOccupancyInfo;

    private OccupancyCalculatorService occupancyCalculatorService;


    @BeforeEach
    void setUp() {
        occupancyCalculatorService = new OccupancyCalculatorService(List.of(firstBooker, secondBooker), clientService, occupancyInfoConverter, contextFactory);
    }

    @Test
    void shouldCalculateOccupancy() {
        // given
        RoomsAvailability roomsAvailability = mock(RoomsAvailability.class);
        CalculationContext calculationContext = mock(CalculationContext.class);
        Collection<RoomsAvailability> availabilityRequest = List.of(roomsAvailability);
        Collection<Double> clientBudgets = Arrays.asList(100.0, 200.0);
        List<ClientBookingInfo> clientInfos = List.of(mock(ClientBookingInfo.class));

        given(clientService.getClientBudged()).willReturn(clientBudgets);
        given(contextFactory.createCalculationContext(availabilityRequest, clientBudgets)).willReturn(calculationContext);
        given(calculationContext.getClientBookingInfos()).willReturn(clientInfos);
        given(occupancyInfoConverter.toRoomOccupancyInfo(clientInfos))
                .willReturn(Collections.singletonList(roomOccupancyInfo));

        // when
        Collection<RoomOccupancyInfo> actualOccupancyInfos = occupancyCalculatorService.calculateOccupancy(availabilityRequest);

        // then
        assertThat(actualOccupancyInfos).containsExactly(roomOccupancyInfo);

        InOrder inOrder = inOrder(firstBooker, secondBooker);
        inOrder.verify(firstBooker).bookMatchedClients(calculationContext);
        inOrder.verify(secondBooker).bookMatchedClients(calculationContext);
    }
}