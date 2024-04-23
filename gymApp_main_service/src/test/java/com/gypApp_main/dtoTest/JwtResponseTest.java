package com.gypApp_main.dtoTest;

import com.gypApp_main.dto.JwtResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class JwtResponseTest {

    @Test
    public void testJwtResponseGetter() {
        String token = "exampleToken";
        JwtResponse response = new JwtResponse(token);

        assertEquals(token, response.getToken());
    }

    @Test
    public void testJwtResponseSetter() {
        String token = "exampleToken";
        JwtResponse response = new JwtResponse(token);

        response.setToken(token);

        assertEquals(token, response.getToken());
    }

    @Test
    public void testJwtResponseEqualsAndHashCode() {
        JwtResponse response1 = new JwtResponse("token");
        JwtResponse response2 = new JwtResponse("token");
        JwtResponse response3 = new JwtResponse("differentToken");

        assertEquals(response1, response2);
        assertNotEquals(response1, response3);

        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    public void testJwtResponseToString() {
        String token = "exampleToken";
        JwtResponse response = new JwtResponse(token);

        assertEquals("JwtResponse(token=" + token + ")", response.toString());
    }
}
