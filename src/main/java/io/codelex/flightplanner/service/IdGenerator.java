package io.codelex.flightplanner.service;

public class IdGenerator {
    private static long currentId = 1;

    public static synchronized long generateNewFlightId() {
        return currentId++;
    }
}
