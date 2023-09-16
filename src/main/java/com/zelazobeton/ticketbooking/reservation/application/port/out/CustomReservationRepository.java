package com.zelazobeton.ticketbooking.reservation.application.port.out;

import com.zelazobeton.ticketbooking.reservation.model.Reservation;
import com.zelazobeton.ticketbooking.screening.model.Seat;

import java.util.Set;

public interface CustomReservationRepository {

    void saveReservedSeats(Set<Seat> seats, Long reservationId);

    Reservation save(Reservation reservation);

    Reservation findById(Long reservationId);
}
