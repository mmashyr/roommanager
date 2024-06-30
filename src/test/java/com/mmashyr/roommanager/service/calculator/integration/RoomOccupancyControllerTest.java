package com.mmashyr.roommanager.service.calculator.integration;

import com.mmashyr.roommanager.RoommanagerApplication;
import com.mmashyr.roommanager.dto.RoomOccupancyResponse;
import com.mmashyr.roommanager.model.RoomOccupancyInfo;
import com.mmashyr.roommanager.model.RoomType;
import com.mmashyr.roommanager.model.RoomsAvailability;
import com.mmashyr.roommanager.service.calculator.integration.steps.RestSteps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = RoommanagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomOccupancyControllerTest {

    @Autowired
    private RestSteps restSteps;

    @Test
    public void calculatesOccupancyWithEnoughRooms() {
        // given
        RoomsAvailability economy = new RoomsAvailability(RoomType.ECONOMY, 3);
        RoomsAvailability premium = new RoomsAvailability(RoomType.PREMIUM, 3);

        // when
        ResponseEntity<RoomOccupancyResponse> response = restSteps.sendOccupancyRequest(List.of(economy, premium));

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        List<RoomOccupancyInfo> expected = List.of(
                new RoomOccupancyInfo(RoomType.PREMIUM, 3, 738),
                new RoomOccupancyInfo(RoomType.ECONOMY, 3, 167.99));
        assertThat(response.getBody().infos()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void calculatesOccupancyWithUpgradedClient() {
        // given
        RoomsAvailability economy = new RoomsAvailability(RoomType.ECONOMY, 5L);
        RoomsAvailability premium = new RoomsAvailability(RoomType.PREMIUM, 7L);

        // when
        ResponseEntity<RoomOccupancyResponse> response = restSteps.sendOccupancyRequest(List.of(economy, premium));

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        List<RoomOccupancyInfo> expected = Arrays.asList(
                new RoomOccupancyInfo(RoomType.PREMIUM, 6, 1054.0),
                new RoomOccupancyInfo(RoomType.ECONOMY, 4, 189.99)
        );
        assertThat(response.getBody().infos()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void calculatesOccupancyWithExtraRooms() {
        // given
        RoomsAvailability economy = new RoomsAvailability(RoomType.ECONOMY, 7L);
        RoomsAvailability premium = new RoomsAvailability(RoomType.PREMIUM, 2L);

        // when
        ResponseEntity<RoomOccupancyResponse> response = restSteps.sendOccupancyRequest(List.of(economy, premium));

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        List<RoomOccupancyInfo> expected = Arrays.asList(
                new RoomOccupancyInfo(RoomType.PREMIUM, 2, 583.0),
                new RoomOccupancyInfo(RoomType.ECONOMY, 4, 189.99)
        );
        assertThat(response.getBody().infos()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void calculatesOccupancyWithMultipleUpgradedClients() {
        // given
        RoomsAvailability economy = new RoomsAvailability(RoomType.ECONOMY, 1L);
        RoomsAvailability premium = new RoomsAvailability(RoomType.PREMIUM, 7L);

        // when
        ResponseEntity<RoomOccupancyResponse> response = restSteps.sendOccupancyRequest(List.of(economy, premium));

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        List<RoomOccupancyInfo> expected = Arrays.asList(
                new RoomOccupancyInfo(RoomType.PREMIUM, 7, 1153.99),
                new RoomOccupancyInfo(RoomType.ECONOMY, 1, 45.00)
        );
        assertThat(response.getBody().infos()).containsExactlyInAnyOrderElementsOf(expected);
    }
}