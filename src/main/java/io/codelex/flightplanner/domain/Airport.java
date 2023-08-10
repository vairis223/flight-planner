package io.codelex.flightplanner.domain;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


import java.util.Objects;

@Entity
@Table(name = "airports")
public class Airport {

    @NotNull
    @NotEmpty
    private String country;
    @NotNull
    @NotEmpty
    private String city;

    @Id
    @Column(name = "airport_id")
    @NotNull
    @NotEmpty
    private String airport;

    public Airport() {
    }

    public Airport(@NotNull String country, @NotNull String city, @NotNull String airport) {
        this.country = country;
        this.city = city;
        this.airport = airport;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(@NotNull String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(@NotNull String city) {
        this.city = city;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(@NotNull String airport) {
        this.airport = airport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport1 = (Airport) o;
        return Objects.equals(country, airport1.country) && Objects.equals(city, airport1.city) && Objects.equals(airport, airport1.airport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, airport);
    }

    @Override
    public String toString() {
        return "Airport{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", airport='" + airport + '\'' +
                '}';
    }
}