package com.wemingle_chat.chat.domain.entity.role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    USER("ROLE_USER"),
    UNVERIFIED_USER("ROLE_UNVERIFIED_USER"),
    ADMIN("ROLE_ADMIN");

    private final String roleName;
}
