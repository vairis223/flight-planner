package io.codelex.flightplanner.flight;

import io.codelex.flightplanner.controller.ApiController;
import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.request.FlightRequest;
import io.codelex.flightplanner.response.SearchResponse;
import io.codelex.flightplanner.service.FlightServiceInMemory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
    public class SearchFlightTest {

    @Mock
    FlightServiceInMemory flightServiceInMemory;

    @InjectMocks
    ApiController apiController;

    @Test
    void searchForFlight() {
        // Given

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime departureTime = LocalDateTime.of(2023, 7, 18, 10, 0);
        LocalDateTime arrivalTime = LocalDateTime.of(2023, 7, 18, 11, 30);

        FlightRequest flightRequest = new FlightRequest();
        flightRequest.setFrom("RIX");
        flightRequest.setTo("ARN");
        flightRequest.setDepartureDate(departureTime.format(formatter));

        List<Flight> expectedFlights = new ArrayList<>();
        Airport airportFrom = new Airport("Latvia", "Riga", "RIX");
        Airport airportTo = new Airport("Sweden", "Stockholm", "ARN");

        Flight flight1 = new Flight(1L, airportFrom, airportTo, "Ryanair", departureTime, arrivalTime);
        expectedFlights.add(flight1);

        SearchResponse mockResponse = new SearchResponse(expectedFlights.size(), expectedFlights.size(), expectedFlights);
        Mockito.when(flightServiceInMemory.searchForFlights(Mockito.any(FlightRequest.class))).thenReturn(mockResponse);

        // When
        SearchResponse actualResponse = apiController.searchFlights(flightRequest);

        // Then
        verify(flightServiceInMemory).searchForFlights(argThat(request ->
                request.getFrom().equals("RIX") &&
                        request.getTo().equals("ARN") &&
                        request.getDepartureDate().equals(departureTime.format(formatter)) &&
                        createSearchText(request).equals("RIX to ARN")
        ));
    }

    private String createSearchText(FlightRequest request) {
        return request.getFrom() + " to " + request.getTo();
    }
}




