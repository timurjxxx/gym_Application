package com.gypApp_workLoadService.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration
@ComponentScan(basePackages = "com.example.gymAp.service") // Specify the package of your service classes
public class TransactionLoggingAspectTest {

    @InjectMocks
    private TransactionLoggingAspect transactionLoggingAspect;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogTransaction() {
        Object dummyResult = new Object();
        JoinPoint joinPoint = createJoinPointMock(dummyResult);

        transactionLoggingAspect.logTransaction(joinPoint, dummyResult);

    }

    private JoinPoint createJoinPointMock(Object result) {
        JoinPoint joinPoint = mock(JoinPoint.class);
        Signature signature = mock(Signature.class);

        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.toShortString()).thenReturn("mockedMethod");
        when(joinPoint.getTarget()).thenReturn(new Object());
        when(joinPoint.getArgs()).thenReturn(new Object[]{});
        return joinPoint;
    }
}