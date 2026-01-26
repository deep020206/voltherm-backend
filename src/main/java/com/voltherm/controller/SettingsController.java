package com.voltherm.controller;

import com.voltherm.dto.ApiResponse;
import com.voltherm.model.PasswordChangeRequest;
import com.voltherm.model.UsernameChangeRequest;
import com.voltherm.model.OtpVerificationRequest;
import com.voltherm.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {
    
    private final AuthService authService;

    public SettingsController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/password/initiate")
    public ResponseEntity<ApiResponse<?>> initiatePasswordChange(
            Authentication authentication,
            @RequestBody PasswordChangeRequest request) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, 
                        new ApiResponse.ErrorInfo("UNAUTHORIZED", "Admin authentication required", 401)));
        }

        String currentUsername = authentication.getName();
        String message = authService.initiatePasswordChange(request, currentUsername);
        
        return ResponseEntity.ok(new ApiResponse<>(true, message));
    }

    @PostMapping("/password/verify")
    public ResponseEntity<ApiResponse<?>> verifyOtpAndChangePassword(
            Authentication authentication,
            @RequestBody OtpVerificationRequest request) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, 
                        new ApiResponse.ErrorInfo("UNAUTHORIZED", "Admin authentication required", 401)));
        }

        String currentUsername = authentication.getName();
        String message = authService.verifyOtpAndChangePassword(request, currentUsername);
        
        return ResponseEntity.ok(new ApiResponse<>(true, message));
    }

    @PostMapping("/username")
    public ResponseEntity<ApiResponse<?>> changeUsername(
            Authentication authentication,
            @RequestBody UsernameChangeRequest request) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, 
                        new ApiResponse.ErrorInfo("UNAUTHORIZED", "Admin authentication required", 401)));
        }

        String currentUsername = authentication.getName();
        String message = authService.changeUsername(request, currentUsername);
        
        return ResponseEntity.ok(new ApiResponse<>(true, message));
    }
}
