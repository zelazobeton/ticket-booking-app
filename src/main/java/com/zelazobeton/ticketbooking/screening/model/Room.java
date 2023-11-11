package com.zelazobeton.ticketbooking.screening.model;

import com.zelazobeton.ticketbooking.shared.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Room extends BaseEntity {
    private String name;

    @Lob
    @Type(type = "org.hibernate.type.MaterializedClobType")
    private String seatsSchema;
}
