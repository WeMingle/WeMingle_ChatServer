package com.wemingle_chat.chat.domain.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Table
public class Room {

    @PrimaryKey
    @Column
    private String id;
    @Column
    private List<RoomMember> roomMemberList;

    public Room() {
    }

    @Builder
    public Room(String id, List<RoomMember> roomMemberList) {
        this.id = id;
        this.roomMemberList = roomMemberList;
    }

    @Getter
    @Setter
    public static class RoomMember {
        private String memberId;
        private LocalDateTime joinedAt;
        private LocalDateTime lastReadTime;

        public RoomMember() {
        }

        @Builder
        public RoomMember(String memberId, LocalDateTime joinedAt, LocalDateTime lastReadTime) {
            this.memberId = memberId;
            this.joinedAt = joinedAt;
            this.lastReadTime = lastReadTime;
        }
    }
}