package com.zelazobeton.ticketbooking.recommendation.impl;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.zelazobeton.ticketbooking.recommendation.SeatsRecommendator;
import com.zelazobeton.ticketbooking.screening.model.Seat;

@Component
class BasicSeatsRecommendator implements SeatsRecommendator {

    @Override
    public int calculateWeightForSeat(int numberOfRows, int numberOfSeatsInRow, int rowNumber, int seatNumber) {
        int weightDueToDistanceFromRowCenter;
        if (this.isEven(numberOfSeatsInRow)) {
            weightDueToDistanceFromRowCenter = calculateWeightDueToDistanceFromRowCenterForEvenNumberOfSeats(
                    numberOfSeatsInRow, seatNumber);
        } else {
            weightDueToDistanceFromRowCenter = calculateWeightDueToDistanceFromRowCenterForOddNumberOfSeats(
                    numberOfSeatsInRow, seatNumber);
        }
        int weightDueToDistanceFromTopRow = (numberOfRows - rowNumber) * 2;
        return weightDueToDistanceFromRowCenter + weightDueToDistanceFromTopRow;
    }

    @Override
    public HashSet<Seat> getRecommendedSeats(Map<Integer, List<Seat>> rowsToSortedSeats, int numberOfRequiredSeats) {
        Deque<Seat> globalBestSeats = new ArrayDeque<>();
        int globalBestSeatsWeight = Integer.MAX_VALUE;
        Deque<Seat> currentBestSeats;
        int currentBestSeatsWeight;

        for (var entry : rowsToSortedSeats.entrySet()) {
            currentBestSeats = new ArrayDeque<>();
            currentBestSeatsWeight = 0;
            for (Seat seat : entry.getValue()) {
                if (!seat.isReserved()) {
                    if (currentBestSeats.size() < numberOfRequiredSeats) {
                        currentBestSeatsWeight += seat.getWeight();
                        currentBestSeats.add(seat);
                    } else if (currentBestSeats.getFirst().getWeight() > seat.getWeight()) {
                        Seat removedSeat = currentBestSeats.removeFirst();
                        currentBestSeats.add(seat);
                        currentBestSeatsWeight += seat.getWeight();
                        currentBestSeatsWeight -= removedSeat.getWeight();
                    }
                    if (currentBestSeats.size() == numberOfRequiredSeats &&
                            currentBestSeatsWeight < globalBestSeatsWeight) {
                        globalBestSeats = currentBestSeats;
                        globalBestSeatsWeight = currentBestSeatsWeight;
                    }
                } else {
                    currentBestSeats = new ArrayDeque<>();
                    currentBestSeatsWeight = 0;
                }
            }
        }
        return new HashSet<>(globalBestSeats);
    }

    private boolean isEven(int number) {
        return (number & 1) == 0;
    }

    private int calculateWeightDueToDistanceFromRowCenterForEvenNumberOfSeats(int numberOfSeatsInRow, int seatNumber) {
        int zeroWeightSeatNumber = numberOfSeatsInRow / 2;
        int distanceFromLowerZeroWeightSeatNumber = zeroWeightSeatNumber - seatNumber;
        if (distanceFromLowerZeroWeightSeatNumber >= 0) {
            return distanceFromLowerZeroWeightSeatNumber;
        } else {
            return Math.abs(distanceFromLowerZeroWeightSeatNumber) - 1;
        }
    }

    private int calculateWeightDueToDistanceFromRowCenterForOddNumberOfSeats(int numberOfSeatsInRow, int seatNumber) {
        int zeroWeightSeatNumber = numberOfSeatsInRow / 2 + 1;
        return Math.abs(zeroWeightSeatNumber - seatNumber);
    }
}
