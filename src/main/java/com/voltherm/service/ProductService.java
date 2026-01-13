package com.voltherm.service;

import com.voltherm.exception.ResourceNotFoundException;
import com.voltherm.model.Product;
import com.voltherm.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileStorageService fileStorageService;

    private static final Map<String, String> pdfFileMap = new HashMap<>();

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));
    }

    public List<Product> findFeatured() {
        return productRepository.findFeatured();
    }

    public Product create(Product product, MultipartFile image, MultipartFile pdf) throws IOException {
        ensureUniqueId(product.getProductId());
        handleFiles(product, image, pdf, true);
        return productRepository.save(product);
    }

    public Product update(String productId, Product incoming, MultipartFile image, MultipartFile pdf) throws IOException {
        Product existing = findById(productId);
        merge(existing, incoming);
        handleFiles(existing, image, pdf, false);
        return productRepository.save(existing);
    }

    public void delete(String productId) {
        findById(productId);
        pdfFileMap.remove(productId);
        productRepository.deleteById(productId);
    }

    public Resource getPdf(String productId) {
        Product product = findById(productId);
        String url = product.getPdfDownloadUrl();
        if (url == null) throw new ResourceNotFoundException("PDF not available for product: " + productId);
        
        String filename = extractPdfFilename(productId);
        Resource res = fileStorageService.loadPdf(filename);
        if (res == null || !res.exists()) throw new ResourceNotFoundException("PDF file missing for product: " + productId);
        return res;
    }

    // --- helpers ---

    private void ensureUniqueId(String productId) {
        if (productId != null) {
            productRepository.findById(productId)
                    .ifPresent(p -> { throw new IllegalArgumentException("productId must be unique"); });
        }
    }

    private void handleFiles(Product product, MultipartFile image, MultipartFile pdf, boolean isCreate) throws IOException {
        if (image != null && !image.isEmpty()) {
            String imageUrl = fileStorageService.storeImage(product.getProductId(), image);
            product.setImageUrl(imageUrl);
        } else if (isCreate && (product.getImageUrl() == null || product.getImageUrl().isBlank())) {
            throw new IllegalArgumentException("Image is required for new product");
        }

        if (pdf != null && !pdf.isEmpty()) {
            String pdfFilename = fileStorageService.storePdf(product.getProductId(), pdf);
            product.setPdfDownloadUrl("/api/products/" + product.getProductId() + "/pdf");
            storePdfFilename(product.getProductId(), pdfFilename);
        } else if (isCreate) {
            product.setPdfDownloadUrl(null);
            storePdfFilename(product.getProductId(), null);
        }
    }

    private void merge(Product target, Product src) {
        if (src.getProductName() != null) target.setProductName(src.getProductName());
        if (src.getPrice() != null) target.setPrice(src.getPrice());
        if (src.isFeatured()) target.setFeatured(src.isFeatured());
        if (src.getCategory() != null) target.setCategory(src.getCategory());
        if (src.getSubCategory() != null) target.setSubCategory(src.getSubCategory());
        if (src.getBatteryChemistry() != null) target.setBatteryChemistry(src.getBatteryChemistry());
        if (src.getCapacityAh() != 0) target.setCapacityAh(src.getCapacityAh());
        if (src.getNominalVoltageV() != 0) target.setNominalVoltageV(src.getNominalVoltageV());
        if (src.getOperatingVoltageV() != null) target.setOperatingVoltageV(src.getOperatingVoltageV());
        if (src.getNominalEnergyWh() != 0) target.setNominalEnergyWh(src.getNominalEnergyWh());
        if (src.getUsableEnergyWh() != 0) target.setUsableEnergyWh(src.getUsableEnergyWh());
        if (src.getChargeDischargeCurrentA() != null) target.setChargeDischargeCurrentA(src.getChargeDischargeCurrentA());
        if (src.getImageUrl() != null) target.setImageUrl(src.getImageUrl());
        if (src.getPdfDownloadUrl() != null) target.setPdfDownloadUrl(src.getPdfDownloadUrl());
    }

    private void storePdfFilename(String productId, String filename) {
        if (filename == null) {
            pdfFileMap.remove(productId);
        } else {
            pdfFileMap.put(productId, filename);
        }
    }

    private String extractPdfFilename(String productId) {
        String fn = pdfFileMap.get(productId);
        if (fn == null) throw new ResourceNotFoundException("PDF mapping not found for product: " + productId);
        return fn;
    }
}
