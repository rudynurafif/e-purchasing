package com.rudyenigma.epurchasing.service.impl;

import com.rudyenigma.epurchasing.entity.Role;
import com.rudyenigma.epurchasing.entity.constant.ERole;
import com.rudyenigma.epurchasing.repository.RoleRepository;
import com.rudyenigma.epurchasing.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getOrSave(ERole role) {
        return roleRepository.findByRole(role).orElseGet(() -> roleRepository.save(Role.builder().role(role).build()));
    }
}
