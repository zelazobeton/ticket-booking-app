package com.zelazobeton.ticketbooking.reservation.application.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.zelazobeton.ticketbooking.reservation.application.SeatService;
import com.zelazobeton.ticketbooking.reservation.infrastructure.SeatRepository;
import com.zelazobeton.ticketbooking.reservation.model.vo.ReservedSeatDto;
import com.zelazobeton.ticketbooking.screening.model.Screening;
import com.zelazobeton.ticketbooking.screening.model.Seat;

@Service
class SeatServiceImpl implements SeatService {

    private SeatRepository seatRepository;

    public SeatServiceImpl(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Override
    public Set<Seat> findReservedSeats(List<ReservedSeatDto> reservedSeats, Screening screening) {
        Set<Seat> seatsToBeReserved = new HashSet<>();
        for (ReservedSeatDto reservedSeat : reservedSeats) {
            List<Seat> seats = this.seatRepository.getSeatByScreeningRowAndNumber(screening, reservedSeat.getRow(),
                    reservedSeat.getNumber());
            if (seats.size() != 1) {
                throw new EntityNotFoundException(
                        String.format("Seat with given number: %d in selected row: %d does not exist.",
                                reservedSeat.getNumber(), reservedSeat.getRow()));
            }
            Seat seat = seats.get(0);
            if (seat.isReserved()) {
                throw new EntityNotFoundException(String.format("Selected seat number: %d in row: %d is already reserved.",
                        reservedSeat.getNumber(), reservedSeat.getRow()));
            }
            seatsToBeReserved.add(seat);
        }
        return seatsToBeReserved;
    }
}
