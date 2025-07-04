package com.field2fork.dtos;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponseDTO {
	 private Long id;
	 private String productName;
	 private int quantity;
	 private BigDecimal price;
	 
}
