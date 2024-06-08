package com.wemingle_chat.chat.domain.repository;

import com.wemingle_chat.chat.domain.entity.OnlineUserData;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class OnlineUserRepositoryImpl implements OnlineUserRepository{
    Map<String, OnlineUserData> onlineUserBySessionId = new HashMap<>();
    Map<String,List<String>> onlineUserByRoomId = new HashMap<>(); //roomId : List<memberId>

    @Override
    public void saveOnlineUser(String sessionId, OnlineUserData onlineUserData) {
        onlineUserBySessionId.put(sessionId, onlineUserData);
        List<String> onlineUserList = findOnlineUserByRoomId(onlineUserData.getRoomId());

        if (Objects.isNull(onlineUserList)) {
            ArrayList<String> memberList = new ArrayList<>();
            memberList.add(onlineUserData.getMemberId());
            onlineUserByRoomId.put(onlineUserData.getRoomId(), memberList);
        }
        onlineUserList.add(onlineUserData.getMemberId());
        onlineUserByRoomId.put(onlineUserData.getRoomId(), onlineUserList);
    }

    @Override
    public void deleteOnlineUser(String sessionId) {
        OnlineUserData removed = deleteOnlineUserBySessionId(sessionId);
        deleteOnlineUserByRoomId(removed.getRoomId());
    }

    @Override
    public List<String> findOnlineUserListByRoomId(String roomId) {
        List<String> onlineUserList = findOnlineUserByRoomId(roomId);
        if (Objects.isNull(onlineUserList)) {
            return List.of();
        }
        return onlineUserList;
    }

    private OnlineUserData findOnlineUserBySessionId(String sessionId) {
        return onlineUserBySessionId.get(sessionId);
    }

    private OnlineUserData deleteOnlineUserBySessionId(String sessionId) {
        return onlineUserBySessionId.remove(sessionId);
    }

    private List<String> findOnlineUserByRoomId(String roomId) {
        return onlineUserByRoomId.get(roomId);
    }

    private void deleteOnlineUserByRoomId(String roomId) {
        List<String> memberList = findOnlineUserByRoomId(roomId);
        memberList.remove(roomId);
        if (memberList.isEmpty()) {
            onlineUserByRoomId.remove(roomId);
        }

    }
}
