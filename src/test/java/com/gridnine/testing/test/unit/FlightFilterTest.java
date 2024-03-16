package com.gridnine.testing.test.unit;

import com.gridnine.testing.models.Flight;
import com.gridnine.testing.services.FlightFilterService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FlightFilterTest {

    private static List<Flight> testFlights;
    private static FlightFilterService ffs;
    private static Flight flDepartureInPast;
    private static Flight flArrivalBeforeDeparture;
    private static Flight fl1WithMoreThenTwoHoursOnGround;
    private static Flight fl2WithMoreThenTwoHoursOnGround;

    @BeforeAll
    public static void flightsInit() {
        testFlights = Flight.FlightBuilder.createFlights();
        flDepartureInPast = testFlights.get(2);
        flArrivalBeforeDeparture = testFlights.get(3);
        fl1WithMoreThenTwoHoursOnGround = testFlights.get(4);
        fl2WithMoreThenTwoHoursOnGround = testFlights.get(5);
        ffs = new FlightFilterService();
    }

    @Test
    public void departureAfterDateTimeNowMustReturnFlightsWithDepartureAfterNow() {
        List<Flight> flightsDepartureAfterNow =
                ffs.departureAfterDateTime(testFlights, LocalDateTime.now());

        assertAll(
                () -> assertEquals(5, flightsDepartureAfterNow.size()),
                () -> assertFalse(flightsDepartureAfterNow.contains(flDepartureInPast))
        );
    }

    @Test
    public void filterAllSegmentsCorrectMustReturnFlightsWithCorrectsSegments() {
        List<Flight> flightsWithCorrectsSegments =
                ffs.filterAllSegmentsCorrect(testFlights);

        assertAll(
                () -> assertEquals(5, flightsWithCorrectsSegments.size()),
                () -> assertFalse(flightsWithCorrectsSegments.contains(flArrivalBeforeDeparture))
        );
    }

    @Test
    public void filterTimeOnGroundLessMinutes120MustReturnFlightsWithShortTransfer() {
        List<Flight> flightsShortTransfer =
                ffs.filterTimeOnGroundLessMinutes(testFlights, 120);

        assertAll(
                () -> assertEquals(4, flightsShortTransfer.size()),
                () -> assertFalse(flightsShortTransfer.contains(fl1WithMoreThenTwoHoursOnGround)),
                () -> assertFalse(flightsShortTransfer.contains(fl2WithMoreThenTwoHoursOnGround))
        );
    }

}
