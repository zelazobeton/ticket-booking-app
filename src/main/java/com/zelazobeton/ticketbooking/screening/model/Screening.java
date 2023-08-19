package com.zelazobeton.ticketbooking.screening.model;

import java.time.LocalDateTime;

import com.zelazobeton.ticketbooking.shared.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Screening extends BaseEntity {
    private Long movieId;
    private Long roomId;
    private LocalDateTime time;

    public Screening(Long movieId, Long roomId, LocalDateTime time) {
        this.movieId = movieId;
        this.roomId = roomId;
        this.time = time;
    }
}
