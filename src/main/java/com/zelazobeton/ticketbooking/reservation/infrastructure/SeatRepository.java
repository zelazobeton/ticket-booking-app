package com.zelazobeton.ticketbooking.reservation.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zelazobeton.ticketbooking.screening.model.Screening;
import com.zelazobeton.ticketbooking.screening.model.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query("select s from Seat s where s.screening = :screening and s.rowNumber = :rowNumber and s.number = :number")
    List<Seat> getSeatByScreeningRowAndNumber(@Param("screening") Screening screening,
            @Param("rowNumber") int rowNumber, @Param("number") int number);
}
