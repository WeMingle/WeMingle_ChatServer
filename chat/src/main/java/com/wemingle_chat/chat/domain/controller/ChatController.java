package com.wemingle_chat.chat.domain.controller;

import com.wemingle_chat.chat.domain.dto.MessageDto;
import com.wemingle_chat.chat.domain.dto.RoomDto;
import com.wemingle_chat.chat.domain.service.MessageService;
import com.wemingle_chat.chat.domain.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {
    private final MessageService messageService;
    private final RabbitTemplate rabbitTemplate;



    @MessageMapping("chat.enter.{roomId}")
    public void enterUser(@DestinationVariable("roomId") Long roomId, @Payload MessageDto.RequestDto message) {

        log.info(roomId+" : roomid");
        log.info(message.getMessage()+" : " + message.getSender());
        message.setMessage(message.getSender() + "님이 채팅방에 입장하였습니다.");
        rabbitTemplate.convertAndSend("chat.exchange", "enter.room." + roomId, message);
//        return messageService.saveMessage(message);
    }



    @MessageMapping("chat.talk.{roomId}")
    public void talkUser(@DestinationVariable("roomId") Long roomId, @Payload MessageDto.RequestDto message) {
        log.info(roomId+" : roomid");
        log.info(message.getMessage()+" : " + message.getSender());
        rabbitTemplate.convertAndSend("chat.exchange", "talk.room." + roomId, message);
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
