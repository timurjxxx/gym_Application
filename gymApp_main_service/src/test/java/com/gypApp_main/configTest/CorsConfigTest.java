package com.gypApp_main.configTest;

import com.gypApp_main.config.CorsConfig;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CorsConfigTest {

    @Test
    void testCorsConfigurationSource() {
        // Create an instance of CorsConfig to get CorsConfigurationSource
        CorsConfig corsConfig = new CorsConfig();
        CorsConfigurationSource configurationSource = corsConfig.corsConfigurationSource();

        // Mock HttpServletRequest with the desired URL path
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/test");

        // Get CorsConfiguration for the mock request
        CorsConfiguration configuration = configurationSource.getCorsConfiguration(request);

        // Assertions
        assertTrue(configuration.getAllowedOrigins().contains("*"));
        List<String> expectedMethods = Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS");
        assertEquals(expectedMethods, configuration.getAllowedMethods());
        assertTrue(configuration.getAllowedHeaders().contains("*"));
        assertTrue(configuration.getAllowCredentials());
    }
}
