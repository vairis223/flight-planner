package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.repository.AirportRepository;
import io.codelex.flightplanner.repository.FlightRepository;
import io.codelex.flightplanner.request.AddFlightRequest;
import io.codelex.flightplanner.request.FlightRequest;
import io.codelex.flightplanner.response.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Primary
@Transactional
public class FlightDatabaseService implements FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    public FlightDatabaseService(FlightRepository flightRepository, AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
    }

    @Override
    public synchronized Flight addFlight(AddFlightRequest addFlightRequest) {
        Airport fromAirport = getOrCreateAirport(addFlightRequest.getFrom());
        Airport toAirport = getOrCreateAirport(addFlightRequest.getTo());


        if (fromAirport.equals(toAirport)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Departure and arrival airports cannot be the same.");
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arrival time must be after departure time.");
        }

        boolean flightExists = this.flightRepository.existsByFromAndToAndCarrierAndDepartureTimeAndArrivalTime(fromAirport, toAirport, addFlightRequest.getCarrier(), departureTime, arrivalTime);

        if (flightExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Flight already exists.");
        }

        Flight flight = new Flight();
        flight.setFrom(fromAirport);
        flight.setTo(toAirport);
        flight.setCarrier(addFlightRequest.getCarrier());
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);

        return flightRepository.save(flight);
    }

    private Airport getOrCreateAirport(Airport airport) {
        return airportRepository.findByCountryAndCityAndAirport(airport.getCountry(), airport.getCity(), airport.getAirport()).orElseGet(() -> airportRepository.save(airport));
    }


    @Override
    public Flight getFlightById(long id) {
        return this.flightRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @Override
    public List<Airport> searchForAirports(String search) {
        List<Airport> airports = new ArrayList<>();

        List<Flight> flights = flightRepository.findAll();

        flights.stream().flatMap(flight -> Stream.of(flight.getFrom(), flight.getTo())).forEach(airport -> {
            if (airport.getAirport().toLowerCase().contains(search.trim().toLowerCase()) || airport.getCity().toLowerCase().contains(search.trim().toLowerCase()) || airport.getCountry().toLowerCase().contains(search.trim().toLowerCase())) {
                airports.add(airport);
            }
        });

        return airports;
    }

    private static boolean isAirportsEqual(String departureAirport, String arrivalAirport) {
        return departureAirport.equalsIgnoreCase(arrivalAirport);
    }

    @Override
    public synchronized SearchResponse searchForFlights(FlightRequest flightRequest) {
        Airport fromAirport = (Airport) airportRepository.findByAirportIgnoreCase(flightRequest.getFrom()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Airport toAirport = (Airport) airportRepository.findByAirportIgnoreCase(flightRequest.getTo()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (isAirportsEqual(fromAirport.getAirport(), toAirport.getAirport())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        LocalDate departureDate = LocalDate.parse(flightRequest.getDepartureDate());

        List<Flight> flights = flightRepository.findByFromAndToAndDepartureTimeBetween(fromAirport, toAirport, departureDate.atStartOfDay(), departureDate.plusDays(1).atStartOfDay());

        int totalFlights = flights.size();
        int filteredFlightsCount = flights.size();

        return new SearchResponse(totalFlights, filteredFlightsCount, flights);
    }

    @Override
    public void deleteFlightById(Long id) {
        this.flightRepository.deleteById(id);
    }

    @Override
    public void clearAllFlights() {
        this.flightRepository.deleteAll();
    }
}