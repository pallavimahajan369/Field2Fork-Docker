package com.field2fork.service;

import com.field2fork.dtos.ProductImageResponseDTO;
import com.field2fork.pojos.Product;
import com.field2fork.pojos.ProductImage;
import com.field2fork.dao.ProductImageDao;
import com.field2fork.dao.ProductDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageDao productImageDao;
    private final ProductDao productDao;
    
    @Value("${image.base.url}") // e.g. http://localhost:8080/product-images/images/
    private String imageBaseUrl;

    @Override
    public ProductImageResponseDTO uploadImage(Long productId, MultipartFile imageFile) throws IOException {
        // Ensure the product exists
        Product product = productDao.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // Create and save the ProductImage
        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImage.setImageData(imageFile.getBytes());
        ProductImage savedImage = productImageDao.save(productImage);
        
        // Generate the URL to serve this image via our API endpoint
        return new ProductImageResponseDTO(
                savedImage.getId(),
                savedImage.getProduct().getId(),
                imageBaseUrl + savedImage.getId()
        );
    }

    @Override
    public List<ProductImageResponseDTO> getImagesByProductId(Long productId) {
        return productImageDao.findByProductId(productId).stream()
                .map(img -> new ProductImageResponseDTO(
                        img.getId(),
                        img.getProduct().getId(),
                        imageBaseUrl + img.getId()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteImage(Long id) {
        productImageDao.deleteById(id);
    }
    
    @Override
    public byte[] getImageDataById(Long id) {
        ProductImage image = productImageDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found with ID: " + id));
        return image.getImageData();
    }
}
