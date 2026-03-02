package com.voltherm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;

public class Inquiry {

    /** Lightweight cart entry sent from the frontend. */
    public static class CartItem {
        @JsonProperty
        private String title;
        @JsonProperty
        private int quantity;

        public CartItem() {}

        public CartItem(String title, int quantity) {
            this.title = title;
            this.quantity = quantity;
        }

        public String getTitle()   { return title; }
        public void setTitle(String title) { this.title = title; }
        public int getQuantity()   { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    @JsonProperty
    private String id;
    
    @JsonProperty
    private Instant createdAt;

    @JsonProperty
    private String email;
    
    @JsonProperty
    private String name;
    
    @JsonProperty
    private String phoneNumber;
    
    @JsonProperty
    private String requirements;

    @JsonProperty
    private String company;

    @JsonProperty
    private List<String> interestedProducts;

    @JsonProperty
    private List<CartItem> cartItems;

    @JsonProperty
    private String status;

    public Inquiry() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public List<String> getInterestedProducts() { return interestedProducts; }
    public void setInterestedProducts(List<String> interestedProducts) { this.interestedProducts = interestedProducts; }

    public List<CartItem> getCartItems() { return cartItems; }
    public void setCartItems(List<CartItem> cartItems) { this.cartItems = cartItems; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
