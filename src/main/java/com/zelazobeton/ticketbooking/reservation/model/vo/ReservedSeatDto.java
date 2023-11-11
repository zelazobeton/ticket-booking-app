package com.zelazobeton.ticketbooking.reservation.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservedSeatDto {
    private int row;
    private int number;
    TicketType type;

    public ReservedSeatDto(int row, int number, TicketType type) {
        this.row = row;
        this.number = number;
        this.type = type;
    }
}
