package com.voltherm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Certificate {
    @JsonProperty
    private String id;
    
    @JsonProperty
    private String name;
    
    @JsonProperty
    private String imageUrl;

    public Certificate() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
