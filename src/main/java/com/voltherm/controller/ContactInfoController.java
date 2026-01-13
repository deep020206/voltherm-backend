package com.voltherm.controller;

import com.voltherm.dto.ApiResponse;
import com.voltherm.model.ContactInfo;
import com.voltherm.service.ContactInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact-info")
public class ContactInfoController {
    
    @Autowired
    private ContactInfoService contactInfoService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getContactInfo() {
        ContactInfo info = contactInfoService.getContactInfo();
        return ResponseEntity.ok(new ApiResponse<>(true, info));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<?>> updateContactInfo(
            Authentication authentication,
            @RequestBody ContactInfo contactInfoData) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, 
                        new ApiResponse.ErrorInfo("UNAUTHORIZED", "Admin authentication required", 401)));
        }
        
        ContactInfo updated = contactInfoService.update(contactInfoData);
        return ResponseEntity.ok(new ApiResponse<>(true, updated));
    }
}
