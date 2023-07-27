package io.codelex.flightplanner.controller;

import io.codelex.flightplanner.service.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testing-api")
public class TestingController {
    public FlightService flightService;

    public TestingController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/clear")
    public ResponseEntity<String> clearFlights() {
        flightService.clearAllFlights();
        return ResponseEntity.ok("All flights deleted successfully!");
    }
}