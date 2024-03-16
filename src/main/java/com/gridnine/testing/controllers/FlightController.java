package com.gridnine.testing.controllers;

import com.gridnine.testing.models.Flight;
import com.gridnine.testing.services.FlightFilterService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class FlightController {
    private final Map<String, BiFunction<List<Flight>, String, List<Flight>>> controllerFilterMethods;
    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private final FlightFilterService flightFilterService;
    private final List<Flight> flightsFromDB;
    public FlightController() {
        controllerFilterMethods = new HashMap<>();
        flightFilterService = new FlightFilterService();
        flightsFromDB = flightFilterService.filterAllSegmentsCorrect(Flight.FlightBuilder.createFlights());
        controllerFilterMethods.put("depBef", this::depBef);
        controllerFilterMethods.put("depAft", this::depAft);
        controllerFilterMethods.put("arrBef", this::arrBef);
        controllerFilterMethods.put("arrAft", this::arrAft);
        controllerFilterMethods.put("transNum", this::transNum);
        controllerFilterMethods.put("transDur", this::transDur);
    }

    public boolean isRequestValid(String request) {
        if (request.equals("www.gridnine.com/testing/flights")) {
            return true;
        }
        String[] pathAndParam = request.split("\\?");
        if (pathAndParam.length != 2) {
            return false;
        }
        if (!pathAndParam[0].equals("www.gridnine.com/testing/flights")) {
            return false;
        }

        String[] params = pathAndParam[1].split("&");
        for (String par : params) {
            if (!par.contains("=") || par.indexOf("=") == (par.length() - 1)) {
                return false;
            }
        }

        Set<String> uniqueParameters = new HashSet<>();
        Arrays.asList(params).forEach(p -> uniqueParameters.add(p.substring(0, p.indexOf("="))));
        if (uniqueParameters.size() != params.length) {
            return false;
        }

        for (String par : uniqueParameters) {
            if (!controllerFilterMethods.containsKey(par)){
                return false;
            }
        }

        return true;
    }

    public String getResponse (String request) {
        if (!isRequestValid(request)) {
            return "Status 404: resource not found. Check URI and available http params";
        }

        List<Flight> flights = flightsFromDB;

        Map<String, String> keyValHttpParameter;
        keyValHttpParameter = Arrays.stream(request.split("\\?"))
                .skip(1)
                .flatMap(s -> Arrays.stream(s.split("&")))
                .collect(Collectors.toMap(
                        kv -> {
                            var arr = kv.split("=");
                            return arr[0];
                        },
                        kv -> {
                            var arr = kv.split("=");
                            return arr[1];
                        }
                ));
        for (Map.Entry<String, String> set : keyValHttpParameter.entrySet()) {
            try{
                flights = controllerFilterMethods.get(set.getKey()).apply(flights, set.getValue());
            } catch (RuntimeException e) {
                return "Введены некорректные значения параметров фильтра";
            }
        }
        return flights.toString();
    }

    private List<Flight> depBef(List<Flight> flights, String dt) {
        LocalDateTime ldt = LocalDateTime.parse(dt, dtf);
        return flightFilterService.departureBeforeDateTime(flights, ldt);
    }

    private List<Flight> depAft(List<Flight> flights, String dt) {
        LocalDateTime ldt = LocalDateTime.parse(dt, dtf);
        return flightFilterService.departureAfterDateTime(flights, ldt);
    }

    private List<Flight> arrBef(List<Flight> flights, String dt) {
        LocalDateTime ldt = LocalDateTime.parse(dt, dtf);
        return flightFilterService.arrivalBeforeDateTime(flights, ldt);
    }

    private List<Flight> arrAft(List<Flight> flights, String dt) {
        LocalDateTime ldt = LocalDateTime.parse(dt, dtf);
        return flightFilterService.arrivalAfterDateTime(flights, ldt);
    }

    private List<Flight> transNum(List<Flight> flights, String num) {
        int numbers = Integer.parseInt(num);
        return flightFilterService.filterByNumberOfTransfers(flights, numbers);
    }

    private List<Flight> transDur(List<Flight> flights, String dur) {
        int duration = Integer.parseInt(dur);
        return flightFilterService.filterTimeOnGroundLessMinutes(flights, duration);
    }

    public List<Flight> getFlightsFromDB() {
        return flightsFromDB;
    }
}
