package com.seungminyi.geera.utill.session;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.servlet.http.HttpSession;

@SpringBootTest
public class HttpSessionManagerTest {

    @InjectMocks
    private HttpSessionManager httpSessionManager;

    @Mock
    private HttpSession httpSession;

    @Test
    void testSetAttribute() {
        String attributeName = "testAttribute";
        String attributeValue = "testValue";

        httpSessionManager.setAttribute(attributeName, attributeValue);

        Mockito.verify(httpSession, Mockito.times(1)).setMaxInactiveInterval(300);
        Mockito.verify(httpSession, Mockito.times(1)).setAttribute(attributeName, attributeValue);
    }

    @Test
    void testGetAttribute() {
        String attributeName = "testAttribute";
        String expectedValue = "testValue";

        when(httpSession.getAttribute(attributeName)).thenReturn(expectedValue);

        Object result = httpSessionManager.getAttribute(attributeName);

        assertEquals(expectedValue, result);
    }
}