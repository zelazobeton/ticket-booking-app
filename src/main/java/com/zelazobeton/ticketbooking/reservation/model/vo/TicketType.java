package com.zelazobeton.ticketbooking.reservation.model.vo;

import java.math.BigDecimal;

public enum TicketType {
    ADULT(BigDecimal.valueOf(25)),
    CHILD(BigDecimal.valueOf(12.5)),
    STUDENT(BigDecimal.valueOf(18));

    TicketType(BigDecimal price) {
        this.price = price;
    }

    BigDecimal price;

    public BigDecimal getPrice() {
        return price;
    }
}
