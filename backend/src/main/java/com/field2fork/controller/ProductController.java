package com.field2fork.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.field2fork.service.ProductService;
import com.field2fork.pojos.ProductCategory;
import com.field2fork.dtos.ProductRespDTO;
import com.field2fork.dtos.ApiResponse;
import com.field2fork.dtos.ProductRequestDTO;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {
	@Autowired
	private ProductService prodService;

	public ProductController() {
		System.out.println("in ctor " + getClass());
	}
	
	
	/*1.
	 * Desc - get all products 
	 * URL - http://host:port/products 
	 * Method - GET
	 */
	@GetMapping
	public ResponseEntity<?> getAllProducts() {
		System.out.println("in get all categories");
		List<ProductRespDTO> products = 
				prodService.getAllProducts();
		if (products.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.ok(products);
		}
	}

	
	/*2.
	 * Desc - Add new product 
	 * URL - http://host:port/products
	 * Method - POST
	 */
	@PostMapping("/add")
	public ResponseEntity<?> addNewProduct(@RequestBody ProductRequestDTO dto) {
		System.out.println("in add new product " + dto);
		try {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(prodService.addNewProduct(dto));
					
		} catch (RuntimeException e) {
			return ResponseEntity.
					status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(e.getMessage()));
		}
	}
	
	
	/*
	 * Desc - Delete product by id 
	 * URL - http://host:port/products/{id}
	 * Method - PATCH Payload
	 */
	@PatchMapping("/{product_id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long product_id) {
		System.out.println("in delete product " + product_id);
		return ResponseEntity.ok(
				prodService.deleteProduct(product_id));
	}
	
	/*
	 * Desc - Update product details 
	 * URL - http://host:port/products Method - PUT
	 */
	@PutMapping("/{product_id}")
	public ResponseEntity<?> updateProductDetails(@RequestBody ProductRequestDTO dto ,@PathVariable Long product_id) {
		System.out.println("in update " + dto +" "+product_id);
		return ResponseEntity.ok(
				prodService.updateProductDetails(dto,product_id));
	}
	
	/*
	 * Desc - Make product available 
	 * URL - http://host:port/products/{id}/restore
	 * Method - PUT
	 */
	@PatchMapping("/{product_id}/restore")
	public ResponseEntity<?> restoreProduct(@PathVariable Long product_id)
	{
		System.out.println("In restore Product "+ product_id);
		return ResponseEntity.ok(prodService.restoreProduct(product_id));
	}
	
	/*
	 * Desc - Get all Categories 
	 * URL - http://host:port/products/categories
	 * Method - GET
	 */
	@GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
		System.out.println("in get all categories");
        List<ProductCategory> categories = prodService.getAllCategories();
        if (categories.isEmpty()) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("No categories");
        } else {
            return ResponseEntity.ok(categories);
        }
    }
	
	/*
	 * Desc - Get product by id
	 * URL - http://host:port/products/{id}
	 * Method - GET
	 */
	@GetMapping("/{product_id}")
	public ResponseEntity<?> getProductById(@PathVariable Long product_id) {
	    System.out.println("in get product by id");
	    ProductRespDTO product = prodService.getProductById(product_id);
	    if (product == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("No such product with given Id");
	    } else {
	        return ResponseEntity.ok(product);
	    }
	}
	
	 @GetMapping("/seller/{sellerId}")
	    public ResponseEntity<?> getProductsBySeller(@PathVariable Long sellerId) {
	        System.out.println("in get products by seller " + sellerId);
	        List<ProductRespDTO> products = prodService.getProductsBySeller(sellerId);
	        if (products == null || products.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("No products found for seller id " + sellerId);
	        } else {
	            return ResponseEntity.ok(products);
	        }
	    }
	
	
	/*
	 * Desc - Get products by category
	 * URL - http://host:port/products/category
	 * Method - GET
	 */
	@GetMapping("/category")
	public ResponseEntity<?> getProductsByCategory(@RequestParam("category") String category) {
	    List<ProductRespDTO> products = prodService.getProductsByCategory(category);
	    if (products.isEmpty()) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("No Products in mentioned category");
	    } else {
	        return ResponseEntity.ok(products);
	    }
	}

}
