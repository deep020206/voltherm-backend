package com.voltherm.service;

import com.voltherm.exception.ResourceNotFoundException;
import com.voltherm.exception.ValidationException;
import com.voltherm.model.Certificate;
import com.voltherm.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CertificateService {
    
    @Autowired
    private CertificateRepository certificateRepository;

    public List<Certificate> findAll() {
        return certificateRepository.findAll();
    }

    public Certificate findById(String id) {
        return certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found with id: " + id));
    }

    public Certificate create(Certificate certificate) {
        if (certificate.getName() == null || certificate.getName().isBlank()) {
            throw new ValidationException("Certificate name is required");
        }
        
        if (certificate.getUrl() == null || certificate.getUrl().isBlank()) {
            throw new ValidationException("Certificate URL is required");
        }
        
        return certificateRepository.save(certificate);
    }

    public void delete(String id) {
        findById(id); // Ensure exists
        certificateRepository.deleteById(id);
    }
}
