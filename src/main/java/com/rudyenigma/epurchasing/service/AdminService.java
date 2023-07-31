package com.rudyenigma.epurchasing.service;

import com.rudyenigma.epurchasing.entity.Admin;

public interface AdminService {

    Admin create(Admin admin);
    Admin getById(String id);
    Admin update(Admin admin);

}
