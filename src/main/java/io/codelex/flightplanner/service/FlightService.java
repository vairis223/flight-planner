package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.repository.FlightRepository;
import io.codelex.flightplanner.request.AddFlightRequest;
import io.codelex.flightplanner.request.FlightRequest;
import io.codelex.flightplanner.response.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Validated
@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public synchronized Flight addFlight(AddFlightRequest addFlightRequest) {

        if (addFlightRequest.getFrom().getAirport().equalsIgnoreCase(addFlightRequest.getTo().getAirport().trim())
                && addFlightRequest.getFrom().getCity().equalsIgnoreCase(addFlightRequest.getTo().getCity().trim())
                && addFlightRequest.getFrom().getCountry().equalsIgnoreCase(addFlightRequest.getTo().getCountry().trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        LocalDateTime arrivalTime;
        LocalDateTime departureTime;

        try {
            arrivalTime = LocalDateTime.parse(addFlightRequest.getArrivalTime(), formatter);
            departureTime = LocalDateTime.parse(addFlightRequest.getDepartureTime(), formatter);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format.");
        }

        if (arrivalTime.isBefore(departureTime) || arrivalTime.isEqual(departureTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        boolean flightExists = flightRepository.FlightList().stream()
                .anyMatch(flight ->
                        flight.getFrom().equals(addFlightRequest.getFrom()) &&
                                flight.getTo().equals(addFlightRequest.getTo()) &&
                                flight.getCarrier().equals(addFlightRequest.getCarrier()) &&
                                flight.getDepartureTime().isEqual(departureTime) &&
                                flight.getArrivalTime().isEqual(arrivalTime));

        if (flightExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Flight already exists");
        }


        long newFlightId = IdGenerator.generateNewFlightId();
        Flight flight = new Flight(newFlightId, addFlightRequest.getFrom(), addFlightRequest.getTo(),
                addFlightRequest.getCarrier(), departureTime, arrivalTime);
        this.flightRepository.saveFlight(flight);
        return flight;
    }


    public Flight getFlightById(long id) {
        Flight flightFound = null;
        for (Flight flight : this.flightRepository.FlightList()) {
            if (flight.getId() == id) {
                flightFound = flight;
                break;
            }
        }
        if (flightFound == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return flightFound;
    }

    public List<Airport> searchForAirports(String search) {
        List<Airport> airports = new ArrayList<>();

        this.flightRepository.FlightList().stream()
                .flatMap(flight -> Stream.of(flight.getFrom(), flight.getTo()))
                .forEach(airport -> {
                    if (airport.getAirport().toLowerCase().contains(search.trim().toLowerCase())
                            || airport.getCity().toLowerCase().contains(search.trim().toLowerCase())
                            || airport.getCountry().toLowerCase().contains(search.trim().toLowerCase())) {
                        airports.add(airport);
                    }
                });

        return airports;
    }
    private boolean isAirportsEqual(String airport1, String airport2) {
        return airport1.toLowerCase().equals(airport2.toLowerCase());
    }

    private boolean isDatesEqual(String date1, String date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate1 = LocalDate.parse(date1, formatter);
       LocalDate localDate2 = LocalDate.parse(date2, formatter);
        return localDate1.isEqual(localDate2);
    }

    public synchronized SearchResponse searchForFlights(FlightRequest flightRequest) {
        if (isAirportsEqual(flightRequest.getFrom(), flightRequest.getTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        List<Flight> flights = this.flightRepository.FlightList().stream()
                .filter(flight ->
                        isAirportsEqual(flight.getFrom().getAirport(), flightRequest.getFrom()) &&
                                isAirportsEqual(flight.getTo().getAirport(), flightRequest.getTo()) &&
                                isDatesEqual(String.valueOf(flight.getDepartureTime().toLocalDate()), flightRequest.getDepartureDate()))
                .collect(Collectors.toList());

        int totalFlights = flights.size();
        int filteredFlightsCount = flights.size();

        return new SearchResponse(totalFlights, filteredFlightsCount, flights);
    }

    public void deleteFlightById(Long id) {
        flightRepository.deleteFlightById(id);
    }

    public void clearAllFlights() {
        flightRepository.deleteAllFlights();
    }
}
