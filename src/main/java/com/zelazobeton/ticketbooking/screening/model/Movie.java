package com.zelazobeton.ticketbooking.screening.model;

import com.zelazobeton.ticketbooking.shared.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Movie extends BaseEntity {
    private String title;
}
