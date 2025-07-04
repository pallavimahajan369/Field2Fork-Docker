package com.field2fork.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDTO {
    private Long userId; // ID of the user placing the order
    private List<OrderItemRequestDTO> orderItems; // List of order items
}
