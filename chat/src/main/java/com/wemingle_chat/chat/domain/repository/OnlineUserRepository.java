package com.wemingle_chat.chat.domain.repository;

import com.wemingle_chat.chat.domain.entity.OnlineUserData;

import java.util.List;

public interface OnlineUserRepository {
    void saveOnlineUser(String sessionId, OnlineUserData onlineUserData);

    void deleteOnlineUser(String sessionId);

    List<String> findOnlineUserListByRoomId(String roomId);



//    List<OnlineUserData> findOnlineUserByRoomId(String roomId);


}
