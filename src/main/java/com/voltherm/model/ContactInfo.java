package com.voltherm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ContactInfo {
    @JsonProperty
    private String id;

    @JsonProperty
    private String salesEmail;
    
    @JsonProperty
    private String salesPhoneNumber;
    
    @JsonProperty
    private String businessEmail;
    
    @JsonProperty
    private String supportPhoneNumber;

    @JsonProperty
    private String mainAddress;

    @JsonProperty
    private List<Office> branches;

    @JsonProperty
    private String facebookUrl;

    @JsonProperty
    private String xUrl;

    @JsonProperty
    private String instagramUrl;

    @JsonProperty
    private String linkedinUrl;

    @JsonProperty
    private String indiamartUrl;

    public ContactInfo() {}

    // Nested Office class
    public static class Office {
        @JsonProperty
        private String branchId;
        
        @JsonProperty
        private String branchName;
        
        @JsonProperty
        private String addressLine1;
        
        @JsonProperty
        private String addressLine2;
        
        @JsonProperty
        private String city;
        
        @JsonProperty
        private String state;
        
        @JsonProperty
        private String mapUrl;
        
        @JsonProperty
        private String phoneNumber;
        
        @JsonProperty
        private int pincode;

        public Office() {}

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getAddressLine1() {
            return addressLine1;
        }

        public void setAddressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
        }

        public String getAddressLine2() {
            return addressLine2;
        }

        public void setAddressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getMapUrl() {
            return mapUrl;
        }

        public void setMapUrl(String mapUrl) {
            this.mapUrl = mapUrl;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public int getPincode() {
            return pincode;
        }

        public void setPincode(int pincode) {
            this.pincode = pincode;
        }
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getXUrl() {
        return xUrl;
    }

    public void setXUrl(String xUrl) {
        this.xUrl = xUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getIndiamartUrl() {
        return indiamartUrl;
    }

    public void setIndiamartUrl(String indiamartUrl) {
        this.indiamartUrl = indiamartUrl;
    }

    public String getMainAddress() {
        return mainAddress;
    }

    public void setMainAddress(String mainAddress) {
        this.mainAddress = mainAddress;
    }

    public List<Office> getBranches() {
        return branches;
    }

    public void setBranches(List<Office> branches) {
        this.branches = branches;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSalesEmail() {
        return salesEmail;
    }

    public void setSalesEmail(String salesEmail) {
        this.salesEmail = salesEmail;
    }

    public String getSalesPhoneNumber() {
        return salesPhoneNumber;
    }

    public void setSalesPhoneNumber(String salesPhoneNumber) {
        this.salesPhoneNumber = salesPhoneNumber;
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public String getSupportPhoneNumber() {
        return supportPhoneNumber;
    }

    public void setSupportPhoneNumber(String supportPhoneNumber) {
        this.supportPhoneNumber = supportPhoneNumber;
    }
}
