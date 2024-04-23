package com.gypApp_main.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 3;
    private static final long BLOCK_DURATION = 10 * 1000; // 5 minutes in milliseconds

    private final Map<String, Integer> attemptsCache = new HashMap<>();
    private final Map<String, Long> blockList = new HashMap<>();

    public void loginFailed(String username) {
        attemptsCache.put(username, attemptsCache.getOrDefault(username, 0) + 1);
        if (attemptsCache.getOrDefault(username, 0) >= MAX_ATTEMPTS) {
            blockList.put(username, System.currentTimeMillis() + BLOCK_DURATION);
            log.warn("User {} is blocked due to too many failed login attempts", username);
        } else {
            log.debug("Failed login attempt for user {}", username);
        }
    }

    public boolean isBlocked(String username) {
        if (blockList.containsKey(username) && blockList.get(username) > System.currentTimeMillis()) {
            log.info("User {} is blocked", username);
            return true;
        }
        return false;
    }

    public void loginSucceeded(String username) {
        attemptsCache.remove(username);
        blockList.remove(username);
        log.info("Login succeeded for user {}", username);
    }
}
