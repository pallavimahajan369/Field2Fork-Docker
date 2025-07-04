package com.field2fork.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CartItemRequestDTO {
	private Long product_id;
	private Integer quantity;

}
