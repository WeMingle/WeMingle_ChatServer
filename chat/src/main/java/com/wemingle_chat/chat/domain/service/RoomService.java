package com.wemingle_chat.chat.domain.service;

import com.wemingle_chat.chat.domain.dto.RoomDto;
import com.wemingle_chat.chat.domain.entity.Room;
import com.wemingle_chat.chat.domain.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    public void createChatRoom(RoomDto.createRoom createRoom) {
        Room room = Room.builder()
                .id(createRoom.getRoomId())
                .roomMemberList(
                        List.of(
                                Room.RoomMember.builder()
                                        .memberId(createRoom.getMasterId())
                                        .joinedAt(LocalDateTime.now())
                                        .lastReadTime(LocalDateTime.now())
                                        .build()
                        )
                ).build();
        roomRepository.save(room);
    }

    public void enterChatRoom(RoomDto.enterRoom enterRoom) {
        Room room = roomRepository.findByRoomId(enterRoom.getRoomId()).orElseThrow(() -> new NoSuchElementException("NOT FOUND CHAT ROOM BY ROOM ID"));
        room.getRoomMemberList().add(
                Room.RoomMember.builder()
                        .memberId(enterRoom.getMemberId())
                        .joinedAt(LocalDateTime.now())
                        .lastReadTime(LocalDateTime.now())
                        .build()
        );
        roomRepository.save(room);
    }

    public void leaveChatRoom() {

    }
}
