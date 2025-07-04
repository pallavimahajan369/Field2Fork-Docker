package com.field2fork.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.field2fork.dtos.ApiResponse;
import com.field2fork.dtos.OrderItemResponseDTO;
import com.field2fork.dtos.OrderRequestDTO;
import com.field2fork.dtos.OrderResponseDTO;
import com.field2fork.service.OrderService;


@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {
	private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
    	List<OrderResponseDTO> orders = orderService.getAllOrders();
    	if (orders.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.ok(orders);
		}
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable Long userId) {
    	List<OrderResponseDTO> orders = orderService.getOrdersByUserId(userId);
    	if (orders.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.ok(orders);
		}
    }
    
    
// This API is for getting all the items in the order_item table
    
    @GetMapping("/Items")
    public ResponseEntity<?> getAllOrderItems(){
    	List<OrderItemResponseDTO> orderItems = orderService.getAllOrderItems();
    	if (orderItems.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.ok(orderItems);
		}
    }
    
    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItemResponseDTO>> getOrderItems(@PathVariable Long orderId) {
        List<OrderItemResponseDTO> orderItems = orderService.getOrderItemsByOrderId(orderId);
        if (orderItems.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.ok(orderItems);
		}
    }
    
    @PostMapping
    public ResponseEntity<Object> placeOrder(@RequestBody OrderRequestDTO orderRequestDto) {
    	try {
    		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(orderRequestDto));
    	}
    	catch(RuntimeException e) {
    		return ResponseEntity.
    				status(HttpStatus.INTERNAL_SERVER_ERROR)
    				.body(new ApiResponse(e.getMessage()));
    	}
    }
    
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody String newStatus) {
        try {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, newStatus));
        }catch(RuntimeException e) {
        	return ResponseEntity.
    				status(HttpStatus.INTERNAL_SERVER_ERROR)
    				.body(new ApiResponse(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        try {
        return ResponseEntity.ok(orderService.deleteOrder(orderId));
        }catch(RuntimeException e) {
        	return ResponseEntity.
    				status(HttpStatus.INTERNAL_SERVER_ERROR)
    				.body(new ApiResponse(e.getMessage()));
        }
    }
}
