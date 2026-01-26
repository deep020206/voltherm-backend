package com.voltherm.security;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Component
public class SessionInvalidationListener implements HttpSessionListener {

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String sessionId = se.getSession().getId();
        WebApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(se.getSession().getServletContext());
        if (context != null) {
            SessionManager sessionManager = context.getBean(SessionManager.class);
            sessionManager.unregisterSession(sessionId);
        }
    }
}
