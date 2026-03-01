package com.voltherm.model;

public class Product {
    private String productId;
    private String productName;
    private double price;
    private boolean featured;
    private boolean isAvailable;
    private String category;
    private String subCategory;
    private String[] specificationFields;
    private String[] specificationValues;
    private String[] quickSpecs;
    private String imageUrl;          // e.g. /images/uuid.jpg
    private String pdfDownloadUrl;    // e.g. /api/products/{productId}/pdf
    private String productDescription;

    public Product() {}

    public Product(String productId, String productName, double price, boolean featured,
                   boolean isAvailable, String category, String subCategory,
                   String[] specificationFields, String[] specificationValues, String[] quickSpecs,
                   String imageUrl, String pdfDownloadUrl, String productDescription) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.featured = featured;
        this.isAvailable = isAvailable;
        this.category = category;
        this.subCategory = subCategory;
        this.specificationFields = specificationFields;
        this.specificationValues = specificationValues;
        this.quickSpecs = quickSpecs;
        this.imageUrl = imageUrl;
        this.pdfDownloadUrl = pdfDownloadUrl;
        this.productDescription = productDescription;
    }

    // Getters and Setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSubCategory() { return subCategory; }
    public void setSubCategory(String subCategory) { this.subCategory = subCategory; }

    public String[] getSpecificationFields() { return specificationFields; }
    public void setSpecificationFields(String[] specificationFields) { this.specificationFields = specificationFields; }

    public String[] getSpecificationValues() { return specificationValues; }
    public void setSpecificationValues(String[] specificationValues) { this.specificationValues = specificationValues; }

    public String[] getQuickSpecs() { return quickSpecs; }
    public void setQuickSpecs(String[] quickSpecs) { this.quickSpecs = quickSpecs; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getPdfDownloadUrl() { return pdfDownloadUrl; }
    public void setPdfDownloadUrl(String pdfDownloadUrl) { this.pdfDownloadUrl = pdfDownloadUrl; }

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }
}
