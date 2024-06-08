package com.wemingle_chat.chat.domain.repository;

import com.wemingle_chat.chat.domain.entity.Room;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository {

    void save(Room room);
    Optional<Room> findByRoomId(String id);

}
