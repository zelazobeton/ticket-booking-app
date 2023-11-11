package com.zelazobeton.ticketbooking.screening.model.vo;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AvailableSeatsDto {
    private JsonNode seatsRepresentation;
    private JsonNode listsOfAvailableSeatsPerRow;

    public AvailableSeatsDto(JsonNode seatsRepresentation, JsonNode listsOfAvailableSeatsPerRow) {
        this.seatsRepresentation = seatsRepresentation;
        this.listsOfAvailableSeatsPerRow = listsOfAvailableSeatsPerRow;
    }
}
