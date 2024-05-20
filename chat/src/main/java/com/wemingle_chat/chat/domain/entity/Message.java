package com.wemingle_chat.chat.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Table
public class Message {

    @PrimaryKey
    @Column
    private UUID id;
    @Column
    private String roomId;
    @Column
    private String message;
    @Column
    private String sender;
    @Column
    private LocalDateTime sendTime;
    @Column
    private List<String> readMember;
    @Column
    private List<String> picIdList;
    @Column
    private Objects sharedPostData; //todo 글 공유 객체

    @Builder
    public Message(UUID id, String roomId, String message, String sender, LocalDateTime sendTime, List<String> readMember, List<String> picIdList, Objects sharedPostData) {
        this.id = id;
        this.roomId = roomId;
        this.message = message;
        this.sender = sender;
        this.sendTime = sendTime;
        this.readMember = readMember;
        this.picIdList = picIdList;
        this.sharedPostData = sharedPostData;
    }
}
