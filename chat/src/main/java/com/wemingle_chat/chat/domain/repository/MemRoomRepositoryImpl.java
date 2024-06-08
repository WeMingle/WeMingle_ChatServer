package com.wemingle_chat.chat.domain.repository;

import com.wemingle_chat.chat.domain.entity.Room;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemRoomRepositoryImpl implements RoomRepository {

    HashMap<String, Room> roomHashMap = new LinkedHashMap<>();

    @Override
    public void save(Room room) {

        roomHashMap.put(room.getId(), room);
    }

    @Override
    public Optional<Room> findByRoomId(String id) {
        return Optional.ofNullable(roomHashMap.get(id));
    }
}
