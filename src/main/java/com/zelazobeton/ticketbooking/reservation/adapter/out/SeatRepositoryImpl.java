package com.zelazobeton.ticketbooking.reservation.adapter.out;

import com.zelazobeton.ticketbooking.reservation.application.port.out.SeatRepository;
import com.zelazobeton.ticketbooking.screening.model.Screening;
import com.zelazobeton.ticketbooking.screening.model.Seat;
import com.zelazobeton.ticketbooking.shared.AbstractRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@NoArgsConstructor
class SeatRepositoryImpl extends AbstractRepository<Seat> implements SeatRepository {

    @Override
    public List<Seat> getSeatByScreeningRowAndNumber(Screening screening, int rowNumber, int number) {
        return this.getJPQLQuery("select s from Seat s where s.screening = :screening and s.rowNumber = :rowNumber and s.number = :number")
                .setParameter("screening", screening)
                .setParameter("rowNumber", rowNumber)
                .setParameter("number", number)
                .getResultList();
    }
}
