package com.gypApp_main.serviceTest;

import com.gypApp_main.service.LoginAttemptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginAttemptServiceTest {

    private LoginAttemptService loginAttemptService;

    @BeforeEach
    public void setUp() {
        loginAttemptService = new LoginAttemptService();
    }

    @Test
    public void testLoginFailed() {
        String username = "testUser";

        // Perform 2 failed login attempts
        loginAttemptService.loginFailed(username);
        loginAttemptService.loginFailed(username);

        // Check if user is not blocked yet
        assertFalse(loginAttemptService.isBlocked(username));

        // Perform 3rd failed login attempt
        loginAttemptService.loginFailed(username);

        // Check if user is blocked
        assertTrue(loginAttemptService.isBlocked(username));
    }

    @Test
    public void testLoginSucceeded() {
        String username = "testUser";

        // Perform failed login attempts
        loginAttemptService.loginFailed(username);
        loginAttemptService.loginFailed(username);
        loginAttemptService.loginFailed(username);

        // Check if user is blocked
        assertTrue(loginAttemptService.isBlocked(username));

        // Simulate successful login
        loginAttemptService.loginSucceeded(username);

        // Check if user is unblocked
        assertFalse(loginAttemptService.isBlocked(username));
    }

    @Test
    public void testIsBlocked() throws InterruptedException {
        String username = "testUser";

        // Perform failed login attempts
        loginAttemptService.loginFailed(username);
        loginAttemptService.loginFailed(username);
        loginAttemptService.loginFailed(username);

        // Check if user is blocked
        assertTrue(loginAttemptService.isBlocked(username));

        // Wait for the block duration to expire
        TimeUnit.MILLISECONDS.sleep(10000);

        // Check if user is unblocked after the block duration expires
        assertFalse(loginAttemptService.isBlocked(username));
    }
}
