package com.wemingle_chat.chat.domain.controller;

import com.wemingle_chat.chat.domain.dto.RoomDto;
import com.wemingle_chat.chat.domain.service.OnlineUserService;
import com.wemingle_chat.chat.domain.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final OnlineUserService onlineUserService;

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

    //todo 방에서 아예 나가는 api(그룹 탈퇴 또는 강퇴)

    @MessageMapping("room.online.{roomId}")
    public void onlineUserUpdate(@DestinationVariable("roomId") String roomId,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 SimpMessageHeaderAccessor simpMessageHeaderAccessor) {

        String sessionId = simpMessageHeaderAccessor.getSessionId();
        onlineUserService.saveOnlineUser(sessionId,roomId, userDetails.getUsername());
    }
}
