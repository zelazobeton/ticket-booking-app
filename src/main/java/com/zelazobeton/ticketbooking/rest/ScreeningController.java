package com.zelazobeton.ticketbooking.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zelazobeton.ticketbooking.reservation.application.ReservationService;
import com.zelazobeton.ticketbooking.reservation.model.vo.Bill;
import com.zelazobeton.ticketbooking.reservation.model.vo.ReservationData;
import com.zelazobeton.ticketbooking.screening.model.vo.AvailableSeatsDto;
import com.zelazobeton.ticketbooking.screening.model.vo.ScreeningDto;
import com.zelazobeton.ticketbooking.screening.application.ScreeningService;

@RestController
@RequestMapping
public class ScreeningController {

    private ScreeningService screeningService;
    private ReservationService reservationService;

    public ScreeningController(ScreeningService screeningService, ReservationService reservationService) {
        this.screeningService = screeningService;
        this.reservationService = reservationService;
    }

    @GetMapping(path = "screening", produces = { "application/json" })
    public ResponseEntity<List<ScreeningDto>> getScreeningsInGivenInterval(@RequestParam("date") String date,
            @RequestParam("time") String time) {
        return new ResponseEntity<>(this.screeningService.getScreenings(date, time), HttpStatus.OK);
    }

    @GetMapping(path = "/seats", produces = { "application/json" })
    public ResponseEntity<AvailableSeatsDto> getAvailableSeats(@RequestParam("title") String title,
            @RequestParam("date") String date, @RequestParam("time") String time, @RequestParam("seats") int seats) {
        return new ResponseEntity<>(this.screeningService.getAvailableSeats(title, date, time, seats), HttpStatus.OK);
    }

    @PostMapping(path = "/reservation", produces = { "application/json" })
    public ResponseEntity<Bill> createReservation(@Valid @RequestBody ReservationData reservationData) {
        return new ResponseEntity<>(this.reservationService.createReservation(reservationData), HttpStatus.OK);
    }
}
