package com.rudyenigma.epurchasing.security;

import com.rudyenigma.epurchasing.entity.Admin;
import com.rudyenigma.epurchasing.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSecurity {

    private final AdminService adminService;

    public boolean checkAdmin(Authentication authentication, String adminId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Admin admin = adminService.getById(adminId);
        return userDetails.getUsername().equals(userDetails.getUsername());
    }

}
