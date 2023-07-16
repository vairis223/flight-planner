package io.codelex.flightplanner.controller;

import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.request.AddFlightRequest;
import io.codelex.flightplanner.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin-api")
public class AdminController {
    public FlightService flightService;

    public AdminController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PutMapping("/flights")
    public ResponseEntity<Flight> addFlight(@Valid @RequestBody AddFlightRequest addFlightRequest) {
        try {
            Flight flight = flightService.addFlight(addFlightRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(flight);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/flights/{id}")
    public ResponseEntity<String> deleteFlightById(@PathVariable("id") Long id) {
        flightService.deleteFlightById(id);
        String message = "Flight with ID " + id + " Deleted successfully!";
        return ResponseEntity.ok(message);
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable("id") Long id) {
        Flight flight = flightService.getFlightById(id);
        return ResponseEntity.ok(flight);
    }
}
