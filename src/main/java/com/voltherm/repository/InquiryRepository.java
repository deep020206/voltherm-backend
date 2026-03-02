package com.voltherm.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.voltherm.model.Inquiry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Repository
public class InquiryRepository {
    
    private final Path inquiriesJsonPath;
    private final ObjectMapper objectMapper;
    private final List<Inquiry> inquiries = new ArrayList<>();

    public InquiryRepository(@Value("${app.inquiries.json:/opt/app-data/inquiries.json}") String inquiriesJsonPath) throws IOException {
        this.inquiriesJsonPath = Path.of(inquiriesJsonPath);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        Files.createDirectories(this.inquiriesJsonPath.getParent());
        init();
    }

    private void init() throws IOException {
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
            inquiry.setId(generateInquiryId());
            inquiry.setCreatedAt(Instant.now());
        }
        
        inquiries.removeIf(i -> i.getId().equals(inquiry.getId()));
        inquiries.add(inquiry);
        
        persist();
        return inquiry;
    }

    public void deleteById(String id) {
        inquiries.removeIf(i -> i.getId().equals(id));
        persist();
    }

    public void deleteAllByIds(List<String> ids) {
        inquiries.removeIf(i -> ids.contains(i.getId()));
        persist();
    }

    private synchronized void persist() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(inquiriesJsonPath.toFile(), inquiries);
        } catch (IOException e) {
            throw new RuntimeException("Failed to persist inquiries.json", e);
        }
    }

    /**
     * Generates a meaningful, unique inquiry ID in the format:
     * IN&lt;dd&gt;&lt;MM&gt;&lt;yyyy&gt;&lt;HH&gt;&lt;mm&gt;&lt;ss&gt;&lt;xxx&gt;
     * where xxx is a random 3-digit suffix to prevent collisions within the same second.
     * Example: IN01032026201414523
     */
    private String generateInquiryId() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        String timestamp = now.format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"));
        int suffix = ThreadLocalRandom.current().nextInt(100, 1000); // 100–999
        return "IN" + timestamp + suffix;
    }
}
