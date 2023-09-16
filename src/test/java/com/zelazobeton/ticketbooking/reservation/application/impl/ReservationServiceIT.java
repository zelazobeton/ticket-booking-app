package com.zelazobeton.ticketbooking.reservation.application.impl;

import com.zelazobeton.ticketbooking.reservation.application.ReservationService;
import com.zelazobeton.ticketbooking.reservation.model.vo.ClientDto;
import com.zelazobeton.ticketbooking.reservation.model.vo.ReservationData;
import com.zelazobeton.ticketbooking.screening.model.vo.ScreeningDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ReservationTestContext.class})
@WebAppConfiguration
public class ReservationServiceIT {
    private static final ClientDto TEST_CLIENT = new ClientDto("Jan", "Kowalski");
    private static final LocalDateTime TEST_SCREENING_TIME = LocalDateTime.of(LocalDate.of(2023, 10, 15), LocalTime.NOON);
    private static final ScreeningDto TEST_SCREENING = new ScreeningDto("MOCK_TITLE", TEST_SCREENING_TIME);
    private static final Long TEST_MOVIE_ID = 11L;
    private static final Long TEST_ROOM_ID = 22L;

    @Autowired
    private ReservationService reservationService;

    @Test
    public void createReservationThrowsWhenNoSeatsAreRequired() {
        // given
        ReservationData reservationData = new ReservationData(TEST_CLIENT, TEST_SCREENING, Collections.emptyList());

        // when & then
        Assertions.assertThatThrownBy(() -> this.reservationService.createReservation(reservationData)).isOfAnyClassIn(RuntimeException.class);
    }
}