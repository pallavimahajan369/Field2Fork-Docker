package com.field2fork.service;

import java.util.List;

import com.field2fork.dtos.ApiResponse;
import com.field2fork.dtos.OrderItemResponseDTO;
import com.field2fork.dtos.OrderRequestDTO;
import com.field2fork.dtos.OrderResponseDTO;

public interface OrderService {
    List<OrderResponseDTO> getAllOrders();

    ApiResponse placeOrder(OrderRequestDTO orderRequestDto);

	List<OrderItemResponseDTO> getAllOrderItems();
	
	List<OrderItemResponseDTO> getOrderItemsByOrderId(Long orderId);

	ApiResponse deleteOrder(Long orderId);

	ApiResponse updateOrderStatus(Long orderId, String newStatus);

	List<OrderResponseDTO> getOrdersByUserId(Long userId); 
}
