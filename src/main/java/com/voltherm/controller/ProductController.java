package com.voltherm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voltherm.dto.ApiResponse;
import com.voltherm.model.Product;
import com.voltherm.service.ProductService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    public ProductController(ProductService productService, ObjectMapper objectMapper) {
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getProducts() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, products));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<?>> getProductById(@PathVariable String productId) {
        Product product = productService.findById(productId);
        return ResponseEntity.ok(new ApiResponse<>(true, product));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<?>> getFeaturedProducts() {
        List<Product> featuredProducts = productService.findFeatured();
        return ResponseEntity.ok(new ApiResponse<>(true, featuredProducts));
    }

    @GetMapping("/{productId}/pdf")
    public ResponseEntity<Resource> downloadPdf(@PathVariable String productId) {
        Resource pdf = productService.getPdf(productId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + productId + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> createProduct(
            Authentication authentication,
            @RequestPart("product") String productJson,
            @RequestPart("image") MultipartFile image,
            @RequestPart(value = "pdf", required = false) MultipartFile pdf) throws IOException {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(false, new ApiResponse.ErrorInfo("UNAUTHORIZED", "Admin authentication required", 401)));
        }
        
        Product product = objectMapper.readValue(productJson, Product.class);
        Product created = productService.create(product, image, pdf);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, created));
    }

    @PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> updateProduct(
            @PathVariable String productId,
            Authentication authentication,
            @RequestPart("product") String productJson,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "pdf", required = false) MultipartFile pdf) throws IOException {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(false, new ApiResponse.ErrorInfo("UNAUTHORIZED", "Admin authentication required", 401)));
        }
        
        Product product = objectMapper.readValue(productJson, Product.class);
        Product updated = productService.update(productId, product, image, pdf);
        return ResponseEntity.ok(new ApiResponse<>(true, updated));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<?>> deleteProduct(
            @PathVariable String productId,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(false, new ApiResponse.ErrorInfo("UNAUTHORIZED", "Admin authentication required", 401)));
        }
        productService.delete(productId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product deleted"));
    }
}
