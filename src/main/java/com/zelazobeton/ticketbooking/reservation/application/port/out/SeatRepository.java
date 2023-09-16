package com.zelazobeton.ticketbooking.reservation.application.port.out;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zelazobeton.ticketbooking.screening.model.Seat;

@Repository
public interface SeatRepository extends CrudRepository<Seat, Long> {

    @Query("select s.* from seat s " +
            "where s.screening_id = :screeningId " +
            "and s.row_number = :rowNumber " +
            "and s.number = :number")
    List<Seat> getSeatByScreeningRowAndNumber(@Param("screeningId") Long screeningId,
            @Param("rowNumber") int rowNumber, @Param("number") int number);

    @Query("select s.* from seat s " +
            "join screening scr on s.screening_id=scr.id " +
            "join movie m on m.id=scr.movie_id " +
            "where scr.time = :time " +
            "and m.title = :title")
    Set<Seat> getSeatsByTitleAndScreeningTime(@Param("title") String title, @Param("time") LocalDateTime time);
}
