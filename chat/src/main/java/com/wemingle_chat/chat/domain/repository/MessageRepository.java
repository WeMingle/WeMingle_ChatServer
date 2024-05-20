package com.wemingle_chat.chat.domain.repository;

import com.wemingle_chat.chat.domain.entity.Message;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends CassandraRepository<Message, UUID> {

    @AllowFiltering
    List<Message> findBySendTimeAfter(LocalDateTime sendTime);
}
