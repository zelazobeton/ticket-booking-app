package com.zelazobeton.ticketbooking.screening.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zelazobeton.ticketbooking.screening.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
