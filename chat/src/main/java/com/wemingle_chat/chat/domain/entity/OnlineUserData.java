package com.wemingle_chat.chat.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OnlineUserData {
    private String roomId;
    private String memberId;
    private LocalDateTime onlineStartTime;
}
