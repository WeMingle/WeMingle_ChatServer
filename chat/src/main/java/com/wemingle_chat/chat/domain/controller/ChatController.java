package com.wemingle_chat.chat.domain.controller;

import com.wemingle_chat.chat.domain.dto.MessageDto;
import jakarta.security.auth.message.MessageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final MessageService messageService;
    private final RabbitTemplate rabbitTemplate;

    // 채팅방 입장
    @MessageMapping("chat.enter.{roomId}")
    public void enterUser(@DestinationVariable("roomId") Long roomId, @Payload MessageDto message) {
        message.setMessage(message.getSender() + "님이 채팅방에 입장하였습니다.");
        rabbitTemplate.convertAndSend("chat.exchange", "enter.room." + roomId, message);
//        return messageService.saveMessage(message);
    }

    // 채팅방 대화
    @MessageMapping("chat.talk.{roomId}")
    public void talkUser(@DestinationVariable("roomId") Long roomId, @Payload MessageDto message) {
        rabbitTemplate.convertAndSend("chat.exchange", "*.room." + roomId, message);
//        return messageService.saveMessage(message);
    }

    // 채팅방 퇴장
    @MessageMapping("chat.exit.{roomId}")
    public void exitUser(@DestinationVariable("roomId") Long roomId, @Payload MessageDto message){
        message.setMessage(message.getSender() + "님이 채팅방에 퇴장하였습니다.");
        rabbitTemplate.convertAndSend("chat.exchange", "exit.room." + roomId, message);
//        return messageService.saveMessage(message);
    }
}
