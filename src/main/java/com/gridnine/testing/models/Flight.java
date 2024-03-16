package com.gridnine.testing.models;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Bean that represents a flight.
 */
public class Flight {

    private final List<Segment> segments;

    private Flight(final List<Segment> segs) {
        segments = segs;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    @Override
    public String toString() {
        return segments.stream().map(Object::toString)
                .collect(Collectors.joining("<->"));
    }

    public LocalDateTime departureDateTime() {
        return getSegments().getFirst().getDepartureDate();
    }

    public boolean isDirectFlight() {
        return getSegments().size() == 1;
    }

    public long timeOnGroundInMinutes() {
        long waitTimeInMinute = 0;

        if (isDirectFlight()) {
            return waitTimeInMinute;
        }

        List<Segment> segments = getSegments();
        for (int i = 0; i < segments.size() - 1; i++) {
            LocalDateTime arrival = segments.get(i).getArrivalDate();
            LocalDateTime departure = segments.get(i + 1).getDepartureDate();
            waitTimeInMinute += ChronoUnit.MINUTES.between(arrival, departure);
        }
        return waitTimeInMinute;
    }

    public static class FlightBuilder {
        public static Flight createFlight(final LocalDateTime... dates) {
            if ((dates.length % 2) != 0) {
                throw new IllegalArgumentException(
                        "you must pass an even number of dates");
            }
            List<Segment> segments = new ArrayList<>(dates.length / 2);
            for (int i = 0; i < (dates.length - 1); i += 2) {
                segments.add(new Segment(dates[i], dates[i + 1]));
            }
            return new Flight(segments);
        }

        public static List<Flight> createFlights() {
            LocalDateTime threeDaysFromNow = LocalDateTime.now().plusDays(3);
            return Arrays.asList(

                    //A normal flight with two hour duration
                    createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2)),

                    //A normal multi segment flight
                    createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                            threeDaysFromNow.plusHours(3), threeDaysFromNow.plusHours(5)),

                    //A flight departing in the past
                    createFlight(threeDaysFromNow.minusDays(6), threeDaysFromNow),

                    //A flight that departs before it arrives
                    createFlight(threeDaysFromNow, threeDaysFromNow.minusHours(6)),

                    //A flight with more than two hours ground time
                    createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                            threeDaysFromNow.plusHours(5), threeDaysFromNow.plusHours(6)),

                    //Another flight with more than two hours ground time
                    createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                            threeDaysFromNow.plusHours(3), threeDaysFromNow.plusHours(4),
                            threeDaysFromNow.plusHours(6), threeDaysFromNow.plusHours(7)));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flight flight)) return false;
        return getSegments() != null ? getSegments().equals(flight.getSegments()) : flight.getSegments() == null;
    }

    @Override
    public int hashCode() {
        return getSegments() != null ? getSegments().hashCode() : 0;
    }
}
