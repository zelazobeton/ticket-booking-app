package com.zelazobeton.ticketbooking.screening.infrastructure;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.zelazobeton.ticketbooking.screening.model.vo.ScreeningDto;
import com.zelazobeton.ticketbooking.screening.model.Screening;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreeningRepository extends CrudRepository<Screening, Long> {

    @Query("select m.title, s.time " +
           "from screening s join movie m on m.id=s.movie_id " +
            "where s.time < :upperLimit " +
            "and s.time > :lowerLimit")
    List<ScreeningDto> getScreeningsInGivenTimeInterval(@Param("lowerLimit") LocalDateTime lowerLimit,
            @Param("upperLimit") LocalDateTime upperLimit, Sort sort);

    @Query("select s.* from screening s " +
            "join movie m on s.movie_id=m.id " +
            "where s.time = :time " +
            "and m.title = :title " +
            "limit 1")
    Screening getScreeningByTitleAndTime(@Param("title") String title, @Param("time") LocalDateTime time);
}
