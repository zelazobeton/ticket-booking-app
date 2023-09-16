package com.zelazobeton.ticketbooking.reservation.model.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class Bill {
    LocalDateTime reservationExpiryDate;
    BigDecimal amountToBePaid;

    public Bill(LocalDateTime reservationExpiryDate, BigDecimal amountToBePaid) {
        this.reservationExpiryDate = reservationExpiryDate;
        this.amountToBePaid = amountToBePaid;
    }
}
