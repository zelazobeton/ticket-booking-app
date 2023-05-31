package com.zelazobeton.ticketbooking.screening.model;

import java.time.LocalDateTime;
import java.util.Set;

import com.zelazobeton.ticketbooking.shared.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Screening extends BaseEntity {
    @ManyToOne
    private Movie movie;
    @ManyToOne
    private Room room;
    private LocalDateTime time;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Seat> seats;

    public Screening(Movie movie, Room room, LocalDateTime time) {
        this.movie = movie;
        this.room = room;
        this.time = time;
    }
}
