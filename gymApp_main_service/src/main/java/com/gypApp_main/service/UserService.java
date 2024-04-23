package com.gypApp_main.service;

import com.gypApp_main.dao.UserDAO;
import com.gypApp_main.exception.UserNotFoundException;
import com.gypApp_main.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserDAO userDAO;
    private final RoleService roleService;

    @Value("${app.user.password.chars}")
    private String passwordChars;
    @Value("${app.user.password.length}")
    private int passwordLength;

    @Transactional(readOnly = true)
    public User findUserByUserName(String username) {
        log.info("Finding user by username: {}", username);
        log.debug("Base username: {}", username);

        return userDAO.findUserByUserName(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + "is not found "));
    }

    @Transactional
    public User createUser(@Valid User newUser) {
        newUser.setRoles(List.of(roleService.getUserRole()));
        newUser.setUserName(generateUsername(newUser.getFirstName() + "." + newUser.getLastName()));
        log.info("Creating new user: {}", newUser.getUserName());
        log.debug("User details {}", newUser);
        return userDAO.save(newUser);
    }

    @Transactional
    public User updateUser(@NotNull String userName, @Valid User updatedUser) {
        User user = userDAO.findUserByUserName(userName).orElseThrow(() -> new UserNotFoundException("User with username " + userName + "is not found "));
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setIsActive(updatedUser.getIsActive());
        log.info("Updating user with username: {}", userName);
        log.debug("UpdatedUser details {}" , user);
        return userDAO.save(user);
    }

    @Transactional
    public void delete(Long id) {
        userDAO.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + "is not found "));
        log.info("Deleting user with ID: {}", id);
        userDAO.deleteById(id);
    }

    @Transactional
    public String changePassword(@NotBlank String username, @NotBlank String newPassword) {
        log.info("Changing password for user: {}", username);

        User user = userDAO.findUserByUserName(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + "is not found "));
        user.setPassword(newPassword);
        userDAO.save(user);
        return newPassword;
    }

    @Transactional
    public boolean changeStatus(@NotBlank String username) {
        log.info("Deactivating user: {}", username);

        User user = userDAO.findUserByUserName(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + "is not found "));
        user.setIsActive(!user.getIsActive());
        userDAO.save(user);
        return user.getIsActive();
    }

    public String generateUsername(String username1) {
        log.info("Generating username");
        log.debug("Base username: {}", username1);

        return IntStream.iterate(1, i -> i + 1)
                .mapToObj(serialNumber -> username1 + ((serialNumber == 1) ? "" : "." + serialNumber))
                .filter(username -> !userDAO.findUserByUserName(username).isPresent())
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public String generatePassword() {
        Random random = new Random();
        String password = IntStream.range(0, passwordLength)
                .mapToObj(i -> passwordChars.charAt(random.nextInt(passwordChars.length())))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();

        log.info("Generating unique password");
        log.debug("Generated password: {}", password);

        return password;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUserName(username);
        log.info("Load user from database by username{}", username);
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())

        );
    }
}
