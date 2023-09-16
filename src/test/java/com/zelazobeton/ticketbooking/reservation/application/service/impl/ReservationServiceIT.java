package com.zelazobeton.ticketbooking.reservation.application.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zelazobeton.ticketbooking.recommendation.SeatsRecommendator;
import com.zelazobeton.ticketbooking.reservation.adapter.out.CustomReservationRepositoryImpl;
import com.zelazobeton.ticketbooking.reservation.application.port.out.CustomReservationRepository;
import com.zelazobeton.ticketbooking.reservation.application.port.out.SeatRepository;
import com.zelazobeton.ticketbooking.reservation.application.service.ReservationService;
import com.zelazobeton.ticketbooking.reservation.model.Reservation;
import com.zelazobeton.ticketbooking.reservation.model.vo.ClientDto;
import com.zelazobeton.ticketbooking.reservation.model.vo.ReservationData;
import com.zelazobeton.ticketbooking.reservation.model.vo.ReservedSeatDto;
import com.zelazobeton.ticketbooking.reservation.model.vo.TicketType;
import com.zelazobeton.ticketbooking.screening.application.SeatsFactory;
import com.zelazobeton.ticketbooking.screening.infrastructure.MovieRepository;
import com.zelazobeton.ticketbooking.screening.infrastructure.RoomRepository;
import com.zelazobeton.ticketbooking.screening.infrastructure.ScreeningRepository;
import com.zelazobeton.ticketbooking.screening.model.Movie;
import com.zelazobeton.ticketbooking.screening.model.Room;
import com.zelazobeton.ticketbooking.screening.model.Screening;
import com.zelazobeton.ticketbooking.screening.model.vo.ScreeningDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.sql.DataSource;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ReservationTestContext.class})
@WebAppConfiguration
public class ReservationServiceIT {
    private static final ClientDto TEST_CLIENT = new ClientDto("Jan", "Kowalski");
    private static final LocalDateTime TEST_SCREENING_TIME = LocalDateTime.of(LocalDate.of(2023, 10, 15), LocalTime.NOON);
    private static final String TEST_TITLE = "TEST_TITLE";
    private static final ScreeningDto TEST_SCREENING = new ScreeningDto(TEST_TITLE, TEST_SCREENING_TIME);
    private static final ReservedSeatDto RESERVED_SEAT_1 = new ReservedSeatDto(1, 2, TicketType.ADULT);
    private static final ReservedSeatDto RESERVED_SEAT_2 = new ReservedSeatDto(1, 3, TicketType.ADULT);
    private static final Long TEST_MOVIE_ID = 11L;
    private static final Long TEST_ROOM_ID = 22L;
    private String TEST_ROOM_SEATS = "[9, 12, 12, 12, 12, 12, 15, 15, 15]";

    private ReservationService reservationService;

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatsRecommendator seatsRecommendator;

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    Clock clock;

    CustomReservationRepository customReservationRepository;

    AutoCloseable mocks;

    void initTestedClass() {
        this.mocks = MockitoAnnotations.openMocks(this);
        this.customReservationRepository = Mockito.spy(new CustomReservationRepositoryImpl(this.jdbcTemplate, this.dataSource));
        this.reservationService = new ReservationServiceImpl(this.customReservationRepository, this.screeningRepository, this.seatRepository, this.clock);
    }

    @Test
    public void createReservationThrowsWhenNoSeatsAreRequired() {
        // given
        ReservationData reservationData = new ReservationData(TEST_CLIENT, TEST_SCREENING, Collections.emptyList());

        // when & then
        Assertions.assertThatThrownBy(() -> this.reservationService.createReservation(reservationData)).isOfAnyClassIn(RuntimeException.class);
    }

    @Test
    public void createReservationIsDoneInSingleTransaction() throws Exception {
        // given
        this.initTestedClass();
        this.saveSampleData();
        ReservationData reservationData = new ReservationData(TEST_CLIENT, TEST_SCREENING, List.of(RESERVED_SEAT_1, RESERVED_SEAT_2));
        doThrow(new RuntimeException()).when(this.customReservationRepository).saveReservedSeats(any(), any());

        // when
        try {
            this.reservationService.createReservation(reservationData);
        } catch (RuntimeException ex) {}

        // then
        Reservation savedReservation = this.customReservationRepository.findById(1L);
        Assertions.assertThat(savedReservation).isEqualTo(null);
        this.mocks.close();
    }

    private void saveSampleData() throws JsonProcessingException {
        Movie testMovie = this.movieRepository.save(new Movie(TEST_TITLE));
        Room testRoom = this.roomRepository.save(new Room("TEST_ROOM", this.TEST_ROOM_SEATS));
        Screening testScreening = this.screeningRepository.save(new Screening(testMovie.getId(), testRoom.getId(),
                TEST_SCREENING_TIME));
        this.seatRepository.saveAll(SeatsFactory.createSeatsForScreening(testRoom.getSeatsSchema(), this.seatsRecommendator, testScreening));
    }
}