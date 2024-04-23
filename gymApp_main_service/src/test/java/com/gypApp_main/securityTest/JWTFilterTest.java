package com.gypApp_main.securityTest;

import com.gypApp_main.security.JWTFilter;
import com.gypApp_main.security.JWTProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
public class JWTFilterTest {

    @Mock
    private JWTProvider jwtProvider;

    @InjectMocks
    private JWTFilter jwtFilter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testDoFilterInternal() throws ServletException, IOException, jakarta.servlet.ServletException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        String jwt = "validToken";
        String username = "testUser";
        UserDetails userDetails = new User(username, "", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

        when(jwtProvider.getUsername(jwt)).thenReturn(username);
        when(jwtProvider.getRoles(jwt)).thenReturn(Arrays.asList("ROLE_USER"));

        // Act
        request.addHeader("Authorization", "Bearer " + jwt);
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());
        assertEquals(1, SecurityContextHolder.getContext().getAuthentication().getAuthorities().size());
        assertTrue(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

}
