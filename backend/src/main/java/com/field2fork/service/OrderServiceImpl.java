package com.field2fork.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.field2fork.dao.OrderDao;
import com.field2fork.dao.OrderItemDao;
import com.field2fork.dao.ProductDao;
import com.field2fork.dtos.ApiResponse;
import com.field2fork.dtos.OrderItemRequestDTO;
import com.field2fork.dtos.OrderItemResponseDTO;
import com.field2fork.dtos.OrderRequestDTO;
import com.field2fork.dtos.OrderResponseDTO;
import com.field2fork.pojos.Order;
import com.field2fork.pojos.OrderItem;
import com.field2fork.pojos.OrderStatus;
import com.field2fork.pojos.Product;
import com.field2fork.pojos.User;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private OrderItemDao orderItemDao; 
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
    private ProductDao productDao;
	

	@Override
	public List<OrderResponseDTO> getAllOrders() {
		// TODO Auto-generated method stub
		return orderDao.findAll().stream()
                .map(order -> modelMapper.map(order, OrderResponseDTO.class))
                .collect(Collectors.toList());
	}
	
	@Override
	public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
	          List<Order> orders = orderDao.findByUserId(userId);       
	            // Convert Order entities to OrderResponseDTOs
	          return orders.stream()
                      .map(order -> modelMapper.map(order, OrderResponseDTO.class))
                      .collect(Collectors.toList());
	}
	
	
	@Override
	public List<OrderItemResponseDTO> getAllOrderItems() {
	    return orderItemDao.findAll().stream()
	            .map(orderItem -> {
	                OrderItemResponseDTO dto = modelMapper.map(orderItem, OrderItemResponseDTO.class);
	                // Fetch the product name from the product entity
	                dto.setProductName(orderItem.getProduct().getName());  
	                return dto;
	            })
	            .collect(Collectors.toList());
	}
	
	@Override
	public List<OrderItemResponseDTO> getOrderItemsByOrderId(Long orderId) {
	    return orderItemDao.findByOrderId(orderId).stream()
	            .map(orderItem -> {
	                OrderItemResponseDTO dto = modelMapper.map(orderItem, OrderItemResponseDTO.class);
	                // Fetch the product name from the product entity
	                dto.setProductName(orderItem.getProduct().getName()); // Assuming Product has a getName() method
	                return dto;
	            })
	            .collect(Collectors.toList());
	}

	
	//  This API is Additional for placing the one item order directly(Needs to be updated)
	@Override
	public ApiResponse placeOrder(OrderRequestDTO orderRequestDto) {
		try {
		Order order = new Order();
        order.setUser (new User(orderRequestDto.getUserId())); 
        order.setOrderStatus(OrderStatus.PENDING); // Set initial status
        order.setOrder_date(new Timestamp(System.currentTimeMillis())); // Set current timestamp
        
        // Set delivery date to 3 days from now
        LocalDateTime deliveryDate = LocalDateTime.now().plusDays(3);
        order.setDeliveryDate(Timestamp.valueOf(deliveryDate));
        
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        
     // Loop through the order items and create OrderItem objects
        for (OrderItemRequestDTO itemDto : orderRequestDto.getOrderItems()) {
            Product product = productDao.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found")); // Fetch product by ID

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(product.getPricePerUnit().multiply(new BigDecimal(itemDto.getQuantity()))); // Calculate price

            orderItem.setOrder(order); // Set the order reference
            orderItems.add(orderItem); // Add to the list of order items

            totalAmount = totalAmount.add(orderItem.getPrice()); // Update total amount
        }

        order.setTotalAmount(totalAmount); // Set total amount for the order
        order.setOrderItems(orderItems); // Set the order items

        // Save the order (this will also save the order items due to CascadeType.ALL)
        orderDao.save(order);
        return new ApiResponse("Order Placed Successfully!");
		}catch(RuntimeException e) {
			return new ApiResponse("Order Failed");
		}
    }

	@Override
	public ApiResponse deleteOrder(Long orderId) {
		 try {
	            Optional<Order> optionalOrder = orderDao.findById(orderId);
	            if (optionalOrder.isPresent()) {
	                Order order = optionalOrder.get();
	                order.setOrderStatus(OrderStatus.CANCELLED); // Mark as deleted
	                orderDao.save(order); // Save the updated order
	                return new ApiResponse("Order cancelled successfully");
	            } else {
	                return new ApiResponse("Order not found");
	            }
	        } catch (Exception e) {
	            return new ApiResponse("An unexpected error occurred");
	        }
	}
	
	@Override
	public ApiResponse updateOrderStatus(Long orderId, String newStatus) {
	    try {
	        if (newStatus == null || newStatus.trim().isEmpty()) {
	            return new ApiResponse("Status cannot be null or empty");
	        }

	        // Normalize the input
	        newStatus = newStatus.trim().replace("\"", "");

	        Optional<Order> optionalOrder = orderDao.findById(orderId);
	        if (optionalOrder.isPresent()) {
	            Order order = optionalOrder.get();
	            try {
	                if (!isValidStatus(newStatus)) {
	                    return new ApiResponse("Invalid order status: " + newStatus);
	                }
	                OrderStatus status = OrderStatus.valueOf(newStatus.toUpperCase());
	                order.setOrderStatus(status); // Update the status
	                orderDao.save(order); // Save the updated order
	                return new ApiResponse("Order status updated successfully");
	            } catch (IllegalArgumentException e) {
	                return new ApiResponse("Invalid order status: " + newStatus);
	            }
	        } else {
	            return new ApiResponse("Order not found");
	        }
	    } catch (Exception e) {
	        return new ApiResponse("An unexpected error occurred");
	    }
	}

	private boolean isValidStatus(String status) {
	    for (OrderStatus orderStatus : OrderStatus.values()) {
	        if (orderStatus.name().equalsIgnoreCase(status)) {
	            return true;
	        }
	    }
	    return false;
	}
}
