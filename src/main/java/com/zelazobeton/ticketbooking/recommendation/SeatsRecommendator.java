package com.zelazobeton.ticketbooking.recommendation;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.zelazobeton.ticketbooking.screening.model.Seat;

public interface SeatsRecommendator {
    int calculateWeightForSeat(int numberOfRows, int numberOfSeatsInRow, int rowNumber, int seatNumber);

    HashSet<Seat> getRecommendedSeats(Map<Integer, List<Seat>> rowsToSortedSeats, int numberOfRequiredSeats);
}
