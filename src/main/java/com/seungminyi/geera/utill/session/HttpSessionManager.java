package com.seungminyi.geera.utill.session;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Enumeration;

@Component
public class HttpSessionManager implements SessionManager {

    @Autowired
    private HttpSession httpSession;


    @Override
    public void setAttribute(String attributeName, Object attributeValue) {
        httpSession.setMaxInactiveInterval(300);
        httpSession.setAttribute(attributeName, attributeValue);
    }

    @Override
    public Object getAttribute(String attributeName) {
        return httpSession.getAttribute(attributeName);
    }
}
