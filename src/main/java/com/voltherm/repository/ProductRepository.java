package com.voltherm.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voltherm.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProductRepository {
    
    private final Path productsJsonPath;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Product> products = new ArrayList<>();

    public ProductRepository(@Value("${app.products.json:/opt/app-data/products.json}") String productsJsonPath) throws IOException {
        this.productsJsonPath = Path.of(productsJsonPath);
        Files.createDirectories(this.productsJsonPath.getParent());
        init();
    }

    private void init() throws IOException {
        if (Files.exists(productsJsonPath)) {
            products.clear();
            products.addAll(objectMapper.readValue(productsJsonPath.toFile(), new TypeReference<List<Product>>() {}));
        } else {
            persist();
        }
    }

    public List<Product> findAll() {
        return new ArrayList<>(products);
    }

    public Optional<Product> findById(String productId) {
        return products.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst();
    }

    public List<Product> findFeatured() {
        return products.stream()
                .filter(Product::isFeatured)
                .toList();
    }

    public Product save(Product product) {
        if (product.getProductId() == null) {
            product.setProductId(UUID.randomUUID().toString());
        }
        
        products.removeIf(p -> p.getProductId().equals(product.getProductId()));
        products.add(product);
        
        persist();
        return product;
    }

    public void deleteById(String productId) {
        products.removeIf(p -> p.getProductId().equals(productId));
        persist();
    }

    private synchronized void persist() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(productsJsonPath.toFile(), products);
        } catch (IOException e) {
            throw new RuntimeException("Failed to persist products.json", e);
        }
    }
}
