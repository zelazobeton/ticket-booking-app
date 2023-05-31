package com.zelazobeton.ticketbooking.reservation.application;

import java.util.List;
import java.util.Set;

import com.zelazobeton.ticketbooking.reservation.model.vo.ReservedSeatDto;
import com.zelazobeton.ticketbooking.screening.model.Screening;
import com.zelazobeton.ticketbooking.screening.model.Seat;

public interface SeatService {
    Set<Seat> findReservedSeats(List<ReservedSeatDto> reservedSeats, Screening screening);
}
