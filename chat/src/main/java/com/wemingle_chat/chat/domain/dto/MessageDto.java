package com.wemingle_chat.chat.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public class MessageDto {
    @Getter
    @Setter
    public static class RequestDto{
        String message;
        String sender;
        LocalDateTime sendTime;
        List<MultipartFile> picList;
    }

    @Getter
    @Setter
    public static class ResponseDto{
        String message;
        String sender;
        LocalDateTime sendTime;
        int writeCnt;
        List<MultipartFile> picList;
    }

    @Getter
    @Setter
    public static class readCntDto{
        List<Map<String,Object>> firstAndLastMessageAtList;
    }

}
