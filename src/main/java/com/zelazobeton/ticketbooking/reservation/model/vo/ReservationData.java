package com.zelazobeton.ticketbooking.reservation.model.vo;

import java.util.List;

import javax.validation.Valid;

import com.zelazobeton.ticketbooking.screening.model.vo.ScreeningDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationData {
    @Valid
    private ClientDto client;
    private ScreeningDto screening;
    private List<ReservedSeatDto> seats;

    public ReservationData(ClientDto client, ScreeningDto screening, List<ReservedSeatDto> seats) {
        this.client = client;
        this.screening = screening;
        this.seats = seats;
    }
}
