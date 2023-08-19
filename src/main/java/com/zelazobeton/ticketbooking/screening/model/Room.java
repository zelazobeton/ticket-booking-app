package com.zelazobeton.ticketbooking.screening.model;

import com.zelazobeton.ticketbooking.shared.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Room extends BaseEntity {
    private String name;

    private String seatsSchema;
}
