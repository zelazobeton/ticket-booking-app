package com.zelazobeton.ticketbooking.screening.model.vo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScreeningDto {
    private String title;
    private LocalDateTime time;

    public ScreeningDto(String title, LocalDateTime time) {
        this.title = title;
        this.time = time;
    }
}
