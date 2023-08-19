package com.zelazobeton.ticketbooking.shared;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
public class BaseEntity implements Serializable {
    @Id
    protected Long id;
}
