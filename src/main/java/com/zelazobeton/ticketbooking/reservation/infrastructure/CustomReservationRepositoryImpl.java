package com.zelazobeton.ticketbooking.reservation.infrastructure;

import com.zelazobeton.ticketbooking.reservation.model.Reservation;
import com.zelazobeton.ticketbooking.reservation.model.vo.ClientDto;
import com.zelazobeton.ticketbooking.screening.model.Seat;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Repository
public class CustomReservationRepositoryImpl implements CustomReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CustomReservationRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("RESERVATION").usingGeneratedKeyColumns("ID");
    }

    @Override
    public void saveReservedSeats(Set<Seat> seats, Long reservationId) {
        for (Seat seat : seats) {
            this.jdbcTemplate.update("INSERT INTO RESERVATION_SEATS (reservation_id, seat_id) VALUES (?, ?)",
                    reservationId,
                    seat.getId());
        }
    }

    @Override
    public Reservation findById(Long reservationId) {
        return this.jdbcTemplate.queryForObject("SELECT R.* FROM RESERVATION R WHERE R.ID = ?", new Object[]{reservationId}, new ReservationRowMapper());
    }

    @Override
    public Reservation save(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("NAME", reservation.getClientDto().getName());
        parameters.put("SURNAME", reservation.getClientDto().getSurname());
        parameters.put("SCREENING_ID", reservation.getScreeningId());
        parameters.put("IS_PAID", reservation.isPaid());
        parameters.put("EXPIRY_DATE", reservation.getExpiryDate());
        Number id = this.simpleJdbcInsert.executeAndReturnKey(parameters);
        return this.findById(id.longValue());
    }

    private class ReservationRowMapper implements RowMapper<Reservation> {

        @Override
        public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Reservation(rs.getLong("ID"),
                    new ClientDto(rs.getString("NAME"), rs.getString("SURNAME")),
                    rs.getLong("SCREENING_ID"), rs.getBoolean("IS_PAID"), rs.getTimestamp("EXPIRY_DATE").toLocalDateTime());
        }
    }
}
