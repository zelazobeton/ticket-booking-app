package com.zelazobeton.ticketbooking.reservation.adapter.out;

import com.zelazobeton.ticketbooking.reservation.application.port.out.ReservationRepository;
import com.zelazobeton.ticketbooking.reservation.model.Reservation;
import com.zelazobeton.ticketbooking.shared.AbstractRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@NoArgsConstructor
class ReservationRepositoryImpl extends AbstractRepository<Reservation> implements ReservationRepository {

}
