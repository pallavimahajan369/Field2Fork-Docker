package com.field2fork.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.field2fork.custom_exception.ResourceNotFoundException;
import com.field2fork.dao.ProductDao;
import com.field2fork.dao.UserDao;
import com.field2fork.dtos.ApiResponse;
import com.field2fork.dtos.ProductRespDTO;
import com.field2fork.dtos.ProductRequestDTO;
import com.field2fork.pojos.Product;
import com.field2fork.pojos.ProductCategory;
import com.field2fork.pojos.ProductStatus;
import com.field2fork.pojos.User;


@Service
@Transactional
public class ProductServiceImple implements ProductService {

	@Autowired
	private ProductDao productDao;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UserDao userDao;
	
	@Override
	public List<ProductRespDTO> getAllProducts() {
		// TODO Auto-generated method stub
		return productDao.findAll()
				.stream()
				.map(product -> 
				modelMapper.map(product, 
						ProductRespDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public ApiResponse addNewProduct(ProductRequestDTO dto) {
		// TODO Auto-generated method stub
		//dto -> entity
		Product transientProduct = 
			modelMapper.map(dto, Product.class);
		 if (transientProduct.getStatus() == null) {
		        transientProduct.setStatus(ProductStatus.IN_STOCK);
		    }
		    if (transientProduct.getActiveStatus() == null) {
		        transientProduct.setActiveStatus(true);
		    }
		    User user = userDao.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));

 
              transientProduct.setUser(user);
		Product persistentProduct = productDao.save(transientProduct);
				return new ApiResponse("Added new product with ID " 
						+ persistentProduct.getId());
	}

	@Override
	public ApiResponse deleteProduct(Long product_id) {
		// TODO Auto-generated method stub
		String mesg = "Invalid Product ID !!!!!";
		Product productData = productDao.findById(product_id).orElseThrow(() -> new ResourceNotFoundException("Invalid Product Id!"));
		productData.setActiveStatus(false);
		productDao.save(productData);
		mesg = "Product Deleted";
		return new ApiResponse(mesg);
	}

	@Override
	public ApiResponse updateProductDetails(ProductRequestDTO dto, Long product_id) {
	    // Retrieve the existing product.
	    Product productEnt = productDao.findById(product_id)
	            .orElseThrow(() -> new ResourceNotFoundException("Invalid Product ID!!!"));
	    
	    // Verify that the product belongs to the seller attempting the update.
	    // Here, we compare the existing product's user id with the one sent in the DTO.
	    // You can also compare with the currently authenticated seller's id.
	    if (!productEnt.getUser().getId().equals(dto.getUserId())) {
	        throw new RuntimeException("Unauthorized: You cannot update a product that does not belong to you.");
	    }
	    
	    // Update only the allowed fields.
	    productEnt.setName(dto.getName());
	    productEnt.setDescription(dto.getDescription());
	    productEnt.setPricePerUnit(dto.getPricePerUnit());
	    productEnt.setStockQuantity(dto.getStockQuantity());
	    productEnt.setStatus(dto.getStatus());
	    productEnt.setCategory(dto.getCategory());
	    
	    // Do NOT update the owner (user); this ensures the product remains tied to the same seller.
	    productDao.save(productEnt);
	    
	    return new ApiResponse("Product updated!");
	}

	@Override
	public ApiResponse restoreProduct(Long product_id) {
		String mesg = "Invalid Product Id!";
		Product productData = productDao.findById(product_id).orElseThrow(() -> new ResourceNotFoundException("Invalid Product Id!"));
		productData.setActiveStatus(true);
		productDao.save(productData);
		mesg = "Product is available!";
		return new ApiResponse(mesg);
		
	}

	@Override
	public List<ProductCategory> getAllCategories() {
		return Arrays.asList(ProductCategory.values());
		
	}

	@Override
	public ProductRespDTO getProductById(Long product_id) {
	    // Retrieve the product or throw an exception if not found
	    Product product = productDao.findById(product_id)
	            .orElseThrow(() -> new ResourceNotFoundException("Invalid Product ID!"));
	    
	    // Manually map fields from product to the DTO
	    ProductRespDTO dto = new ProductRespDTO();
	    dto.setId(product.getId());
	    dto.setName(product.getName());
	    dto.setDescription(product.getDescription());
	    dto.setPricePerUnit(product.getPricePerUnit());
	    dto.setStockQuantity(product.getStockQuantity());
	    dto.setStatus(product.getStatus());
	    dto.setCategory(product.getCategory());
	    dto.setActiveStatus(product.getActiveStatus());
	    
	    // Ensure the associated user is loaded (if lazy, you may need a join fetch or to initialize it)
	    // For now, we assume that product.getUser() is available.
	    if (product.getUser() != null) {
	        dto.setUserId(product.getUser().getId());
	    } else {
	        dto.setUserId(null);
	    }
	    
	    return dto;
	}

	@Override
	public List<ProductRespDTO> getProductsByCategory(String category) {
		try {
			ProductCategory category_name = ProductCategory.valueOf(category.toUpperCase());
			
		return productDao.findByCategory(category_name)
				.stream()
				.filter(product -> product.getActiveStatus())
				.map(Product -> modelMapper.map(Product, ProductRespDTO.class))
				.collect(Collectors.toList());
		}
		catch(IllegalArgumentException e)
		{
			throw new ResourceNotFoundException("Invalid Category "+ category);
		}
	}
	
	@Override
	public List<ProductRespDTO> getProductsBySeller(Long sellerId) {
	    return productDao.findByUserId(sellerId)
	        .stream()
	        .map(product -> {
	            ProductRespDTO dto = new ProductRespDTO();
	            dto.setId(product.getId());
	            dto.setName(product.getName());
	            dto.setDescription(product.getDescription());
	            dto.setPricePerUnit(product.getPricePerUnit());
	            dto.setStockQuantity(product.getStockQuantity());
	            dto.setStatus(product.getStatus());
	            dto.setCategory(product.getCategory());
	            dto.setActiveStatus(product.getActiveStatus());
	            dto.setUserId(product.getUser() != null ? product.getUser().getId() : null);
	            return dto;
	        })
	        .collect(Collectors.toList());
	}



}
