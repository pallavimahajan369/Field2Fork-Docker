package com.field2fork.controller;

import com.field2fork.dtos.ProductImageResponseDTO;
import com.field2fork.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product-images")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ProductImageController {
	
    private final ProductImageService productImageService;

    // API 1: Upload Image
    @PostMapping("/upload")
    public ResponseEntity<ProductImageResponseDTO> uploadImage(
            @RequestParam("productId") Long productId,
            @RequestParam("image") MultipartFile imageFile) throws IOException {
        ProductImageResponseDTO response = productImageService.uploadImage(productId, imageFile);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // API 2: Get Images by Product ID
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductImageResponseDTO>> getImagesByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productImageService.getImagesByProductId(productId));
    }

    // API 3: Delete Image by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        productImageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
    
    // Additional API: Serve the raw image data
    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        byte[] imageData = productImageService.getImageDataById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Default to JPEG; adjust if needed
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }
}
