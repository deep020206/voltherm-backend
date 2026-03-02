package com.voltherm.service;

import com.voltherm.exception.UnauthorizedException;
import com.voltherm.exception.ValidationException;
import com.voltherm.model.PasswordChangeRequest;
import com.voltherm.model.UsernameChangeRequest;
import com.voltherm.model.OtpVerificationRequest;
import com.voltherm.security.AdminPrincipal;
import com.voltherm.security.AdminUser;
import com.voltherm.security.SessionManager;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
public class AuthService {
    
    private final SessionManager sessionManager;
    private final OtpStorage otpStorage;
    private final EmailService emailService;
    private final EmailConfigService emailConfigService;
    
    @Value("${admin.username:admin}")
    private String adminUsername;
    
    @Value("${admin.password:admin123}")
    private String adminPassword;

    public AuthService(SessionManager sessionManager, OtpStorage otpStorage, EmailService emailService, EmailConfigService emailConfigService) {
        this.sessionManager = sessionManager;
        this.otpStorage = otpStorage;
        this.emailService = emailService;
        this.emailConfigService = emailConfigService;
    }

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

    public String initiatePasswordChange(PasswordChangeRequest request, String currentUsername) {
        // Validate current password
        if (!adminPassword.equals(request.getCurrentPassword())) {
            throw new UnauthorizedException("Current password is incorrect");
        }

        // Validate new password strength
        validatePasswordStrength(request.getNewPassword());

        // Ensure email is configured before trying to send
        if (!emailService.isEmailConfigured()) {
            throw new ValidationException(
                "Email service is not configured. Please set up sender and receiver email addresses in Admin Settings before using OTP-based password change.");
        }

        // Generate 6-digit OTP
        String otp = generateOtp();

        // Store OTP with 5 minutes expiration
        Instant expirationTime = Instant.now().plus(5, ChronoUnit.MINUTES);
        otpStorage.storeOtp(currentUsername, otp, expirationTime);

        // Send OTP via email
        emailService.sendOtpEmail(currentUsername, otp);

        return "OTP has been sent to your email. It will expire in 5 minutes.";
    }

    public String verifyOtpAndChangePassword(OtpVerificationRequest request, String currentUsername) {
        // Retrieve stored OTP
        OtpStorage.OtpData otpData = otpStorage.getOtp(currentUsername);

        if (otpData == null) {
            throw new ValidationException("No OTP found. Please initiate password change first.");
        }

        // Check if OTP is expired
        if (otpData.isExpired()) {
            otpStorage.removeOtp(currentUsername);
            throw new ValidationException("OTP has expired. Please request a new one.");
        }

        // Validate OTP
        if (!otpData.getOtp().equals(request.getOtp())) {
            throw new ValidationException("Invalid OTP");
        }

        // Validate new password strength
        validatePasswordStrength(request.getNewPassword());

        // Update password
        adminPassword = request.getNewPassword();

        // Clear OTP
        otpStorage.removeOtp(currentUsername);

        return "Password changed successfully";
    }

    public String changeUsername(UsernameChangeRequest request, String currentUsername) {
        // Validate current password
        if (!adminPassword.equals(request.getCurrentPassword())) {
            throw new UnauthorizedException("Current password is incorrect");
        }

        // Validate new username
        if (request.getNewUsername() == null || request.getNewUsername().trim().isEmpty()) {
            throw new ValidationException("New username cannot be empty");
        }

        if (request.getNewUsername().length() < 3) {
            throw new ValidationException("Username must be at least 3 characters long");
        }

        // Update username
        adminUsername = request.getNewUsername();

        return "Username changed successfully";
    }

    public String changeReceiverEmail(String newReceiverEmail) {
        emailConfigService.updateReceiverEmail(newReceiverEmail);
        return "Receiver email updated successfully to: " + newReceiverEmail;
    }

    public String changeSenderEmail(String newSenderEmail, String newAppPassword) {
        emailConfigService.updateSenderEmail(newSenderEmail, newAppPassword);
        return "Sender email and credentials updated successfully";
    }

    private void validatePasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }

        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0);

        if (!hasUppercase) {
            throw new ValidationException("Password must contain at least one uppercase letter");
        }
        if (!hasLowercase) {
            throw new ValidationException("Password must contain at least one lowercase letter");
        }
        if (!hasDigit) {
            throw new ValidationException("Password must contain at least one digit");
        }
        if (!hasSpecial) {
            throw new ValidationException("Password must contain at least one special character");
        }
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
