package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.request.AddFlightRequest;
import io.codelex.flightplanner.request.FlightRequest;
import io.codelex.flightplanner.response.SearchResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FlightService {
    Flight addFlight(AddFlightRequest addFlightRequest);

    Flight getFlightById(long id);

    List<Airport> searchForAirports(String search);

    SearchResponse searchForFlights(FlightRequest flightRequest);

    void deleteFlightById(Long id);

    void clearAllFlights();
}