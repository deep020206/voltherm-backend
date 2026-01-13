package com.voltherm.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voltherm.model.Inquiry;
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
public class InquiryRepository {
    
    private final Path inquiriesJsonPath;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Inquiry> inquiries = new ArrayList<>();

    public InquiryRepository(@Value("${app.inquiries.json:/opt/app-data/inquiries.json}") String inquiriesJsonPath) throws IOException {
        this.inquiriesJsonPath = Path.of(inquiriesJsonPath);
        init();
    }

    private void init() throws IOException {
        Files.createDirectories(inquiriesJsonPath.getParent());
        if (Files.exists(inquiriesJsonPath)) {
            inquiries.clear();
            inquiries.addAll(objectMapper.readValue(inquiriesJsonPath.toFile(), new TypeReference<List<Inquiry>>() {}));
        } else {
            persist();
        }
    }

    public List<Inquiry> findAll() {
        return new ArrayList<>(inquiries);
    }

    public Optional<Inquiry> findById(String id) {
        return inquiries.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();
    }

    public Inquiry save(Inquiry inquiry) {
        if (inquiry.getId() == null) {
            inquiry.setId(UUID.randomUUID().toString());
            inquiry.setCreatedAt(System.currentTimeMillis());
        }
        
        inquiry.setUpdatedAt(System.currentTimeMillis());
        inquiries.removeIf(i -> i.getId().equals(inquiry.getId()));
        inquiries.add(inquiry);
        
        persist();
        return inquiry;
    }

    public void deleteById(String id) {
        inquiries.removeIf(i -> i.getId().equals(id));
        persist();
    }

    private synchronized void persist() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(inquiriesJsonPath.toFile(), inquiries);
        } catch (IOException e) {
            throw new RuntimeException("Failed to persist inquiries.json", e);
        }
    }
}
