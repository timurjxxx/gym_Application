package com.gypApp_main.dao;

import com.gypApp_main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserDAO  extends JpaRepository<User, Long> {


    @Transactional(readOnly = true)
    Optional<User> findUserByUserName(String username);

}