package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.domain.Flight;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FlightRepository {
    private final List<Flight> saveFlights = new ArrayList<>();

    public void saveFlight(Flight flight) {
        this.saveFlights.add(flight);
    }

    public List<Flight> FlightList() {
        return this.saveFlights;
    }
    public synchronized void deleteFlightById(Long id) {
        saveFlights.removeIf(flight -> flight.getId().equals(id));
            }

    public void deleteAllFlights() {
        saveFlights.clear();
    }

}
