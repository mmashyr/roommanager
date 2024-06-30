package com.mmashyr.roommanager.service.calculator;

import com.mmashyr.roommanager.model.ClientBookingInfo;
import com.mmashyr.roommanager.model.RoomType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenericRoomBookerTest {

    @Mock
    private CalculationContext calculationContext;

    @Mock
    private RoomType roomType;

    @Mock
    private Predicate<ClientBookingInfo> matchingUserPredicate;

    @Mock
    private Comparator<ClientBookingInfo> sortingOrder;

    @Mock
    private ClientBookingInfo clientInfo1;

    @Mock
    private ClientBookingInfo clientInfo2;

    @InjectMocks
    private GenericRoomBooker roomBooker;


    @Test
    void shouldBookClientWhenRoomsAreAvailable() {
        // given
        given(calculationContext.getNumberOfRoomsOfType(roomType)).willReturn(1L);
        given(calculationContext.getMatchingClients(matchingUserPredicate, sortingOrder))
                .willReturn(List.of(clientInfo1));

        // when
        roomBooker.bookMatchedClients(calculationContext);

        // then
        verify(calculationContext, times(1)).bookRoom(clientInfo1, roomType);
    }

    @Test
    void shouldNotBookClientsWhenNoRoomsAreAvailable() {
        // given
        given(calculationContext.getNumberOfRoomsOfType(roomType)).willReturn(0L);

        // when
        roomBooker.bookMatchedClients(calculationContext);

        // then
        verify(calculationContext, never()).bookRoom(any(ClientBookingInfo.class), eq(roomType));
    }

    @Test
    void shouldBookOnlyAvailableRooms() {
        // given
        given(calculationContext.getNumberOfRoomsOfType(roomType)).willReturn(1L);
        given(calculationContext.getMatchingClients(matchingUserPredicate, sortingOrder))
                .willReturn(asList(clientInfo1, clientInfo2));

        // when
        roomBooker.bookMatchedClients(calculationContext);

        // then
        verify(calculationContext, times(1)).bookRoom(clientInfo1, roomType);
        verify(calculationContext, never()).bookRoom(clientInfo2, roomType);
    }

    @Test
    void shouldBookClientsInCorrectOrder() {
        // given
        given(calculationContext.getNumberOfRoomsOfType(roomType)).willReturn(2L);
        given(calculationContext.getMatchingClients(matchingUserPredicate, sortingOrder))
                .willReturn(asList(clientInfo2, clientInfo1));

        // when
        roomBooker.bookMatchedClients(calculationContext);

        // then
        verify(calculationContext).bookRoom(clientInfo2, roomType);
        verify(calculationContext).bookRoom(clientInfo1, roomType);
    }

    @Test
    void shouldNotBookWhenNoClientsMatch() {
        // given
        given(calculationContext.getNumberOfRoomsOfType(roomType)).willReturn(2L);
        given(calculationContext.getMatchingClients(matchingUserPredicate, sortingOrder))
                .willReturn(emptyList());

        // when
        roomBooker.bookMatchedClients(calculationContext);

        // then
        verify(calculationContext, never()).bookRoom(any(), any());
    }
}