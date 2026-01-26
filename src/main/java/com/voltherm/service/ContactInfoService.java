package com.voltherm.service;

import com.voltherm.exception.ResourceNotFoundException;
import com.voltherm.exception.ValidationException;
import com.voltherm.model.ContactInfo;
import com.voltherm.repository.ContactInfoRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class ContactInfoService {
    
    private final ContactInfoRepository contactInfoRepository;

    public ContactInfoService(ContactInfoRepository contactInfoRepository) {
        this.contactInfoRepository = contactInfoRepository;
    }

    public ContactInfo getContactInfo() {
        ContactInfo info = contactInfoRepository.getContactInfo();
        if (info == null) {
            throw new ResourceNotFoundException("Contact info not found");
        }
        return info;
    }

    public ContactInfo update(ContactInfo contactInfoData) {
        ContactInfo info = contactInfoRepository.getContactInfo();
        
        if (info == null) {
            info = new ContactInfo();
        }
        
        if (contactInfoData.getSalesEmail() != null) {
            info.setSalesEmail(contactInfoData.getSalesEmail());
        }
        
        if (contactInfoData.getSalesPhoneNumber() != null) {
            info.setSalesPhoneNumber(contactInfoData.getSalesPhoneNumber());
        }
        
        if (contactInfoData.getBusinessEmail() != null) {
            info.setBusinessEmail(contactInfoData.getBusinessEmail());
        }
        
        if (contactInfoData.getSupportPhoneNumber() != null) {
            info.setSupportPhoneNumber(contactInfoData.getSupportPhoneNumber());
        }

        if (contactInfoData.getMainAddress() != null) {
            info.setMainAddress(contactInfoData.getMainAddress());
        }

        if (contactInfoData.getBranches() != null) {
            info.setBranches(contactInfoData.getBranches());
        }

        if (contactInfoData.getFacebookUrl() != null) {
            info.setFacebookUrl(contactInfoData.getFacebookUrl());
        }

        if (contactInfoData.getXUrl() != null) {
            info.setXUrl(contactInfoData.getXUrl());
        }

        if (contactInfoData.getInstagramUrl() != null) {
            info.setInstagramUrl(contactInfoData.getInstagramUrl());
        }

        if (contactInfoData.getLinkedinUrl() != null) {
            info.setLinkedinUrl(contactInfoData.getLinkedinUrl());
        }

        if (contactInfoData.getIndiamartUrl() != null) {
            info.setIndiamartUrl(contactInfoData.getIndiamartUrl());
        }
        
        return contactInfoRepository.save(info);
    }

    public ContactInfo.Office addOffice(ContactInfo.Office office) {
        if (office.getBranchName() == null || office.getBranchName().isBlank()) {
            throw new ValidationException("Branch name is required");
        }
        
        ContactInfo info = contactInfoRepository.getContactInfo();
        if (info == null) {
            info = new ContactInfo();
        }
        
        if (info.getBranches() == null) {
            info.setBranches(new ArrayList<>());
        }
        
        // Generate branchId if not provided
        if (office.getBranchId() == null || office.getBranchId().isBlank()) {
            office.setBranchId(UUID.randomUUID().toString());
        }
        
        info.getBranches().add(office);
        contactInfoRepository.save(info);
        
        return office;
    }

    public ContactInfo.Office updateOffice(String branchId, ContactInfo.Office officeData) {
        ContactInfo info = contactInfoRepository.getContactInfo();
        if (info == null || info.getBranches() == null) {
            throw new ResourceNotFoundException("No offices found");
        }
        
        ContactInfo.Office existingOffice = info.getBranches().stream()
                .filter(o -> o.getBranchId().equals(branchId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Office not found with branchId: " + branchId));
        
        // Update fields
        if (officeData.getBranchName() != null) {
            existingOffice.setBranchName(officeData.getBranchName());
        }
        if (officeData.getAddressLine1() != null) {
            existingOffice.setAddressLine1(officeData.getAddressLine1());
        }
        if (officeData.getAddressLine2() != null) {
            existingOffice.setAddressLine2(officeData.getAddressLine2());
        }
        if (officeData.getCity() != null) {
            existingOffice.setCity(officeData.getCity());
        }
        if (officeData.getState() != null) {
            existingOffice.setState(officeData.getState());
        }
        if (officeData.getMapUrl() != null) {
            existingOffice.setMapUrl(officeData.getMapUrl());
        }
        if (officeData.getPhoneNumber() != null) {
            existingOffice.setPhoneNumber(officeData.getPhoneNumber());
        }
        if (officeData.getPincode() != 0) {
            existingOffice.setPincode(officeData.getPincode());
        }
        
        contactInfoRepository.save(info);
        return existingOffice;
    }

    public void deleteOffice(String branchId) {
        ContactInfo info = contactInfoRepository.getContactInfo();
        if (info == null || info.getBranches() == null) {
            throw new ResourceNotFoundException("No offices found");
        }
        
        boolean removed = info.getBranches().removeIf(o -> o.getBranchId().equals(branchId));
        if (!removed) {
            throw new ResourceNotFoundException("Office not found with branchId: " + branchId);
        }
        
        contactInfoRepository.save(info);
    }
}
