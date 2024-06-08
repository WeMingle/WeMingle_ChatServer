package com.wemingle_chat.chat.domain.dto;

import com.wemingle_chat.chat.domain.entity.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class MessageDto {
    @Getter
    @Setter
    public static class RequestDto{
        MessageType messageType;
        String message;
        String sender;
        LocalDateTime sendTime;
        List<UUID> picIdList;
    }

    @Getter
    @Setter
    public static class textDto {
        String message;
        String sender;
        LocalDateTime sendTime;
        int readCnt;

        @Builder
        public textDto(String message, String sender, LocalDateTime sendTime, int readCnt) {
            this.message = message;
            this.sender = sender;
            this.sendTime = sendTime;
            this.readCnt = readCnt;
        }
    }

    @Getter
    @Setter
    public static class imageDto {
        String sender;
        LocalDateTime sendTime;
        int readCnt;
        List<String> picList;

        @Builder
        public imageDto(String sender, LocalDateTime sendTime, int readCnt, List<String> picList) {
            this.sender = sender;
            this.sendTime = sendTime;
            this.readCnt = readCnt;
            this.picList = picList;
        }
    }

    @Getter
    @Setter
    public static class readCntDto{
        List<Map<String,Object>> firstAndLastMessageAtList;

        @Builder
        public readCntDto(List<Map<String, Object>> firstAndLastMessageAtList) {
            this.firstAndLastMessageAtList = firstAndLastMessageAtList;
        }
    }

}
