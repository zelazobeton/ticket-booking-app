package com.zelazobeton.ticketbooking.screening.application;

import com.zelazobeton.ticketbooking.screening.model.vo.AvailableSeatsDto;
import com.zelazobeton.ticketbooking.screening.model.vo.ScreeningDto;

import java.util.List;

public interface ScreeningService {
    List<ScreeningDto> getScreenings(String date, String time);

    AvailableSeatsDto getAvailableSeats(String title, String date, String time, int seats);
}
