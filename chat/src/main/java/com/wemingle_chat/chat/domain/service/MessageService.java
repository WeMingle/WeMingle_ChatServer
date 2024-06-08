package com.wemingle_chat.chat.domain.service;

import com.wemingle_chat.chat.domain.dto.MessageDto;
import com.wemingle_chat.chat.domain.entity.Message;
import com.wemingle_chat.chat.domain.entity.MessageType;
import com.wemingle_chat.chat.domain.entity.OnlineUserData;
import com.wemingle_chat.chat.domain.entity.Room;
import com.wemingle_chat.chat.domain.repository.MessageRepository;
import com.wemingle_chat.chat.domain.repository.OnlineUserRepository;
import com.wemingle_chat.chat.domain.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final RabbitTemplate rabbitTemplate;
    private final OnlineUserRepository onlineUserRepository;
    private final RoomRepository roomRepository;

    public List<Object> getUnreadMessage(String roomId, String memberId) {
        List<Message> unreadMessages = messageRepository.findByUnReadMemberAndRoomId(roomId, memberId);
        unreadMessages.forEach(unreadMessage -> unreadMessage.getUnReadMember().remove(memberId));

        MessageDto.readCntDto readCntDto = generateReadCntDto(unreadMessages);

        rabbitTemplate.convertAndSend("chat.exchange","talk.room"+roomId,readCntDto);

        return unreadMessages.stream().map(message -> {
            if (message.getMessageType().equals(MessageType.TEXT)) {
                return MessageDto.textDto.builder()
                        .message(message.getMessage())
                        .sender(message.getSender())
                        .sendTime(message.getSendTime())
                        .readCnt(message.getCurrentMemberCnt() - message.getUnReadMember().size())
                        .build();
            } else {
                return MessageDto.imageDto.builder()
                        .sender(message.getSender())
                        .sendTime(message.getSendTime())
                        .readCnt(message.getCurrentMemberCnt() - message.getUnReadMember().size())
                        .build();
            }
        }).toList();
    }

    private MessageDto.readCntDto generateReadCntDto(List<Message> readMessageList) {

        Map<Integer, List<Message>> groupedMessages = readMessageList.stream()
                .collect(Collectors.groupingBy(message -> message.getUnReadMember().size()));

//        MessageDto.readCntDto.ReadCntDtoBuilder readCntDtoBuilder = MessageDto.readCntDto.builder();
        List<Map<String, Object>> firstAndLastMessageAtList = new ArrayList<>();

        groupedMessages.forEach((unReadMemberCount, messages) -> {
            messages.sort(Comparator.comparing(Message::getSendTime));
            LocalDateTime firstSendTime = messages.get(0).getSendTime();
            LocalDateTime lastSendTime = messages.get(messages.size() - 1).getSendTime();

            Map<String, Object> unreadCntInfo = new LinkedHashMap<>();
            unreadCntInfo.put("firstMessageAt", firstSendTime);
            unreadCntInfo.put("lastMessageAt", lastSendTime);
            unreadCntInfo.put("unreadCount", unReadMemberCount);

            firstAndLastMessageAtList.add(unreadCntInfo);
        });
        return MessageDto.readCntDto.builder().firstAndLastMessageAtList(firstAndLastMessageAtList).build();
    }

    @Transactional
    public Object saveMessage(String roomId, List<String> onlineUserList, MessageDto.RequestDto message) {
        List<Room.RoomMember> roomMemberList = roomRepository.findByRoomId(roomId).orElseThrow(() -> new NoSuchElementException("메시지를 전송할 방을 찾지 못했습니다.")).getRoomMemberList();

        roomMemberList.removeIf(roomMember -> onlineUserList.contains(roomMember.getMemberId()));
        List<String> unReadMemberList = roomMemberList.stream().map(Room.RoomMember::getMemberId).toList();


        Message newMessage = Message.builder()
                .id(UUID.randomUUID())
                .roomId(roomId)
                .messageType(message.getMessageType())
                .message(message.getMessage())
                .sender(message.getSender())
                .sendTime(message.getSendTime())
                .currentMemberCnt(onlineUserList.size())
                .unReadMember(unReadMemberList)
                .build();
        messageRepository.save(newMessage);
        return newMessage;

    }
}
