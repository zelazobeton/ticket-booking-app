package com.zelazobeton.ticketbooking.recommendation.impl;

import com.zelazobeton.ticketbooking.recommendation.SeatsRecommendator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SeatsRecommendatorTest {
    private static final int NUMBER_OF_ROWS = 10;
    private static final int EVEN_NUMBER_OF_SEATS_IN_ROW = 8;
    private static final int BEST_SEAT_WEIGHT = 0;

    private SeatsRecommendator sut;

    @BeforeClass
    public void setupSystemUnderTest() {
        this.sut = new BasicSeatsRecommendator();
    }

    @Test
    public void calculateWeightForSeatReturnsZeroForTheCentralTopSeat() {
        // given
        int rowNumber = 10;
        int seatNumber = 4;

        // when
        int weight = this.sut.calculateWeightForSeat(NUMBER_OF_ROWS, EVEN_NUMBER_OF_SEATS_IN_ROW, rowNumber, seatNumber);

        // then
        Assert.assertEquals(weight, BEST_SEAT_WEIGHT);
    }
}