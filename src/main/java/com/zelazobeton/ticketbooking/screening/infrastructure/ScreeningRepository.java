package com.zelazobeton.ticketbooking.screening.infrastructure;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zelazobeton.ticketbooking.screening.model.vo.ScreeningDto;
import com.zelazobeton.ticketbooking.screening.model.Screening;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    @Query("select new com.zelazobeton.ticketbooking.screening.model.vo.ScreeningDto(s.movie.title, s.time) " +
                   "from Screening s where s.time < :upperLimit and s.time > :lowerLimit")
    List<ScreeningDto> getScreeningsInGivenTimeInterval(@Param("lowerLimit") LocalDateTime lowerLimit,
            @Param("upperLimit") LocalDateTime upperLimit, Sort sort);

    @Query("select s from Screening s where s.time = :time and s.movie.title = :title")
    List<Screening> getScreeningByTitleAndTime(@Param("title") String title, @Param("time") LocalDateTime time);
}
