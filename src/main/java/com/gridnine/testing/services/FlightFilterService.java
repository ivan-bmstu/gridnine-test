package com.gridnine.testing.services;

import com.gridnine.testing.models.Flight;
import com.gridnine.testing.models.Segment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class FlightFilterService {

    public List<Flight> departureBeforeDateTime(List<Flight> flights, LocalDateTime dateTime) {
        return flights.stream()
                .filter(flight ->
                        flight.departureDateTime().isBefore(dateTime) ||
                        flight.departureDateTime().isEqual(dateTime))
                .toList();
    }

    public List<Flight> departureAfterDateTime(List<Flight> flights, LocalDateTime dateTime) {
        return flights.stream()
                .filter(flight ->
                        flight.departureDateTime().isAfter(dateTime) ||
                        flight.departureDateTime().isEqual(dateTime))
                .toList();
    }

    public List<Flight> arrivalBeforeDateTime(List<Flight> flights, LocalDateTime dateTime) {
        return flights.stream()
                .filter(flight ->
                        flight.arrivalDateTime().isBefore(dateTime) ||
                        flight.arrivalDateTime().isEqual(dateTime))
                .toList();
    }

    public List<Flight> arrivalAfterDateTime(List<Flight> flights, LocalDateTime dateTime) {
        return flights.stream()
                .filter(flight ->
                        flight.arrivalDateTime().isAfter(dateTime) ||
                        flight.arrivalDateTime().isEqual(dateTime))
                .toList();
    }

    public List<Flight> filterAllSegmentsCorrect(List<Flight> flights) {
        return flights.stream()
                .filter(flight -> {
                    Optional<Segment> incorrectSegment = flight.getSegments().stream()
                            .filter(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate()))
                            .findFirst();
                    return incorrectSegment.isEmpty();})
                .toList();
    }

    public List<Flight> filterTimeOnGroundLessMinutes(List<Flight> flights, int timeInMinutes) {
        return flights.stream()
                .filter(flight -> flight.timeOnGroundInMinutes() <= timeInMinutes)
                .toList();
    }

    public List<Flight> filterByNumberOfTransfers(List<Flight> flights, int numOfTransfers) {
        return flights.stream()
                .filter(flight -> (flight.getSegments().size() - 1) <= numOfTransfers)
                .toList();
    }
}
