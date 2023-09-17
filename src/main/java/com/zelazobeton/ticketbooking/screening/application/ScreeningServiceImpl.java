package com.zelazobeton.ticketbooking.screening.application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zelazobeton.ticketbooking.recommendation.SeatsRecommendator;
import com.zelazobeton.ticketbooking.screening.application.port.out.ScreeningRepository;
import com.zelazobeton.ticketbooking.screening.model.Screening;
import com.zelazobeton.ticketbooking.screening.model.Seat;
import com.zelazobeton.ticketbooking.screening.model.vo.AvailableSeatsDto;
import com.zelazobeton.ticketbooking.screening.model.vo.ScreeningDto;

@Service
class ScreeningServiceImpl implements ScreeningService {
    private ScreeningRepository screeningRepository;
    private SeatsRecommendator seatsRecommendator;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static int FIRST_OBJECT_RETURNED = 0;

    public ScreeningServiceImpl(ScreeningRepository screeningRepository, SeatsRecommendator seatsRecommendator) {
        this.screeningRepository = screeningRepository;
        this.seatsRecommendator = seatsRecommendator;
    }

    @Override
    public List<ScreeningDto> getScreenings(String date, String time) {
        LocalDateTime dateTime = LocalDateTime.parse(date + " " + time, this.formatter);
        LocalDateTime upperBoundary = dateTime.plusHours(4);
        LocalDateTime lowerBoundary = dateTime.minusHours(1);
        return this.screeningRepository.getScreeningsInGivenTimeInterval(lowerBoundary, upperBoundary);
    }

    @Override
    public AvailableSeatsDto getAvailableSeats(String title, String date, String time, int seats) {
        LocalDateTime dateTime = LocalDateTime.parse(date + " " + time, this.formatter);
        Screening screening = this.findScreeningByTitleDateAndTime(title, dateTime);
        Map<Integer, List<Seat>> rowsToSeats = screening.getSeats()
                .stream()
                .collect(Collectors.groupingBy(Seat::getRowNumber));
        rowsToSeats.forEach((key, value) -> Collections.sort(value));
        HashSet<Seat> recommendedSeats = this.seatsRecommendator.getRecommendedSeats(rowsToSeats, seats);
        return this.createAvailableSeatsData(rowsToSeats, recommendedSeats);
    }

    @Override
    public Screening findScreeningByTitleDateAndTime(String title, LocalDateTime dateTime) {
        List<Screening> selectedScreening = this.screeningRepository.getScreeningByTitleAndTime(title, dateTime);
        if (selectedScreening.size() == 0) {
            throw new EntityNotFoundException("There is no screening of selected movie on given date and time");
        }
        return selectedScreening.get(FIRST_OBJECT_RETURNED);
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
