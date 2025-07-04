package com.field2fork.service;

import com.field2fork.dtos.ProductImageResponseDTO;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface ProductImageService {
    ProductImageResponseDTO uploadImage(Long productId, MultipartFile imageFile) throws IOException;
    
    List<ProductImageResponseDTO> getImagesByProductId(Long productId);
    
    void deleteImage(Long id);
    
    byte[] getImageDataById(Long id);
}
