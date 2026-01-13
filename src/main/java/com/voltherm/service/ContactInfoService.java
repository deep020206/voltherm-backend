package com.voltherm.service;

import com.voltherm.exception.ResourceNotFoundException;
import com.voltherm.model.ContactInfo;
import com.voltherm.repository.ContactInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactInfoService {
    
    @Autowired
    private ContactInfoRepository contactInfoRepository;

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
        
        if (contactInfoData.getBusinessPhoneNumber() != null) {
            info.setBusinessPhoneNumber(contactInfoData.getBusinessPhoneNumber());
        }

        if (contactInfoData.getMainAddress() != null) {
            info.setMainAddress(contactInfoData.getMainAddress());
        }

        if (contactInfoData.getBranchAddresses() != null) {
            info.setBranchAddresses(contactInfoData.getBranchAddresses());
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
}
