package com.voltherm.service;

import com.voltherm.exception.ResourceNotFoundException;
import com.voltherm.exception.ValidationException;
import com.voltherm.model.Inquiry;
import com.voltherm.repository.InquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InquiryService {
    
    private final InquiryRepository inquiryRepository;

    @Autowired
    private EmailService emailService;

    public InquiryService(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    public List<Inquiry> findAll() {
        return inquiryRepository.findAll();
    }

    public Inquiry findById(String id) {
        return inquiryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inquiry not found with id: " + id));
    }

    public Inquiry create(Inquiry inquiry) {
        if (inquiry.getName() == null || inquiry.getName().isBlank()) {
            throw new ValidationException("Name is required");
        }
        
        if (inquiry.getEmail() == null || inquiry.getEmail().isBlank()) {
            throw new ValidationException("Email is required");
        }
        
        if (inquiry.getRequirements() == null || inquiry.getRequirements().isBlank()) {
            throw new ValidationException("Requirements are required");
        }
        
        inquiry.setStatus("new");
        Inquiry savedInquiry = inquiryRepository.save(inquiry);
        
        // Send email notification
        emailService.sendInquiryNotification(savedInquiry);
        
        return savedInquiry;
    }

    public Inquiry updateStatus(String id, String status) {
        Inquiry inquiry = findById(id);
        inquiry.setStatus(status);
        return inquiryRepository.save(inquiry);
    }
}
