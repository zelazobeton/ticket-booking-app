package com.zelazobeton.ticketbooking.screening.model;

import com.zelazobeton.ticketbooking.shared.BaseEntity;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Movie extends BaseEntity {
    private String title;
}
