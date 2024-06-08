package com.wemingle_chat.chat.domain.controller;

import com.wemingle_chat.chat.domain.dto.MessageDto;
import com.wemingle_chat.chat.domain.entity.MessageType;
import com.wemingle_chat.chat.domain.service.MessageService;
import com.wemingle_chat.chat.domain.service.OnlineUserService;
import com.wemingle_chat.chat.domain.service.S3ImgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {
    private final MessageService messageService;
    private final OnlineUserService onlineUserService;
    private final RabbitTemplate rabbitTemplate;
    private final S3ImgService s3ImgService;

    @ResponseBody
    @GetMapping("/rooms/{roomId}/unread-message")
    public ResponseEntity<List<Object>> getUnreadMessages(@PathVariable("roomId") String roomId, @AuthenticationPrincipal UserDetails userDetails) {
        List<Object> unreadMessage = messageService.getUnreadMessage(roomId, userDetails.getUsername());
        return ResponseEntity.ok(unreadMessage);
    }



//    @MessageMapping("chat.enter.{roomId}")
//    public void enterUser(@DestinationVariable("roomId") Long roomId, @Payload MessageDto.RequestDto message) {
//
//        log.info(roomId+" : roomid");
//        log.info(message.getMessage()+" : " + message.getSender());
//        message.setMessage(message.getSender() + "님이 채팅방에 입장하였습니다.");
//        rabbitTemplate.convertAndSend("chat.exchange", "enter.room." + roomId, message);
////        return messageService.saveMessage(message);
//    }



    @MessageMapping("chat.talk.{roomId}")
    public void talkUser(@DestinationVariable("roomId") Long roomId, @Payload MessageDto.RequestDto message) {
        log.info(roomId+" : roomid");
        log.info(message.getMessage()+" : " + message.getSender());
        //todo if(message.getMessageType.equals(MessageType.IMAGE.toString)){ presignedUrl insurance logic -> message content에 url 전송
        List<String> onlineUserListByRoomId = onlineUserService.findOnlineUserListByRoomId(roomId.toString());
        if (message.getMessageType().equals(MessageType.IMAGE)) {
            List<String> groupChatPicsDownloadUrl = s3ImgService.getGroupChatPicsDownloadUrl(message.getPicIdList());


            MessageDto.imageDto imageDto = MessageDto.imageDto.builder()
                    .sender(message.getSender())
                    .sendTime(message.getSendTime())
                    .readCnt(onlineUserListByRoomId.size())
                    .picList(groupChatPicsDownloadUrl)
                    .build();

            rabbitTemplate.convertAndSend("chat.exchange", "talk.room." + roomId, imageDto);
        }
        MessageDto.textDto textDto = MessageDto.textDto.builder()
                .message(message.getMessage())
                .sender(message.getSender())
                .sendTime(message.getSendTime())
                .readCnt(onlineUserListByRoomId.size())
                .build();

        rabbitTemplate.convertAndSend("chat.exchange", "talk.room." + roomId, textDto);
        messageService.saveMessage(roomId.toString(), onlineUserListByRoomId, message);
//        return messageService.saveMessage(message);
    }

    @MessageMapping("chat.exit.{roomId}")
    public void exitUser(@DestinationVariable("roomId") Long roomId, @Payload MessageDto.RequestDto message){
        log.info(roomId+" : roomid");
        log.info(message.getMessage()+" : " + message.getSender());
        message.setMessage(message.getSender() + "님이 채팅방에 퇴장하였습니다.");
        rabbitTemplate.convertAndSend("chat.exchange", "exit.room." + roomId, message);
//        return messageService.saveMessage(message);
    }
}
