package com.wemingle_chat.chat.domain.controller;

import com.wemingle_chat.chat.domain.dto.RoomDto;
import com.wemingle_chat.chat.domain.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/room")
    public ResponseEntity<Object> createChatRoom(@RequestBody RoomDto.createRoom createRoom) {
        log.info(createRoom.getRoomId());
        roomService.createChatRoom(createRoom);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/room/enter")
    public ResponseEntity<Object> createChatRoom(@RequestBody RoomDto.enterRoom enterRoom) {
        log.info(enterRoom.getRoomId());
        roomService.enterChatRoom(enterRoom);
        return ResponseEntity.ok().build();
    }
}
