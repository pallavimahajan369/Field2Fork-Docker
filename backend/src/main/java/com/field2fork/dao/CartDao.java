package com.field2fork.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.field2fork.pojos.Cart;

public interface CartDao extends JpaRepository<Cart, Long>{
	Optional<Cart> findByUserId(Long userId);
}
