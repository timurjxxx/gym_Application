package com.gypApp_main.securityTest;

import com.gypApp_main.security.JWTProvider;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class JWTProviderTest {

    @Mock
    private Claims claims;


    @InjectMocks
    private JWTProvider jwtProvider;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        // Set the value of the ACCESS_SECRET field using reflection
        Field field = JWTProvider.class.getDeclaredField("ACCESS_SECRET");
        field.setAccessible(true);
        field.set(jwtProvider, "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
    }
    @Test
    public void testGenerateToken() {
        // Arrange
        UserDetails userDetails = new User("username", "password", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

        // Act
        String token = jwtProvider.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    public void testInvalidateToken() {
        // Arrange
        String token = "validToken";
        JWTProvider jwtProviderMock = mock(JWTProvider.class);

        // Act
        jwtProviderMock.invalidateToken(token);

        // Assert
        verify(jwtProviderMock, times(1)).invalidateToken(token);
    }


}
