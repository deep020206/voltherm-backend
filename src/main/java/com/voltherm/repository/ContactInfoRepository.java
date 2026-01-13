package com.voltherm.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voltherm.model.ContactInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ContactInfoRepository {
    
    private final Path contactInfoJsonPath;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<ContactInfo> contactInfoList = new ArrayList<>();

    public ContactInfoRepository(@Value("${app.contact-info.json:/opt/app-data/contact-info.json}") String contactInfoJsonPath) throws IOException {
        this.contactInfoJsonPath = Path.of(contactInfoJsonPath);
        init();
    }

    private void init() throws IOException {
        Files.createDirectories(contactInfoJsonPath.getParent());
        if (Files.exists(contactInfoJsonPath)) {
            contactInfoList.clear();
            contactInfoList.addAll(objectMapper.readValue(contactInfoJsonPath.toFile(), new TypeReference<List<ContactInfo>>() {}));
        } else {
            persist();
        }
    }

    public ContactInfo getContactInfo() {
        return contactInfoList.isEmpty() ? null : contactInfoList.get(0);
    }

    public ContactInfo save(ContactInfo contactInfo) {
        if (contactInfo.getId() == null) {
            contactInfo.setId(UUID.randomUUID().toString());
        }
        
        // Only one record allowed
        contactInfoList.clear();
        contactInfoList.add(contactInfo);
        
        persist();
        return contactInfo;
    }

    private synchronized void persist() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(contactInfoJsonPath.toFile(), contactInfoList);
        } catch (IOException e) {
            throw new RuntimeException("Failed to persist contact-info.json", e);
        }
    }
}
