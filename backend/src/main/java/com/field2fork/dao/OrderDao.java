package com.field2fork.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.field2fork.pojos.Order;

public interface OrderDao extends JpaRepository<Order, Long>{
	List<Order> findByUserId(Long userId);
}
