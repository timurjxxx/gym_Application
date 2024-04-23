package com.gypApp_main.configTest;

import com.gypApp_main.config.SecurityConfig;
import com.gypApp_main.security.JWTFilter;
import com.gypApp_main.security.JWTProvider;
import com.gypApp_main.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SecurityConfigTest {

    @Mock
    private UserService userService;

    @Mock
    private JWTFilter filter;
    @Mock
    private JWTProvider provider;

    @InjectMocks
    private SecurityConfig securityConfig;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(securityConfig).build();
    }





    @Test
    public void testPasswordEncoder() {
        BCryptPasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        assertNotNull(passwordEncoder);

        String encodedPassword = passwordEncoder.encode("password");
        assertTrue(passwordEncoder.matches("password", encodedPassword));
    }



}
