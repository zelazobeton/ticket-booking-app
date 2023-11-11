package com.zelazobeton.ticketbooking.screening.application;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zelazobeton.ticketbooking.recommendation.SeatsRecommendator;
import com.zelazobeton.ticketbooking.screening.model.Movie;
import com.zelazobeton.ticketbooking.screening.model.Room;
import com.zelazobeton.ticketbooking.screening.model.Screening;
import com.zelazobeton.ticketbooking.screening.model.Seat;

public class ScreeningFactory {

    public static Screening createScreening(Movie movie, Room room,
            SeatsRecommendator seatsRecommendator, LocalDateTime time)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode seatsSchema = mapper.readTree(room.getSeatsSchema());
        Screening screening = new Screening(movie, room, time);
        Set<Seat> seats = ScreeningFactory.createSeatsFromSeatsSchema(seatsSchema, seatsRecommendator, screening);
        screening.setSeats(seats);
        return screening;
    }

    private static Set<Seat> createSeatsFromSeatsSchema(JsonNode jsonNode,
            SeatsRecommendator seatsRecommendator, Screening screening) {
        Set<Seat> seats = new HashSet<>();
        if (jsonNode.isArray()) {
            int numberOfRows = jsonNode.size();
            int rowNumber = 1;
            Iterator<JsonNode> itr = jsonNode.elements();
            while (itr.hasNext()) {
                int seatsInRow = itr.next().asInt();
                for (int seatIdx = 1; seatIdx <= seatsInRow; seatIdx++) {
                    int weight = seatsRecommendator.calculateWeightForSeat(numberOfRows, seatsInRow,
                            rowNumber, seatIdx);
                    seats.add(new Seat(rowNumber, seatIdx, false, weight, screening));
                }
                rowNumber++;
            }
        } else {
            System.out.println("ERROR");
        }
        return seats;
    }
}
