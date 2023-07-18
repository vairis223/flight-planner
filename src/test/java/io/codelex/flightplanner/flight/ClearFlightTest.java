package io.codelex.flightplanner.flight;

import io.codelex.flightplanner.controller.AdminController;
import io.codelex.flightplanner.controller.TestingController;
import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.repository.FlightRepository;
import io.codelex.flightplanner.request.AddFlightRequest;
import io.codelex.flightplanner.service.FlightService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@SpringBootTest
public class ClearFlightTest {
    @Autowired
    AdminController adminController;

    @Autowired
    TestingController testingController;

    @Autowired
    FlightRepository flightRepository;
    @Autowired
    FlightService flightService;

    @Test
    void clearAllFlights() {

        Airport fromAirport = new Airport("Latvia", "Riga", "RIX");
        Airport toAirport = new Airport("Sweden", "Stockholm", "ARN");
        LocalDateTime departureTime = LocalDateTime.of(2023, 7, 18, 10, 0);
        LocalDateTime arrivalTime = LocalDateTime.of(2023, 7, 18, 11, 30);
        String carrier = "Ryanair";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        AddFlightRequest request = new AddFlightRequest(
                fromAirport,
                toAirport,
                carrier,
                formatter.format(departureTime),
                formatter.format(arrivalTime)
        );
        adminController.addFlight(request);
        testingController.clearFlights();

    }
}

