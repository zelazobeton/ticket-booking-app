package com.zelazobeton.ticketbooking.screening.application.port.out;

import com.zelazobeton.ticketbooking.screening.model.Screening;
import com.zelazobeton.ticketbooking.screening.model.vo.ScreeningDto;
import com.zelazobeton.ticketbooking.shared.GenericRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScreeningRepository extends GenericRepository<Screening> {

    List<ScreeningDto> getScreeningsInGivenTimeInterval(LocalDateTime lowerLimit, LocalDateTime upperLimit);

    List<Screening> getScreeningByTitleAndTime(String title, LocalDateTime time);
}
