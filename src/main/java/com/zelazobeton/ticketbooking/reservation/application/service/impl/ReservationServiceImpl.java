package com.zelazobeton.ticketbooking.reservation.application.service.impl;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zelazobeton.ticketbooking.reservation.application.port.out.CustomReservationRepository;
import com.zelazobeton.ticketbooking.reservation.application.port.out.SeatRepository;
import com.zelazobeton.ticketbooking.screening.infrastructure.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zelazobeton.ticketbooking.reservation.application.service.ReservationService;
import com.zelazobeton.ticketbooking.reservation.model.Reservation;
import com.zelazobeton.ticketbooking.reservation.model.vo.Bill;
import com.zelazobeton.ticketbooking.reservation.model.vo.ReservationData;
import com.zelazobeton.ticketbooking.reservation.model.vo.ReservedSeatDto;
import com.zelazobeton.ticketbooking.screening.model.Screening;
import com.zelazobeton.ticketbooking.screening.model.Seat;
import org.springframework.transaction.support.TransactionTemplate;

@Service
class ReservationServiceImpl implements ReservationService {

    private final CustomReservationRepository customReservationRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final Clock clock;

    @Autowired
    PlatformTransactionManager transactionManager;

    public ReservationServiceImpl(CustomReservationRepository customReservationRepository, ScreeningRepository screeningRepository, SeatRepository seatRepository, Clock clock) {
        this.customReservationRepository = customReservationRepository;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.clock = clock;
    }

    @Override
    public Bill createReservation(ReservationData reservationData) {
        TransactionTemplate tt = new TransactionTemplate(this.transactionManager);
        Bill bill = tt.execute(status -> {
            this.validateRequestedSeats(reservationData.getSeats());
            Screening screening = this.screeningRepository.getScreeningByTitleAndTime(reservationData.getScreening().getTitle(),
                    reservationData.getScreening().getTime());
            this.validateReservationRequestTime(screening.getTime());

            Set<Seat> seats = this.findReservedSeats(reservationData.getSeats(), screening);
            seats.forEach(Seat::reserve);
            this.seatRepository.saveAll(seats);
            Reservation newReservation = new Reservation(reservationData.getClient(), screening.getId());
            newReservation = this.customReservationRepository.save(newReservation);
            this.customReservationRepository.saveReservedSeats(seats, newReservation.getId());
            newReservation.setPaid(true);
            BigDecimal amountToBePaid = this.calculateAmountToBePaid(reservationData.getSeats());
            return new Bill(newReservation.getExpiryDate(), amountToBePaid);
        });
        return bill;
    }

    private Set<Seat> findReservedSeats(List<ReservedSeatDto> reservedSeats, Screening screening) {
        Set<Seat> seatsToBeReserved = new HashSet<>();
        for (ReservedSeatDto reservedSeat : reservedSeats) {
            List<Seat> seats = this.seatRepository.getSeatByScreeningRowAndNumber(screening.getId(), reservedSeat.getRow(),
                    reservedSeat.getNumber());
            if (seats.size() != 1) {
                throw new RuntimeException(
                        String.format("Seat with given number: %d in selected row: %d does not exist.",
                                reservedSeat.getNumber(), reservedSeat.getRow()));
            }
            Seat seat = seats.get(0);
            if (seat.isReserved()) {
                throw new RuntimeException(String.format("Selected seat number: %d in row: %d is already reserved.",
                        reservedSeat.getNumber(), reservedSeat.getRow()));
            }
            seatsToBeReserved.add(seat);
        }
        return seatsToBeReserved;
    }

    private BigDecimal calculateAmountToBePaid(List<ReservedSeatDto> seats) {
        BigDecimal price = BigDecimal.ZERO;
        for (ReservedSeatDto seatDto : seats) {
            price = price.add(seatDto.getType().getPrice());
        }
        return price;
    }

    private void validateReservationRequestTime(LocalDateTime screeningTime) {
        LocalDateTime now = LocalDateTime.now(this.clock);
        if (now.isAfter(screeningTime.minusMinutes(15))) {
            throw new RuntimeException("Reservations can be done only until 15 minutes before screening.");
        }
    }

    private void validateRequestedSeats(List<ReservedSeatDto> seats) {
        if (seats == null || seats.size() == 0) {
            throw new RuntimeException("Reservation has to contain at least one seat.");
        }
    }

}
