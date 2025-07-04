package com.field2fork.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.field2fork.pojos.ProductImage;

public interface ProductImageDao extends JpaRepository<ProductImage, Long> {
	
	List<ProductImage> findByProductId(Long productId);

}
