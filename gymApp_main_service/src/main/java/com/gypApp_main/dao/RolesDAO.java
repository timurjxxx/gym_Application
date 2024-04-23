package com.gypApp_main.dao;

import com.gypApp_main.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesDAO extends JpaRepository<Roles, Integer> {
    Optional<Roles> findByName(String name);

}
