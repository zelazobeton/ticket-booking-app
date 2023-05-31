package com.zelazobeton.ticketbooking.screening.application;

import java.time.LocalDateTime;
import java.util.List;

import com.zelazobeton.ticketbooking.screening.model.Screening;
import com.zelazobeton.ticketbooking.screening.model.vo.AvailableSeatsDto;
import com.zelazobeton.ticketbooking.screening.model.vo.ScreeningDto;

public interface ScreeningService {
    List<ScreeningDto> getScreenings(String date, String time);

    AvailableSeatsDto getAvailableSeats(String title, String date, String time, int seats);

    Screening findScreeningByTitleDateAndTime(String title, LocalDateTime dateTime);
}
