package com.gypApp_main.service;

import com.gypApp_main.dao.RolesDAO;
import com.gypApp_main.model.Roles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {
    private final RolesDAO roleRepository;

    public Roles getUserRole() {
        log.info("Get roles by name");
        return roleRepository.findByName("ROLE_USER").get();
    }
}