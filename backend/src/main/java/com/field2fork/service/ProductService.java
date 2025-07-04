package com.field2fork.service;

import java.util.List;
import com.field2fork.dtos.ProductRespDTO;
import com.field2fork.pojos.ProductCategory;
import com.field2fork.dtos.ApiResponse;
import com.field2fork.dtos.ProductRequestDTO;

public interface ProductService {

	List<ProductRespDTO> getAllProducts();

	ApiResponse addNewProduct(ProductRequestDTO dto);

	ApiResponse deleteProduct(Long product_id);

	ApiResponse updateProductDetails(ProductRequestDTO dto, Long product_id);

	ApiResponse restoreProduct(Long product_id);

	List<ProductCategory> getAllCategories();

	ProductRespDTO getProductById(Long product_id);

	List<ProductRespDTO> getProductsByCategory(String category);
	
	List<ProductRespDTO> getProductsBySeller(Long sellerId);

}