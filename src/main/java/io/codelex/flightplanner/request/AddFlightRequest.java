package io.codelex.flightplanner.request;


import io.codelex.flightplanner.domain.Airport;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class AddFlightRequest {

    @Valid
    @NotNull
    private Airport from;
    @Valid
    @NotNull
    private Airport to;
    @NotBlank(message = "Carrier must not be blank")
    private String carrier;
    @NotBlank(message = "Departure time must not be blank")
    private String departureTime;
    @NotBlank(message = "Arrival time must not be blank")
    private String arrivalTime;


    public AddFlightRequest(@NotNull Airport from, @NotNull Airport to, String carrier, String departureTime, String arrivalTime) {
        this.from = from;
        this.to = to;
        this.carrier = carrier;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public @NotNull Airport getFrom() {
        return from;
    }

    public void setFrom(@NotNull Airport from) {
        this.from = from;
    }

    public @NotNull Airport getTo() {
        return to;
    }

    public void setTo(@NotNull Airport to) {
        this.to = to;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}

