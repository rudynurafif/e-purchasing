package com.rudyenigma.epurchasing.entity.constant;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum ERole {

    ROLE_ADMIN,
    ROLE_VENDOR;

    public static ERole get(String value) {
        for (ERole eRole : ERole.values()) {
            if (eRole.name().equalsIgnoreCase(value)) return eRole;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "role not found");
    }

}
