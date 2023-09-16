package com.zelazobeton.ticketbooking.reservation.application.service.impl;

import com.zelazobeton.ticketbooking.reservation.application.port.out.CustomReservationRepository;
import com.zelazobeton.ticketbooking.reservation.application.port.out.SeatRepository;
import com.zelazobeton.ticketbooking.reservation.model.vo.ClientDto;
import com.zelazobeton.ticketbooking.reservation.model.vo.ReservationData;
import com.zelazobeton.ticketbooking.reservation.model.vo.ReservedSeatDto;
import com.zelazobeton.ticketbooking.reservation.model.vo.TicketType;
import com.zelazobeton.ticketbooking.screening.infrastructure.ScreeningRepository;
import com.zelazobeton.ticketbooking.screening.model.Screening;
import com.zelazobeton.ticketbooking.screening.model.vo.ScreeningDto;
import org.assertj.core.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.*;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {
    private static final ClientDto TEST_CLIENT = new ClientDto("Jan", "Kowalski");
    private static final LocalDateTime TEST_SCREENING_TIME = LocalDateTime.of(LocalDate.of(2023, 10, 15), LocalTime.NOON);
    private static final ScreeningDto TEST_SCREENING = new ScreeningDto("MOCK_TITLE", TEST_SCREENING_TIME);
    private static final Long TEST_MOVIE_ID = 11L;
    private static final Long TEST_ROOM_ID = 22L;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private CustomReservationRepository customReservationRepository;
    @Mock
    private ScreeningRepository screeningRepository;
    @Mock
    private SeatRepository seatRepository;

    @Mock
    private Clock clock;

    AutoCloseable mocks;

    @BeforeClass
    public void setUp() throws Exception {
        this.mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterClass
    public void tearDown() throws Exception {
        this.mocks.close();
    }

    @Test
    public void createReservationThrowsWhenNoSeatsAreRequired() {
        // given
        ReservationData reservationData = new ReservationData(TEST_CLIENT, TEST_SCREENING, Collections.emptyList());
        this.mockScreeningRepository();

        // when & then
        Assertions.assertThatThrownBy(() -> this.reservationService.createReservation(reservationData)).isOfAnyClassIn(RuntimeException.class);
    }

    @Test
    public void createReservationThrowsWhenScreeningIsInLessThanFifteenMinutes() {
        // given
        ReservationData reservationData = new ReservationData(
                TEST_CLIENT,
                TEST_SCREENING,
                List.of(new ReservedSeatDto(5, 6, TicketType.ADULT), new ReservedSeatDto(5, 7, TicketType.ADULT))
        );
        this.mockScreeningRepository();
        this.mockLocalDateTime(TEST_SCREENING_TIME.minusMinutes(10));

        // when & then
        Assertions.assertThatThrownBy(() -> this.reservationService.createReservation(reservationData)).isOfAnyClassIn(RuntimeException.class);
    }

    private void mockScreeningRepository() {
        when(this.screeningRepository.getScreeningByTitleAndTime(anyString(), any())).thenReturn(new Screening(TEST_MOVIE_ID, TEST_ROOM_ID, TEST_SCREENING_TIME));
    }

    private void mockLocalDateTime(LocalDateTime mockedCurrentTime) {
        Clock fixedClock = Clock.fixed(mockedCurrentTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(this.clock).instant();
        doReturn(fixedClock.getZone()).when(this.clock).getZone();
    }
}