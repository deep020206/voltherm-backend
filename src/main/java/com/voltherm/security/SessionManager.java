package com.voltherm.security;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    private static final String CURRENT_ADMIN_SESSION_ID = "CURRENT_ADMIN_SESSION_ID";
    private final ConcurrentHashMap<String, String> adminSessions = new ConcurrentHashMap<>();

    // /**
    //  * Register a new admin session.
    //  * If another admin is already logged in, their session ID is returned (to be invalidated).
    //  * @param sessionId the new session ID
    //  * @param username the admin username
    //  * @return the previous session ID to invalidate, or null if none
    //  */
    public synchronized String registerSession(String sessionId, String username) {
        String previousSessionId = adminSessions.get(CURRENT_ADMIN_SESSION_ID);
        adminSessions.put(CURRENT_ADMIN_SESSION_ID, sessionId);
        adminSessions.put(sessionId, username);
        return previousSessionId;
    }

    /**
     * Unregister an admin session.
     */
    public synchronized void unregisterSession(String sessionId) {
        String currentSessionId = adminSessions.get(CURRENT_ADMIN_SESSION_ID);
        if (currentSessionId != null && currentSessionId.equals(sessionId)) {
            adminSessions.remove(CURRENT_ADMIN_SESSION_ID);
        }
        adminSessions.remove(sessionId);
    }

    /**
     * Get the current admin's session ID.
     */
    public String getCurrentAdminSessionId() {
        return adminSessions.get(CURRENT_ADMIN_SESSION_ID);
    }

    /**
     * Get the username for a session.
     */
    public String getUsernameForSession(String sessionId) {
        return adminSessions.get(sessionId);
    }

    /**
     * Check if a session is the current active admin session.
     */
    public boolean isCurrentAdminSession(String sessionId) {
        String currentSessionId = adminSessions.get(CURRENT_ADMIN_SESSION_ID);
        return currentSessionId != null && currentSessionId.equals(sessionId);
    }
}
