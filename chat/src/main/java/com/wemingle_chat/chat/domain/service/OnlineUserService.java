package com.wemingle_chat.chat.domain.service;

import com.wemingle_chat.chat.domain.dto.MessageDto;
import com.wemingle_chat.chat.domain.entity.Message;
import com.wemingle_chat.chat.domain.entity.OnlineUserData;
import com.wemingle_chat.chat.domain.repository.OnlineUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OnlineUserService {
    private final OnlineUserRepository onlineUserRepository;

    public void saveOnlineUser(String sessionId, String roomId, String memberId) {
        OnlineUserData onlineUserData = OnlineUserData.builder()
                .memberId(memberId)
                .roomId(roomId)
                .onlineStartTime(LocalDateTime.now())
                .build();
        onlineUserRepository.saveOnlineUser(sessionId,onlineUserData);
    }

    public List<String> findOnlineUserListByRoomId(String roomId) {
        return onlineUserRepository.findOnlineUserListByRoomId(roomId);
    }
//    public MessageDto.imageDto updateReadCntForGroupChatPic()
}
