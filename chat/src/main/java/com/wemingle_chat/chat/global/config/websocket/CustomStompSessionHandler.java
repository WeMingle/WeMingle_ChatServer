package com.wemingle_chat.chat.global.config.websocket;

import com.wemingle_chat.chat.domain.repository.OnlineUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomStompSessionHandler extends StompSessionHandlerAdapter {
    private final OnlineUserRepository onlineUserRepository;
    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.info("클라이언트와 비정상적으로 연결이 해제되었습니다");
        onlineUserRepository.deleteOnlineUser(session.getSessionId());
    }


}
