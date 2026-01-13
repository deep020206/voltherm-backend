package com.voltherm.security;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionInvalidationListener implements HttpSessionListener {
    
    @Autowired
    private SessionManager sessionManager;

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String sessionId = se.getSession().getId();
        sessionManager.unregisterSession(sessionId);
    }
}
