package com.voltherm.controller;

import com.voltherm.dto.ApiResponse;
import com.voltherm.model.Inquiry;
import com.voltherm.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inquiries")
public class InquiryController {
    
    @Autowired
    private InquiryService inquiryService;

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

}
