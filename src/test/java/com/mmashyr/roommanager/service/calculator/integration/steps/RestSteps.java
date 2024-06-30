package com.mmashyr.roommanager.service.calculator.integration.steps;

import com.mmashyr.roommanager.dto.RoomOccupancyResponse;
import com.mmashyr.roommanager.model.RoomsAvailability;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RestSteps {

    private static final String ROOT_HOTEL_URL = "/hotel";
    private static final String LOCALHOST = "localhost";

    private TestRestTemplate restTemplate;

    private final Environment environment;

    public ResponseEntity<RoomOccupancyResponse> sendOccupancyRequest(Collection<RoomsAvailability> request) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(LOCALHOST)
                .port(environment.getProperty("local.server.port"))
                .path(ROOT_HOTEL_URL + "/occupancy").build();
        return restTemplate.exchange(uriComponents.toUriString(), HttpMethod.POST, new HttpEntity<>(request), RoomOccupancyResponse.class);
    }
}