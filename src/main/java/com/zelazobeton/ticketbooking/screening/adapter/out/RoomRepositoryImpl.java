package com.zelazobeton.ticketbooking.screening.adapter.out;

import com.zelazobeton.ticketbooking.screening.application.port.out.RoomRepository;
import com.zelazobeton.ticketbooking.screening.model.Room;
import com.zelazobeton.ticketbooking.shared.AbstractRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@NoArgsConstructor
class RoomRepositoryImpl extends AbstractRepository<Room> implements RoomRepository {

}
