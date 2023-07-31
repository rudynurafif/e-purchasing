package com.rudyenigma.epurchasing.service;

import com.rudyenigma.epurchasing.entity.Role;
import com.rudyenigma.epurchasing.entity.constant.ERole;

public interface RoleService {

    Role getOrSave(ERole role);

}
