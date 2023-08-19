package com.zelazobeton.ticketbooking.reservation.application.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.zelazobeton.ticketbooking.reservation.infrastructure.CustomReservationRepository;
import com.zelazobeton.ticketbooking.reservation.infrastructure.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zelazobeton.ticketbooking.reservation.application.ReservationService;
import com.zelazobeton.ticketbooking.reservation.application.SeatService;
import com.zelazobeton.ticketbooking.reservation.model.Reservation;
import com.zelazobeton.ticketbooking.reservation.model.vo.Bill;
import com.zelazobeton.ticketbooking.reservation.model.vo.ReservationData;
import com.zelazobeton.ticketbooking.reservation.model.vo.ReservedSeatDto;
import com.zelazobeton.ticketbooking.screening.application.ScreeningService;
import com.zelazobeton.ticketbooking.screening.model.Screening;
import com.zelazobeton.ticketbooking.screening.model.Seat;

@Service
class ReservationServiceImpl implements ReservationService {

    private CustomReservationRepository customReservationRepository;
    private ScreeningService screeningService;
    private SeatService seatService;
    private SeatRepository seatRepository;

    public ReservationServiceImpl(ScreeningService screeningService, SeatService seatService, SeatRepository seatRepository, CustomReservationRepository customReservationRepository) {
        this.customReservationRepository = customReservationRepository;
        this.screeningService = screeningService;
        this.seatService = seatService;
        this.seatRepository = seatRepository;
    }

    @Override
    @Transactional
    public Bill createReservation(ReservationData reservationData) {
        this.validateRequestedSeats(reservationData.getSeats());
        Screening screening = this.screeningService.findScreeningByTitleDateAndTime(reservationData.getScreening().getTitle(),
                reservationData.getScreening().getTime());
        this.validateReservationRequestTime(screening.getTime());

        Set<Seat> seats = this.seatService.findReservedSeats(reservationData.getSeats(), screening);
        seats.forEach(Seat::reserve);
        this.seatRepository.saveAll(seats);
        Reservation newReservation = new Reservation(reservationData.getClient(), screening.getId());
        newReservation = this.customReservationRepository.save(newReservation);
        this.customReservationRepository.saveReservedSeats(seats, newReservation.getId());
        BigDecimal amountToBePaid = this.calculateAmountToBePaid(reservationData.getSeats());
        return new Bill(newReservation.getExpiryDate(), amountToBePaid);
    }

    private BigDecimal calculateAmountToBePaid(List<ReservedSeatDto> seats) {
        BigDecimal price = BigDecimal.ZERO;
        for (ReservedSeatDto seatDto : seats) {
            price = price.add(seatDto.getType().getPrice());
        }
        return price;
    }

    private void validateReservationRequestTime(LocalDateTime screeningTime) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(screeningTime.minusMinutes(15))) {
            throw new RuntimeException("Reservations can be done only until 15 minutes before screening.");
        }
    }

    private void validateRequestedSeats(List<ReservedSeatDto> seats) {
        if (seats == null || seats.size() == 1) {
            throw new RuntimeException("Reservation has to contain at least one seat.");
        }
    }

}
