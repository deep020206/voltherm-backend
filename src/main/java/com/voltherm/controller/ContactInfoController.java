package com.voltherm.controller;

import com.voltherm.dto.ApiResponse;
import com.voltherm.model.ContactInfo;
import com.voltherm.service.ContactInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact-info")
public class ContactInfoController {
    
    private final ContactInfoService contactInfoService;

    public ContactInfoController(ContactInfoService contactInfoService) {
        this.contactInfoService = contactInfoService;
    }

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

    @PostMapping("/offices")
    public ResponseEntity<ApiResponse<?>> addOffice(
            Authentication authentication,
            @RequestBody ContactInfo.Office office) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, 
                        new ApiResponse.ErrorInfo("UNAUTHORIZED", "Admin authentication required", 401)));
        }
        
        ContactInfo.Office created = contactInfoService.addOffice(office);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, created));
    }

    @PutMapping("/offices/{branchId}")
    public ResponseEntity<ApiResponse<?>> updateOffice(
            @PathVariable String branchId,
            Authentication authentication,
            @RequestBody ContactInfo.Office office) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, 
                        new ApiResponse.ErrorInfo("UNAUTHORIZED", "Admin authentication required", 401)));
        }
        
        ContactInfo.Office updated = contactInfoService.updateOffice(branchId, office);
        return ResponseEntity.ok(new ApiResponse<>(true, updated));
    }

    @DeleteMapping("/offices/{branchId}")
    public ResponseEntity<ApiResponse<?>> deleteOffice(
            @PathVariable String branchId,
            Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, 
                        new ApiResponse.ErrorInfo("UNAUTHORIZED", "Admin authentication required", 401)));
        }
        
        contactInfoService.deleteOffice(branchId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Office deleted successfully"));
    }
}
