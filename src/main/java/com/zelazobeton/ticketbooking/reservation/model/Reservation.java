package com.zelazobeton.ticketbooking.reservation.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.zelazobeton.ticketbooking.reservation.model.vo.ClientDto;
import com.zelazobeton.ticketbooking.screening.model.Screening;
import com.zelazobeton.ticketbooking.screening.model.Seat;
import com.zelazobeton.ticketbooking.shared.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Reservation extends BaseEntity {
    @Embedded
    private ClientDto clientDto;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Screening screening;

    @OneToMany(cascade = CascadeType.PERSIST)
    private Set<Seat> reservedSeats;

    private boolean isPaid;

    private LocalDateTime expiryDate;

    public Reservation(ClientDto clientDto, Screening screening, Set<Seat> reservedSeats) {
        this.clientDto = clientDto;
        this.screening = screening;
        this.reservedSeats = reservedSeats;
        this.isPaid = false;
        this.expiryDate = LocalDateTime.ofInstant(Instant.now().plusSeconds(60L * 15L), ZoneId.of("Europe/Warsaw"))
                .truncatedTo(ChronoUnit.MINUTES);
    }

    public void markAsPaid() {
        this.isPaid = true;
    }
}
