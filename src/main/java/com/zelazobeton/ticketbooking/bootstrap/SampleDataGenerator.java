package com.zelazobeton.ticketbooking.bootstrap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zelazobeton.ticketbooking.recommendation.SeatsRecommendator;
import com.zelazobeton.ticketbooking.screening.application.ScreeningFactory;
import com.zelazobeton.ticketbooking.screening.application.port.out.MovieRepository;
import com.zelazobeton.ticketbooking.screening.application.port.out.RoomRepository;
import com.zelazobeton.ticketbooking.screening.application.port.out.ScreeningRepository;
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

    private String SEATS_IN_ROOM_CANNES = "[9, 12, 12, 12, 12, 12, 15, 15, 15]";
    private String SEATS_IN_ROOM_GDYNIA = "[9, 9, 9, 9, 9, 9, 9, 9, 9]";
    private String SEATS_IN_ROOM_SUNDANCE = "[10, 10, 10, 10, 13, 13, 13, 13, 13]";
    private LocalDate SAMPLE_DATE = LocalDate.of(2024, 7, 1);

    public SampleDataGenerator(ScreeningRepository screeningRepository, RoomRepository roomRepository,
            MovieRepository movieRepository, SeatsRecommendator seatsRecommendator) {
        this.screeningRepository = screeningRepository;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.seatsRecommendator = seatsRecommendator;
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

        this.screeningRepository.save(ScreeningFactory.createScreening(movie_1, cannes, this.seatsRecommendator,
                LocalDateTime.of(this.SAMPLE_DATE, LocalTime.of(21, 40))));
        this.screeningRepository.save(ScreeningFactory.createScreening(movie_2, cannes, this.seatsRecommendator,
                LocalDateTime.of(this.SAMPLE_DATE, LocalTime.of(18, 20))));

        this.screeningRepository.save(ScreeningFactory.createScreening(movie_3, gdynia, this.seatsRecommendator,
                LocalDateTime.of(this.SAMPLE_DATE, LocalTime.of(20, 00))));
        this.screeningRepository.save(ScreeningFactory.createScreening(movie_4, gdynia, this.seatsRecommendator,
                LocalDateTime.of(this.SAMPLE_DATE, LocalTime.of(16, 40))));

        this.screeningRepository.save(ScreeningFactory.createScreening(movie_5, sundance, this.seatsRecommendator,
                LocalDateTime.of(this.SAMPLE_DATE, LocalTime.of(17, 20))));
        this.screeningRepository.save(ScreeningFactory.createScreening(movie_6, sundance, this.seatsRecommendator,
                LocalDateTime.of(this.SAMPLE_DATE, LocalTime.of(20, 40))));
    }
}
