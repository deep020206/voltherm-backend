package com.voltherm.controller;

import com.voltherm.dto.ApiResponse;
import com.voltherm.model.Certificate;
import com.voltherm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {
    
    @Autowired
    private CertificateService certificateService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> listCertificates() {
        List<Certificate> certificates = certificateService.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, certificates));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createCertificate(
            Authentication authentication,
            @RequestBody Certificate certificate) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, 
                        new ApiResponse.ErrorInfo("UNAUTHORIZED", "Admin authentication required", 401)));
        }
        
        Certificate created = certificateService.create(certificate);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, created));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteCertificate(
            @PathVariable String id,
            Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, 
                        new ApiResponse.ErrorInfo("UNAUTHORIZED", "Admin authentication required", 401)));
        }
        
        certificateService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Certificate deleted successfully"));
    }
}
