package com.zelazobeton.ticketbooking.reservation.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import com.zelazobeton.ticketbooking.reservation.model.vo.ClientDto;
import com.zelazobeton.ticketbooking.shared.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@Setter
@NoArgsConstructor
public class Reservation extends BaseEntity {

    private ClientDto clientDto;

    @Column("screening_id")
    private Long screeningId;
    @Column("is_paid")
    private boolean isPaid;
    @Column("expiry_date")
    private LocalDateTime expiryDate;

    public Reservation(ClientDto clientDto, Long screeningId) {
        this.clientDto = clientDto;
        this.screeningId = screeningId;
        this.isPaid = false;
        this.expiryDate = LocalDateTime.ofInstant(Instant.now().plusSeconds(60L * 15L), ZoneId.of("Europe/Warsaw"))
                .truncatedTo(ChronoUnit.MINUTES);
    }

    public Reservation(Long id, ClientDto clientDto, Long screeningId, boolean isPaid, LocalDateTime expiryDate) {
        this.id = id;
        this.clientDto = clientDto;
        this.screeningId = screeningId;
        this.isPaid = isPaid;
        this.expiryDate = expiryDate;
    }
}
