package com.voltherm.service;

import com.voltherm.exception.ResourceNotFoundException;
import com.voltherm.exception.ValidationException;
import com.voltherm.model.Certificate;
import com.voltherm.repository.CertificateRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class CertificateService {
    
    private final CertificateRepository certificateRepository;
    private final FileStorageService fileStorageService;

    public CertificateService(CertificateRepository certificateRepository, FileStorageService fileStorageService) {
        this.certificateRepository = certificateRepository;
        this.fileStorageService = fileStorageService;
    }

    public List<Certificate> findAll() {
        return certificateRepository.findAll();
    }

    public Certificate findById(String id) {
        return certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found with id: " + id));
    }

    public Certificate create(String name, MultipartFile image) throws IOException {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Certificate name is required");
        }

        Certificate certificate = new Certificate();
        certificate.setId(UUID.randomUUID().toString());
        certificate.setName(name);

        String imageUrl = fileStorageService.storeImage(certificate.getId(), image);
        certificate.setImageUrl(imageUrl);

        return certificateRepository.save(certificate);
    }

    public void delete(String id) {
        findById(id); // Ensure exists
        certificateRepository.deleteById(id);
    }
}
