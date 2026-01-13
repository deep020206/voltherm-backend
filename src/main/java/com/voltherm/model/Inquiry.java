package com.voltherm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Inquiry {
    @JsonProperty
    private String id;
    
    @JsonProperty
    private long createdAt;
    
    @JsonProperty
    private long updatedAt;

    @JsonProperty
    private String email;
    
    @JsonProperty
    private String name;
    
    @JsonProperty
    private String phoneNumber;
    
    @JsonProperty
    private String requirements;


    public Inquiry() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }
}
