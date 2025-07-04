package com.field2fork.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.field2fork.pojos.CartItem;

public interface CartItemDao extends JpaRepository<CartItem, Long> {

}
