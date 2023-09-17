package com.zelazobeton.ticketbooking.reservation.application.port.out;

import com.zelazobeton.ticketbooking.screening.model.Screening;
import com.zelazobeton.ticketbooking.screening.model.Seat;
import com.zelazobeton.ticketbooking.shared.GenericRepository;

import java.util.List;

public interface SeatRepository extends GenericRepository<Seat> {

    List<Seat> getSeatByScreeningRowAndNumber(Screening screening, int rowNumber, int number);
}
