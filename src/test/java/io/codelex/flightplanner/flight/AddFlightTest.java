package io.codelex.flightplanner.flight;

import io.codelex.flightplanner.controller.AdminController;
import io.codelex.flightplanner.controller.TestingController;
import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.request.AddFlightRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AddFlightTest {

 @Autowired
 AdminController adminController;
 @Autowired
 TestingController testingController;


 @Test
 void addFlight() {

  Airport fromAirport = new Airport("Latvia", "Riga", "RIX");
  Airport toAirport = new Airport("Sweden", "Stockholm", "ARN");
  LocalDateTime departureTime = LocalDateTime.of(2023, 7, 18, 10, 0);
  LocalDateTime arrivalTime = LocalDateTime.of(2023, 7, 18, 11, 30);
  String carrier = "Ryanair";
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  AddFlightRequest request = new AddFlightRequest(fromAirport, toAirport, carrier, formatter.format(departureTime), formatter.format(arrivalTime));

  Flight savedFlight = adminController.addFlight(request).getBody();


  assertNotNull(savedFlight.getId());
  assertEquals(fromAirport, savedFlight.getFrom());
  assertEquals(toAirport, savedFlight.getTo());
  assertEquals(carrier, savedFlight.getCarrier());
  assertEquals(departureTime, savedFlight.getDepartureTime());
  assertEquals(arrivalTime, savedFlight.getArrivalTime());


 }


 @Test
 void addFlight_WhenDateFormatsAreInvalid() {

  Airport fromAirport = new Airport("Ireland", "Dublin", "DUB");
  Airport toAirport = new Airport("Sweden", "Stockholm", "ARN");
  String carrier = "Ryanair";
  AddFlightRequest request = new AddFlightRequest(fromAirport, toAirport, carrier, "2023-07-18", "11:30");
  assertThrows(ResponseStatusException.class, () -> {
   AdminController.addFlight(request);
  });
 }

 @Test
 void addFlight_WhenArrivalTimeIsBeforeDepartureTime() {

  Airport fromAirport = new Airport("Latvia", "Riga", "RIX");
  Airport toAirport = new Airport("Sweden", "Stockholm", "ARN");
  LocalDateTime departureTime = LocalDateTime.of(2023, 7, 18, 10, 0);
  LocalDateTime arrivalTime = LocalDateTime.of(2023, 7, 18, 9, 30);
  String carrier = "Ryanair";
  AddFlightRequest request = new AddFlightRequest(fromAirport, toAirport, carrier, departureTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), arrivalTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
  );
  assertThrows(ResponseStatusException.class, () -> {
   AdminController.addFlight(request);
  });
 }
}

