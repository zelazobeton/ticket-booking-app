package com.zelazobeton.ticketbooking.screening.adapter.out;

import com.zelazobeton.ticketbooking.screening.application.port.out.ScreeningRepository;
import com.zelazobeton.ticketbooking.screening.model.Screening;
import com.zelazobeton.ticketbooking.screening.model.vo.ScreeningDto;
import com.zelazobeton.ticketbooking.shared.AbstractRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@NoArgsConstructor
class ScreeningRepositoryImpl extends AbstractRepository<Screening> implements ScreeningRepository {

    @Override
    public List<Screening> getScreeningByTitleAndTime(String title, LocalDateTime time) {
        return this.getJPQLQuery("select s from Screening s left join fetch s.seats where s.time = :time and s.movie.title = :title")
                .setParameter("title", title)
                .setParameter("time", time)
                .getResultList();
    }

    @Override
    public List<ScreeningDto> getScreeningsInGivenTimeInterval(LocalDateTime lowerLimit, LocalDateTime upperLimit) {
        List<Screening> screenings = this.getJPQLQuery("select s from Screening s where s.time < :upperLimit and s.time > :lowerLimit ORDER BY s.movie.title, s.time")
                .setParameter("lowerLimit", lowerLimit)
                .setParameter("upperLimit", upperLimit)
                .getResultList();
        return screenings.stream().map(s -> new ScreeningDto(s.getMovie().getTitle(), s.getTime())).collect(Collectors.toList());
    }
}
