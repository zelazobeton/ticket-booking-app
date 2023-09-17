package com.zelazobeton.ticketbooking.screening.application.port.out;

import com.zelazobeton.ticketbooking.shared.GenericRepository;
import org.springframework.stereotype.Repository;

import com.zelazobeton.ticketbooking.screening.model.Room;

@Repository
public interface RoomRepository extends GenericRepository<Room> {
}
