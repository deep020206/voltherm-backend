package com.voltherm.controller;

import com.voltherm.dto.ApiResponse;
import com.voltherm.security.AdminUser;
import com.voltherm.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    private final AuthService authService;

    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(
            @RequestBody Map<String, String> credentials,
            HttpSession session) {
        
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        authService.login(username, password, session);
        
        Map<String, String> response = Map.of(
            "username", username,
            "message", "Login successful"
        );
        
        return ResponseEntity.ok(new ApiResponse<>(true, response));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<?>> getProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, 
                        new ApiResponse.ErrorInfo("UNAUTHORIZED", "Admin authentication required", 401)));
        }
        
        String username = authentication.getName();
        AdminUser profile = authService.getProfile(username);
        return ResponseEntity.ok(new ApiResponse<>(true, profile));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(
            Authentication authentication,
            HttpSession session) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, 
                        new ApiResponse.ErrorInfo("UNAUTHORIZED", "Admin authentication required", 401)));
        }
        
        authService.logout(session);
        return ResponseEntity.ok(new ApiResponse<>(true, "Logged out successfully"));
    }
}
