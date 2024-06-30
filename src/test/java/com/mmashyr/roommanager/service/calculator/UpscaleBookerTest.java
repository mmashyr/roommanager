package com.mmashyr.roommanager.service.calculator;

import com.mmashyr.roommanager.model.ClientBookingInfo;
import com.mmashyr.roommanager.model.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UpscaleBookerTest {

    @Mock
    private CalculationContext calculationContext;

    @Mock
    private RoomType baseRoomType;

    @Mock
    private RoomType upgradeRoomType;

    @Mock
    private Predicate<ClientBookingInfo> matchingUserPredicate;

    @Mock
    private Comparator<ClientBookingInfo> sortingOrder;

    @Mock
    private ClientBookingInfo clientInfo1;

    @Mock
    private ClientBookingInfo clientInfo2;

    private UpscaleBooker upscaleBooker;

    @BeforeEach
    void setUp() {
        upscaleBooker = new UpscaleBooker(baseRoomType, upgradeRoomType, matchingUserPredicate, sortingOrder);
    }

    @Test
    void shouldUpgradeClientsWhenBaseRoomsAreNotAvailable() {
        // given
        given(calculationContext.getNumberOfRoomsOfType(baseRoomType)).willReturn(0L);
        given(calculationContext.getNumberOfRoomsOfType(upgradeRoomType)).willReturn(2L);
        given(calculationContext.getMatchingClients(matchingUserPredicate, sortingOrder))
                .willReturn(Arrays.asList(clientInfo1, clientInfo2));

        // when
        upscaleBooker.bookMatchedClients(calculationContext);

        // then
        verify(calculationContext, times(1)).bookRoom(clientInfo1, upgradeRoomType);
        verify(calculationContext, times(1)).bookRoom(clientInfo2, upgradeRoomType);
    }

    @Test
    void shouldNotUpgradeClientsWhenNoUpgradeRoomsAreAvailable() {
        // given
        given(calculationContext.getNumberOfRoomsOfType(baseRoomType)).willReturn(0L);
        given(calculationContext.getNumberOfRoomsOfType(upgradeRoomType)).willReturn(0L);
        given(calculationContext.getMatchingClients(matchingUserPredicate, sortingOrder))
                .willReturn(Arrays.asList(clientInfo1, clientInfo2));

        // when
        upscaleBooker.bookMatchedClients(calculationContext);

        // then
        verify(calculationContext, never()).bookRoom(any(), any());
    }

    @Test
    void shouldUpgradeOnlyWhenBaseTypeRoomsNotEnough() {
        // given
        given(calculationContext.getNumberOfRoomsOfType(baseRoomType)).willReturn(1L);
        given(calculationContext.getNumberOfRoomsOfType(upgradeRoomType)).willReturn(1L);
        given(calculationContext.getMatchingClients(matchingUserPredicate, sortingOrder))
                .willReturn(Arrays.asList(clientInfo1, clientInfo2));

        // when
        upscaleBooker.bookMatchedClients(calculationContext);

        // then
        verify(calculationContext, times(1)).bookRoom(clientInfo1, upgradeRoomType);
        verify(calculationContext, never()).bookRoom(eq(clientInfo2), any());
    }

    @Test
    void shouldUpgradeClientsInCorrectOrder() {
        // given
        given(calculationContext.getNumberOfRoomsOfType(baseRoomType)).willReturn(0L);
        given(calculationContext.getNumberOfRoomsOfType(upgradeRoomType)).willReturn(2L);
        given(calculationContext.getMatchingClients(matchingUserPredicate, sortingOrder))
                .willReturn(Arrays.asList(clientInfo1, clientInfo2));

        // when
        upscaleBooker.bookMatchedClients(calculationContext);

        // then
        verify(calculationContext, times(1)).bookRoom(clientInfo1, upgradeRoomType);
        verify(calculationContext, times(1)).bookRoom(clientInfo2, upgradeRoomType);
    }

    @Test
    void shouldNotBookWhenNoClientsMatch() {
        // given
        given(calculationContext.getMatchingClients(matchingUserPredicate, sortingOrder))
                .willReturn(emptyList());

        // when
        upscaleBooker.bookMatchedClients(calculationContext);

        // then
        verify(calculationContext, never()).bookRoom(any(), any());
    }
}