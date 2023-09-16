package com.zelazobeton.ticketbooking.screening.application.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.zelazobeton.ticketbooking.reservation.application.port.out.SeatRepository;
import com.zelazobeton.ticketbooking.screening.application.ScreeningService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zelazobeton.ticketbooking.recommendation.SeatsRecommendator;
import com.zelazobeton.ticketbooking.screening.infrastructure.ScreeningRepository;
import com.zelazobeton.ticketbooking.screening.model.Seat;
import com.zelazobeton.ticketbooking.screening.model.vo.AvailableSeatsDto;
import com.zelazobeton.ticketbooking.screening.model.vo.ScreeningDto;

@Service
class ScreeningServiceImpl implements ScreeningService {
    private final ScreeningRepository screeningRepository;
    private final SeatsRecommendator seatsRecommendator;
    private final SeatRepository seatRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ScreeningServiceImpl(ScreeningRepository screeningRepository, SeatsRecommendator seatsRecommendator, SeatRepository seatRepository) {
        this.screeningRepository = screeningRepository;
        this.seatsRecommendator = seatsRecommendator;
        this.seatRepository = seatRepository;
    }

    @Override
    public List<ScreeningDto> getScreenings(String date, String time) {
        LocalDateTime dateTime = LocalDateTime.parse(date + " " + time, this.formatter);
        LocalDateTime upperBoundary = dateTime.plusHours(4);
        LocalDateTime lowerBoundary = dateTime.minusHours(1);
        return this.screeningRepository.getScreeningsInGivenTimeInterval(lowerBoundary, upperBoundary,
                Sort.by("movie.title", "time"));
    }

    @Override
    public AvailableSeatsDto getAvailableSeats(String title, String date, String time, int numberOfSeats) {
        LocalDateTime dateTime = LocalDateTime.parse(date + " " + time, this.formatter);
        Set<Seat> seats = this.seatRepository.getSeatsByTitleAndScreeningTime(title, dateTime);
        Map<Integer, List<Seat>> rowsToSeats = seats
                .stream()
                .collect(Collectors.groupingBy(Seat::getRowNumber));
        rowsToSeats.forEach((key, value) -> Collections.sort(value));
        HashSet<Seat> recommendedSeats = this.seatsRecommendator.getRecommendedSeats(rowsToSeats, numberOfSeats);
        return this.createAvailableSeatsData(rowsToSeats, recommendedSeats);
    }

    private AvailableSeatsDto createAvailableSeatsData(Map<Integer, List<Seat>> rowsToSeats,
                                                       HashSet<Seat> recommendedSeats) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode roomWithSeats = mapper.createObjectNode();
        roomWithSeats.put("Screen", "Screen is located on this side of the room.");
        ObjectNode graphicSeatsRepresentation = mapper.createObjectNode();
        ObjectNode listsOfAvailableSeatsPerRow = mapper.createObjectNode();
        for (Integer rowNumber = 1; rowNumber <= rowsToSeats.size(); rowNumber++) {
            String rowName = "row " + rowNumber;
            ArrayNode availableSeatsInRow = mapper.createArrayNode();
            listsOfAvailableSeatsPerRow.putIfAbsent(rowName, availableSeatsInRow);
            StringBuilder sb = new StringBuilder();
            sb.append(" ");
            for (Seat seat : rowsToSeats.get(rowNumber)) {
                if (seat.isReserved()) {
                    sb.append("X");
                } else if (recommendedSeats.contains(seat)) {
                    sb.append("O");
                    availableSeatsInRow.add(seat.getNumber());
                } else {
                    sb.append(".");
                    availableSeatsInRow.add(seat.getNumber());
                }
                sb.append(" ");
            }
            graphicSeatsRepresentation.put(rowName, sb.toString());
            sb.setLength(0);

        }
        roomWithSeats.putIfAbsent("Rows", graphicSeatsRepresentation);
        roomWithSeats.put("Back", "Back wall is located on this side of the room.");
        return new AvailableSeatsDto(roomWithSeats, listsOfAvailableSeatsPerRow);
    }
}
