package com.field2fork.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.field2fork.pojos.Product;
import com.field2fork.pojos.ProductCategory;

public interface ProductDao extends JpaRepository<Product, Long> {
    List<Product> findByCategory(ProductCategory category);
    
    long countByActiveStatus(boolean activeStatus);
    
    // Use join fetch to load the user associated with each product
    @Query("select p from Product p join fetch p.user where p.user.id = :userId")
    List<Product> findByUserId(@Param("userId") Long userId);
}
