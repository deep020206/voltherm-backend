package com.voltherm.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path imagesDir;
    private final Path pdfsDir;
    private static final Set<String> IMAGE_TYPES = Set.of("image/jpeg", "image/png");
    private static final Set<String> PDF_TYPES = Set.of("application/pdf");
    private static final long MAX_IMAGE_BYTES = 5 * 1024 * 1024;   // 5MB
    private static final long MAX_PDF_BYTES = 15 * 1024 * 1024;    // 15MB

    public FileStorageService(
            @Value("${app.images.dir:/opt/app-data/images}") String imagesDir,
            @Value("${app.pdfs.dir:/opt/app-data/pdfs}") String pdfsDir) throws IOException {
        this.imagesDir = Path.of(imagesDir);
        this.pdfsDir = Path.of(pdfsDir);
        Files.createDirectories(this.imagesDir);
        Files.createDirectories(this.pdfsDir);
    }

    public String storeImage(String productId, MultipartFile file) throws IOException {
        validate(file, IMAGE_TYPES, MAX_IMAGE_BYTES);
        String ext = getExtension(file.getOriginalFilename(), "jpg");
        String filename = productId + "-" + UUID.randomUUID() + "." + ext;
        Path target = imagesDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return "/images/" + filename;
    }

    public String storePdf(String productId, MultipartFile file) throws IOException {
        validate(file, PDF_TYPES, MAX_PDF_BYTES);
        String ext = getExtension(file.getOriginalFilename(), "pdf");
        String filename = productId + "-" + UUID.randomUUID() + "." + ext;
        Path target = pdfsDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return filename; // served via download endpoint, not exposed directly
    }

    public Resource loadPdf(String filename) {
        Path path = pdfsDir.resolve(filename);
        return Files.exists(path) ? new FileSystemResource(path) : null;
    }

    private void validate(MultipartFile file, Set<String> allowedTypes, long maxSize) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }
        if (!allowedTypes.contains(file.getContentType())) {
            throw new IllegalArgumentException("Unsupported file type: " + file.getContentType());
        }
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("File too large");
        }
    }

    private String getExtension(String originalName, String defaultExt) {
        if (originalName == null) return defaultExt;
        String ext = StringUtils.getFilenameExtension(originalName);
        return (ext == null || ext.isBlank()) ? defaultExt : ext.toLowerCase();
    }
}
