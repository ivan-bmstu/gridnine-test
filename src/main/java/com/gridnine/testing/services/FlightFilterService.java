package com.gridnine.testing.services;

import com.gridnine.testing.models.Flight;
import com.gridnine.testing.models.Segment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class FlightFilterService {

    /*
    * Иначально данные методы я расположил в данном классе, но мне кажется, что это больше относится
    * к сущности Flight, чем к бизнесовому сценарию. Но на данный момент я не совсем уверен, зависит от архитектуры программы
    * Если FLight будет оформлена как Entity, я оставил бы эти методы в классе сервиса*/
//    private LocalDateTime arrivalDateTime(Flight flight) {
//        return flight.getSegments().getLast().getArrivalDate();
//    }
//
//    private LocalDateTime departureDateTime(Flight flight) {
//        return flight.getSegments().getFirst().getDepartureDate();
//    }
//
//    private long flightDurationInMinutes(Flight flight) {
//        return ChronoUnit.MINUTES.between(departureDateTime(flight), arrivalDateTime(flight));
//    }
//
//    private long flightDurationInHours(Flight flight) {
//        return ChronoUnit.HOURS.between(departureDateTime(flight), arrivalDateTime(flight));
//    }
//
//    private boolean isDirectFlight(Flight flight) {
//        return flight.getSegments().size() == 1;
//    }
//
//    private long timeOnGroundInMinutes(Flight flight) {
//        long waitTimeInMinute = 0;
//
//        if (flight.isDirectFlight()) {
//            return waitTimeInMinute;
//        }
//
//        List<Segment> segments = flight.getSegments();
//        for (int i = 0; i < segments.size() - 1; i++) {
//            LocalDateTime arrival = segments.get(i).getArrivalDate();
//            LocalDateTime departure = segments.get(i + 1).getDepartureDate();
//            waitTimeInMinute += ChronoUnit.MINUTES.between(departure, arrival);
//        }
//        return waitTimeInMinute;
//    }

    public List<Flight> departureAfterDateTime(List<Flight> flights, LocalDateTime dateTime) {
        return flights.stream()
                .filter(flight -> flight.departureDateTime().isAfter(dateTime))
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
}
