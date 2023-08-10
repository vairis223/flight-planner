package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    boolean existsByFromAndToAndCarrierAndDepartureTimeAndArrivalTime(Airport fromAirport,
                                                                      Airport toAirport,
                                                                      String carrier,
                                                                      LocalDateTime departureTime,
                                                                      LocalDateTime arrivalTime);

    List<Flight> findByFromAndToAndDepartureTimeBetween(Airport fromAirport,
                                                        Airport toAirport,
                                                        LocalDateTime localDateTime,
                                                        LocalDateTime localDateTime1);
}
