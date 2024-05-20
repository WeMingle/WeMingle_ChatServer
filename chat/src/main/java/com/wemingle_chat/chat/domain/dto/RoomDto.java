package com.wemingle_chat.chat.domain.dto;

import lombok.Getter;
import lombok.Setter;

public class RoomDto {
    @Getter
    @Setter
    public static class createRoom{
        private String roomId;
        private String masterId;
    }

    @Getter
    @Setter
    public static class enterRoom{
        private String roomId;
        private String memberId;
    }

    @Getter
    @Setter
    public static class leaveRoom{
        private String roomId;
        private String memberId;
    }
}
