package com.gridnine.testing.test.unit;

import com.gridnine.testing.controllers.FlightController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FlightControllerTest {

    private static FlightController flightController;

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private final static String correctUrl1 = "www.gridnine.com/testing/flights";
    private final static String correctUrl2 = "www.gridnine.com/testing/flights?depBef=" + LocalDateTime.now().minusDays(2).format(dtf)
            +"&depAft=" + LocalDateTime.now().minusDays(3).format(dtf)
            +"&arrBef=" + LocalDateTime.now().plusDays(4).format(dtf)
            +"&arrAft=" + LocalDateTime.now().plusDays(2).format(dtf)
            +"&transNum=1&transDur=30";
    private final static String incorrectUrl1 = "www.gridnine.com/testing/flights?";
    private final static String incorrectUrl2 = "www.gridnine.com/testing/flights?xxx";
    private final static String incorrectUrl3 = "www.gridnine.com/testing/flights?transNum";
    private final static String incorrectUrl4 = "www.gridnine.com/testing/flights?transNum=";
    private final static String incorrectUrl5 = "www.gridnine.com/testing/flights?transNum=1&TransNum=1";
    private final static String incorrectUrl6 = "www.gridnine.com/testing/flights?transNum=1&TransDur=1";
    private final static String notFoundMsg = "Status 404: resource not found. Check URI and available http params";




    @BeforeAll
    public static void testInit() {
        flightController = new FlightController();
    }

    @ParameterizedTest
    @MethodSource("correctUrl")
    public void isRequestValidWithCorrectUrlMustReturnTrue(String url) {
        assertTrue(flightController.isRequestValid(url));
    }

    @ParameterizedTest
    @MethodSource("incorrectUrl")
    public void isRequestValidWithIncorrectUrlMustReturnFalse(String url) {
        assertFalse(flightController.isRequestValid(url));
    }

    @ParameterizedTest
    @MethodSource("incorrectUrl")
    public void getResponseWithIncorrectUrlMustReturn404Status(String url) {
        assertEquals(notFoundMsg, flightController.getResponse(url));
    }

    @Test
    public void getResponseWithCorrectUrlMustReturnFlight() {
        assertEquals(List.of(flightController.getFlightsFromDB().get(2)).toString(), flightController.getResponse(correctUrl2));
    }

    private static Stream<Arguments> correctUrl() {
        return Stream.of(
                Arguments.of(correctUrl1),
                Arguments.of(correctUrl2)
        );
    }

    private static Stream<Arguments> incorrectUrl() {
        return Stream.of(
                Arguments.of(incorrectUrl1),
                Arguments.of(incorrectUrl2),
                Arguments.of(incorrectUrl3),
                Arguments.of(incorrectUrl4),
                Arguments.of(incorrectUrl5),
                Arguments.of(incorrectUrl6)
        );
    }

}
