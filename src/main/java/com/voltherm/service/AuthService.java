package com.voltherm.service;

import com.voltherm.exception.UnauthorizedException;
import com.voltherm.security.AdminPrincipal;
import com.voltherm.security.AdminUser;
import com.voltherm.security.SessionManager;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private SessionManager sessionManager;
    
    @Value("${admin.username:admin}")
    private String adminUsername;
    
    @Value("${admin.password:admin123}")
    private String adminPassword;

    /**
     * Authenticate admin and create a session.
     * If another admin is logged in, their session is invalidated.
     */
    public void login(String username, String password, HttpSession session) {
        if (!adminUsername.equals(username) || !adminPassword.equals(password)) {
            throw new UnauthorizedException("Invalid credentials");
        }
        
        // Register this session and get any previous session to invalidate
        String previousSessionId = sessionManager.registerSession(session.getId(), username);
        
        // Set authentication in security context
        AdminPrincipal principal = new AdminPrincipal(username);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(principal);
        SecurityContextHolder.setContext(context);
        
        // Store in session
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
        
        // Invalidate previous session if it exists
        if (previousSessionId != null && !previousSessionId.equals(session.getId())) {
            // Previous admin will be kicked out on their next request or when session times out
            sessionManager.unregisterSession(previousSessionId);
        }
    }

    /**
     * Logout admin by clearing session and security context.
     */
    public void logout(HttpSession session) {
        String sessionId = session.getId();
        sessionManager.unregisterSession(sessionId);
        SecurityContextHolder.clearContext();
        session.invalidate();
    }

    /**
     * Get profile of authenticated admin.
     */
    public AdminUser getProfile(String username) {
        return new AdminUser(username);
    }
}
