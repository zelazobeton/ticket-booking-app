package com.zelazobeton.ticketbooking.screening.model;

import java.util.Objects;
import com.zelazobeton.ticketbooking.shared.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class Seat extends BaseEntity implements Comparable<Seat> {
    private int rowNumber;
    private int number;
    private boolean reserved;
    private int weight;
    private Long screeningId;

    public Seat(int rowNumber, int number, boolean reserved, int weight, Long screeningId) {
        this.rowNumber = rowNumber;
        this.number = number;
        this.reserved = reserved;
        this.weight = weight;
        this.screeningId = screeningId;
    }

    public void reserve() {
        this.reserved = true;
    }

    @Override
    public int compareTo(Seat other) {
        int result = Integer.compare(this.rowNumber, other.getRowNumber());
        if (result == 0) {
            result = Integer.compare(this.number, other.getNumber());
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Seat seat = (Seat) o;
        return rowNumber == seat.rowNumber && number == seat.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowNumber, number);
    }
}
