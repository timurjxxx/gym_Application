package com.gypApp_main.dtoTest;

import com.gypApp_main.dto.ChangeLoginRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ChangeLoginRequestTest {

    @Test
    public void testChangeLoginRequest() {
        String username = "username";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        ChangeLoginRequest request = new ChangeLoginRequest(username, oldPassword, newPassword);

        assertEquals(username, request.getUsername());
        assertEquals(oldPassword, request.getOldPassword());
        assertEquals(newPassword, request.getNewPassword());

        String newUsername = "newUsername";
        String newOldPassword = "newOldPassword";
        String newNewPassword = "newNewPassword";

        request.setUsername(newUsername);
        request.setOldPassword(newOldPassword);
        request.setNewPassword(newNewPassword);

        assertEquals(newUsername, request.getUsername());
        assertEquals(newOldPassword, request.getOldPassword());
        assertEquals(newNewPassword, request.getNewPassword());
    }

    @Test
    public void testChangeLoginRequestEqualsAndHashCode() {
        ChangeLoginRequest request1 = new ChangeLoginRequest("username", "oldPassword", "newPassword");
        ChangeLoginRequest request2 = new ChangeLoginRequest("username", "oldPassword", "newPassword");
        ChangeLoginRequest request3 = new ChangeLoginRequest("differentUsername", "oldPassword", "newPassword");

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    public void testChangeLoginRequestToString() {
        String username = "username";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        ChangeLoginRequest request = new ChangeLoginRequest(username, oldPassword, newPassword);

        assertEquals("ChangeLoginRequest(username=" + username + ", oldPassword=" + oldPassword + ", newPassword=" + newPassword + ")", request.toString());
    }
}
