package io.codelex.flightplanner.controller;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.request.FlightRequest;
import io.codelex.flightplanner.response.SearchResponse;
import io.codelex.flightplanner.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    public FlightService flightService;

    public ApiController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/airports")
    public List<Airport> searchAirport(@RequestParam("search") String search) {
        return flightService.searchForAirports(search);
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<Flight> searchFlightById(@PathVariable("id") Long id) {
        Flight flight = flightService.getFlightById(id);
        return ResponseEntity.ok(flight);
    }

    @PostMapping("/flights/search")
    public SearchResponse searchFlights(@Valid @RequestBody FlightRequest searchFlight) {
        return flightService.searchForFlights(searchFlight);
    }

}