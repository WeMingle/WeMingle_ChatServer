package com.wemingle_chat.chat.global.config.websocket;

import com.wemingle_chat.chat.domain.repository.OnlineUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomWebSocketHandler extends TextWebSocketHandler {
    private final OnlineUserRepository onlineUserRepository;
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("클라이언트와 정상적으로 연결이 해제되었습니다");
        onlineUserRepository.deleteOnlineUser(session.getId());
    }
}
