package com.zelazobeton.ticketbooking.screening.application;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zelazobeton.ticketbooking.recommendation.SeatsRecommendator;
import com.zelazobeton.ticketbooking.screening.model.Screening;
import com.zelazobeton.ticketbooking.screening.model.Seat;

public class SeatsFactory {

    public static Set<Seat> createSeatsForScreening(String seatsSchema,
                                                    SeatsRecommendator seatsRecommendator, Screening screening) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(seatsSchema);
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
                    seats.add(new Seat(rowNumber, seatIdx, false, weight, screening.getId()));
                }
                rowNumber++;
            }
        } else {
            System.out.println("ERROR");
        }
        return seats;
    }
}
