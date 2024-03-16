package com.gridnine.testing;

import com.gridnine.testing.controllers.FlightController;
import com.gridnine.testing.models.Flight;
import com.gridnine.testing.services.FlightFilterService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        printInfoFromTask();
        help();

        var flightController = new FlightController();
        try (var br = new BufferedReader(new InputStreamReader(System.in))) {
             String line;
             while (!(line = br.readLine()).equals("\\q")) {
                 System.out.println(flightController.getResponse(line));
             }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printInfoFromTask() {
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
    private static void help() {
        System.out.println("\n\n\nВведите запрос на поиск перелетов с/без учетом фильтров");
        System.out.println("Введите www.gridnine.com/testing/flights - будут напечатаны все записанные перелеты.");
        System.out.println("Для применения филтров используйте параметры http запроса, где доступные параметры:");
        System.out.println("depBef, depAft - отправление до, после");
        System.out.println("arrBef, arrAft - прибытие до, после");
        System.out.println("transNum - максимальное количество пересадок");
        System.out.println("transDur - максимальная длительность времени на земле в минутах");
        System.out.println("формат времени в запросе: yyyy-MM-dd'T'HH:mm");
        System.out.println("Пример: www.gridnine.com/testing/flights?depBef=2024-03-16T21:00&transNum=2");
        System.out.println("Для выхода из программы введите \\q");
    }
}
