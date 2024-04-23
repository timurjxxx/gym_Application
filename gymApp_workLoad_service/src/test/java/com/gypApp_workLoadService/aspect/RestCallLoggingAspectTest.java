package com.gypApp_workLoadService.aspect;


import com.gypApp_workLoadService.controller.TrainerWorkloadController;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestCallLoggingAspectTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private JoinPoint joinPoint;

    @Mock
    private ServletRequestAttributes servletRequestAttributes;

    @InjectMocks
    private RestCallLoggingAspect restCallLoggingAspect;

    @Mock
    private TrainerWorkloadController controller; // Assuming you have a controller to test
    private static Logger LOGGER = LoggerFactory.getLogger(RestCallLoggingAspect.class);

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExtractResponseDetailsWithResponseEntity() {
        RestCallLoggingAspect aspect = new RestCallLoggingAspect();

        ResponseEntity<String> mockResponseEntity = new ResponseEntity<>("Mock Response Body", HttpStatus.OK);

        String result = aspect.extractResponseDetails(mockResponseEntity);

        assertEquals("Status: 200, Response Body: Mock Response Body", result);
    }

    @Test
    public void testExtractResponseDetailsWithInvalidResponseType() {
        RestCallLoggingAspect aspect = new RestCallLoggingAspect();

        Object mockInvalidResponse = "Invalid Response";

        String result = aspect.extractResponseDetails(mockInvalidResponse);

        assertEquals("Invalid response type", result);
    }

    @Test
    public void testExtractRequestParameters() {
        RestCallLoggingAspect aspect = new RestCallLoggingAspect();

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addParameter("param1", "value1");
        mockRequest.addParameter("param2", "value2");

        String result = aspect.extractRequestParameters(mockRequest);

        assertEquals("param1=value1; param2=value2; ", result);
    }

    @Test
    public void testLogRestCallResult() {
        Object result = "Sample Result";

        restCallLoggingAspect.logRestCallResult(result);

    }



}