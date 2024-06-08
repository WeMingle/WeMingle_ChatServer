package com.wemingle_chat.chat.global.config.websocket;

import com.wemingle_chat.chat.domain.repository.OnlineUserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

public class CustomWebSocketHandlerDecorator extends WebSocketHandlerDecorator {
    private final CustomWebSocketHandler customWebSocketHandler;

    public CustomWebSocketHandlerDecorator(WebSocketHandler delegate, CustomWebSocketHandler customWebSocketHandler) {
        super(delegate);
        this.customWebSocketHandler = customWebSocketHandler;
    }



    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        super.afterConnectionClosed(session, closeStatus);
        customWebSocketHandler.afterConnectionClosed(session, closeStatus);
    }
}
