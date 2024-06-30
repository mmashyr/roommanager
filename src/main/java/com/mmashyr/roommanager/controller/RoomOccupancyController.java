package com.mmashyr.roommanager.controller;

import com.mmashyr.roommanager.dto.RoomOccupancyResponse;
import com.mmashyr.roommanager.model.RoomOccupancyInfo;
import com.mmashyr.roommanager.model.RoomsAvailability;
import com.mmashyr.roommanager.service.calculator.OccupancyCalculatorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("hotel")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RoomOccupancyController {

    private final OccupancyCalculatorService calculatorService;

    @PostMapping("occupancy")
    public ResponseEntity<RoomOccupancyResponse> calculateRoomOccupancy(@RequestBody Collection<RoomsAvailability> availabilityRequest) {
        Collection<RoomOccupancyInfo> occupancyInfos = calculatorService.calculateOccupancy(availabilityRequest);
        return ResponseEntity.ok(new RoomOccupancyResponse(occupancyInfos));
    }
}
