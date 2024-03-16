package com.gridnine.testing;

import com.gridnine.testing.models.Flight;
import com.gridnine.testing.services.FlightFilterService;

import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<Flight> flights = Flight.FlightBuilder.createFlights();

        FlightFilterService ffs = new FlightFilterService();

        List<Flight> task1 = ffs.departureAfterDateTime(flights, LocalDateTime.now());
        List<Flight> task2 = ffs.filterAllSegmentsCorrect(flights);
        List<Flight> task3 = ffs.filterTimeOnGroundLessMinutes(flights, 120);

        System.out.println("исходные полеты:");
        flights.forEach(System.out::println);
        System.out.println();

        System.out.println("#### TASK1 - время вылета после now() ####");
        task1.forEach(System.out::println);
        System.out.println("#######\n");

        System.out.println("#### TASK2 - во всех сегментах время вылета всегда меньше времени прибытия ####");
        task2.forEach(System.out::println);
        System.out.println("#######\n");

        System.out.println("#### TASK3 - суммарное время на земле меньше чем 2 часа ####");
        task3.forEach(System.out::println);
        System.out.println("#######");
    }
}
