package com.zelazobeton.ticketbooking.bootstrap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.zelazobeton.ticketbooking.reservation.application.port.out.SeatRepository;
import com.zelazobeton.ticketbooking.screening.application.SeatsFactory;
import com.zelazobeton.ticketbooking.screening.model.Screening;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zelazobeton.ticketbooking.recommendation.SeatsRecommendator;
import com.zelazobeton.ticketbooking.screening.infrastructure.MovieRepository;
import com.zelazobeton.ticketbooking.screening.infrastructure.RoomRepository;
import com.zelazobeton.ticketbooking.screening.infrastructure.ScreeningRepository;
import com.zelazobeton.ticketbooking.screening.model.Movie;
import com.zelazobeton.ticketbooking.screening.model.Room;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SampleDataGenerator implements CommandLineRunner {
    private ScreeningRepository screeningRepository;
    private RoomRepository roomRepository;
    private MovieRepository movieRepository;
    private SeatsRecommendator seatsRecommendator;
    private SeatRepository seatRepository;

    private String SEATS_IN_ROOM_CANNES = "[9, 12, 12, 12, 12, 12, 15, 15, 15]";
    private String SEATS_IN_ROOM_GDYNIA = "[9, 9, 9, 9, 9, 9, 9, 9, 9]";
    private String SEATS_IN_ROOM_SUNDANCE = "[10, 10, 10, 10, 13, 13, 13, 13, 13]";
    private LocalDate SAMPLE_DATE = LocalDate.of(2024, 7, 1);

    public SampleDataGenerator(ScreeningRepository screeningRepository,
                               RoomRepository roomRepository,
                               MovieRepository movieRepository,
                               SeatsRecommendator seatsRecommendator,
                               SeatRepository seatRepository) {
        this.screeningRepository = screeningRepository;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.seatsRecommendator = seatsRecommendator;
        this.seatRepository = seatRepository;
    }

    @Override
    public void run(String... args) throws JsonProcessingException {
        this.loadSampleData();
    }

    private void loadSampleData() throws JsonProcessingException {
        Movie movie_1 = this.movieRepository.save(new Movie("Dancing with sharks"));
        Movie movie_2 = this.movieRepository.save(new Movie("Citizen Bane"));
        Movie movie_3 = this.movieRepository.save(new Movie("Saving private Brian"));
        Movie movie_4 = this.movieRepository.save(new Movie("Moose hunter"));
        Movie movie_5 = this.movieRepository.save(new Movie("Godmother"));
        Movie movie_6 = this.movieRepository.save(new Movie("Eyes wide open"));

        Room cannes = this.roomRepository.save(new Room("Cannes", this.SEATS_IN_ROOM_CANNES));
        Room gdynia = this.roomRepository.save(new Room("Gdynia", this.SEATS_IN_ROOM_GDYNIA));
        Room sundance = this.roomRepository.save(new Room("Sundance", this.SEATS_IN_ROOM_SUNDANCE));

        Screening screening_1 = this.screeningRepository.save(new Screening(movie_1.getId(), cannes.getId(),
                LocalDateTime.of(this.SAMPLE_DATE, LocalTime.of(21, 40))));
        Screening screening_2 = this.screeningRepository.save(new Screening(movie_2.getId(), cannes.getId(),
                LocalDateTime.of(this.SAMPLE_DATE, LocalTime.of(18, 20))));
        Screening screening_3 = this.screeningRepository.save(new Screening(movie_3.getId(), gdynia.getId(),
                LocalDateTime.of(this.SAMPLE_DATE, LocalTime.of(20, 00))));
        Screening screening_4 = this.screeningRepository.save(new Screening(movie_4.getId(), gdynia.getId(),
                LocalDateTime.of(this.SAMPLE_DATE, LocalTime.of(16, 40))));
        Screening screening_5 = this.screeningRepository.save(new Screening(movie_5.getId(), sundance.getId(),
                LocalDateTime.of(this.SAMPLE_DATE, LocalTime.of(17, 20))));
        Screening screening_6 = this.screeningRepository.save(new Screening(movie_6.getId(), sundance.getId(),
                LocalDateTime.of(this.SAMPLE_DATE, LocalTime.of(20, 40))));

        this.seatRepository.saveAll(SeatsFactory.createSeatsForScreening(cannes.getSeatsSchema(), this.seatsRecommendator, screening_1));
        this.seatRepository.saveAll(SeatsFactory.createSeatsForScreening(cannes.getSeatsSchema(), this.seatsRecommendator, screening_2));
        this.seatRepository.saveAll(SeatsFactory.createSeatsForScreening(gdynia.getSeatsSchema(), this.seatsRecommendator, screening_3));
        this.seatRepository.saveAll(SeatsFactory.createSeatsForScreening(gdynia.getSeatsSchema(), this.seatsRecommendator, screening_4));
        this.seatRepository.saveAll(SeatsFactory.createSeatsForScreening(sundance.getSeatsSchema(), this.seatsRecommendator, screening_5));
        this.seatRepository.saveAll(SeatsFactory.createSeatsForScreening(sundance.getSeatsSchema(), this.seatsRecommendator, screening_6));


    }
}
