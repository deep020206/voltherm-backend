package com.voltherm.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voltherm.model.ContactInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Repository
public class ContactInfoRepository {
    
    private final Path contactInfoJsonPath;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ContactInfo contactInfo;

    public ContactInfoRepository(@Value("${app.contact-info.json:/opt/app-data/contact-info.json}") String contactInfoJsonPath) throws IOException {
        this.contactInfoJsonPath = Path.of(contactInfoJsonPath);
        Files.createDirectories(this.contactInfoJsonPath.getParent());
        init();
    }

    private void init() throws IOException {
        if (Files.exists(contactInfoJsonPath)) {
            ContactInfo parsed = objectMapper.readValue(contactInfoJsonPath.toFile(), ContactInfo.class);
            // Treat a file containing literal "null" JSON the same as no file
            if (parsed != null) {
                contactInfo = parsed;
            }
        }
        // Do NOT persist null on first startup – let save() create the file with real data
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public ContactInfo save(ContactInfo contactInfo) {
        if (contactInfo.getId() == null) {
            contactInfo.setId(UUID.randomUUID().toString());
        }
        
        this.contactInfo = contactInfo;
        
        persist();
        return contactInfo;
    }

    private synchronized void persist() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(contactInfoJsonPath.toFile(), contactInfo);
        } catch (IOException e) {
            throw new RuntimeException("Failed to persist contact-info.json", e);
        }
    }
}
