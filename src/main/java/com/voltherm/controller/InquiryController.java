package com.voltherm.controller;

import com.voltherm.dto.ApiResponse;
import com.voltherm.model.Inquiry;
import com.voltherm.service.InquiryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inquiries")
public class InquiryController {
    
    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createInquiry(@RequestBody Inquiry inquiry) {
        Inquiry created = inquiryService.create(inquiry);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> listInquiries(Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, 
                        new ApiResponse.ErrorInfo("UNAUTHORIZED", "Admin authentication required", 401)));
        }
        
        List<Inquiry> inquiries = inquiryService.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, inquiries));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<?>> updateInquiryStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> statusUpdate,
            Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, 
                        new ApiResponse.ErrorInfo("UNAUTHORIZED", "Admin authentication required", 401)));
        }
        
        String status = statusUpdate.get("status");
        if (status == null || status.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, 
                        new ApiResponse.ErrorInfo("BAD_REQUEST", "Status is required", 400)));
        }
        
        Inquiry updated = inquiryService.updateStatus(id, status);
        return ResponseEntity.ok(new ApiResponse<>(true, updated));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<?>> deleteInquiries(
            @RequestBody Map<String, List<String>> body,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false,
                        new ApiResponse.ErrorInfo("UNAUTHORIZED", "Admin authentication required", 401)));
        }

        List<String> ids = body.get("ids");
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false,
                        new ApiResponse.ErrorInfo("BAD_REQUEST", "At least one inquiry ID is required", 400)));
        }

        inquiryService.deleteAll(ids);
        return ResponseEntity.ok(new ApiResponse<>(true, "Inquiries deleted successfully"));
    }
}
