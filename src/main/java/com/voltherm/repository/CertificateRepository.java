package com.voltherm.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voltherm.model.Certificate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CertificateRepository {
    
    private final Path certificatesJsonPath;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Certificate> certificates = new ArrayList<>();

    public CertificateRepository(@Value("${app.certificates.json:/opt/app-data/certificates.json}") String certificatesJsonPath) throws IOException {
        this.certificatesJsonPath = Path.of(certificatesJsonPath);
        init();
    }

    private void init() throws IOException {
        Files.createDirectories(certificatesJsonPath.getParent());
        if (Files.exists(certificatesJsonPath)) {
            certificates.clear();
            certificates.addAll(objectMapper.readValue(certificatesJsonPath.toFile(), new TypeReference<List<Certificate>>() {}));
        } else {
            persist();
        }
    }

    public List<Certificate> findAll() {
        return new ArrayList<>(certificates);
    }

    public Optional<Certificate> findById(String id) {
        return certificates.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public Certificate save(Certificate certificate) {
        if (certificate.getId() == null) {
            certificate.setId(UUID.randomUUID().toString());
        }
        
        certificates.removeIf(c -> c.getId().equals(certificate.getId()));
        certificates.add(certificate);
        
        persist();
        return certificate;
    }

    public void deleteById(String id) {
        certificates.removeIf(c -> c.getId().equals(id));
        persist();
    }

    private synchronized void persist() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(certificatesJsonPath.toFile(), certificates);
        } catch (IOException e) {
            throw new RuntimeException("Failed to persist certificates.json", e);
        }
    }
}
