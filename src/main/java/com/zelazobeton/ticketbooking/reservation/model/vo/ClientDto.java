package com.zelazobeton.ticketbooking.reservation.model.vo;

import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class ClientDto {
    @Pattern(regexp = "^\\p{Lu}\\p{L}{2,}$",
             message = "Name has to contain at least 3 letters. It has to start with capital letter.")
    private String name;

    @Pattern(regexp = "^(?:\\p{Lu}\\p{L}{2,}|\\p{Lu}\\p{L}{0,}-\\p{Lu}\\p{L}{1,}|\\p{Lu}\\p{L}{1,}-\\p{Lu}\\p{L}{0,})$",
             message = "Surname has to contain at least 3 letters. It has to start with capital letter.")
    private String surname;

    public ClientDto(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }
}
