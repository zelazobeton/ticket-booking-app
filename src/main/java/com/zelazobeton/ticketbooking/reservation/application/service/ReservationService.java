package com.zelazobeton.ticketbooking.reservation.application.service;

import com.zelazobeton.ticketbooking.reservation.model.vo.Bill;
import com.zelazobeton.ticketbooking.reservation.model.vo.ReservationData;

public interface ReservationService {
    Bill createReservation(ReservationData reservationData);
}
