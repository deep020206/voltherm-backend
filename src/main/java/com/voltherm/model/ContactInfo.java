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
    private String businessPhoneNumber;

    @JsonProperty
    private String mainAddress;

    @JsonProperty
    private List<String> branchAddresses;

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

    public List<String> getBranchAddresses() {
        return branchAddresses;
    }

    public void setBranchAddresses(List<String> branchAddresses) {
        this.branchAddresses = branchAddresses;
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

    public String getBusinessPhoneNumber() {
        return businessPhoneNumber;
    }

    public void setBusinessPhoneNumber(String businessPhoneNumber) {
        this.businessPhoneNumber = businessPhoneNumber;
    }
}
